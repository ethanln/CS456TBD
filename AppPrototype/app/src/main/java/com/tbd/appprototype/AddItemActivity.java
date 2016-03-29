package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.CustomImageUtil;
import util.UIMessageUtil;

public class AddItemActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int PIC_CROP = 2;

    private boolean isLoading;
    private String newImageBinary;
    private String listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Item");
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

        Intent intent = getIntent();
        this.listID = intent.getExtras().getString("listID");
        this.newImageBinary = "";
        this.isLoading = false;
    }

    public void onSubmit(View view){

        if(this.isLoading){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Item is being added...");
            return;
        }

        // set is loading state.
        TBDApplication app = (TBDApplication) getApplication();

        // if user is not logged in
        if(app.getCurrentUser() == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "No User Logged In");
            return;
        }

        TextView itemName = (TextView)findViewById(R.id.itemName);
        TextView itemDescription = (TextView)findViewById(R.id.itemDescription);

        //String userId = app.getCurrentUser().getUserID();

        String listId = this.listID;
        String itemId = "";
        String name = itemName.getText().toString();
        String description = itemDescription.getText().toString();
        String image = this.newImageBinary;

        if(name.length() == 0) {
            UIMessageUtil.showResultMessage(getApplicationContext(), "Please provide name");
            return;
        }
        if(description.length() == 0){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Please provide description");
            return;
        }

        this.isLoading = true;

        InventoryItem item = new InventoryItem(itemId, name, description, image, listId);

        NetworkManager.getInstance().makeCreateItemRequest(item, new GenericCallback() {
            @Override
            public void callback() {
                UIMessageUtil.showResultMessage(getApplicationContext(), "Item Added");
                // isLoading state set to false.
                isLoading = false;
                switchIntent();
            }
        });
        
    }

    private void switchIntent(){
        onBackPressed();
    }


    public void addImage(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // get camera results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");

                imageBitmap = CustomImageUtil.squareImage(imageBitmap);

                this.newImageBinary = ConvertToBlobUtil.convertToBlob(imageBitmap, "png", getApplicationContext());

                ImageView image = (ImageView)findViewById(R.id.prev_image);
                BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                imageLoader.loadImage(this.newImageBinary, image, 0);
            }
        }
    }
}
