package networking;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();
    private static final String usersEndpoint = "https://tbd456.firebaseio.com/users";
    private static final String listsEndpoint = "https://tbd456.firebaseio.com/lists";
    private static final String itemsEndpoint = "https://tbd456.firebaseio.com/items";

    private NetworkManager() {}

    public static NetworkManager getInstance() {
        return instance;
    }

    public static NetworkManager inst() {
        return getInstance();
    }

    // HELPERS
    // -------

    // TODO:

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
