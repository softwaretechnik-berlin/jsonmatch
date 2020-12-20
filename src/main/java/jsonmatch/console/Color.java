package jsonmatch.console;

import static jsonmatch.console.ConsoleUtils.ANSI_RESET;

public enum Color {
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    WHITE("\u001B[37m"),
    GRAY("\u001B[90m");
    private final String ansi;

    Color(String ansi) {
        this.ansi = ansi;
    }

    public String render(String s) {
        return ansi + s + ANSI_RESET;
    }
}
