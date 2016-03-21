package com.tbd.appprototype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
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

        this.currentItem = null;
        this.itemImage = (ImageView)findViewById(R.id.my_item_image);
        this.itemTitleView = (TextView) findViewById(R.id.my_item_title);
        this.itemDescriptionView = (TextView) findViewById(R.id.my_item_description);
        setupItem(this);
    }

    private void setupItem(final Context context){
        NetworkManager.getInstance().makeGetItemRequest(this.itemID, new ItemCallback(){
            @Override
            public void callback() {
                // get item
                InventoryItem item = getItem();
                currentItem = item;
                // load image
                if(item.getImageURL().length() == 0) {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
                    String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(encodedString, itemImage, 550);
                }
                else {
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(item.getImageURL(), itemImage, 550);
                }

                // load title
                itemTitleView.setText(item.getTitle());
                // load description
                itemDescriptionView.setText(item.getDescription());

            }
        });
    }

    public void editItem(View view){
        if(currentItem == null){
            showResultMessage("Still loading information...");
            return;
        }

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
