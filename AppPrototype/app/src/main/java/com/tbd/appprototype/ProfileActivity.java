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

        if(app.getCurrentUser().getImageURL().length() == 0) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", this);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, image, 550);
        }
        else{
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(app.getCurrentUser().getImageURL(), image, 550);
        }
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


    // display result message
    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
