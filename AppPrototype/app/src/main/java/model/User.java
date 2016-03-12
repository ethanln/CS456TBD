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
    private ArrayList<String> friendIDs;
    private String password;

    public User() {
        this.username = "";
        this.imageURL = "";
        this.friendIDs = new ArrayList<>();
    }

    public User(String username, String imageURL, ArrayList<String> friendIDs) {
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
    }

    public User(String userID, String username, String password, String imageURL, ArrayList<String> friendIDs) {
        this.userID = userID;
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
        this.password = password;
    }

    public User(String username, String password, String imageURL, ArrayList<String> friendIDs) {
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

    public List<String> getFriendIDs() {
        return friendIDs;
    }

    public void setFriendIDs(ArrayList<String> friendIDs) {
        this.friendIDs = friendIDs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!userID.equals("")) {
            map.put("userID", userID);
        }
        map.put("username", username);
        map.put("password", password);
        map.put("imageURL", imageURL);
        map.put("friendIDs", friendIDs);
        return map;
    }
}
