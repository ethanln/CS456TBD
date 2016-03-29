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
import java.util.Iterator;
import java.util.List;

import model.FriendRequest;
import model.InventoryItem;
import model.InventoryList;
import model.ItemRequest;
import model.User;
import networking.callback.Callback;
import networking.callback.FriendRequestCallBack;
import networking.callback.GenericCallback;
import networking.callback.ItemCallback;
import networking.callback.ItemRequestCallBack;
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
    private static final String friendRequestEndpoint = "https://tbd456.firebaseio.com/friendRequest/";
    private static final String itemRequestEndpoint = "https://tbd456.firebaseio.com/itemRequest/";

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
        if (user.getUserID() == null) {
            user.setUserID(newUser.getKey());
        } else if (user.getUserID().equals("")) {
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

    public void makeAddFriendRequest(String userID, String friendID, final GenericCallback callback) {
        if (friendID.equals(application.getCurrentUserID())) {
            callback.error = new FirebaseError(-2, "Cannot add self as friend!");
            callback.callback();
            return;
        }

        Firebase friendsRef = new Firebase(usersEndpoint + userID + "/friendIDs");
        final Firebase addFriendRef = friendsRef.child(friendID);

        addFriendRef.setValue(friendID, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data += "1";
                callback.callback();
            }
        });

        Firebase otherFriendRef = new Firebase(usersEndpoint + friendID + "/friendIDs");
        final Firebase addForOtherFriend = otherFriendRef.child(userID);

        addForOtherFriend.setValue(userID, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data += "2";
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
     * Get all Lists for logged in User
     * @return All InventoryLists
     */
    public void makeGetListsRequest(final ArrayList<InventoryList> lists, final GenericCallback callback) {
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
                    lists.add(list);
                }
                if (callback != null) {
                    String totalLists = String.valueOf(lists.size());
                    callback.data = "Total Lists: " + totalLists;
                    callback.callback();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot data) {
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
                    lists.remove(list);
                }
                if (callback != null) {
                    String totalLists = String.valueOf(lists.size());
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
     * get lists for specific user with arraylist
     * @param userID
     * @param userLists
     * @param callback
     */
    public void makeGetListsForUserRequest(String userID, final ArrayList<InventoryList> userLists, final GenericCallback callback) {
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
                    userLists.add(list);
                }
                if (callback != null) {
                    String totalLists = String.valueOf(userLists.size());
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

            @Override
            public void onChildChanged(DataSnapshot data, String s) {
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
                    list.remove(item);
                    list.add(item);
                }

                if (callback != null) {
                    String totalItems = String.valueOf(list.size());
                    callback.data = "Total Itesm: " + totalItems;
                    callback.callback();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot data) {
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
                    list.remove(item);
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
        final Firebase itemRef = new Firebase(itemsEndpoint);
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

            @Override
            public void onChildChanged(DataSnapshot data, String s) {
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

                if (callback != null) {
                    callback.callback();
                }
            }
        });
        addNoDataAvailableListener(query, callback);
    }


    // FRIEND REQUESTS
    // -----

    /**
     * Get FriendRequest by ID
     * @param userID
     * @return
     */
    public void makeGetFriendRequestRequest(String userID, final FriendRequestCallBack callback) {
        final Firebase itemRef = new Firebase(friendRequestEndpoint);
        Query query = itemRef.orderByChild("to").equalTo(userID);

        callback.setFriendRequests(new ArrayList<FriendRequest>());

        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                FriendRequest friendRequest = new FriendRequest();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        friendRequest.setID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("to")) {
                        friendRequest.setTo(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("from")) {
                        friendRequest.setFrom(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("fromImage")) {
                        friendRequest.setFromImage(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("fromName")) {
                        friendRequest.setFromName(listData.getValue().toString());
                    }
                }

                if(friendRequest.validate()) {
                    callback.addFriendRequest(friendRequest);
                }
                callback.callback();
            }
        });
        addNoDataAvailableListener(query, callback);
    }

    /**
     * Create Friend Request
     * @param friendRequest - item to add
     */
    public void makeCreateFriendRequestRequest(FriendRequest friendRequest, final GenericCallback callback) {

        if (friendRequest.getFrom().equals("")) {
            callback.error = new FirebaseError(-2, "No From Given in ItemRequest");
            callback.callback();
            return;
        }
        if (friendRequest.getFromName().equals("")) {
            callback.error = new FirebaseError(-2, "No From Name is Given in ItemRequest");
            callback.callback();
            return;
        }
        if (friendRequest.getTo().equals("")) {
            callback.error = new FirebaseError(-2, "No To Given in ItemRequest");
            callback.callback();
            return;
        }

        final Firebase newItem = new Firebase(friendRequestEndpoint).push();
        if (friendRequest.getID().equals("")) {
            friendRequest.setID(newItem.getKey());
        }
        newItem.setValue(friendRequest.toHashMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newItem.getKey();
                callback.callback();
            }
        });
    }

    /**
     * Delete Item by ID
     * @param friendRequestID
     */
    public void makeDeleteFriendRequestRequest(String friendRequestID, final GenericCallback callback) {
        Firebase itemRef = new Firebase(friendRequestEndpoint);
        if (friendRequestID.equals("")) {
            callback.error = new FirebaseError(-2, "No FriendRequestID Given");
            callback.callback();
            return;
        }
        itemRef.child(friendRequestID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.callback();
            }
        });
    }


    // ITEM REQUESTS
    // -----

    /**
     * Get ItemRequest by ID
     * @param userID
     * @return
     */
    public void makeGetItemRequestRequest(String userID, final ItemRequestCallBack callback) {
        final Firebase itemRef = new Firebase(itemRequestEndpoint);
        Query query = itemRef.orderByChild("to").equalTo(userID);

        callback.setItemRequests(new ArrayList<ItemRequest>());

        query.addChildEventListener(new RetrieveDataListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String s) {
                ItemRequest itemRequest = new ItemRequest();
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        itemRequest.setID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("to")) {
                        itemRequest.setTo(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("from")) {
                        itemRequest.setFrom(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("itemID")) {
                        itemRequest.setItemID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageCache")) {
                        itemRequest.setImageCache(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("fromName")) {
                        itemRequest.setFromName(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("itemName")) {
                        itemRequest.setItemName(listData.getValue().toString());
                    }
                }

                if(itemRequest.validate()) {
                    callback.addItemRequest(itemRequest);
                }
                callback.callback();
            }

            /*@Override
            public void onChildRemoved(DataSnapshot data) {
                ItemRequest itemRequest = new ItemRequest();
                callback.setItemRequests(new ArrayList<ItemRequest>());
                for (DataSnapshot listData : data.getChildren()) {

                    if (listData.getKey().equals("id")) {
                        itemRequest.setID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("to")) {
                        itemRequest.setTo(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("from")) {
                        itemRequest.setFrom(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("itemID")) {
                        itemRequest.setItemID(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("imageCache")) {
                        itemRequest.setImageCache(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("fromName")) {
                        itemRequest.setFromName(listData.getValue().toString());
                    }
                    if (listData.getKey().equals("itemName")) {
                        itemRequest.setItemName(listData.getValue().toString());
                    }
                }

                if(itemRequest.validate()) {
                    callback.addItemRequest(itemRequest);
                }
                callback.callback();
            }*/
        });
        addNoDataAvailableListener(query, callback);
    }


    /**
     * Create ItemRequest
     * @param itemRequest - item to add
     */
    public void makeCreateItemRequestRequest(ItemRequest itemRequest, final GenericCallback callback) {
        if (itemRequest.getItemID().equals("")) {
            callback.error = new FirebaseError(-2, "No ItemID Given in ItemRequest");
            callback.callback();
            return;
        }
        if (itemRequest.getFrom().equals("")) {
            callback.error = new FirebaseError(-2, "No From Given in ItemRequest");
            callback.callback();
            return;
        }
        if (itemRequest.getTo().equals("")) {
            callback.error = new FirebaseError(-2, "No To Given in ItemRequest");
            callback.callback();
            return;
        }

        if (itemRequest.getFromName().equals("")) {
            callback.error = new FirebaseError(-2, "No FromName Given in ItemRequest");
            callback.callback();
            return;
        }

        if (itemRequest.getItemName().equals("")) {
            callback.error = new FirebaseError(-2, "No ItemName Given in ItemRequest");
            callback.callback();
            return;
        }

        final Firebase newItem = new Firebase(itemRequestEndpoint).push();
        if (itemRequest.getID().equals("")) {
            itemRequest.setID(newItem.getKey());
        }
        newItem.setValue(itemRequest.toHashMap(), new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.data = newItem.getKey();
                callback.callback();
            }
        });
    }
    /**
     * Delete Item by ID
     * @param itemRequestID
     */
    public void makeDeleteItemRequestRequest(String itemRequestID, final GenericCallback callback) {
        Firebase itemRef = new Firebase(itemRequestEndpoint);
        if (itemRequestID.equals("")) {
            callback.error = new FirebaseError(-2, "No ItemRequestID Given");
            callback.callback();
            return;
        }
        itemRef.child(itemRequestID).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                callback.callback();
            }
        });
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

    public void makeDeleteItemsRequest(final String listID, final GenericCallback callback) {
        Firebase itemRef = new Firebase(itemsEndpoint);
        if (listID.equals("")) {
            callback.error = new FirebaseError(-2, "No ListID Given");
            callback.callback();
            return;
        }

        Firebase listOfObjects = itemRef.getRef();
        listOfObjects.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot querySnapshot) {
                 //querySnapshot.child(listID).getRef().removeValue();
                 Iterator<DataSnapshot> items = querySnapshot.getChildren().iterator();
                 while(items.hasNext()){
                     DataSnapshot item = items.next();
                     String id = item.child("listID").getValue().toString();
                     if(id.equals(listID)){
                         item.getRef().removeValue();
                     }
                     //items.remove();
                 }
                 callback.callback();
             }

             @Override
             public void onCancelled(FirebaseError firebaseError) {

             }
        });

        //itemRef.child(listID).removeValue(new Firebase.CompletionListener() {
        //    @Override
        //    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        //        callback.callback();
        //    }
        //});
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