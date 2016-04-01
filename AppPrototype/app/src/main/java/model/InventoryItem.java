package model;

import java.util.HashMap;

/**
 * Created by mitch10e on 12 March2016.
 */
public class InventoryItem extends ModelObject {

    private String itemID;
    private String title;
    private String description;
    private String imageURL;
    private String listID;

    private String lendedTo;
    private String lendedToImage;
    private String lendedToName;
    private boolean isAvailable;

    private String ownerId;
    private String ownerName;

    public InventoryItem() {
        this.itemID = "";
        this.title = "";
        this.description = "";
        this.imageURL = "";
        this.listID = "";

        this.lendedTo = "";
        this.lendedToImage = "";
        this.lendedToName = "";
        this.isAvailable = true;

        this.ownerId = "";
        this.ownerName = "";
    }

    public InventoryItem(String itemID, String title, String description, String imageURL, String listID, String lendedTo, String lendedToImage, String lendedToName, boolean isAvailable, String ownerId, String ownerName) {
        this.itemID = itemID;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.listID = listID;

        this.lendedTo = lendedTo;
        this.lendedToImage = lendedToImage;
        this.lendedToName = lendedToName;
        this.isAvailable = isAvailable;

        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (!itemID.equals("")) {
            map.put("id", itemID);
        }
        map.put("title", title);
        map.put("description", description);
        map.put("listID", listID);
        map.put("imageURL", imageURL);

        map.put("lendedTo", lendedTo);
        map.put("lendedToImage", lendedToImage);
        map.put("lendedToName", lendedToName);
        map.put("isAvailable", isAvailable);

        map.put("ownerId", ownerId);
        map.put("ownerName", ownerName);
        return map;
    }

    public boolean validate() {
        return !itemID.equals("")
                && !title.equals("")
                && !description.equals("")
                && !listID.equals("")
                && !ownerId.equals("")
                && !ownerName.equals("");
    }

    @Override
    public boolean equals(Object obj){
        InventoryItem item = (InventoryItem) obj;
        return item.getItemID().equals(this.itemID);
    }

    public String getLendedTo() {
        return lendedTo;
    }

    public void setLendedTo(String lendedTo) {
        this.lendedTo = lendedTo;
    }

    public String getLendedToName() {
        return lendedToName;
    }

    public void setLendedToName(String lendedToName) {
        this.lendedToName = lendedToName;
    }

    public String getLendedToImage() {
        return lendedToImage;
    }

    public void setLendedToImage(String lendedToImage) {
        this.lendedToImage = lendedToImage;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
