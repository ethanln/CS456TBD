package networking.logging;

/**
 * Created by mitch10e on 12 March2016.
 */
public class NetworkLog {

    public static void debug(String message) {
        String tag = "[DEBUG]";
        printMessage(tag, ColorCodes.GREEN, message);
    }

    public static void info(String message) {
        String tag = "[INFO] ";
        printMessage(tag, ColorCodes.BLUE, message);
    }

    public static void error(String message) {
        String tag = "[ERROR]";
        printMessage(tag, ColorCodes.RED, message);
    }

    public static void warn(String message) {
        String tag = "[WARN] ";
        printMessage(tag, ColorCodes.YELLOW, message);
    }

    private static void printMessage(String tag, String color, String message) {
        System.out.println(color + tag + "\t" + message);
    }

}
