package model;

import java.util.HashMap;

/**
 * Created by Ethan on 3/17/2016.
 */
public class ItemRequest extends ModelObject {

    private String id;
    private String to;
    private String from;
    private String itemID;
    private String imageCache;
    private String fromName;
    private String itemName;
    private String fromImage;

    public ItemRequest(){
        this.id = "";
        this.to = "";
        this.from = "";
        this.itemID = "";
        this.imageCache = "";
        this.fromName = "";
        this.itemName = "";
        this.fromName = "";
    }

    public ItemRequest(String _id, String _to, String _from, String _itemID, String _imageCache, String _fromName, String _itemName, String _fromImage){
        this.id = _id;
        this.to = _to;
        this.from = _from;
        this.itemID = _itemID;
        this.imageCache = _imageCache;
        this.fromName = _fromName;
        this.itemName = _itemName;
        this.fromName = _fromImage;
    }

    public void setID(String _id){
        this.id = _id;
    }

    public void setTo(String _to){
        this.to = _to;
    }

    public void setFrom(String _from){
        this.from = _from;
    }

    public void setItemID(String _itemID){
        this.itemID = _itemID;
    }

    public void setImageCache(String _imageCache){
        this.imageCache = _imageCache;
    }

    public void setFromName(String _fromName){
        this.fromName = _fromName;
    }

    public void setItemName(String _itemName){
        this.itemName = _itemName;
    }

    public String getID(){
        return this.id;
    }

    public String getTo(){
        return this.to;
    }

    public String getFrom(){
        return this.from;
    }

    public String getItemID(){
        return this.itemID;
    }

    public String getImageCache(){
        return this.imageCache;
    }

    public String getFromName(){
        return this.fromName;
    }

    public String getItemName(){
        return this.itemName;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!this.id.equals("")) {
            map.put("id", this.id);
        }

        map.put("to", this.to);
        map.put("from", this.from);
        map.put("itemID", this.itemID);
        map.put("imageCache", this.imageCache);
        map.put("fromName", this.fromName);
        map.put("itemName", this.itemName);
        map.put("fromImage", this.fromImage);
        return map;
    }

    @Override
    public String toString(){
        String str = "";
        str += this.id + "\n";
        str += this.to + "\n";
        str += this.from + "\n";
        str += this.itemID + "\n";
        return str;
    }

    public boolean validate(){
        return !this.id.equals("")
                && !this.to.equals("")
                && !this.from.equals("")
                && !this.itemID.equals("")
                && !this.fromName.equals("")
                && !this.itemName.equals("");
    }

    @Override
    public boolean equals(Object obj){
        ItemRequest item = (ItemRequest) obj;
        return item.getID().equals(this.id);
    }

    public String getFromImage() {
        return fromImage;
    }

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }
}
