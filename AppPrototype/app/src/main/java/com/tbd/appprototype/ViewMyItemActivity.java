package com.tbd.appprototype;

import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ItemCallback;
import util.ImageLoaderUtil;

public class ViewMyItemActivity extends AppCompatActivity {

    private String itemID;
    private String listID;
    private String itemTitle;

    private InventoryItem currentItem;

    private ImageView itemImage;
    private TextView itemTitleView;
    private TextView itemDescriptionView;

    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        this.itemID = intent.getExtras().getString("itemID");
        this.listID = intent.getExtras().getString("listID");
        this.itemTitle = intent.getExtras().getString("itemTitle");

        setTitle(itemTitle);
        setContentView(R.layout.activity_view_my_item);
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

        this.itemImage = (ImageView)findViewById(R.id.my_item_image);
        this.itemTitleView = (TextView) findViewById(R.id.my_item_title);
        this.itemDescriptionView = (TextView) findViewById(R.id.my_item_description);
        setupItem();
    }

    private void setupItem(){
        NetworkManager.getInstance().makeGetItemRequest(this.itemID, new ItemCallback(){
            @Override
            public void callback() {
                ImageLoaderUtil imageLoader = new ImageLoaderUtil();
                InventoryItem item = getItem();
                currentItem = item;
                // load image
                imageLoader.loadImage(item.getImageURL(), itemImage, 500);
                // load title
                itemTitleView.setText(item.getTitle());
                // load description
                itemDescriptionView.setText(item.getDescription());

            }
        });
    }

    public void editItem(View view){
        Intent i = new Intent(ViewMyItemActivity.this, EditItemActivity.class);
        i.putExtra("itemID", itemID);
        i.putExtra("listID", listID);
        i.putExtra("imageURL", currentItem.getImageURL());
        i.putExtra("title", currentItem.getTitle());
        i.putExtra("description", currentItem.getDescription());
        startActivity(i);
    }

    public void removeItem(View view){
        NetworkManager.getInstance().makeDeleteItemRequest(itemID, new GenericCallback() {
            @Override
            public void callback() {
                showResultMessage("Item Removed");
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
