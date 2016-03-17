package com.tbd.appprototype;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.ImageLoaderUtil;

public class ProfileActivity extends AppCompatActivity {

    private Toast toast;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        this.setProfileInfo();
    }

    /**
     * set profile info
     */
    private void setProfileInfo(){
        TBDApplication app = (TBDApplication)getApplication();

        if(app.getCurrentUser() == null){
            // go back to login if no user is available.
            showResultMessage("User no longer logged in");
            this.switchIntent();
        }
        // set profile name
        TextView nameTitle = (TextView)findViewById(R.id.label_name);
        nameTitle.setText(app.getCurrentUser().getUsername());

        // set profile image
        ImageView image = (ImageView)findViewById(R.id.profile_image);
        BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
        imageLoader.loadImage(app.getCurrentUser().getImageURL(), image, 550);
    }


    private void switchIntent(){
        Intent intent = new Intent(null, LoginActivity.class);
        startActivity(intent);
    }

    // activate camera intent
    public void changeProfilePhoto(View view){
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

                String encodedImage = ConvertToBlobUtil.convertToBlob(imageBitmap, "png", getApplicationContext());

                ImageView image = (ImageView)findViewById(R.id.profile_image);
                BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                imageLoader.loadImage(encodedImage, image, 550);

                // save encodedImage in ImageURL for user
                TBDApplication app = (TBDApplication)getApplication();
                User user = app.getCurrentUser();
                user.setImageURL(encodedImage);
                NetworkManager.getInstance().makeUpdateUserRequest(user, new GenericCallback() {
                    @Override
                    public void callback() {
                        showResultMessage("Profile picture changed");
                    }
                });
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

    // display result message
    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
