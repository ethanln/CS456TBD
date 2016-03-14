package networking.callback;

/**
 * Created by mitch10e on 12 March 2016.
 */
public abstract class UserCallbackDelegate implements CallbackDelegate {
    private UserCallback callback;

    public UserCallbackDelegate(UserCallback callback) {
        this.callback = callback;
    }

    public UserCallback getCallback() {
        return callback;
    }

    public void setCallback(UserCallback callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
