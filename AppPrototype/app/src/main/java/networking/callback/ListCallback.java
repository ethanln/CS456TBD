package networking.callback;

import model.InventoryList;

/**
 * Created by mitch10e on 14 March 2016.
 */
public class ListCallback implements Callback {

    private InventoryList list;
    private ListCallbackDelegate delegate;

    public InventoryList getList() {
        return list;
    }

    public void setList(InventoryList list) {
        this.list = list;
    }

    public ListCallbackDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ListCallbackDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void callback() {
        delegate.makeCallback();
    }
}
