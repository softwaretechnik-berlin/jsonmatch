package jsonmatch;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextUtils {
    public static String indent(String s) {
        return Arrays.stream(s
            .split("\n"))
            .map(line -> "    " + line)
            .collect(Collectors.joining("\n"));
    }

    public static String annotate(String text, String annotation) {
        String[] lines = text.split("\n");

        if (lines.length == 1) {
            return text + " ╶ " + annotation;
        }

        List<String> cleanLines = Arrays.stream(lines).map(s -> s.replaceAll("\u001B\\[[\\d;]*[^\\d;]", "")).collect(Collectors.toList());
        if (lines.length > 1) {
            int maxLineLength = cleanLines.stream().map(String::length).max(Integer::compareTo).get();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String separator = i == 0 ? "╮" : (i == lines.length - 1 ? "╯" : "│");
                if (i == lines.length / 2 - 1) {
                    lines[i] = line + StringUtils.repeat(" ", maxLineLength - cleanLines.get(i).length()) + " " + separator + " " + annotation;
                } else {
                    lines[i] = line + StringUtils.repeat(" ", maxLineLength - cleanLines.get(i).length()) + " " + separator;
                }

            }
            return StringUtils.join(lines, "\n");
        }
        throw new IllegalStateException("This shouldn't happen");
    }

    public static String annotate(String text, String annotation, int extraIndent) {
            String[] lines = text.split("\n");

            if (lines.length == 1) {
                return text + " ╶ " + annotation;
            }

            List<String> cleanLines = Arrays.stream(lines).map(s -> s.replaceAll("\u001B\\[[\\d;]*[^\\d;]", "")).collect(Collectors.toList());
            cleanLines.set(0, StringUtils.repeat(" ", extraIndent) + cleanLines.get(0));
            if (lines.length > 1) {
                int maxLineLength = cleanLines.stream().map(String::length).max(Integer::compareTo).get();
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    String separator = i == 0 ? "╮" : (i == lines.length - 1 ? "╯" : "│");
                    if (i == lines.length / 2 - 1) {
                        if (separator.equals("│")) {
                            separator = "├";
                        }
                        lines[i] = line + StringUtils.repeat(" ", maxLineLength - cleanLines.get(i).length()) + " " + separator + " " + annotation;
                    } else {
                        lines[i] = line + StringUtils.repeat(" ", maxLineLength - cleanLines.get(i).length()) + " " + separator;
                    }

                }
                return StringUtils.join(lines, "\n");
            }
            throw new IllegalStateException("This shouldn't happen");
        }
}
