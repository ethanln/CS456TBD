package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class CreateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }


    public void createUser(View view) {
        TextView email = (TextView)findViewById(R.id.username);
        TextView password = (TextView)findViewById(R.id.password);

        // get credential text
        final String userEmail = email.getText().toString();
        final String userPassword = password.getText().toString();

        final User user = new User();
        user.setUsername(userEmail);
        user.setPassword(userPassword);

        // log on
        NetworkManager.inst().makeCreateUserRequest(user, new GenericCallback() {
            @Override
            public void callback() {
                LoadingScreenUtil.start(CreateUserActivity.this, "Creating User...");
                NetworkManager.getInstance().makeLoginUserRequest(user.getUsername(), user.getPassword(), new GenericCallback() {
                    @Override
                    public void callback() {
                        TBDApplication app = (TBDApplication) getApplication();
                        User user = app.getCurrentUser();
                        if (user != null) {
                            // if User login succeeds
                            LoadingScreenUtil.setEndMessage(getApplicationContext(), "User Created");
                            switchIntent();
                            LoadingScreenUtil.end();
                        } else {
                            // if User login fails
                            LoadingScreenUtil.setEndMessage(getApplicationContext(), "Failed to create user");
                            LoadingScreenUtil.end();
                        }
                    }
                });
            }
        });




    }

    private void switchIntent(){
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
    }

}
