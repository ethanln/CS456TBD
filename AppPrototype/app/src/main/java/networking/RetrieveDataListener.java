package networking;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

/**
 * Created by mitch10e on 12 March 2016.
 */
public abstract class RetrieveDataListener implements ChildEventListener {
    @Override
    public abstract void onChildAdded(DataSnapshot data, String s);

    @Override
    public void onChildChanged(DataSnapshot data, String s) {}

    @Override
    public void onChildRemoved(DataSnapshot data) {}

    @Override
    public void onChildMoved(DataSnapshot data, String s) {}

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.e("onCancelled", firebaseError.getMessage());
        Log.e("onCancelled", firebaseError.getDetails());
    }
}
