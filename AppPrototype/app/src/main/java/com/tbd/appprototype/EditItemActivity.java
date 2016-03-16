package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;

public class EditItemActivity extends AppCompatActivity {

    private String itemID;
    private String listID;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
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

        // get item id
        Intent intent = getIntent();
        this.itemID = intent.getExtras().getString("itemID");
        this.listID = intent.getExtras().getString("listID");

        // fill in preexisting values
        fillInFields();
    }

    private void fillInFields(){
        Intent intent = getIntent();

        String imageURL = intent.getExtras().getString("imageURL");
        String title = intent.getExtras().getString("title");
        String description = intent.getExtras().getString("description");

        TextView imageURLView = (TextView)findViewById(R.id.item_image_url_textbox);
        TextView titleView = (TextView)findViewById(R.id.item_title_textbox);
        TextView descriptionView = (TextView)findViewById(R.id.item_description_textbox);

        imageURLView.setText(imageURL);
        titleView.setText(title);
        descriptionView.setText(description);
    }

    public void submitChanges(View view){
        InventoryItem updatedItem = new InventoryItem();

        TextView imageURLView = (TextView)findViewById(R.id.item_image_url_textbox);
        TextView titleView = (TextView)findViewById(R.id.item_title_textbox);
        TextView descriptionView = (TextView)findViewById(R.id.item_description_textbox);

        updatedItem.setTitle(titleView.getText().toString());
        updatedItem.setImageURL(imageURLView.getText().toString());
        updatedItem.setDescription(descriptionView.getText().toString());
        updatedItem.setItemID(itemID);
        updatedItem.setListID(listID);

        NetworkManager.getInstance().makeUpdateItemRequest(updatedItem, new GenericCallback() {
            @Override
            public void callback() {
                showResultMessage("Item Succesfully Edited");
                onBackPressed();
            }
        });
    }

    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
