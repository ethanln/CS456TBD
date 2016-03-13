package networking;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.tbd.appprototype.TBDApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.InventoryItem;
import model.InventoryList;
import model.User;
import networking.callback.GenericCallback;
import networking.callback.UserCallback;
import networking.callback.UsersCallback;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();

    private static final String usersEndpoint = "https://tbd456.firebaseio.com/users/";
    private static final String listsEndpoint = "https://tbd456.firebaseio.com/lists/";
    private static final String itemsEndpoint = "https://tbd456.firebaseio.com/items/";

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
    public void makeCreateUserRequest(User user, final GenericCallback callback) {
        final Firebase newUser = new Firebase(usersEndpoint).push();
        if (user.getUserID().equals("")) {
            user.setUserID(newUser.getKey());
        }
        newUser.setValue(user.toHashMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newUser.getKey();
                callback.callback();
            }
        });
    }

    public void makeLoginUserRequest(final String username, final String password, final GenericCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("username").equalTo(username);

        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                Log.d("LOGIN-ON CHILD ADDED", data.getKey());
                boolean usernameMatches = false;
                boolean passwordMatches = false;
                User currentUser = new User();
                for (DataSnapshot userData : data.getChildren()) {
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
                callback.callback();
            }
        });

        // To Catch if username doesn't exist
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("Data Exists!");
                } else {
                    System.out.println("Data Doesn't Exist!");
                    application.setCurrentUser(null);
                    callback.callback();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void makeLogoutUserRequest(GenericCallback callback) {
        application.setCurrentUser(null);
        callback.callback();
    }

    /**
     * Get all Users
     */
    public void makeGetUsersRequest(final ArrayAdapter<User> adapter, final UsersCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("username");
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                User user = new User();
                for (DataSnapshot userData : data.getChildren()) {
                    if (userData.getKey().equals("username")) {
                        user.setUsername(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("password")) {
                        user.setPassword(userData.getValue().toString()); // Need password to update data
                    }
                    if (userData.getKey().equals("id")) {
                        user.setUserID(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("imageURL")) {
                        user.setImageURL(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("friendIDs")) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                        };
                        ArrayList<String> friendIDs = (ArrayList<String>) userData.getValue();
                        user.setFriendIDs(friendIDs);
                    }
                }
                adapter.add(user);
                if (callback.getUsers() == null) {
                    callback.setUsers(new ArrayList<User>());
                }
                callback.getUsers().add(user);
                callback.callback();
                System.out.println("Added Another User - Total: " + adapter.getCount());
            }
        });
    }

    public void makeGetUsersAsListRequest(final UsersCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("username");
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                User user = new User();
                for (DataSnapshot userData : data.getChildren()) {
                    if (userData.getKey().equals("username")) {
                        user.setUsername(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("password")) {
                        user.setPassword(userData.getValue().toString()); // Need password to update data
                    }
                    if (userData.getKey().equals("id")) {
                        user.setUserID(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("imageURL")) {
                        user.setImageURL(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("friendIDs")) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                        };
                        ArrayList<String> friendIDs = (ArrayList<String>) userData.getValue();
                        user.setFriendIDs(friendIDs);
                    }
                }
                if (callback.getUsers() == null) {
                    callback.setUsers(new ArrayList<User>());
                }
                callback.getUsers().add(user);
                callback.callback();
            }
        });
    }

    /**
     * Get User by ID
     * @param userID
     * @return User
     */
    public void makeGetUserRequest(String userID, final UserCallback callback) {
        System.out.println("makeGetUserRequest");
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("id").equalTo(userID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                Log.d("LOGIN-ON CHILD ADDED", data.getKey());
                User user = new User();
                for (DataSnapshot userData : data.getChildren()) {
                    if (userData.getKey().equals("username")) {
                        user.setUsername(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("password")) {
                        user.setPassword(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("id")) {
                        user.setUserID(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("imageURL")) {
                        user.setImageURL(userData.getValue().toString());
                    }
                    if (userData.getKey().equals("friendIDs")) {
                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                        };
                        ArrayList<String> friendIDs = (ArrayList<String>) userData.getValue();
                        user.setFriendIDs(friendIDs);
                    }
                }
                callback.setUser(user);
                callback.callback();
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("Data Exists!");
                } else {
                    System.out.println("Data Doesn't Exist!");
                    callback.callback();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Update User
     * @param user
     */
    public void makeUpdateUserRequest(User user, final GenericCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        HashMap<String, Object> userMap = user.toHashMap();
        userMap.remove("id"); // Don't want to overwrite userID
        Firebase userWithIDRef = userRef.child(user.getUserID());
        userWithIDRef.updateChildren(userMap, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.firebase = firebase;
                callback.error = firebaseError;
                callback.callback();
            }
        });
    }

    /**
     * Delete User by ID
     * @param userID
     */
    public void makeDeleteUserRequest(String userID, final GenericCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        if (userID == null) {
            callback.error = new FirebaseError(-2, "No UserID Given");
            callback.callback();
            return;
        }
        userRef.child(userID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.callback();
            }
        });
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
