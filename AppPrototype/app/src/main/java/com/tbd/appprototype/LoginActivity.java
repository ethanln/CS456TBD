package com.tbd.appprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view){
        LoadingScreenUtil.start(LoginActivity.this, "Logging in...");
        // get credential components
        TextView email = (TextView)findViewById(R.id.email_address);
        TextView password = (TextView)findViewById(R.id.password);

        // get credential text
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        // log on
        NetworkManager.getInstance().makeLoginUserRequest(userEmail, userPassword, new GenericCallback() {
            @Override
            public void callback() {
                TBDApplication app = (TBDApplication) getApplication();
                User user = app.getCurrentUser();
                if (user != null) {
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Logged In");
                    switchIntent();

                } else {
                    // if User login fails
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Invalid Username / Password");
                    LoadingScreenUtil.end();
                }

            }
        });
    }

    /**
     * go to the main screen
     */
    private void switchIntent(){
        Intent intent = new Intent(this, MyListsActivity.class);
        startActivity(intent);
    }

    public void goToNetworkTests(View view) {
        Intent intent = new Intent(this, NetworkTestActivity.class);
        startActivity(intent);
    }

    public void goToCreateUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

}
