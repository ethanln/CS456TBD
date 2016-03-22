package com.tbd.appprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FriendRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
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
            Intent intent = new Intent(FriendRequestsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friends){
            Intent intent = new Intent(FriendRequestsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_my_lists){
            Intent intent = new Intent(FriendRequestsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_item_requests){
            Intent intent = new Intent(FriendRequestsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_friend_requests){
            Intent intent = new Intent(FriendRequestsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            TBDApplication app = (TBDApplication)getApplication();
            app.setCurrentUser(null);
            startActivity(new Intent(FriendRequestsActivity.this, LoginActivity.class));
        }
        return false;
    }

}
