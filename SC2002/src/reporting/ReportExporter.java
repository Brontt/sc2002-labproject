package reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * A utility class responsible for exporting system data reports to external files.
 * <p>
 * This class provides static helper methods to write data to specific formats,
 * such as CSV (Comma-Separated Values) and plain text files. It encapsulates
 * the file I/O logic and basic exception handling.
 * </p>
 */
public class ReportExporter {

    /**
     * Exports a list of row data to a CSV file.
     * <p>
     * This method iterates through the provided list of string arrays. It joins the
     * elements of each array with a comma ({@code ,}) delimiter and writes them
     * as a new line in the specified file.
     * </p>
     * <p>
     * If an I/O error occurs during the writing process, the exception is caught,
     * and an error message is printed to the console.
     * </p>
     *
     * @param path The file system path (including filename) where the CSV should be saved.
     * @param rows A list where each entry is a {@code String[]} representing a row of data columns.
     */
    public static void toCsv(String path, List<String[]> rows) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (var r : rows) pw.println(String.join(",", r));
            System.out.println("Report exported to: " + path);
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }

    /**
     * Writes raw string content to a text file.
     * <p>
     * This method is useful for exporting unstructured data or pre-formatted reports.
     * It creates a new file (or overwrites an existing one) at the specified path.
     * </p>
     * <p>
     * If an I/O error occurs, the exception is caught and logged to the console.
     * </p>
     *
     * @param path    The file system path where the text file should be saved.
     * @param content The string content to be written to the file.
     */
    public static void toText(String path, String content) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.print(content);
            System.out.println("Report exported to: " + path);
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}