package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.ItemsAdapter;
import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;

public class ListOfItemsActivity extends AppCompatActivity {

    private String listID;
    private ArrayList<InventoryItem> items;
    private ListView listView;
    private ItemsAdapter itemAdapter;
    private String owner;

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
                itemAdapter = new ItemsAdapter(activity, items);
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
                // implement
            }

        }
    };

}