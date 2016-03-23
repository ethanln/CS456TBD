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
import android.view.Menu;
import android.view.MenuItem;
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
import util.SquareImageUtil;
import util.UIMessageUtil;

public class ProfileActivity extends AppCompatActivity {

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
            UIMessageUtil.showResultMessage(getApplicationContext(), "User no longer logged in");
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

                imageBitmap = SquareImageUtil.squareImage(imageBitmap);

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
                        UIMessageUtil.showResultMessage(getApplicationContext(), "Profile picture changed");
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_profile){
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(ProfileActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(ProfileActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            TBDApplication app = (TBDApplication)getApplication();
            app.setCurrentUser(null);
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }
        return false;
    }
}
