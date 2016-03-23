package com.tbd.appprototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.AddFriendsAdapter;
import adapter.FriendsAdapter;
import model.FriendRequest;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.UsersCallback;

public class AddFriendActivity extends AppCompatActivity {

    private ArrayList<User> users;
    private ArrayList<User> matchingUsers;
    private ListView searchResultsView;
    private Toast toast;
    private AddFriendsAdapter addFriendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
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

        this.searchResultsView = (ListView)findViewById(R.id.search_results);
        this.users = new ArrayList<User>();

        getUsers();
        EditText searchBar = (EditText) findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(this.textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            String value = s.toString();
            matchingUsers = new ArrayList<User>();
            TBDApplication app = (TBDApplication)getApplication();
            if(value.length() != 0) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().contains(value)
                            && !app.getCurrentUser().getFriendIDs().containsValue(users.get(i).getUserID())
                            && !users.get(i).getUserID().equals(app.getCurrentUserID())) {
                        matchingUsers.add(users.get(i));
                    }
                }
            }
            loadResults(matchingUsers);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    private void loadResults(ArrayList<User> results){
        this.addFriendsAdapter = new AddFriendsAdapter(this, results, this.addClickListener);
        searchResultsView.setAdapter(addFriendsAdapter);
    }

    private void getUsers(){
        NetworkManager.getInstance().makeGetUsersAsListRequest(new UsersCallback() {
            @Override
            public void callback() {
                users.clear();
                users = new ArrayList<User>();

                ArrayList<User> result = getUsers();
                users.addAll(result);
            }
        });
    }
    public void searchFriends(View view){
        // IMPLEMENT
    }

    View.OnClickListener addClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            TBDApplication app = (TBDApplication)getApplication();
            User user = app.getCurrentUser();

            final ViewGroup row = (ViewGroup)v.getParent();
            TextView friendId = (TextView)row.findViewById(R.id.friend_id);

            FriendRequest friendRequest = new FriendRequest("", friendId.getText().toString(), user.getUserID(), user.getImageURL(), user.getUsername());

            NetworkManager.getInstance().makeCreateFriendRequestRequest(friendRequest, new GenericCallback() {
                @Override
                public void callback() {
                    Button btn = (Button)row.findViewById(R.id.add_friend_icon);
                    btn.setText("Friend Request Sent");
                    btn.setBackgroundColor(Color.GRAY);
                    showResultMessage("Friend Request Sent");
                }
            });
        }
    };

    private void showResultMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
