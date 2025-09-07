package jsonmatch.util;

public enum Color {
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    GRAY("\u001B[90m");

    public static final String ANSI_RESET = "\u001B[0m";

    private final String ansi;

    Color(String ansi) {
        this.ansi = ansi;
    }

    public String render(String s) {
        return ansi + s + ANSI_RESET;
    }

    public String getAnsi() {
        return ansi;
    }
}
