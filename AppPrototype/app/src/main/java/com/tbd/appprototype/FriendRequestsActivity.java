package com.tbd.appprototype;

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
import android.widget.Toast;

import java.util.ArrayList;

import adapter.FriendRequestAdapter;
import adapter.ItemRequestAdapter;
import model.FriendRequest;
import model.ItemRequest;
import networking.NetworkManager;
import networking.callback.FriendRequestCallBack;
import networking.callback.GenericCallback;
import networking.callback.ItemRequestCallBack;
import util.UIMessageUtil;

public class FriendRequestsActivity extends AppCompatActivity {

    private ArrayList<FriendRequest> friendRequests;
    private ListView listView;
    private FriendRequestAdapter friendRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
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

        // initialize adapter
        this.friendRequests = new ArrayList<FriendRequest>();
        // get list view
        this.listView = (ListView)findViewById(R.id.friend_requests_list);
        this.setUpList(this);
    }

    private void setUpList(final FriendRequestsActivity activity){
        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetFriendRequestRequest(app.getCurrentUser().getUserID(), new FriendRequestCallBack() {
            @Override
            public void callback() {
                if (getFriendRequests() != null) {
                    if (getFriendRequests().size() > 0) {
                        friendRequests = getFriendRequests();
                        friendRequestAdapter = new FriendRequestAdapter(activity, getFriendRequests(), declineListener, acceptListener);
                        listView.setAdapter(friendRequestAdapter);
                        listView.setOnItemClickListener(onItemClickListener);
                    }
                }
            }
        });
    }

    private View.OnClickListener declineListener = new View.OnClickListener() {
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();

            TextView idView = (TextView)nextParent.findViewById(R.id.friend_request_id);
            TextView posView = (TextView)nextParent.findViewById(R.id.friend_request_position_id);

            String id = idView.getText().toString();
            final String pos = posView.getText().toString();

            NetworkManager.getInstance().makeDeleteFriendRequestRequest(id, new GenericCallback() {
                @Override
                public void callback() {
                    friendRequestAdapter.remove(Integer.parseInt(pos));
                    UIMessageUtil.showResultMessage(getApplicationContext(), "Request Declined");
                }
            });
        }
    };

    private View.OnClickListener acceptListener = new View.OnClickListener() {
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();

            TextView idView = (TextView)nextParent.findViewById(R.id.friend_request_id);
            TextView posView = (TextView)nextParent.findViewById(R.id.friend_request_position_id);

            String id = idView.getText().toString();
            final String pos = posView.getText().toString();

            NetworkManager.getInstance().makeDeleteFriendRequestRequest(id, new GenericCallback() {
                @Override
                public void callback() {
                    final String idFrom = friendRequests.get(Integer.parseInt(pos)).getFrom();
                    String idTo = friendRequests.get(Integer.parseInt(pos)).getTo();
                    friendRequestAdapter.remove(Integer.parseInt(pos));

                    // IMPLEMENT A ADD FRIEND API
                    NetworkManager.getInstance().makeAddFriendRequest(idTo, idFrom, new GenericCallback() {
                        @Override
                        public void callback() {
                            if(data.contains("1") && data.contains("2")) {
                                TBDApplication app = (TBDApplication)getApplication();
                                app.getCurrentUser().addFriend(idFrom);
                                UIMessageUtil.showResultMessage(getApplicationContext(), "Request Approved");
                            }
                        }
                    });

                }
            });
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String userID = friends.get(position).getUserID();
            //String username = friends.get(position).getUsername();

            //Intent i = new Intent(FriendsActivity.this, FriendsListsActivity.class);
            //i.putExtra("userID", userID);
            //i.putExtra("username", username);
            //startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        /*if(id == R.id.action_profile){
            Intent intent = new Intent(FriendRequestsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(FriendRequestsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            Intent intent = new Intent(FriendRequestsActivity.this, MyListsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(FriendRequestsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(FriendRequestsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            NetworkManager.getInstance().makeLogoutUserRequest(new GenericCallback() {
                @Override
                public void callback() {
                    UIMessageUtil.showResultMessage(getApplicationContext(), "Logging out...");
                    startActivity(new Intent(FriendRequestsActivity.this, LoginActivity.class));
                }
            });
        }*/
        return false;
    }

}
