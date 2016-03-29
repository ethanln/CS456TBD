package com.tbd.appprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import java.util.List;

import adapter.ItemsAdapter;
import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class ListOfItemsActivity extends AppCompatActivity {

    private String listID;
    private ArrayList<InventoryItem> items;
    private ListView listView;
    private ItemsAdapter itemAdapter;
    private String owner;

    private String currentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set page title
        Intent intent = getIntent();
        String ownerUsername = intent.getExtras().getString("ownerUsername");
        if(ownerUsername.equals("My")) {
            setTitle(intent.getExtras().getString("title"));
        }
        else{
            setTitle(ownerUsername + "'s " + intent.getExtras().getString("title"));
        }

        setContentView(R.layout.activity_list_of_items);
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

        // disable add button if this isn't the owner
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(intent.getExtras().getString("owner").equals("self")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListOfItemsActivity.this, AddItemActivity.class);
                    intent.putExtra("listID", listID);
                    startActivity(intent);// TODO pass the item list id to the add item activity
                }
            });
        }
        else{
            fab.hide();
        }

        this.currentItemId = "";

        // get prop data
        this.listID = intent.getExtras().getString("listID");
        this.owner = intent.getExtras().getString("owner");

        // set up list view
        this.items = new ArrayList<InventoryItem>();
        this.listView = (ListView)findViewById(R.id.items_list_view);
        this.setUpList(this);

    }

    /**
     * update the list view
     */
    private void setUpList(final ListOfItemsActivity activity){

        // get friends info
        NetworkManager.getInstance().makeGetItemsRequest(this.listID, this.items, new GenericCallback() {
            @Override
            public void callback() {
                if(owner.equals("self")) {
                    itemAdapter = new ItemsAdapter(activity, items, true, listenerEdit, listenerRemove);
                }
                else{
                    itemAdapter = new ItemsAdapter(activity, items, false, listenerEdit, listenerRemove);
                }
                listView.setAdapter(itemAdapter);
                listView.setOnItemClickListener(onItemClickListener);
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String itemID = itemAdapter.getItem(position).getItemID();
            String itemTitle = itemAdapter.getItem(position).getTitle();

            if(owner.equals("self")) {
                Intent i = new Intent(ListOfItemsActivity.this, ViewMyItemActivity.class);
                i.putExtra("itemID", itemID);
                i.putExtra("listID", listID);
                i.putExtra("itemTitle", itemTitle);
                startActivity(i);
            }
            else if(owner.equals("other")){
                Intent intent = getIntent();
                String userID = intent.getExtras().getString("userID");

                Intent i = new Intent(ListOfItemsActivity.this, ViewFriendItemActivity.class);
                i.putExtra("itemID", itemID);
                i.putExtra("listID", listID);
                i.putExtra("userID", userID);
                i.putExtra("itemTitle", itemTitle);
                startActivity(i);
            }

        }
    };

    private View.OnClickListener listenerEdit = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            TextView pos = (TextView) row.findViewById(R.id.item_pos);
            int position = Integer.parseInt(pos.getText().toString());
            currentItemId = items.get(position).getListID();

            InventoryItem item = items.get(position);
            Intent i = new Intent(ListOfItemsActivity.this, EditItemActivity.class);
            i.putExtra("itemID", item.getItemID());
            i.putExtra("listID", item.getListID());
            i.putExtra("imageURL", item.getImageURL());
            i.putExtra("title", item.getTitle());
            i.putExtra("description", item.getDescription());

            startActivity(i);
        }
    };


    private View.OnClickListener listenerRemove = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            ViewGroup row = (ViewGroup)v.getParent();
            TextView pos = (TextView) row.findViewById(R.id.item_pos);
            int position = Integer.parseInt(pos.getText().toString());
            currentItemId = items.get(position).getItemID();

            // create dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(ListOfItemsActivity.this);
            builder.setMessage("Are you sure you want to remove " + items.get(position).getTitle() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    LoadingScreenUtil.start(ListOfItemsActivity.this, "Removing Item");

                    NetworkManager.getInstance().makeDeleteItemRequest(currentItemId, new GenericCallback() {
                        @Override
                        public void callback() {
                            currentItemId = "";
                            LoadingScreenUtil.setEndMessage(ListOfItemsActivity.this, "Item Removed");
                            LoadingScreenUtil.end();
                        }
                    });

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    currentItemId = "";
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
            Intent intent = new Intent(ListOfItemsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(ListOfItemsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            Intent intent = new Intent(ListOfItemsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(ListOfItemsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(ListOfItemsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            LoadingScreenUtil.start(ListOfItemsActivity.this, "Logging out...");
            NetworkManager.getInstance().makeLogoutUserRequest(new GenericCallback() {
                @Override
                public void callback() {
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Logged out");
                    startActivity(new Intent(ListOfItemsActivity.this, LoginActivity.class));
                    LoadingScreenUtil.end();
                }
            });
        }
        return false;
    }

}
