package networking.callback;

import android.app.Activity;

import model.User;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class UserCallback implements Callback {

    private User user;
    private UserCallbackDelegate delegate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCallbackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UserCallbackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }
}
