package com.tbd.appprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;

public class LoginActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view){
        TextView email = (TextView)findViewById(R.id.email_address);
        TextView password = (TextView)findViewById(R.id.password);

        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        NetworkManager.getInstance().makeLoginUserRequest(userEmail, userPassword, new GenericCallback() {
            @Override
            public void callback() {
                TBDApplication app = (TBDApplication) getApplication();
                User user = app.getCurrentUser();
                if (user != null) {
                    showNetworkTestCompleteToast("Login User Done: " + user.getUserID());
                    switchIntent();
                } else {
                    showNetworkTestCompleteToast("Invalid Username / Password");
                }
            }
        });
    }

    private void switchIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showNetworkTestCompleteToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToNetworkTests(View view) {
        Intent intent = new Intent(this, NetworkTestActivity.class);
        startActivity(intent);
    }

}
