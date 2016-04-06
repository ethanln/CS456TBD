package com.tbd.appprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Network;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.ItemRequestAdapter;
import adapter.ItemRequestItemViewAdapter;
import model.InventoryItem;
import model.ItemRequest;
import model.User;
import networking.NetworkManager;
import networking.callback.GenericCallback;
import networking.callback.ItemCallback;
import networking.callback.ItemRequestCallBack;
import networking.callback.UserCallback;
import util.BlobImageLoaderUtil;
import util.ConvertToBlobUtil;
import util.ImageLoaderUtil;
import util.LoadingScreenUtil;
import util.UIMessageUtil;

public class ViewMyItemActivity extends AppCompatActivity {

    private String itemID;
    private String listID;
    private String itemTitle;

    private InventoryItem currentItem;

    private ImageView itemImage;
    private ImageView lendedToImage;
    private TextView itemTitleView;
    private TextView itemDescriptionView;

    private ArrayList<ItemRequest> itemRequests;
    private ListView listView;
    private ItemRequestItemViewAdapter itemRequestAdapter;
    private boolean isAcceptRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        this.itemID = intent.getExtras().getString("itemID");
        this.listID = intent.getExtras().getString("listID");
        this.itemTitle = intent.getExtras().getString("itemTitle");

        setTitle(itemTitle);
        setContentView(R.layout.activity_view_my_item);
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

        this.currentItem = null;
        this.itemImage = (ImageView)findViewById(R.id.my_item_image);
        this.lendedToImage = (ImageView)findViewById(R.id.lended_to_image);
        this.itemTitleView = (TextView) findViewById(R.id.my_item_title);
        this.itemDescriptionView = (TextView) findViewById(R.id.my_item_description);
        setupItem(this);

        // setting up item requests
        this.itemRequests = new ArrayList<ItemRequest>();
        // get list view
        this.listView = (ListView)findViewById(R.id.list_of_item_requests);

        this.isAcceptRequest = false;
        this.setUpList(this);
    }

    private void setUpList(final ViewMyItemActivity activity){
        // implement here
        NetworkManager.getInstance().makeGetItemRequestByItemIdRequest(this.itemID, new ItemRequestCallBack() {
            @Override
            public void callback() {
                if (getItemRequests() != null && !isAcceptRequest) {
                    LinearLayout layout = (LinearLayout)findViewById(R.id.list_view_layout);
                    if (getItemRequests().size() > 0) {
                        itemRequestAdapter = new ItemRequestItemViewAdapter(activity, getItemRequests(), declineListener, acceptListener);
                        listView.setAdapter(itemRequestAdapter);
                        listView.setOnItemClickListener(onItemClickListener);
                        layout.setVisibility(View.VISIBLE);
                    }
                    else{
                        layout.setVisibility(View.GONE);
                    }

                }
                else{
                    isAcceptRequest = false;
                }
            }
        });
    }

    private View.OnClickListener declineListener = new View.OnClickListener() {
        public void onClick(View v) {
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();

            TextView idView = (TextView)nextParent.findViewById(R.id.item_request_item_view_id);
            TextView posView = (TextView)nextParent.findViewById(R.id.item_request_position_item_view_id);

            final String id = idView.getText().toString();
            final String pos = posView.getText().toString();

            LoadingScreenUtil.start(ViewMyItemActivity.this, "Declining request...");
            NetworkManager.getInstance().makeDeleteItemRequestRequest(id, new GenericCallback() {
                @Override
                public void callback() {
                    itemRequestAdapter.remove(Integer.parseInt(pos));
                    if(itemRequestAdapter.isEmpty()){
                        LinearLayout layout = (LinearLayout)findViewById(R.id.list_view_layout);
                        layout.setVisibility(View.GONE);
                    }
                    LoadingScreenUtil.setEndMessage(ViewMyItemActivity.this, "Request declined");
                    LoadingScreenUtil.end();
                }
            });
        }
    };

    private View.OnClickListener acceptListener = new View.OnClickListener() {
        public void onClick(View v) {
            LoadingScreenUtil.start(ViewMyItemActivity.this, "Accepting Request...");
            ViewGroup row = (ViewGroup)v.getParent();
            ViewGroup nextParent = (ViewGroup)row.getParent();

            TextView idView = (TextView)nextParent.findViewById(R.id.item_request_item_view_id);
            TextView posView = (TextView)nextParent.findViewById(R.id.item_request_position_item_view_id);

            final String id = idView.getText().toString();
            final String pos = posView.getText().toString();
            final int count = itemRequestAdapter.getCount();
            NetworkManager.getInstance().makeGetUserRequest(itemRequestAdapter.get(Integer.parseInt(pos)).getFrom(), new UserCallback() {
                @Override
                public void callback() {
                    final User lendedTo = getUser();
                    if (lendedTo != null) {
                        NetworkManager.getInstance().makeGetItemRequest(itemRequestAdapter.get(Integer.parseInt(pos)).getItemID(), new ItemCallback() {
                            @Override
                            public void callback() {
                                if(isActionFullfilled()){
                                    return;
                                }
                                setIsActionFullfilled(true);
                                InventoryItem item = getItem();
                                if (item != null) {
                                    item.setIsAvailable(false);
                                    item.setLendedTo(lendedTo.getUserID());
                                    item.setLendedToImage(lendedTo.getImageURL());
                                    item.setLendedToName(lendedTo.getUsername());
                                    NetworkManager.getInstance().makeUpdateItemRequest(item, new GenericCallback() {
                                        @Override
                                        public void callback() {
                                            NetworkManager.getInstance().makeDeleteItemRequestsByItemIdRequest(itemID, new GenericCallback() {
                                                @Override
                                                public void callback() {
                                                    isAcceptRequest = true;
                                                    itemRequestAdapter.clear();

                                                    LinearLayout layout = (LinearLayout)findViewById(R.id.list_view_layout);
                                                    layout.setVisibility(View.GONE);

                                                    LoadingScreenUtil.setEndMessage(ViewMyItemActivity.this, "Item Request Approved");
                                                    LoadingScreenUtil.end();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    };

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

    private void setupItem(final Context context){
        NetworkManager.getInstance().makeGetItemRequest(this.itemID, new ItemCallback() {
            @Override
            public void callback() {
                // get item
                InventoryItem item = getItem();
                currentItem = item;
                // load image
                if (item.getImageURL().length() == 0) {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
                    String encodedString = ConvertToBlobUtil.convertToBlob(bm, "png", context);
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(encodedString, itemImage, 550);
                } else {
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(item.getImageURL(), itemImage, 550);
                }

                if(item.getLendedToImage().length() > 0){
                    BlobImageLoaderUtil imageLoader = new BlobImageLoaderUtil();
                    imageLoader.loadImage(item.getLendedToImage(), lendedToImage, 550);
                    LinearLayout layout = (LinearLayout)findViewById(R.id.lended_to_image_wrapper);
                    layout.setVisibility(View.VISIBLE);
                    lendedToImage.setVisibility(View.VISIBLE);
                }
                else{
                    LinearLayout layout = (LinearLayout)findViewById(R.id.lended_to_image_wrapper);
                    layout.setVisibility(View.GONE);
                    lendedToImage.setVisibility(View.GONE);
                }

                // load title
                itemTitleView.setText(item.getTitle());
                // load description
                itemDescriptionView.setText(item.getDescription());

            }
        });
    }

    public void editItem(View view){
        if(currentItem == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
            return;
        }

        Intent i = new Intent(ViewMyItemActivity.this, EditItemActivity.class);
        i.putExtra("itemID", itemID);
        i.putExtra("listID", listID);
        i.putExtra("imageURL", currentItem.getImageURL());
        i.putExtra("title", currentItem.getTitle());
        i.putExtra("description", currentItem.getDescription());

        i.putExtra("lendedTo", currentItem.getLendedTo());
        i.putExtra("lendedToImage", currentItem.getLendedToImage());
        i.putExtra("lendedToName", currentItem.getLendedToName());
        i.putExtra("isAvailable", currentItem.isAvailable());
        i.putExtra("ownerId", currentItem.getOwnerId());
        i.putExtra("ownerName", currentItem.getOwnerName());
        startActivity(i);
    }

    public void removeItem(View view){

        if(currentItem == null){
            UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
            return;
        }
        LoadingScreenUtil.start(ViewMyItemActivity.this, "Removing item...");
        NetworkManager.getInstance().makeDeleteItemRequest(itemID, new GenericCallback() {
            @Override
            public void callback() {
                NetworkManager.getInstance().makeDeleteItemRequestsByItemIdRequest(itemID, new GenericCallback() {
                    @Override
                    public void callback() {
                        LoadingScreenUtil.setEndMessage(ViewMyItemActivity.this, "Item Removed");
                        onBackPressed();
                        LoadingScreenUtil.end();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        getMenuInflater().inflate(R.menu.menu_trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            if(currentItem == null){
                UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
                return false;
            }

            Intent i = new Intent(ViewMyItemActivity.this, EditItemActivity.class);
            i.putExtra("itemID", itemID);
            i.putExtra("listID", listID);
            i.putExtra("imageURL", currentItem.getImageURL());
            i.putExtra("title", currentItem.getTitle());
            i.putExtra("description", currentItem.getDescription());

            i.putExtra("lendedTo", currentItem.getLendedTo());
            i.putExtra("lendedToImage", currentItem.getLendedToImage());
            i.putExtra("lendedToName", currentItem.getLendedToName());
            i.putExtra("isAvailable", currentItem.isAvailable());
            i.putExtra("ownerId", currentItem.getOwnerId());
            i.putExtra("ownerName", currentItem.getOwnerName());
            startActivity(i);
        }
        else if (id == R.id.action_trash) {
            if(currentItem == null){
                UIMessageUtil.showResultMessage(getApplicationContext(), "Still loading information...");
                return false;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewMyItemActivity.this);
            builder.setMessage("Are you sure you want to remove " + currentItem.getTitle() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    LoadingScreenUtil.start(ViewMyItemActivity.this, "Removing Item");

                    NetworkManager.getInstance().makeDeleteItemRequest(itemID, new GenericCallback() {
                        @Override
                        public void callback() {
                            NetworkManager.getInstance().makeDeleteItemRequestsByItemIdRequest(itemID, new GenericCallback() {
                                @Override
                                public void callback() {
                                    LoadingScreenUtil.setEndMessage(ViewMyItemActivity.this, "Item Removed");
                                    onBackPressed();
                                    LoadingScreenUtil.end();
                                }
                            });
                        }
                    });
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public void popupLendedNotificationDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewMyItemActivity.this);
        builder.setMessage(currentItem.getTitle() + " is currently lended to " + currentItem.getLendedToName() + ".").setPositiveButton("Okay", lendedNotificationDialogListener)
                .setNegativeButton("Mark as Returned", lendedNotificationDialogListener).show();
    }


    DialogInterface.OnClickListener lendedNotificationDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    LoadingScreenUtil.start(ViewMyItemActivity.this, "Returning " +  currentItem.getTitle() + "...");
                    currentItem.setLendedTo("");
                    currentItem.setLendedToImage("");
                    currentItem.setLendedToName("");
                    currentItem.setIsAvailable(true);

                    NetworkManager.getInstance().makeReturnItemRequest(currentItem, new GenericCallback() {
                        @Override
                        public void callback() {
                            LoadingScreenUtil.setEndMessage(getApplicationContext(), currentItem.getTitle() + " Returned");
                            LoadingScreenUtil.end();
                        }
                    });
            }
        }
    };
}
