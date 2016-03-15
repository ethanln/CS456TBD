package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;

public class FriendsListsActivity extends AppCompatActivity {

    private ArrayList<InventoryList> lists;
    private ArrayAdapter<InventoryList> adapter;

    private int currentSelectedItem = -1;
    private String currentItemId;
    private View currentSelectedView;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getIntent().getStringExtra("userID");
        setContentView(R.layout.activity_friends_lists);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        NetworkManager.getInstance().makeGetUserRequest(userID, new UserCallback() {
            @Override
            public void callback() {
                toolbar.setTitle(getUser().getUsername() + "'s Lists");
            }
        });

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set up list view
        this.setUpListView();
        // set up list
        this.setUpList();
    }

    /**
     * set up the list view
     */
    private void setUpListView(){
        this.lists = new ArrayList<InventoryList>();

        // create the adapter and override its getView method, we need this to change the text color
        this.adapter = new ArrayAdapter<InventoryList>(this,
                android.R.layout.simple_list_item_1,
                this.lists){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.lists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * update the list view
     */
    private void setUpList(){
        NetworkManager.getInstance().makeGetListsForUserRequest(userID, this.adapter, new GenericCallback() {
            @Override
            public void callback() {
                // Anything that may need to happen when a new list is added to adapter... Used mostly in testing. Can set (new GenericCallback) to null
            }
        });
    }

    /**
     * set up list item click event
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String listID = lists.get(position).getListID();
            // do intent here with the listID stored as prop.
            // IMPLEMENT
        }
    };


    /**
     * go to personal profile
     * @param view
     */
    public void goToProfile(View view){
        startActivity(new Intent(FriendsListsActivity.this, ProfileActivity.class));
    }

    /**
     * go to friends list
     * @param view
     */
    public void goToFriends(View view){
        startActivity(new Intent(FriendsListsActivity.this, FriendsActivity.class));
    }

    /**
     * go to profile settings
     * @param view
     */
    public void goToSettings(View view){

    }
}
