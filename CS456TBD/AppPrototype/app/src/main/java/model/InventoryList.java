package model;

import java.util.HashMap;

import definitions.ListType;

/**
 * Created by Ethan on 3/10/2016.
 */
public class InventoryList extends ModelObject {

    private String listID;
    private String userID;
    private String title;
    private String type;
    private String imageURL;

    public InventoryList() {
        listID = "";
        userID = "";
        title = "";
        type = "";
        imageURL = "";
    }

    public InventoryList(String userID, String title, String type, String imageURL) {
        this.listID = "";
        this.userID = userID;
        this.title = title;
        this.type = type;
        this.imageURL = imageURL;
    }

    public InventoryList(String listID, String userID, String title, String type, String imageURL) {
        this.listID = listID;
        this.userID = userID;
        this.title = title;
        this.type = type;
        this.imageURL = imageURL;
    }

    public String getTitle(){
        return this.title;
    }

    public String getType(){
        return this.type;
    }

    public String getListID(){
        return this.listID;
    }

    public void setTitle(String _title){
        this.title = _title;
    }

    public void setType(String _type){
        this.type = _type;
    }

    public void setListID(String _id){
        this.listID = _id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * set values of the model
     * @param key
     * @param val
     */
    public void setValue(String key, String val){
        switch(key){
            case "title":
                this.title = val;
                break;
            case "type":
                this.type = val;
                break;
            case "id":
                this.listID = val;
                break;
        }
    }

    /**
     * return formatted string
     * @return
     */
    @Override
    public String toString(){
        String str = "";
        str += this.title + "\n";
        str += this.type + "\n";
        return str;
    }

    public String prettyString(){
        String str = "";
        str += this.title + "\n";
        str += this.type + "\n";
        return str;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!listID.equals("")) {
            map.put("id", listID);
        }
        map.put("title", title);
        map.put("type", type);
        map.put("userID", userID);
        map.put("imageURL", imageURL);
        return map;
    }
}
