package networking.callback;

/**
 * Created by mitch10e on 12 March 2016.
 */
public abstract class UsersCallbackDelegate implements CallbackDelegate {

    private UsersCallback callback;

    public UsersCallbackDelegate(UsersCallback callback) {
        this.callback = callback;
    }

    public UsersCallback getCallback() {
        return callback;
    }

    public void setCallback(UsersCallback callback) {
        this.callback = callback;
    }

    public abstract void makeCallback();
}
