package com.tbd.appprototype;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import model.InventoryList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<InventoryList> lists;
    private ArrayAdapter<InventoryList> adapter;

    private int currentSelectedItem = -1;
    private String currentItemId;
    private View currentSelectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // set up list view
        this.setUpListView();

        // set up firebase connection
        this.setUpFireBase();
    }

    /**
     * set up the list view
     */
    private void setUpListView(){
        this.lists = new ArrayList<InventoryList>();

        // create the adapter and override its getView method, we need this to change the text color
        this.adapter = new ArrayAdapter<InventoryList>(this,
                android.R.layout.simple_list_item_1,
                this.lists){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * set up the firebase connection
     */
    private void setUpFireBase(){
        Firebase firebaseRef;

        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase("https://luminous-torch-6850.firebaseio.com/chatty");
        firebaseRef.addChildEventListener(this.childEventListener);
    }

    /**
     * fire base event listeners
     */
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            updateList(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            updateList(dataSnapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
           // updateList(dataSnapshot);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
           // updateList(dataSnapshot);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };

    /**
     * update the list view
     * @param data
     */
    private void updateList(DataSnapshot data){
        InventoryList list = new InventoryList();
        try {
            if(!data.hasChildren()){
                return;
            }
            list.setListID(data.getKey());
            for (DataSnapshot listSnapshot : data.getChildren()) {
                if(listSnapshot.hasChildren()){
                    //  adjustData(messageSnapshot, adjustmentType);
                    return;
                }

                list.setValue("title", "This is a title");
                list.setValue("type", "Movies");
                // list.setValue(listSnapshot.getKey(), (String) listSnapshot.getValue());
            }
            this.adapter.add(list);
        }
        catch(Exception e){

        }
    }

    /**
     * set up list item click event
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String listID = lists.get(position).getListID();
            // do intent here with the listID stored as prop.
        }
    };


    /**
     * go to personal profile
     * @param view
     */
    public void goToProfile(View view){

    }

    /**
     * go to friends list
     * @param view
     */
    public void goToFriends(View view){

    }

    /**
     * go to profile settings
     * @param view
     */
    public void goToSettings(View view){

    }
}
