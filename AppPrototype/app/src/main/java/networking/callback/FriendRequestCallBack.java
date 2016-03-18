package networking.callback;

import java.util.ArrayList;

import model.FriendRequest;
import model.User;

/**
 * Created by Ethan on 3/17/2016.
 */
public class FriendRequestCallBack implements Callback {

    private ArrayList<FriendRequest> friendRequests;
    private FriendRequestCallBackDelegate delegate;

    public ArrayList<FriendRequest> getFriendRequests() {
        return this.friendRequests;
    }

    public void addFriendRequest(FriendRequest _friendRequest) {
        this.friendRequests.add(_friendRequest);
    }

    public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public FriendRequestCallBackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(FriendRequestCallBackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }
}
