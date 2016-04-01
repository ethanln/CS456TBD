package com.tbd.appprototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import adapter.ItemBorrowedAdapter;
import adapter.ItemLendedAdapter;
import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;

public class BorrowedItemsActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> borrowedItems;
    private ListView listView;
    private ItemBorrowedAdapter borrowedItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_items);
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
        this.borrowedItems = new ArrayList<InventoryItem>();

        // get list view
        this.listView = (ListView)findViewById(R.id.item_borrowed_list);
        this.setUpList(this);
    }

    private void setUpList(final BorrowedItemsActivity activity){
        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetBorrowedItemsRequest(app.getCurrentUser().getUserID(), borrowedItems, new GenericCallback() {
            @Override
            public void callback() {
                if (borrowedItems.size() > 0) {
                    borrowedItemsAdapter = new ItemBorrowedAdapter(activity, borrowedItems);
                    listView.setAdapter(borrowedItemsAdapter);
                    listView.setOnItemClickListener(onItemClickListener);
                }
            }
        });
    }

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

}
