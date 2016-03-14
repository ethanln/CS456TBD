package networking.callback;

import model.InventoryList;

/**
 * Created by mitch10e on 14 March 2016.
 */
public abstract class ListCallbackDelegate implements CallbackDelegate {

    private ListCallback callback;

    public ListCallbackDelegate(ListCallback callback) {
        this.callback = callback;
    }

    public ListCallback getCallback() {
        return callback;
    }

    public void setCallback(ListCallback callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
