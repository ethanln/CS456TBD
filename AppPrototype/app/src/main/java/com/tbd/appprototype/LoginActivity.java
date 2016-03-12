package com.tbd.appprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

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
        String actualEmail = getResources().getString(R.string.email_cred);
        String actualPass = getResources().getString(R.string.pass_cred);

        if(userEmail.equals(actualEmail)
                && userPassword.equals(actualPass)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void goToNetworkTests(View view) {
        Intent intent = new Intent(this, NetworkTestActivity.class);
        startActivity(intent);
    }

}
