package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import model.InventoryItem;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.CustomImageUtil;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class EditProfileActivity extends AppCompatActivity {

    private String encodedString;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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

        encodedString = "";
        fillInFields();
    }


    private void fillInFields(){
        TBDApplication app = (TBDApplication)getApplication();
        User user = app.getCurrentUser();

        this.encodedString = user.getImageURL();
        ImageView image = (ImageView)findViewById(R.id.prev_image_profile);
        TextView username = (TextView)findViewById(R.id.profile_name_textbox);

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

        username.setText(user.getUsername());
    }

    public void submitChanges(View view){

        TBDApplication app = (TBDApplication)getApplication();
        User user = app.getCurrentUser();

        TextView usernameText = (TextView)findViewById(R.id.profile_name_textbox);
        TextView passwordText = (TextView)findViewById(R.id.profile_password_textbox);
        TextView passwordConfirmText = (TextView)findViewById(R.id.profile_password_confirm_textbox);

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = passwordConfirmText.getText().toString();

        if(!validateData(username, password, passwordConfirm)){
            return;
        }

        LoadingScreenUtil.start(EditProfileActivity.this, "Saving changes...");

        if(password.length() > 0
                && passwordConfirm.length() > 0
                && passwordConfirm.equals(password)){
            user.setPassword(password);
        }

        user.setUsername(username);
        user.setImageURL(this.encodedString);

        NetworkManager.getInstance().makeUpdateUserRequest(user, new GenericCallback() {
            @Override
            public void callback() {
                LoadingScreenUtil.setEndMessage(getApplicationContext(), "Profile settings saved");
                onBackPressed();
                LoadingScreenUtil.end();
            }
        });
    }

    private boolean validateData(String username, String password, String passwordConfirm){
        boolean usernameValid = true;
        boolean passwordValid = true;
        if(username.equals("")){
            UIMessageUtil.showResultMessage(EditProfileActivity.this, "A username must be provided");
            usernameValid = false;
        }

        if(password.length() > 0 ||
                passwordConfirm.length() > 0){
            if(password.equals("")){
                UIMessageUtil.showResultMessage(EditProfileActivity.this, "Please provide password");
                passwordValid = false;
            }
            else if(passwordConfirm.equals("")){
                UIMessageUtil.showResultMessage(EditProfileActivity.this, "Please confirm password");
                passwordValid = false;
            }
            else if(passwordConfirm.equals(password)){
                passwordValid = true;
            }
            else{
                UIMessageUtil.showResultMessage(EditProfileActivity.this, "Passwords do not match");
                passwordValid = false;
            }
        }
        return usernameValid && passwordValid;
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
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");

                imageBitmap = CustomImageUtil.squareImage(imageBitmap);

                this.encodedString = ConvertToBlobUtil.convertToBlob(imageBitmap, "png", getApplicationContext());

                ImageView image = (ImageView)findViewById(R.id.prev_image_profile);
                BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                imageLoader.loadImage(this.encodedString, image, 0);
            }
        }
    }

}
