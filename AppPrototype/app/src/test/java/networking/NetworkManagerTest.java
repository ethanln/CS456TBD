package networking;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Test;

import model.InventoryItem;
import networking.logging.NetworkLog;

import static org.junit.Assert.*;

/**
 * Created by mitch10e on 12 March 2016.
 */
public class NetworkManagerTest {

    private NetworkManager network;
    private NetworkLog log;
    private Activity testActivity;

    @Before
    public void setUp() throws Exception {
        this.testActivity = new Activity();
        Firebase.setAndroidContext(testActivity);

        this.network = NetworkManager.getInstance();
        this.log = NetworkLog.getInstance();

    }

    @Test
    public void testMakeCreateItemRequest() {
        InventoryItem item = new InventoryItem("", "Test Item", "Test Description", "Test URL", "TestListID");
        String itemID = network.makeCreateItemRequest(item);
        assertNotEquals(item, "");
        log.info("ItemID: " + itemID);
    }
}