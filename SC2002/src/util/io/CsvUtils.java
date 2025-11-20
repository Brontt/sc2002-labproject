package util.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for common CSV operations.
 * Provides reusable methods for CSV file handling (DRY principle).
 */
public class CsvUtils {
    
    /**
     * Opens a file for reading if it exists.
     * @param filename Path to the file
     * @return BufferedReader if file exists, null otherwise
     */
    public static BufferedReader openIfExists(String filename) {
        try {
            Path p = Path.of(filename);
            if (!Files.exists(p)) {
                return null;
            }
            return Files.newBufferedReader(p);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Converts empty or null strings to null, otherwise trims.
     * @param s The string to process
     * @return null if blank, trimmed string otherwise
     */
    public static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
    
    /**
     * Parses a boolean value from various string representations.
     * Accepts: "true", "1", "yes" (case-insensitive)
     * @param s The string to parse
     * @return true if string represents a true value, false otherwise
     */
    public static boolean parseBoolean(String s) {
        return s != null && (
            s.equalsIgnoreCase("true") || 
            s.equals("1") || 
            s.equalsIgnoreCase("yes")
        );
    }
    
    /**
     * Makes a string CSV-safe by replacing commas with semicolons.
     * @param s The string to make safe
     * @return CSV-safe string or empty string if null
     */
    public static String csvSafe(String s) {
        return s == null ? "" : s.replace(",", ";");
    }
    
    /**
     * Skips the header line of a CSV file if present.
     * @param br The BufferedReader to read from
     * @return true if header was skipped, false otherwise
     * @throws IOException if reading fails
     */
    public static boolean skipHeader(BufferedReader br) throws IOException {
        if (br != null) {
            String header = br.readLine();
            return header != null;
        }
        return false;
    }
}