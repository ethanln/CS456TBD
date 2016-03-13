package networking.callback;

import android.telecom.Call;

import java.util.ArrayList;

import model.User;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class UsersCallback implements Callback{

    private ArrayList<User> users;
    private UsersCallbackDelegate delegate;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public UsersCallbackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UsersCallbackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }
}
