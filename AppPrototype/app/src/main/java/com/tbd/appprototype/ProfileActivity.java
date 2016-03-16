package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.ImageLoaderUtil;

public class ProfileActivity extends AppCompatActivity {

    private Toast toast;

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
        //ImageLoaderUtil imageLoader = new ImageLoaderUtil();
        //imageLoader.loadImage(app.getCurrentUser().getImageURL(), image, 550);

        String encodedImage = ConvertToBlobUtil.convertToBlob(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.search_icon), "png", getApplicationContext());

        BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
        imageLoader.loadImage(encodedImage, image, 550);
    }

    // display result message
    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void switchIntent(){
        Intent intent = new Intent(null, LoginActivity.class);
        startActivity(intent);
    }
}
