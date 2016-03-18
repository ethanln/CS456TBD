package com.tbd.appprototype;

import android.content.Intent;
import android.net.Network;
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
import android.widget.Toast;

import java.util.ArrayList;

import adapter.ItemRequestAdapter;
import adapter.ListAdapter;
import model.InventoryList;
import model.ItemRequest;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ItemRequestCallBack;

public class ItemRequestsActivity extends AppCompatActivity {

    private ArrayList<ItemRequest> itemRequests;
    private ListView listView;
    private ItemRequestAdapter itemRequestAdapter;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_requests);
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

        this.itemRequests = new ArrayList<ItemRequest>();
        this.listView = (ListView)findViewById(R.id.item_requests_list);
        this.setUpList(this);
    }

    private void setUpList(final ItemRequestsActivity activity){
        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetItemRequestRequest(app.getCurrentUser().getUserID(), new ItemRequestCallBack() {
            @Override
            public void callback() {
                if(getItemRequests() != null) {
                    if(getItemRequests().size() > 0) {
                        itemRequestAdapter = new ItemRequestAdapter(activity, getItemRequests(), declineListener, acceptListener);
                        listView.setAdapter(itemRequestAdapter);
                        listView.setOnItemClickListener(onItemClickListener);
                    }
                }
            }
        });
    }

    private View.OnClickListener declineListener = new View.OnClickListener() {
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();
            TextView id = (TextView)nextParent.findViewById(R.id.item_request_id);

            NetworkManager.getInstance().makeDeleteItemRequestRequest(id.getText().toString(), new GenericCallback() {
                @Override
                public void callback() {
                    showResultMessage("Request Declined");
                }
            });
            // IMPLEMENT THE ADD FRIEND REQUEST
            //showResultMessage("Friend Request Sent");
        }
    };

    private View.OnClickListener acceptListener = new View.OnClickListener() {
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();
            TextView id = (TextView)nextParent.findViewById(R.id.item_request_id);

            NetworkManager.getInstance().makeDeleteItemRequestRequest(id.getText().toString(), new GenericCallback() {
                @Override
                public void callback() {
                    showResultMessage("Request Approved");
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

    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
