
package util;

import java.util.Scanner;

/**
 * Utility class for common console/UI operations.
 * Centralizes duplicate UI helper methods to follow DRY principle.
 */
public class ConsoleHelper {
    
    /**
     * Pauses execution until user presses Enter.
     * @param sc Scanner instance for user input
     */
    public static void pause(Scanner sc) {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
    
    /**
     * Prompts user for yes/no input with validation.
     * @param sc Scanner instance for user input
     * @param prompt The prompt message to display
     * @return true if user answers yes, false if no
     */
    public static boolean askYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Please enter y/n.");
        }
    }
    
    /**
     * Safely converts a nullable string to display value.
     * @param s The string to convert
     * @return The original string or "-" if null/blank
     */
    public static String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
    
    /**
     * Prints a section header with decorative borders.
     * @param title The title to display
     */
    public static void printSectionHeader(String title) {
        int width = Math.max(48, title.length() + 8);
        String border = "═".repeat(width - 4);
        System.out.println("\n╔" + border + "╗");
        System.out.printf("║  %-" + (width - 6) + "s  ║%n", title);
        System.out.println("╚" + border + "╝");
    }
    
    /**
     * Prompts user for integer input with validation.
     * @param sc Scanner instance for user input
     * @param prompt The prompt message to display
     * @param min Minimum acceptable value (inclusive)
     * @param max Maximum acceptable value (inclusive)
     * @return Valid integer within range
     */
    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = sc.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    
    /**
     * Prompts user for string input that cannot be blank.
     * @param sc Scanner instance for user input
     * @param prompt The prompt message to display
     * @return Non-blank string
     */
    public static String readNonBlankString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }
}
