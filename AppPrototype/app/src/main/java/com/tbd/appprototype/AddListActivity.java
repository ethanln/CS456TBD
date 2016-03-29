package com.tbd.appprototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;

import model.InventoryList;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class AddListActivity extends AppCompatActivity {

    //private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
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
        //this.isLoading = false;
    }

    public void onSubmit(View view){

        LoadingScreenUtil.start(AddListActivity.this, "Adding List");

        TBDApplication app = (TBDApplication) getApplication();

        // if user is not logged in
        if(app.getCurrentUser() == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "No User Logged In");
            return;
        }

        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        Spinner categoryItems = (Spinner)findViewById(R.id.categoryItems);

        // get new list info
        String userId = app.getCurrentUser().getUserID();
        String title = txtTitle.getText().toString();
        String type = categoryItems.getItemAtPosition(categoryItems.getSelectedItemPosition()).toString();

        // create new list instance
        InventoryList newList = new InventoryList(userId, title, type, "");

        // add new list
        NetworkManager.getInstance().makeCreateListRequest(newList, new GenericCallback() {
            @Override
            public void callback() {
                LoadingScreenUtil.setEndMessage(getApplicationContext(), "List Added");
                switchIntent();
                LoadingScreenUtil.end();
            }
        });
    }

    private void switchIntent(){
        onBackPressed();
    }
}
