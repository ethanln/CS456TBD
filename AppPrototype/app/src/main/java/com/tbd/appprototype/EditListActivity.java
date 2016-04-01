package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import model.InventoryItem;
import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.CustomImageUtil;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class EditListActivity extends AppCompatActivity {

    private String listID;
    private String listName;
    private String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        this.listID = intent.getExtras().getString("listID");
        this.listName = intent.getExtras().getString("listName");
        this.listType = intent.getExtras().getString("listType");

        setTitle("Edit " + listName);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
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

        fillInFields();
    }

    private void fillInFields(){
        Intent intent = getIntent();

        TextView titleView = (TextView)findViewById(R.id.list_title_textbox);
        Spinner category = (Spinner)findViewById(R.id.category_list_items);

        titleView.setText(this.listName);
        Bitmap bm = null;
        switch(this.listType){
            case "Movies":
                category.setSelection(0);
                break;
            case "Board Games":
                category.setSelection(1);
                break;
            case "Video Games":
                category.setSelection(2);
                break;
            case "Music":
                category.setSelection(3);
                break;
            case "Books":
                category.setSelection(4);
                break;
        }
    }

    public void onSubmit(View view){

        // get inputs
        TextView titleView = (TextView)findViewById(R.id.list_title_textbox);
        Spinner category = (Spinner)findViewById(R.id.category_list_items);
        if(titleView.getText().toString().equals("")){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Must provide a title");
            return;
        }
        // start loading screen
        LoadingScreenUtil.start(EditListActivity.this, "Saving changes...");

        //get category
        String type = category.getItemAtPosition(category.getSelectedItemPosition()).toString();

        TBDApplication app = (TBDApplication)getApplication();

        // copy to new list instance
        InventoryList list = new InventoryList();
        list.setImageURL("");
        list.setTitle(titleView.getText().toString());
        list.setType(type);
        list.setListID(this.listID);
        list.setUserID(app.getCurrentUserID());


        NetworkManager.getInstance().makeUpdateListRequest(list, new GenericCallback() {
            @Override
            public void callback() {
                LoadingScreenUtil.setEndMessage(getApplicationContext(), "Changes saved");
                onBackPressed();
                LoadingScreenUtil.end();
            }
        });
    }

}
