package com.tbd.appprototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.ItemLendedAdapter;
import adapter.ItemRequestAdapter;
import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ItemRequestCallBack;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class LendedItemsActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> lendedItems;
    private ListView listView;
    private ItemLendedAdapter lendedItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lended_items);
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
        this.lendedItems = new ArrayList<InventoryItem>();

        // get list view
        this.listView = (ListView)findViewById(R.id.item_lended_list);
        this.setUpList(this);
    }

    private void setUpList(final LendedItemsActivity activity){
        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetLendedItemsRequest(app.getCurrentUser().getUserID(), lendedItems, new GenericCallback() {
            @Override
            public void callback() {
                    if (lendedItems.size() > 0) {
                        lendedItemsAdapter = new ItemLendedAdapter(activity, lendedItems, returnListener);
                        listView.setAdapter(lendedItemsAdapter);
                        listView.setOnItemClickListener(onItemClickListener);
                    }
                }
        });
    }

    private View.OnClickListener returnListener = new View.OnClickListener() {
        public void onClick(View v) {

            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();

            TextView idView = (TextView)nextParent.findViewById(R.id.item_lended_id);
            TextView posView = (TextView)nextParent.findViewById(R.id.item_lended_position_id);

            String id = idView.getText().toString();
            final String pos = posView.getText().toString();

            InventoryItem item = lendedItemsAdapter.get(Integer.parseInt(pos));

            LoadingScreenUtil.start(LendedItemsActivity.this, "Returning " + item.getTitle() + "...");

            item.setLendedTo("");
            item.setLendedToImage("");
            item.setLendedToName("");
            item.setIsAvailable(true);

            NetworkManager.getInstance().makeReturnItemRequest(item, new GenericCallback() {
                @Override
                public void callback() {
                    lendedItemsAdapter.remove(Integer.parseInt(pos));
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Item Returned");
                    LoadingScreenUtil.end();
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

}
