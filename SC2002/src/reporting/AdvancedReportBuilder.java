package reporting;

import internship.Internship;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A builder class for generating advanced analytical reports on internship data.
 * <p>
 * This class utilizes the <b>Builder Design Pattern</b> to construct complex text-based reports
 * step-by-step. It allows for chaining methods to add sections, aggregate counts, and calculate
 * metrics like fill rates. Additionally, it provides functionality to export the raw data
 * into a format suitable for CSV generation.
 * </p>
 */
public class AdvancedReportBuilder {
    private final List<Internship> data;
    private final StringBuilder sb = new StringBuilder();

    /**
     * Constructs a new {@code AdvancedReportBuilder} with the source data.
     *
     * @param data The list of {@link Internship} objects to be analyzed and reported on.
     */
    public AdvancedReportBuilder(List<Internship> data){ this.data=data; }

    /**
     * Appends a formatted section header to the report.
     *
     * @param title The title of the section (e.g., "Statistics").
     * @return The current instance of {@code AdvancedReportBuilder} to allow method chaining.
     */
    public AdvancedReportBuilder section(String title){ sb.append("\n=== ").append(title).append(" ===\n"); return this; }

    /**
     * Aggregates and counts internships based on a specific property.
     * <p>
     * This generic method uses a {@link Function} to extract a key from each internship,
     * groups the data by that key, counts the occurrences, and appends the results to the report.
     * </p>
     *
     * @param label The descriptive label for this statistic (e.g., "By Company").
     * @param keyFn A function to extract the grouping key from an {@link Internship} (e.g., {@code Internship::getStatus}).
     * @param <K>   The type of the key used for grouping.
     * @return The current instance of {@code AdvancedReportBuilder} for chaining.
     */
    public <K> AdvancedReportBuilder countBy(String label, Function<Internship,K> keyFn){
        Map<K, Long> map = data.stream().collect(Collectors.groupingBy(keyFn, Collectors.counting()));
        map.forEach((k,v) -> sb.append(String.format("%s: %s -> %d\n", label, k, v)));
        return this;
    }

    /**
     * Calculates and appends the overall fill rate of the internships.
     * <p>
     * The fill rate is calculated as: {@code (Total Slots - Remaining Slots) / Total Slots}.
     * It handles potential division by zero if the total slot count is 0.
     * </p>
     *
     * @return The current instance of {@code AdvancedReportBuilder} for chaining.
     */
    public AdvancedReportBuilder fillRate(){
        double total = data.stream().mapToInt(Internship::getSlots).sum();
        double filled = data.stream().mapToInt(i -> i.getSlots() - i.getSlotsRemaining()).sum();
        double rate = total == 0 ? 0 : (filled / total) * 100.0;
        sb.append(String.format("Fill Rate: %.1f%% (%d/%d)\n", rate, (int)filled, (int)total));
        return this;
    }

    /**
     * Finalizes the construction of the text-based report.
     *
     * @return A {@code String} containing the fully accumulated report.
     */
    public String buildText(){ return sb.toString(); }

    /**
     * Converts the internship data into a list of string arrays suitable for CSV export.
     * <p>
     * The first row contains the headers: "Title", "Company", "Level", "Major", "Open",
     * "Close", "Slots", "Status", "Visible". Subsequent rows contain the corresponding data
     * for each internship.
     * </p>
     *
     * @return A {@code List<String[]>} representing the rows of a CSV file.
     */
    public List<String[]> toCsvRows() {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Title","Company","Level","Major","Open","Close","Slots","Status","Visible"});
        for (var i : data) {
            rows.add(new String[]{
                nz(i.getTitle()), nz(i.getCompanyName()), i.getLevel().name(), nz(i.getPreferredMajor()),
                i.getOpenDate().toString(), i.getCloseDate().toString(),
                String.valueOf(i.getSlots()), i.getStatus().name(), String.valueOf(i.isVisible())
            });
        }
        return rows;
    }

    /**
     * Helper method to handle null strings safely (Null-to-Zero/Empty).
     *
     * @param s The string to check.
     * @return The original string if not null; otherwise, an empty string.
     */
    private static String nz(String s){ return s==null?"":s; }
}