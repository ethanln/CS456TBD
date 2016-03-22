package com.tbd.appprototype;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import model.InventoryItem;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;

public class AddItemActivity extends AppCompatActivity {

    private Toast toast;
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
            showResultMessage("Item is being added...");
            return;
        }

        // set is loading state.
        TBDApplication app = (TBDApplication) getApplication();

        // if user is not logged in
        if(app.getCurrentUser() == null){
            showResultMessage("No User Logged In");
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
            showResultMessage("Please provide name");
            return;
        }
        if(description.length() == 0){
            showResultMessage("Please provide description");
            return;
        }

        this.isLoading = true;

        InventoryItem item = new InventoryItem(itemId, name, description, image, listId);

        NetworkManager.getInstance().makeCreateItemRequest(item, new GenericCallback() {
            @Override
            public void callback() {
                showResultMessage("Item Added");
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

                if (imageBitmap.getWidth() >= imageBitmap.getHeight()){

                    imageBitmap = Bitmap.createBitmap(
                            imageBitmap,
                            imageBitmap.getWidth()/2 - imageBitmap.getHeight()/2,
                            0,
                            imageBitmap.getHeight(),
                            imageBitmap.getHeight()
                    );

                }else{

                    imageBitmap = Bitmap.createBitmap(
                            imageBitmap,
                            0,
                            imageBitmap.getHeight()/2 - imageBitmap.getWidth()/2,
                            imageBitmap.getWidth(),
                            imageBitmap.getWidth()
                    );
                }

                this.newImageBinary = ConvertToBlobUtil.convertToBlob(imageBitmap, "png", getApplicationContext());

                ImageView image = (ImageView)findViewById(R.id.prev_image);
                BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                imageLoader.loadImage(this.newImageBinary, image, 0);
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
