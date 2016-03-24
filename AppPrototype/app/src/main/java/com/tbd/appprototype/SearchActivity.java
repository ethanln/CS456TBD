package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.ItemsAdapter;
import model.InventoryItem;
import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ListCallback;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> friendsItems;
    private ArrayList<InventoryItem> matchingItems;
    private ListView searchResultsView;

    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Search");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
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

        this.searchResultsView = (ListView)findViewById(R.id.search_results);
        this.friendsItems = new ArrayList<>();

        // get all friends' items
        getFriendsItems();
        EditText searchBar = (EditText) findViewById(R.id.search_bar);

        // add text watcher
        searchBar.addTextChangedListener(this.textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            String value = s.toString();
            matchingItems = new ArrayList<>();
            if(value.length() != 0) {
                for (int i = 0; i < friendsItems.size(); i++) {
                    if (friendsItems.get(i).getTitle().contains(value) || friendsItems.get(i).getDescription().contains(value)) {
                        matchingItems.add(friendsItems.get(i));
                    }
                }
            }
            loadResults(matchingItems);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void loadResults(ArrayList<InventoryItem> results){
        this.itemsAdapter = new ItemsAdapter(this, results, false);
        searchResultsView.setAdapter(itemsAdapter);
        searchResultsView.setOnItemClickListener(onItemClickListener);
    }

    private void getFriendsItems(){

        TBDApplication app = (TBDApplication)getApplication();
        final HashMap<String, Object> friendIDs = app.getCurrentUser().getFriendIDs();

        for (final String friendID : friendIDs.keySet()) {
            final ArrayList<InventoryList> listIDs = new ArrayList<>();
            NetworkManager.getInstance().makeGetListsForUserRequest(friendID, listIDs, new GenericCallback() {
                @Override
                public void callback() {
                    final ArrayList<InventoryItem> result = new ArrayList<>();
                    NetworkManager.getInstance().makeGetItemsRequest(listIDs.get(listIDs.size() - 1).getListID(), result, new GenericCallback() {
                        @Override
                        public void callback() {
                            friendsItems.add(result.get(result.size() - 1));
                        }
                    });
                }
            });
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final InventoryItem item = itemsAdapter.getItem(position);

            NetworkManager.getInstance().makeGetListRequest(item.getListID(), new ListCallback() {
                @Override
                public void callback() {
                    Intent i = new Intent(SearchActivity.this, ViewFriendItemActivity.class);
                    i.putExtra("itemID", item.getItemID());
                    i.putExtra("userID", getList().getUserID());
                    i.putExtra("itemTitle", item.getTitle());
                    startActivity(i);
                }
            });
        }
    };
}
