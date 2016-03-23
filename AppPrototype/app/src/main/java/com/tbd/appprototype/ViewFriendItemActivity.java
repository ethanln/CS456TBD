package com.tbd.appprototype;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.InventoryItem;
import model.ItemRequest;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ItemCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.UIMessageUtil;

public class ViewFriendItemActivity extends AppCompatActivity {

    private String itemID;
    private String itemTitle;
    private String friendID;

    private InventoryItem currentItem;

    private ImageView itemImage;
    private TextView itemTitleView;
    private TextView itemDescriptionView;

    private Button requestButton;
    private boolean isRequestAlreadySent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        this.itemID = intent.getExtras().getString("itemID");
        this.itemTitle = intent.getExtras().getString("itemTitle");
        this.friendID = intent.getExtras().getString("userID");

        setTitle(this.itemTitle);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_friend_item);
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

        this.itemImage = (ImageView)findViewById(R.id.friend_item_image);
        this.itemTitleView = (TextView) findViewById(R.id.friend_item_title);
        this.itemDescriptionView = (TextView) findViewById(R.id.friend_item_description);
        this.requestButton = (Button)findViewById(R.id.item_request_btn);
        this.isRequestAlreadySent = false;
        setupItem(this);
    }

    private void setupItem(final Context context){
        NetworkManager.getInstance().makeGetItemRequest(this.itemID, new ItemCallback() {
            @Override
            public void callback() {
                // get item
                InventoryItem item = getItem();
                currentItem = item;
                // load image
                if (item.getImageURL().length() == 0) {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
                    String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(encodedString, itemImage, 550);
                } else {
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

    public void requestItem(View view){
        if(isRequestAlreadySent){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Request Already Sent");
            return;
        }
        if(currentItem == null){
            return;
        }

        TBDApplication app = (TBDApplication) getApplication();

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setImageCache(currentItem.getImageURL());
        itemRequest.setItemID(currentItem.getItemID());
        itemRequest.setItemName(currentItem.getTitle());
        itemRequest.setFrom(app.getCurrentUserID());
        itemRequest.setFromName(app.getCurrentUser().getUsername());
        itemRequest.setTo(friendID);

        NetworkManager.getInstance().makeCreateItemRequestRequest(itemRequest, new GenericCallback() {
            @Override
            public void callback() {
                requestButton.setText("Request Sent");
                requestButton.setBackgroundColor(Color.GRAY);
                isRequestAlreadySent = true;
                UIMessageUtil.showResultMessage(getApplicationContext(), "Request Sent");
            }

        });
    }
}
