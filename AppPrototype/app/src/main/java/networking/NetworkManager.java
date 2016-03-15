package networking;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.firebase.client.ChildEventListener;
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
import networking.callback.Callback;
import networking.callback.GenericCallback;
import networking.callback.ItemCallback;
import networking.callback.ListCallback;
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
                        HashMap<String, Object> friendIDs = (HashMap<String, Object>) userData.getValue();
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

        addNoDataAvailableListener(query, callback);
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
                        HashMap<String, Object> friendIDs = (HashMap<String, Object>) userData.getValue();
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
                        HashMap<String, Object> friendIDs = (HashMap<String, Object>) userData.getValue();
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
     * get users with id's
     * @param callback
     */
    public void makeGetUsersAsListWithIdsRequest(final HashMap<String, Object> ids, final UsersCallback callback) {
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
                        HashMap<String, Object> friendIDs = (HashMap<String, Object>) userData.getValue();
                        user.setFriendIDs(friendIDs);
                    }
                }
                // if the user ID does not match, do not add it to callback
                if(!ids.containsValue(user.getUserID())){
                    return;
                }

                // if callback users are null
                if (callback.getUsers() == null) {
                    callback.setUsers(new ArrayList<User>());
                }

                // add user to callback
                callback.getUsers().add(user);

                if(callback.getUsers().size() == ids.size()) {
                    callback.callback();
                }
            }
        });
    }

    /**
     * Get User by ID
     * @param userID
     * @return User
     */
    public void makeGetUserRequest(String userID, final UserCallback callback) {
        Firebase userRef = new Firebase(usersEndpoint);
        Query query = userRef.orderByChild("id").equalTo(userID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
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
                        HashMap<String, Object> friendIDs = (HashMap<String, Object>) userData.getValue();
                        user.setFriendIDs(friendIDs);
                    }
                }
                callback.setUser(user);
                callback.callback();
            }
        });

        addNoDataAvailableListener(query, callback);
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

    // FRIENDS
    // -------

    // Returns list of userIDs, use makeGetUserRequest to query users.
    public void makeGetFriendsRequest(final ArrayList<String> friendIDs, final GenericCallback callback) {
        String userID = application.getCurrentUserID();
        Firebase friendsRef = new Firebase(usersEndpoint + userID + "/friendIDs");
        Query query = friendsRef.orderByKey();
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                String friendID = data.getValue().toString();
                friendIDs.add(friendID);
                if( callback != null ) {
                    callback.data = "Total Friends: " + friendIDs.size() + "\n";
                    for (String id : friendIDs) {
                        callback.data += id + "\n";
                    }
                    callback.callback();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String removedID = dataSnapshot.getValue().toString();
                friendIDs.remove(removedID);
                if( callback != null ) {
                    callback.data = "Total Friends: " + friendIDs.size() + "\n";
                    for (String id : friendIDs) {
                        callback.data += id + "\n";
                    }
                    callback.callback();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
        addNoDataAvailableListener(query, callback);
    }

    public void makeGetFriendsRequest(final HashMap<String, Object> friendIDs, final GenericCallback callback) {
        String userID = application.getCurrentUserID();
        Firebase friendsRef = new Firebase(usersEndpoint + userID + "/friendIDs");
        Query query = friendsRef.orderByKey();
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                String friendID = data.getValue().toString();
                friendIDs.put(friendID, friendID);
                if( callback != null ) {
                    callback.data = "Total Friends: " + friendIDs.size() + "\n";
                    callback.callback();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String removedID = dataSnapshot.getValue().toString();
                friendIDs.remove(removedID);
                if( callback != null ) {
                    callback.data = "Total Friends: " + friendIDs.size() + "\n";
                    callback.callback();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
        addNoDataAvailableListener(query, callback);
    }

    public void makeAddFriendRequest(String friendID, final GenericCallback callback) {
        if (friendID.equals(application.getCurrentUserID())) {
            callback.error = new FirebaseError(-2, "Cannot add self as friend!");
            callback.callback();
            return;
        }
        Firebase friendsRef = new Firebase(usersEndpoint + application.getCurrentUserID() + "/friendIDs");
        final Firebase newFriend = friendsRef.child(friendID);
        newFriend.setValue(friendID, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newFriend.getKey();
                callback.callback();
            }
        });

    }

    public void makeRemoveFriendRequest(String friendID, final GenericCallback callback) {
        final Firebase friendsRef = new Firebase(usersEndpoint + application.getCurrentUserID() + "/friendIDs");
        friendsRef.child(friendID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.firebase = firebase;
                callback.error = firebaseError;
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
    public void makeCreateListRequest(InventoryList list, final GenericCallback callback) {
        final Firebase newList = new Firebase(listsEndpoint).push();
        if (list.getListID().equals("")) {
            list.setListID(newList.getKey());
        }
        if (list.getUserID().equals("")) {
            if (application != null) {
                if (application.getCurrentUser() != null) {
                    list.setUserID(application.getCurrentUserID());
                } else {
                    list.setUserID("No User Found - Should not allow");
                }
            }
        }
        newList.setValue(list.toHashMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newList.getKey();
                callback.callback();
            }
        });
    }

    /**
     * Get all Lists for logged in User
     * @return All InventoryLists
     */
    public void makeGetListsRequest(final ArrayAdapter<InventoryList> adapter, final GenericCallback callback) {
        if (application.getCurrentUser() == null) {
            callback.error = new FirebaseError(-2, "No User Logged In");
            callback.callback();
            return;
        }
        final Firebase listRef = new Firebase(listsEndpoint);
        String userID = application.getCurrentUserID();

        Query query = listRef.orderByChild("userID").equalTo(userID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryList list = new InventoryList();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        list.setListID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        list.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        list.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("type")) {
                        list.setType(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("userID")) {
                        list.setUserID(listData.getValue().toString());
                    }

                }

                if (list.validate()) {
                    adapter.add(list);
                }
                if (callback != null) {
                    String totalLists = String.valueOf(adapter.getCount());
                    callback.data = "Total Lists: " + totalLists;
                    callback.callback();
                }
            }
        });
    }

    /**
     * Get Lists for specific user
     * @param userID
     * @param adapter
     * @param callback
     */
    public void makeGetListsForUserRequest(String userID, final ArrayAdapter<InventoryList> adapter, final GenericCallback callback) {
        final Firebase listRef = new Firebase(listsEndpoint);
        if (application.getCurrentUser() == null) {
            Log.w("makeGetListsRequest", "No User Logged In. Aborting.");
            return;
        }
        Query query = listRef.orderByChild("userID").equalTo(userID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryList list = new InventoryList();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        list.setListID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        list.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        list.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("type")) {
                        list.setType(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("userID")) {
                        list.setUserID(listData.getValue().toString());
                    }

                }

                if(!list.getListID().equals("")
                        && !list.getTitle().equals("")
                        && !list.getType().equals("")
                        && !list.getUserID().equals("")) {
                    adapter.add(list);
                }
                if (callback != null) {
                    String totalLists = String.valueOf(adapter.getCount());
                    callback.data = "Total Lists: " + totalLists;
                    callback.callback();
                }
            }
        });
    }


    /**
     * Get List by ID
     * @param listID
     * @return
     */
    public void makeGetListRequest(String listID, final ListCallback callback) {
        Firebase listRef = new Firebase(listsEndpoint);
        Query query = listRef.orderByChild("id").equalTo(listID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryList list = new InventoryList();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        list.setListID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        list.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        list.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("type")) {
                        list.setType(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("userID")) {
                        list.setUserID(listData.getValue().toString());
                    }

                }

                if (list.validate()) {
                    callback.setList(list);
                }
                callback.callback();
            }
        });

        addNoDataAvailableListener(query, callback);
    }

    /**
     * Update List
     * @param list
     */
    public void makeUpdateListRequest(InventoryList list, final GenericCallback callback) {
        Firebase listRef = new Firebase(listsEndpoint);
        HashMap<String, Object> listMap = list.toHashMap();
        listMap.remove("id");
        Firebase listWithIDRef = listRef.child(list.getListID());
        listWithIDRef.updateChildren(listMap, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.firebase = firebase;
                callback.error = firebaseError;
                callback.callback();
            }
        });
    }

    /**
     * Delete List by ID
     * @param listID
     */
    public void makeDeleteListRequest(String listID, final GenericCallback callback) {
        Firebase listRef = new Firebase(listsEndpoint);
        if (listID.equals("")) {
            callback.error = new FirebaseError(-2, "No ListID Given");
            callback.callback();
            return;
        }
        listRef.child(listID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.callback();
            }
        });
    }

    // ITEMS
    // -----

    /**
     * Create Item
     * @param item - item to add
     */
    public void makeCreateItemRequest(InventoryItem item, final GenericCallback callback) {
        if (item.getListID().equals("")) {
            callback.error = new FirebaseError(-2, "No ListID Given in Item");
            callback.callback();
            return;
        }
        final Firebase newItem = new Firebase(itemsEndpoint).push();
        if (item.getItemID().equals("")) {
            item.setItemID(newItem.getKey());
        }
        newItem.setValue(item.toHashMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newItem.getKey();
                callback.callback();
            }
        });
    }

    /**
     * Get all Items
     */
    public void makeGetItemsRequest(String listID, final ArrayAdapter<InventoryItem> adapter, final GenericCallback callback) {
        if (listID.equals("")) {
            callback.error = new FirebaseError(-2, "No ListID Given in Item");
            callback.callback();
            return;
        }
        Firebase itemRef = new Firebase(itemsEndpoint);
        Query query = itemRef.orderByChild("listID").equalTo(listID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryItem item = new InventoryItem();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        item.setItemID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        item.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        item.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("description")) {
                        item.setDescription(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("listID")) {
                        item.setListID(listData.getValue().toString());
                    }

                }

                if (item.validate()) {
                    adapter.add(item);
                }
                if (callback != null) {
                    String totalItems = String.valueOf(adapter.getCount());
                    callback.data = "Total Itesm: " + totalItems;
                    callback.callback();
                }
            }
        });

    }

    /**
     * Get all Items
     */
    public void makeGetItemsRequest(String listID, final ArrayList<InventoryItem> list, final GenericCallback callback) {
        if (listID.equals("")) {
            callback.error = new FirebaseError(-2, "No ListID Given in Item");
            callback.callback();
            return;
        }
        Firebase itemRef = new Firebase(itemsEndpoint);
        Query query = itemRef.orderByChild("listID").equalTo(listID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryItem item = new InventoryItem();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        item.setItemID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        item.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        item.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("description")) {
                        item.setDescription(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("listID")) {
                        item.setListID(listData.getValue().toString());
                    }

                }

                if (item.validate()) {
                    list.add(item);
                }

                if (callback != null) {
                    String totalItems = String.valueOf(list.size());
                    callback.data = "Total Itesm: " + totalItems;
                    callback.callback();
                }
            }
        });

    }

    /**
     * Get Item by ID
     * @param itemID
     * @return
     */
    public void makeGetItemRequest(String itemID, final ItemCallback callback) {
        Firebase itemRef = new Firebase(itemsEndpoint);
        Query query = itemRef.orderByChild("id").equalTo(itemID);
        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                InventoryItem item = new InventoryItem();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        item.setItemID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageURL")) {
                        item.setImageURL(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("title")) {
                        item.setTitle(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("description")) {
                        item.setDescription(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("listID")) {
                        item.setListID(listData.getValue().toString());
                    }

                }

                if(item.validate()) {
                    callback.setItem(item);
                }
                callback.callback();
            }
        });
        addNoDataAvailableListener(query, callback);
    }

    /**
     * Update Item
     * @param item
     */
    public void makeUpdateItemRequest(InventoryItem item, final GenericCallback callback) {
        Firebase itemRef = new Firebase(itemsEndpoint);
        HashMap<String, Object> itemMap = item.toHashMap();
        itemMap.remove("id");
        Firebase itemWithIDRef = itemRef.child(item.getItemID());
        itemWithIDRef.updateChildren(itemMap, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.firebase = firebase;
                callback.error = firebaseError;
                callback.callback();
            }
        });
    }

    /**
     * Delete Item by ID
     * @param itemID
     */
    public void makeDeleteItemRequest(String itemID, final GenericCallback callback) {
        Firebase itemRef = new Firebase(itemsEndpoint);
        if (itemID.equals("")) {
            callback.error = new FirebaseError(-2, "No ItemID Given");
            callback.callback();
            return;
        }
        itemRef.child(itemID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.callback();
            }
        });
    }

    // HELPERS
    // -------

    private void addNoDataAvailableListener(Query query, final Callback callback) {
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

}
