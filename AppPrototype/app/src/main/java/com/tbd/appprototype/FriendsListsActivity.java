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

import adapter.ListAdapter;
import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;

public class FriendsListsActivity extends AppCompatActivity {

    private ArrayList<InventoryList> lists;
    private ListView listView;
    private ListAdapter listAdapter;

    private String userID;
    private String ownerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set page title
        Intent intent = getIntent();
        this.ownerUsername = intent.getExtras().getString("username");
        setTitle(this.ownerUsername + "'s Lists");

        setContentView(R.layout.activity_friends_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        this.userID = intent.getExtras().getString("userID");

        this.lists = new ArrayList<InventoryList>();
        this.listView = (ListView)findViewById(R.id.lists);
        this.setUpList(this);

    }

    /**
     * update the list view
     */
    private void setUpList(final FriendsListsActivity activity){
        NetworkManager.getInstance().makeGetListsForUserRequest(userID, this.lists, new GenericCallback() {
            @Override
            public void callback() {
                listAdapter = new ListAdapter(activity, lists);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(onItemClickListener);
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
            Intent i = new Intent(FriendsListsActivity.this, ListOfItemsActivity.class);
            i.putExtra("userID", lists.get(position).getUserID());
            i.putExtra("listID", listID);
            i.putExtra("owner", "other");
            i.putExtra("ownerUsername", ownerUsername);
            i.putExtra("title", lists.get(position).getTitle());
            startActivity(i);
        }
    };

}
