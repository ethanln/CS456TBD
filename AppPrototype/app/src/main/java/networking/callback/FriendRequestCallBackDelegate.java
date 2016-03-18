package networking.callback;

/**
 * Created by Ethan on 3/17/2016.
 */
public abstract class FriendRequestCallBackDelegate implements CallbackDelegate {

    private FriendRequestCallBack callback;

    public FriendRequestCallBackDelegate(FriendRequestCallBack callback) {
        this.callback = callback;
    }

    public FriendRequestCallBack getCallback() {
        return callback;
    }

    public void setCallback(FriendRequestCallBack callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
