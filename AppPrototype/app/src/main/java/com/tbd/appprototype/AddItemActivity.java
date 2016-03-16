package com.tbd.appprototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;

public class AddItemActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
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
    }

    public void onSubmit(View view){

        TBDApplication app = (TBDApplication) getApplication();

        // if user is not logged in
        if(app.getCurrentUser() == null){
            showNetworkTestCompleteToast("No User Logged In");
            return;
        }

        TextView itemName = (TextView)findViewById(R.id.itemName);
        TextView itemDescription = (TextView)findViewById(R.id.itemDescription);

        //String userId = app.getCurrentUser().getUserID();
        String listId = "";// TODO
        String itemId = "";// TODO
        String name = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        String url = "https://pixabay.com/static/uploads/photo/2015/06/21/23/53/subtle-817155_960_720.jpg";
        InventoryItem item = new InventoryItem(itemId, name, description, url, listId);

        NetworkManager.getInstance().makeCreateItemRequest(item, new GenericCallback() {
            @Override
            public void callback() {
                showNetworkTestCompleteToast("Item Added");
                switchIntent();
            }
        });


        /*
        InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "https://pixabay.com/static/uploads/photo/2015/06/21/23/53/subtle-817155_960_720.jpg", "Test List ID");

        // get new list info
        String userId = app.getCurrentUser().getUserID();
        String title = itemName.getText().toString();
        String type = itemDescription.getText().toString();

        // create new list instance
        InventoryList newList = new InventoryList(userId, title, type, "");

        // add new list
        NetworkManager.getInstance().makeCreateListRequest(newList, new GenericCallback() {
            @Override
            public void callback() {
                showNetworkTestCompleteToast("List Added");
                switchIntent();
            }
        });
        */
    }

    private void switchIntent(){
        onBackPressed();
    }

    private void showNetworkTestCompleteToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
