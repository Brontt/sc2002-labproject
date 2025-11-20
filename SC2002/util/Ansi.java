package util;

public class Ansi {
    public static final String RESET="\u001B[0m", GREEN="\u001B[32m", RED="\u001B[31m";
    public static String green(String s){ return GREEN+s+RESET; }
    public static String red(String s){ return RED+s+RESET; }
}
