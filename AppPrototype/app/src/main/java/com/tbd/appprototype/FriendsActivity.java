package com.tbd.appprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.FriendsAdapter;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UsersCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class FriendsActivity extends AppCompatActivity {

    private FriendsAdapter userAdapter;
    private ArrayList<User> friends;

    private ListView listView;

    private String currentFriendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsActivity.this, AddFriendActivity.class));
            }
        });

        this.currentFriendId = "";

        //set up the list view
        this.setUpList(this);
    }

    /**
     * update the list view
     */
    private void setUpList(final FriendsActivity activity){
        // initialize friends list.
        this.friends = new ArrayList<User>();

        // get friend ids
        TBDApplication app = (TBDApplication)getApplication();
        final HashMap<String, Object> friendsIds = app.getCurrentUser().getFriendIDs();

        // get friends info
        NetworkManager.getInstance().makeGetUsersAsListWithIdsRequest(friendsIds, new UsersCallback() {
            @Override
            public void callback() {
                ArrayList<User> users = getUsers();
                // add users to adapter
                for(User user : users){
                    addUserToData(user);
                }

                // add friends to list view
                listView = (ListView) findViewById(R.id.friends);
                userAdapter = new FriendsAdapter(activity, friends, listenerRemove);
                listView.setAdapter(userAdapter);
                listView.setOnItemClickListener(onItemClickListener);
            }
        });
    }

    private void addUserToData(User user){
        this.friends.add(user);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String userID = friends.get(position).getUserID();
            String username = friends.get(position).getUsername();

            Intent i = new Intent(FriendsActivity.this, FriendsListsActivity.class);
            i.putExtra("userID", userID);
            i.putExtra("username", username);
            startActivity(i);
        }
    };


    private View.OnClickListener listenerRemove = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            ViewGroup row = (ViewGroup)v.getParent();
            TextView pos = (TextView) row.findViewById(R.id.friend_pos);
            int position = Integer.parseInt(pos.getText().toString());
            currentFriendId = friends.get(position).getUserID();

            // create dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
            builder.setMessage("Are you sure you want to remove " + friends.get(position).getUsername() + " from your friends?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    LoadingScreenUtil.start(FriendsActivity.this, "Removing Friend");
                    TBDApplication app = (TBDApplication)getApplication();
                    NetworkManager.getInstance().makeDeleteFriendRequest(app.getCurrentUserID(), currentFriendId, new GenericCallback() {
                        @Override
                        public void callback() {
                            if (data.contains("1") && data.contains("2")) {
                                TBDApplication app = (TBDApplication) getApplication();
                                app.getCurrentUser().removeFriend(currentFriendId);

                                setUpList(FriendsActivity.this);
                                LoadingScreenUtil.setEndMessage(getApplicationContext(), "Friend Removed");
                                //UIMessageUtil.showResultMessage(getApplicationContext(), "Request Approved");
                                LoadingScreenUtil.end();

                                currentFriendId = "";
                            }
                        }
                    });

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    currentFriendId = "";
                    break;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_profile){
            Intent intent = new Intent(FriendsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(FriendsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            Intent intent = new Intent(FriendsActivity.this, MyListsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(FriendsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(FriendsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            NetworkManager.getInstance().makeLogoutUserRequest(new GenericCallback() {
                @Override
                public void callback() {
                    UIMessageUtil.showResultMessage(getApplicationContext(), "Logging out...");
                    startActivity(new Intent(FriendsActivity.this, LoginActivity.class));
                }
            });
        }
        return false;
    }

}
