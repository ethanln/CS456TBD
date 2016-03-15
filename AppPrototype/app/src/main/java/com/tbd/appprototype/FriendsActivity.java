package com.tbd.appprototype;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import model.InventoryList;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;
import networking.callback.UsersCallback;

public class FriendsActivity extends AppCompatActivity {

    private ArrayList<User> users;
    private ArrayAdapter<User> userAdapter;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.setUpListView();
        this.setUpList();
    }

    /**
     * set up the list view
     */
    private void setUpListView(){
        this.users = new ArrayList<User>();

        // create the adapter and override its getView method, we need this to change the text color
        this.userAdapter = new ArrayAdapter<User>(this,
                android.R.layout.simple_list_item_1,
                this.users){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.friends);
        listView.setAdapter(this.userAdapter);

        listView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * update the list view
     */
    private void setUpList(){
        TBDApplication app = (TBDApplication)getApplication();
        final HashMap<String, Object> friends = app.getCurrentUser().getFriendIDs();

        NetworkManager.getInstance().makeGetUsersAsListWithIdsRequest(friends, new UsersCallback() {
            @Override
            public void callback() {
                ArrayList<User> users = getUsers();
                if (users == null) {
                    return;
                }

                // add users to adapter
                for(User user : users){
                    addUserToAdapter(user);
                }
            }
        });
    }

    private void addUserToAdapter(User user){
        this.userAdapter.add(user);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String userID = userAdapter.getItem(position).getUserID();
            // do intent here with the listID stored as prop.
        }
    };

}
