package model;

/**
 * Created by mitch10e on 12 March2016.
 */
public class InventoryItem {

    private String itemID;
    private String title;
    private String description;
    private String imageURL;
    private String listID;

    public InventoryItem() {
        this.itemID = "";
        this.title = "";
        this.description = "";
        this.imageURL = "";
        this.listID = "";
    }

    public InventoryItem(String itemID, String title, String description, String imageURL, String listID) {
        this.itemID = itemID;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.listID = listID;
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
}
