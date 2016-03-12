package networking;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.tbd.appprototype.TBDApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.InventoryItem;
import model.InventoryList;
import model.User;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();

    private static final String usersEndpoint = "https://tbd456.firebaseio.com/users";
    private static final String listsEndpoint = "https://tbd456.firebaseio.com/lists";
    private static final String itemsEndpoint = "https://tbd456.firebaseio.com/items";

    private NetworkManager() {}

    public static NetworkManager getInstance() {
        return instance;
    }

    public static NetworkManager inst() {
        return getInstance();
    }

    // Needed for getting current user
    public TBDApplication application;


    // USERS
    // -----

    /**
     * Create User
     * @param user
     * @return userID in database
     */
    public String makeCreateUserRequest(User user) {
        Firebase newUser = new Firebase(usersEndpoint).push();
        if (user.getUserID().equals("")) {
            user.setUserID(newUser.getKey());
        }
        newUser.setValue(user.toHashMap());
        return newUser.getKey();
    }

    public void makeLoginUserRequest(final String username, final String password) {
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("username").equalTo(username);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                Log.d("LOGIN-ON CHILD ADDED", data.getKey());
                boolean usernameMatches = false;
                boolean passwordMatches = false;
                User currentUser = new User();

                for (DataSnapshot userData : data.getChildren()) {
//                    Log.d("userKey", userData.getKey());
//                    Log.d("userValue", userData.getValue().toString());
                    if (userData.getKey().equals("username") && userData.getValue().equals(username)) {
                        usernameMatches = true;
                        currentUser.setUsername(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("password") && userData.getValue().equals(password)) {
                        passwordMatches = true;
                        currentUser.setPassword(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("id")) {
                        currentUser.setUserID(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("imageURL")) {
                        currentUser.setImageURL(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("friendIDs")) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        ArrayList<String> friendIDs = (ArrayList<String>) userData.getValue();
                        currentUser.setFriendIDs(friendIDs);
                    }

                }
                if (usernameMatches && passwordMatches) {
                    application.setCurrentUser(currentUser);
                    Log.d("CURRENT USER SET", application.getCurrentUser().toHashMap().toString());
                } else {
                    application.setCurrentUser(null);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("onCancelled", firebaseError.getMessage());
                Log.e("onCancelled", firebaseError.getDetails());
            }
        });
    }

    public void makeLogoutUserRequest() {
        application.setCurrentUser(null);
    }

    /**
     * Get all Users
     * @return ArrayList of Users
     */
    public ArrayList<User> makeGetUsersRequest() {
        return new ArrayList<>();
    }

    /**
     * Get User by ID
     * @param userID
     * @return User
     */
    public User makeGetUserRequest(String userID) {
        return new User();
    }

    /**
     * Update User
     * @param user
     */
    public void makeUpdateUserRequest(User user) {

    }

    /**
     * Delete User by ID
     * @param userID
     */
    public void makeDeleteUserRequest(String userID) {

    }

    // LISTS
    // -----

    /**
     * Create List - The current user is the userID used for the list.
     * @param list
     * @return listID
     */
    public String makeCreateListRequest(InventoryList list) {
        Firebase newList = new Firebase(listsEndpoint).push();
        if (list.getListID().equals("")) {
            list.setListID(newList.getKey());
        }
        if (list.getUserID().equals("")) {
            if (application != null) {
                if (application.getCurrentUser() != null) {
                    list.setUserID(application.getCurrentUser().getUserID());
                } else {
                    list.setUserID("No User Found - Should not allow");
                }
            }
        }
        newList.setValue(list.toHashMap());
        return newList.getKey();
    }

    /**
     * Get all Lists
     * @return All InventoryLists
     */
    public ArrayList<InventoryList> makeGetListsRequest() {
        return new ArrayList<>();
    }

    /**
     * Get List by ID
     * @param listID
     * @return
     */
    public InventoryList makeGetListRequest(String listID) {
        return new InventoryList();
    }

    /**
     * Update List
     * @param list
     */
    public void makeUpdateListRequest(InventoryList list) {

    }

    /**
     * Delete List by ID
     * @param listID
     */
    public void makeDeleteListRequest(String listID) {

    }

    // ITEMS
    // -----

    /**
     * Create Item
     * @param item
     * @return ItemID
     */
    public String makeCreateItemRequest(InventoryItem item) {
        Firebase newItem = new Firebase(itemsEndpoint).push();
        if (item.getItemID().equals("")) {
            item.setItemID(newItem.getKey());
        }
        newItem.setValue(item.toHashMap());
        return newItem.getKey();
    }

    /**
     * Get all Items
     * @return
     */
    public ArrayList<InventoryItem> makeGetItemsRequest() {
        return new ArrayList<>();
    }

    /**
     * Get Item by ID
     * @param itemID
     * @return
     */
    public InventoryItem makeGetItemRequest(String itemID) {
        return new InventoryItem();
    }

    /**
     * Update Item
     * @param item
     */
    public void makeUpdateItemRequest(InventoryItem item) {

    }

    /**
     * Delete Item by ID
     * @param itemID
     */
    public void makeDeleteItemRequest(String itemID) {

    }

}
