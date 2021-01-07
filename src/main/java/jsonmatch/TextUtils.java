package jsonmatch;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextUtils {
    public static String indent(String s) {
        return Arrays.stream(s
            .split("\n"))
            .map(line -> "    " + line)
            .collect(Collectors.joining("\n"));
    }
}
