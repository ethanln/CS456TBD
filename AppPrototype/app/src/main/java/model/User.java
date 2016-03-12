package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class User {

    private String username;
    private String imageURL;
    private List<String> friendIDs;

    public User() {
        this.username = "";
        this.imageURL = "";
        this.friendIDs = new ArrayList<>();
    }

    public User(String username, String imageURL, List<String> friendIDs) {
        this.username = username;
        this.imageURL = imageURL;
        this.friendIDs = friendIDs;
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

    public void setFriendIDs(List<String> friendIDs) {
        this.friendIDs = friendIDs;
    }
}
