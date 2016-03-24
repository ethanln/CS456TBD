package com.tbd.appprototype;

import android.annotation.TargetApi;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.ItemsAdapter;
import adapter.ListAdapter;
import model.InventoryItem;
import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;

public class MainActivity extends AppCompatActivity {

    private ArrayList<InventoryList> lists;
    private ListView listView;
    private ListAdapter listAdapter;

    private int currentSelectedItem = -1;
    private String currentItemId;
    private View currentSelectedView;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("My Lists");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        //toolbar.setNavigationIcon(R.drawable.search_icon);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show add list pop up
                startActivity(new Intent(MainActivity.this, AddListActivity.class));
            }
        });



        this.lists = new ArrayList<InventoryList>();
        this.listView = (ListView)findViewById(R.id.lists);
        this.setUpList(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_profile){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            finish();
            startActivity(getIntent());
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(MainActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(MainActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            TBDApplication app = (TBDApplication)getApplication();
            app.setCurrentUser(null);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return false;
    }

    /**
     * update the list view
     */
    private void setUpList(final MainActivity activity){
        NetworkManager.getInstance().makeGetListsRequest(this.lists, new GenericCallback() {
            @Override
            public void callback() {
                listAdapter = new ListAdapter(activity, lists, true);
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
            Intent i = new Intent(MainActivity.this, ListOfItemsActivity.class);
            i.putExtra("listID", listID);
            i.putExtra("owner", "self");
            i.putExtra("ownerUsername", "My");
            i.putExtra("title", lists.get(position).getTitle());
            startActivity(i);
        }
    };


    /**
     * go to personal profile
     * @param view
     */
    public void goToProfile(View view){
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

    /**
     * go to friends list
     * @param view
     */
    public void goToFriends(View view){
        startActivity(new Intent(MainActivity.this, FriendsActivity.class));
    }

    /**
     * go to profile settings
     * @param view
     */
    public void goToSettings(View view){

    }
}
