package com.tbd.appprototype;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

public class EditItemActivity extends AppCompatActivity {

    private String itemID;
    private String listID;
    private Toast toast;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int PIC_CROP = 2;

    private String encodedString;

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
        this.encodedString = intent.getExtras().getString("imageURL");
        // fill in preexisting values
        fillInFields();
    }

    private void fillInFields(){
        Intent intent = getIntent();

        String title = intent.getExtras().getString("title");
        String description = intent.getExtras().getString("description");

        ImageView image = (ImageView)findViewById(R.id.prev_image_edit);
        TextView titleView = (TextView)findViewById(R.id.item_title_textbox);
        TextView descriptionView = (TextView)findViewById(R.id.item_description_textbox);

        if(this.encodedString.length() == 0) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.upload_image_thumbnail);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", this);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 0);
        }
        else{
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(this.encodedString, image, 0);
        }

        titleView.setText(title);
        descriptionView.setText(description);
    }

    public void submitChanges(View view){
        InventoryItem updatedItem = new InventoryItem();

        TextView titleView = (TextView)findViewById(R.id.item_title_textbox);
        TextView descriptionView = (TextView)findViewById(R.id.item_description_textbox);

        updatedItem.setTitle(titleView.getText().toString());
        updatedItem.setImageURL(this.encodedString);
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

    public void addImageEdit(View view){
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
                // do crop
                Uri picUri = data.getData();
                this.performCrop(picUri);
            }

            if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");

                this.encodedString = ConvertToBlobUtil.convertToBlob(imageBitmap, "png", getApplicationContext());

                ImageView image = (ImageView)findViewById(R.id.prev_image);
                BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                imageLoader.loadImage(this.encodedString, image, 0);
            }
        }
    }

    private void performCrop(Uri picUri){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
