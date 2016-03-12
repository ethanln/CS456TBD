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
import model.InventoryList;
import model.User;
import networking.NetworkManager;
import networking.testing.NetworkTest;
import networking.testing.NetworkTestAdapter;

public class NetworkTestActivity extends ListActivity {

    private ArrayList<NetworkTest> tests;
    private NetworkTestAdapter adapter;
    private NetworkManager network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        network = NetworkManager.getInstance();
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

        // USER TESTS
        // ----------

        tests.add(new NetworkTest("makeCreateUserRequest") {
            @Override
            public void executeTest() {
                ArrayList<String> friends = new ArrayList<String>();
                friends.add("TestFriendID0");
                friends.add("TestFriendID1");
                friends.add("TestFriendID2");
                User user = new User("Test Username", "test", "\"http://www.golenbock.com/wp-content/uploads/2015/01/placeholder-user.png\"", friends);
                String userID = network.makeCreateUserRequest(user);
                showNetworkTestCompleteToast("User Created: " + userID);
            }
        });

        tests.add(new NetworkTest("Login User Test") {
            @Override
            public void executeTest() {
                network.makeLoginUserRequest("user1", "p");
                showNetworkTestCompleteToast("Login User Done");
            }
        });

        // LIST TESTS
        // ----------

        tests.add(new NetworkTest("makeCreateListRequest") {
            @Override
            public void executeTest() {
                InventoryList list = new InventoryList("", "", "Test Title", "Test Type", "\"https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/512/film_reel.png\"");
                String listID = network.makeCreateListRequest(list);
                showNetworkTestCompleteToast("List Created: " + listID);
            }
        });

        // ITEM TESTS
        // ----------

        tests.add(new NetworkTest("makeCreateItemRequest") {
            @Override
            public void executeTest() {
                InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "\"https://pixabay.com/static/uploads/photo/2015/06/21/23/53/subtle-817155_960_720.jpg\"", "Test List ID");
                String itemID = network.makeCreateItemRequest(item);
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
