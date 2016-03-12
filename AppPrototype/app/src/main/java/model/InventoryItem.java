package model;

/**
 * Created by mitch10e on 12 March2016.
 */
public class InventoryItem {

    private String id;
    private String title;
    private String description;

    public InventoryItem() {
        this.id = "";
        this.title = "";
        this.description = "";
    }

    public InventoryItem(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.description = type;
    }
}
