package com.tbd.appprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class ViewMyItemActivity extends AppCompatActivity {

    private String itemID;
    private String listID;
    private String itemTitle;

    private InventoryItem currentItem;

    private ImageView itemImage;
    private TextView itemTitleView;
    private TextView itemDescriptionView;

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

    public void editItem(View view){
        if(currentItem == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
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
        if(currentItem == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
            return;
        }
        NetworkManager.getInstance().makeDeleteItemRequest(itemID, new GenericCallback() {
            @Override
            public void callback() {
                UIMessageUtil.showResultMessage(getApplicationContext(), "Item Removed");
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        getMenuInflater().inflate(R.menu.menu_trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            if(currentItem == null){
                UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
                return false;
            }

            Intent i = new Intent(ViewMyItemActivity.this, EditItemActivity.class);
            i.putExtra("itemID", itemID);
            i.putExtra("listID", listID);
            i.putExtra("imageURL", currentItem.getImageURL());
            i.putExtra("title", currentItem.getTitle());
            i.putExtra("description", currentItem.getDescription());
            startActivity(i);
        }
        else if (id == R.id.action_trash) {
            if(currentItem == null){
                UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewMyItemActivity.this);
            builder.setMessage("Are you sure you want to remove " + currentItem.getTitle() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    LoadingScreenUtil.start(ViewMyItemActivity.this, "Removing List");

                    NetworkManager.getInstance().makeDeleteItemRequest(itemID, new GenericCallback() {
                        @Override
                        public void callback() {
                            LoadingScreenUtil.setEndMessage(getApplicationContext(), "Item Removed");
                            onBackPressed();
                            LoadingScreenUtil.end();
                        }
                    });
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}
