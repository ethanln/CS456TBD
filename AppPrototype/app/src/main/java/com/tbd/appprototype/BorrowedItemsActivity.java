package com.tbd.appprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.ItemBorrowedAdapter;
import adapter.ItemLendedAdapter;
import model.InventoryItem;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.LoadingScreenUtil;

public class BorrowedItemsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<InventoryItem> borrowedItems;
    private ListView listView;
    private ItemBorrowedAdapter borrowedItemsAdapter;

    private final int item_requests_index = 2;
    private final int friend_requests_index = 3;
    private final int lent_items_index = 4;
    private final int borrowed_items_index = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SET UP DRAWER ITEMS
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.setUpNotifications(navigationView);

        TBDApplication app = (TBDApplication) getApplication();

        ImageView profileImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image_drawer);
        // load profile image on the main navigation
        if(app.getCurrentUser().getImageURL().length() > 0) {
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(app.getCurrentUser().getImageURL(), profileImage, 550);
        }
        else{
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", this);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, profileImage, 550);
        }

        // set profile name
        TextView profileName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.profile_name_drawer);
        profileName.setText(app.getCurrentUser().getUsername());

        // set edit profile listener
        LinearLayout editIcon = (LinearLayout)navigationView.getHeaderView(0).findViewById(R.id.drawer_edit_profile_icon_layout);
        editIcon.setOnClickListener(this.editListener);

        //END DRAWER SET UP

        // initialize adapter
        this.borrowedItems = new ArrayList<InventoryItem>();

        // get list view
        this.listView = (ListView)findViewById(R.id.item_borrowed_list);
        this.setUpList(this);
    }

    private void setUpNotifications(final NavigationView navigationView){

        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetItemRequestCountRequest(app.getCurrentUserID(), new GenericCallback() {
            @Override
            public void callback() {
                int count = Integer.parseInt(data);
                Menu menuNav = navigationView.getMenu();
                MenuItem element = menuNav.getItem(item_requests_index);
                setMenuItemLabel(getString(R.string.menu_item_requests), count, element, Color.RED);
            }
        });

        NetworkManager.getInstance().makeGetFriendRequestCountRequest(app.getCurrentUserID(), new GenericCallback() {

            @Override
            public void callback() {
                Menu menuNav = navigationView.getMenu();
                MenuItem element = menuNav.getItem(friend_requests_index);
                int count = Integer.parseInt(data);
                setMenuItemLabel(getString(R.string.menu_friend_requests), count, element, Color.RED);
            }
        });

        NetworkManager.getInstance().makeGetBorrowedItemsCountRequest(app.getCurrentUserID(), new GenericCallback() {

            @Override
            public void callback() {
                Menu menuNav = navigationView.getMenu();
                MenuItem element = menuNav.getItem(borrowed_items_index);
                int count = Integer.parseInt(data);
                setMenuItemLabel(getString(R.string.menu_borrowed_items), count, element, Color.GRAY);
            }
        });

        NetworkManager.getInstance().makeGetLendedItemsCountRequest(app.getCurrentUserID(), new GenericCallback() {

            @Override
            public void callback() {
                Menu menuNav = navigationView.getMenu();
                MenuItem element = menuNav.getItem(lent_items_index);
                int count = Integer.parseInt(data);
                setMenuItemLabel(getString(R.string.menu_lent_items), count, element, Color.GRAY);
            }
        });

    }

    private void setMenuItemLabel(String title, int count, MenuItem element, int color){
        if (count > 0) {
            String counter = Integer.toString(count);
            String s = title + "   " + counter + " ";
            SpannableString sColored = new SpannableString(s);

            sColored.setSpan(new BackgroundColorSpan(color), s.length() - 3, s.length(), 0);
            sColored.setSpan(new ForegroundColorSpan(Color.WHITE), s.length() - 3, s.length(), 0);

            element.setTitle(sColored);
        }
        else{
            element.setTitle(title);
        }
    }

    private View.OnClickListener editListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(BorrowedItemsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        }
    };

    private void setUpList(final BorrowedItemsActivity activity){
        TBDApplication app = (TBDApplication)getApplication();

        NetworkManager.getInstance().makeGetBorrowedItemsRequest(app.getCurrentUser().getUserID(), borrowedItems, new GenericCallback() {
            @Override
            public void callback() {
                if (borrowedItems.size() > 0) {
                    borrowedItemsAdapter = new ItemBorrowedAdapter(activity, borrowedItems);
                    listView.setAdapter(borrowedItemsAdapter);
                    listView.setOnItemClickListener(onItemClickListener);
                }
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String userID = friends.get(position).getUserID();
            //String username = friends.get(position).getUsername();

            //Intent i = new Intent(FriendsActivity.this, FriendsListsActivity.class);
            //i.putExtra("userID", userID);
            //i.putExtra("username", username);
            //startActivity(i);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.my_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.action_profile) {
            Intent intent = new Intent(MyListsActivity.this, ProfileActivity.class);
            startActivity(intent);
        }*/
        if (id == R.id.action_friends) {
            Intent intent = new Intent(BorrowedItemsActivity.this, FriendsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_lended_items) {
            Intent intent = new Intent(BorrowedItemsActivity.this, LendedItemsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_borrowed_items) {
            finish();
            startActivity(getIntent());
        }
        else if (id == R.id.action_my_lists) {
            Intent intent = new Intent(BorrowedItemsActivity.this, MyListsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_item_requests) {
            Intent intent = new Intent(BorrowedItemsActivity.this, ItemRequestsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_friend_requests) {
            Intent intent = new Intent(BorrowedItemsActivity.this, FriendRequestsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_logout) {
            LoadingScreenUtil.start(BorrowedItemsActivity.this, "Logging out...");
            NetworkManager.getInstance().makeLogoutUserRequest(new GenericCallback() {
                @Override
                public void callback() {
                    LoadingScreenUtil.setEndMessage(getApplicationContext(), "Logged out");
                    startActivity(new Intent(BorrowedItemsActivity.this, LoginActivity.class));
                    LoadingScreenUtil.end();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        setupDrawer();

    }

    private void setupDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TBDApplication app = (TBDApplication) getApplication();

        ImageView profileImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image_drawer);
        // load profile image on the main navigation
        if(app.getCurrentUser().getImageURL().length() > 0) {
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(app.getCurrentUser().getImageURL(), profileImage, 550);
        }
        else{
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
            String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", this);
            BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
            imageLoader.loadImage(encodedString, profileImage, 550);
        }

        // set profile name
        TextView profileName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.profile_name_drawer);
        profileName.setText(app.getCurrentUser().getUsername());
    }
}
