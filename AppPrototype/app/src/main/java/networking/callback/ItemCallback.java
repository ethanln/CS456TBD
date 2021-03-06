package networking.callback;

import model.InventoryItem;

/**
 * Created by mitch10e on 14 March 2016.
 */
public class ItemCallback implements Callback {

    private InventoryItem item;
    private ItemCallbackDelegate delegate;
    private boolean isActionFullfilled;

    public ItemCallback(){
        this.isActionFullfilled = false;
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public ItemCallbackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ItemCallbackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }

    public boolean isActionFullfilled() {
        return isActionFullfilled;
    }

    public void setIsActionFullfilled(boolean isActionFullfilled) {
        this.isActionFullfilled = isActionFullfilled;
    }
}
