package jsonmatch;

import java.util.stream.Collectors;

public class TextUtils {
    public static String indent(String s) {
        return s
            .lines()
            .map(line -> "    " + line)
            .collect(Collectors.joining("\n"));
    }
}
