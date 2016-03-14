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
                    // if User login succeeds
                    showResultMessage("User Logged In");
                    switchIntent();
                } else {
                    // if User login faisl
                    showResultMessage("Invalid Username / Password");
                }
            }
        });
    }

    /**
     * go to the main screen
     */
    private void switchIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * display result message of any action
     * @param message
     */
    private void showResultMessage(String message) {
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