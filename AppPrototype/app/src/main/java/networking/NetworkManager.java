package networking;

import com.firebase.client.Firebase;

import java.util.ArrayList;

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

    // HELPERS
    // -------

    // TODO:

    // USERS
    // -----

    /**
     * Create User
     * @param user
     * @return userID in database
     */
    public String makeCreateUserRequest(User user) {
        Firebase newUser = new Firebase(usersEndpoint).push();


        return "";
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
     * Create List
     * @param list
     * @return listID
     */
    public String makeCreateListRequest(InventoryList list) {
        return "";
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
