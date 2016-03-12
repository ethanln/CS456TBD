package networking.testing;

/**
 * Created by mitch10e on 12 March 2016.
 */
public abstract class NetworkTest {

    private String name;
    public abstract void executeTest();

    public NetworkTest(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
