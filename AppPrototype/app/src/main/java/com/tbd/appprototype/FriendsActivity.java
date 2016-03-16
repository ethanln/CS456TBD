package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.FriendsAdapter;
import model.User;
import networking.NetworkManager;
import networking.callback.UsersCallback;

public class FriendsActivity extends AppCompatActivity {

    private FriendsAdapter userAdapter;
    private ArrayList<User> friends;

    private ListView listView;

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

        //set up the list view
        this.friends = new ArrayList<User>();
        this.setUpList(this);
    }

    /**
     * update the list view
     */
    private void setUpList(final FriendsActivity activity){

        // get friend ids
        TBDApplication app = (TBDApplication)getApplication();
        final HashMap<String, Object> friendsIds = app.getCurrentUser().getFriendIDs();

        // get friends info
        NetworkManager.getInstance().makeGetUsersAsListWithIdsRequest(friendsIds, new UsersCallback() {
            @Override
            public void callback() {
                ArrayList<User> users = getUsers();
                if (users == null) {
                    return;
                }

                // add users to adapter
                for(User user : users){
                    addUserToData(user);
                }

                // add friends to list view
                listView = (ListView) findViewById(R.id.friends);
                userAdapter = new FriendsAdapter(activity, friends);
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

}
