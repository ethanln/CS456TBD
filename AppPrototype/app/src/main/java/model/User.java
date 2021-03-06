package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class User extends ModelObject {

    private String userID;
    private String username;
    private String imageURL;
    private HashMap<String, Object> friendIDs;
    private String password;

    public User() {
        this.username = "";
        this.imageURL = "";
        this.friendIDs = new HashMap<>();
    }

    public User(String username, String imageURL, HashMap<String, Object> friendIDs) {
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
    }

    public User(String userID, String username, String password, String imageURL, HashMap<String, Object> friendIDs) {
        this.userID = userID;
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
        this.password = password;
    }

    public User(String username, String password, String imageURL, HashMap<String, Object> friendIDs) {
        this.userID = "";
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
        this.password = password;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public HashMap<String, Object> getFriendIDs() {
        return friendIDs;
    }

    public void setFriendIDs(HashMap<String, Object> friendIDs) {
        this.friendIDs = friendIDs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFriend(String friendID) {
        this.friendIDs.put(friendID, friendID);
    }

    public void removeFriend(String friendID) {
        this.friendIDs.remove(friendID);
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!userID.equals("")) {
            map.put("id", userID);
        }
        map.put("username", username);
        map.put("password", password);
        map.put("imageURL", imageURL);
        map.put("friendIDs", friendIDs);
        return map;
    }

    @Override
    public String toString(){
        String str = "";
        str += this.username + "\n";
        return str;
    }
}
