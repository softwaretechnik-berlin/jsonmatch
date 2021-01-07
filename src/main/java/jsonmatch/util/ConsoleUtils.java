package jsonmatch.util;

import java.text.DecimalFormat;

public class ConsoleUtils {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static String nullSafeString(String s) {
        return s!=null ? s : "␀";
    }

    public static final DecimalFormat TIME_IN_SECONDS = new DecimalFormat("0.00");
}
