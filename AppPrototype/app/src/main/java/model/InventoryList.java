package model;

import java.util.HashMap;

import definitions.ListType;

/**
 * Created by Ethan on 3/10/2016.
 */
public class InventoryList extends ModelObject {

    private String title;
    private String type;
    private String listID;
    private String userID;
    private String iconURL;


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
    public HashMap<String, String> toHashMap() {
        HashMap<String, String> map = new HashMap<>();

        return map;
    }
}
