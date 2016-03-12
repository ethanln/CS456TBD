package com.tbd.appprototype;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.InventoryItem;
import networking.NetworkManager;
import networking.testing.NetworkTest;
import networking.testing.NetworkTestAdapter;

public class NetworkTestActivity extends ListActivity {

    private ArrayList<NetworkTest> tests;
    private NetworkTestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        setUpList();
    }

    private void setUpList() {
        createTests();

        adapter = new NetworkTestAdapter(this, tests);
        setListAdapter(adapter);
        ListView listView = getListView();
        listView.setOnItemClickListener(onItemClickListener);
    }

    private void createTests() {
        this.tests = new ArrayList<>();
        tests.add(new NetworkTest("makeCreateItemRequest") {
            @Override
            public void executeTest() {
                Log.d("Network Test", getName());
                InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "Test Image URL", "Test List ID");
                String itemID = NetworkManager.inst().makeCreateItemRequest(item);
                showNetworkTestCompleteToast("Item Created: " + itemID);
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Index Tapped", String.valueOf(position));
            tests.get(position).executeTest();
        }
    };

    private void showNetworkTestCompleteToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
