package networking.callback;

/**
 * Created by mitch10e on 14 March 2016.
 */
public abstract class ItemCallbackDelegate implements CallbackDelegate {

    private ItemCallback callback;

    public ItemCallbackDelegate(ItemCallback callback) {
        this.callback = callback;
    }

    public ItemCallback getCallback() {
        return callback;
    }

    public void setCallback(ItemCallback callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
