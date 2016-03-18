package networking.callback;

/**
 * Created by Ethan on 3/17/2016.
 */
public abstract class ItemRequestCallBackDelegate implements CallbackDelegate {

    private ItemRequestCallBack callback;

    public ItemRequestCallBackDelegate(ItemRequestCallBack callback) {
        this.callback = callback;
    }

    public ItemRequestCallBack getCallback() {
        return callback;
    }

    public void setCallback(ItemRequestCallBack callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
