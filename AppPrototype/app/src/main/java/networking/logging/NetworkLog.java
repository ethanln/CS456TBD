package networking.logging;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkLog {

    public static NetworkLog instance = new NetworkLog();

    private NetworkLog() {}

    public static NetworkLog getInstance() {
        return instance;
    }

    public static NetworkLog inst() {
        return getInstance();
    }

    public void debug(String message) {
        String tag = "[DEBUG]";
        printMessage(tag, ColorCodes.GREEN, message);
    }

    public void info(String message) {
        String tag = "[INFO] ";
        printMessage(tag, ColorCodes.BLUE, message);
    }

    public void error(String message) {
        String tag = "[ERROR]";
        printMessage(tag, ColorCodes.RED, message);
    }

    public void warn(String message) {
        String tag = "[WARN] ";
        printMessage(tag, ColorCodes.YELLOW, message);
    }

    private void printMessage(String tag, String color, String message) {
        System.out.println(color + tag + "\t" + message);
    }

}
