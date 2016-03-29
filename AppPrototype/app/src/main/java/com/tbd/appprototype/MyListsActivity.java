package com.tbd.appprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.ListAdapter;
import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class MyListsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<InventoryList> lists;
    private ListView listView;
    private ListAdapter listAdapter;

    private String currentListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyListsActivity.this, AddListActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.currentListId = "";

        this.lists = new ArrayList<InventoryList>();
        this.listView = (ListView)findViewById(R.id.lists);
        this.setUpList(this);
    }

    /**
     * update the list view
     */
    private void setUpList(final MyListsActivity activity){
        NetworkManager.getInstance().makeGetListsRequest(this.lists, new GenericCallback() {
            @Override
            public void callback() {
                listAdapter = new ListAdapter(activity, lists, true, listenerEdit, listenerRemove);
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
            Intent i = new Intent(MyListsActivity.this, ListOfItemsActivity.class);
            i.putExtra("listID", listID);
            i.putExtra("owner", "self");
            i.putExtra("ownerUsername", "My");
            i.putExtra("title", lists.get(position).getTitle());
            startActivity(i);
        }
    };

    private View.OnClickListener listenerEdit = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            ViewGroup row = (ViewGroup)v.getParent();
            TextView pos = (TextView) row.findViewById(R.id.list_pos);
            int position = Integer.parseInt(pos.getText().toString());

            Intent intent = new Intent(MyListsActivity.this, EditListActivity.class);
            intent.putExtra("listID", lists.get(position).getListID());
            intent.putExtra("listName", lists.get(position).getTitle());
            intent.putExtra("listType", lists.get(position).getType());

            startActivity(intent);
        }
    };


    private View.OnClickListener listenerRemove = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            ViewGroup row = (ViewGroup)v.getParent();
            TextView pos = (TextView) row.findViewById(R.id.list_pos);
            int position = Integer.parseInt(pos.getText().toString());
            currentListId = lists.get(position).getListID();

            // create dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(MyListsActivity.this);
            builder.setMessage("Are you sure you want to remove " + lists.get(position).getTitle() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    LoadingScreenUtil.start(MyListsActivity.this, "Removing List");

                    NetworkManager.getInstance().makeDeleteItemsRequest(currentListId, new GenericCallback() {
                        @Override
                        public void callback() {
                            NetworkManager.getInstance().makeDeleteListRequest(currentListId, new GenericCallback() {
                                @Override
                                public void callback() {
                                    currentListId = "";
                                    LoadingScreenUtil.end();
                                    UIMessageUtil.showResultMessage(getApplicationContext(), "List Removed");
                                }
                            });
                        }
                    });

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    currentListId = "";
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(MyListsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_friends) {
            Intent intent = new Intent(MyListsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_my_lists) {
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.action_item_requests) {
            Intent intent = new Intent(MyListsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_friend_requests) {
            Intent intent = new Intent(MyListsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_logout) {
            LoadingScreenUtil.start(MyListsActivity.this, "Logging out...");
            NetworkManager.getInstance().makeLogoutUserRequest(new GenericCallback() {
                @Override
                public void callback() {
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Logged out");
                    startActivity(new Intent(MyListsActivity.this, LoginActivity.class));
                    LoadingScreenUtil.end();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
