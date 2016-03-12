package networking;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();
    private static final String usersEndpoint = "https://tbd456.firebaseio.com/";
    private static final String listsEndpoint = "https://tbd456lists.firebaseio.com/";
    private static final String itemsEndpoint = "https://tbd456items.firebaseio.com/";

    private NetworkManager() {}

    public static NetworkManager getInstance() {
        return instance;
    }

    public static NetworkManager inst() {
        return getInstance();
    }

    // HELPERS
    // -------

    private void

    // USERS
    // -----

    // TODO:

    // LISTS
    // -----

    // TODO:

    // ITEMS
    // -----

    // TODO:
}
