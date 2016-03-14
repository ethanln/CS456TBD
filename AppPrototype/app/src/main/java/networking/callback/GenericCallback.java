package networking.callback;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;

/**
 * Created by mitch10e on 12 March 2016.
 *
 * Quick and dirty Callback since I don't have the time or energy to make callbacks for each value type.
 */
public abstract class GenericCallback implements Callback {

    public Firebase firebase;
    public FirebaseError error;
    public HashMap<String, Object> map;
    public String data;

    @Override
    public abstract void callback();
}
