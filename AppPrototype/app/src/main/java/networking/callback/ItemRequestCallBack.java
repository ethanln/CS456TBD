package networking.callback;

import java.util.ArrayList;

import model.FriendRequest;
import model.ItemRequest;

/**
 * Created by Ethan on 3/17/2016.
 */
public class ItemRequestCallBack implements Callback {
    private ArrayList<ItemRequest> itemRequests;
    private ItemRequestCallBackDelegate delegate;

    public ArrayList<ItemRequest> getItemRequests() {
        return this.itemRequests;
    }

    public void addItemRequest(ItemRequest _itemRequest) {
        this.itemRequests.add(_itemRequest);
    }

    public void setItemRequests(ArrayList<ItemRequest> itemRequests) {
        this.itemRequests = itemRequests;
    }

    public ItemRequestCallBackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ItemRequestCallBackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }
}
