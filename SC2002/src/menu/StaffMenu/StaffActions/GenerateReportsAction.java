package menu.StaffMenu.StaffActions;

import internship.Internship;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import menu.MenuAction;
import repository.Repository;
import util.ConsoleHelper;
import util.TablePrinter;

/**
 * Implements the {@link MenuAction} for Career Centre Staff to generate detailed reports on internships.
 * <p>
 * This action allows staff members to view a list of internships filtered by specific criteria:
 * <ul>
 * <li><b>Status:</b> (e.g., PENDING, APPROVED, FILLED)</li>
 * <li><b>Preferred Major:</b> (String match)</li>
 * <li><b>Level:</b> (e.g., BASIC, ADVANCED)</li>
 * </ul>
 * The results are fetched from the {@link Repository} and displayed in a formatted table.
 * </p>
 */
public class GenerateReportsAction implements MenuAction {
    private final Scanner sc;

    /**
     * Constructs a new {@code GenerateReportsAction}.
     *
     * @param sc The {@link Scanner} used to capture user input for filtering criteria.
     */
    public GenerateReportsAction(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Executes the report generation workflow.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Prompts the user to input filters for Status, Major, and Level.</li>
     * <li>Retrieves all internships from the repository.</li>
     * <li>Filters the list based on the provided criteria using Java Streams.</li>
     * <li>Displays the filtered results using a formatted table or a "no match" message.</li>
     * </ol>
     * </p>
     */
    @Override
    public void execute() {
        System.out.println("\n--- GENERATE INTERNSHIP REPORT ---");

        // ===== Ask for filters =====
        System.out.print("Filter by Status (ALL/PENDING/APPROVED/REJECTED/FILLED): ");
        String statusIn = sc.nextLine().trim().toUpperCase();
        Internship.InternshipStatus statusFilterTemp = null;
        if (!statusIn.isEmpty() && !"ALL".equals(statusIn)) {
            try {
                statusFilterTemp = Internship.InternshipStatus.valueOf(statusIn);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status, ignoring filter.");
            }
        }
        final Internship.InternshipStatus statusFilter = statusFilterTemp;

        System.out.print("Filter by Preferred Major (leave blank for ALL): ");
        String majorInput = sc.nextLine().trim();
        final String majorFilter = majorInput.isEmpty() ? null : majorInput;

        System.out.print("Filter by Level (ALL/BASIC/INTERMEDIATE/ADVANCED): ");
        String levelIn = sc.nextLine().trim().toUpperCase();
        Internship.InternshipLevel levelFilterTemp = null;
        if (!levelIn.isEmpty() && !"ALL".equals(levelIn)) {
            try {
                levelFilterTemp = Internship.InternshipLevel.valueOf(levelIn);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level, ignoring filter.");
            }
        }
        final Internship.InternshipLevel levelFilter = levelFilterTemp;

        // ===== Apply filters =====
        List<Internship> all = Repository.findAllInternships();
        List<Internship> filtered = all.stream()
                .filter(i -> statusFilter == null || i.getStatus() == statusFilter)
                .filter(i -> majorFilter == null
                        || (i.getPreferredMajor() != null
                        && i.getPreferredMajor().equalsIgnoreCase(majorFilter)))
                .filter(i -> levelFilter == null || i.getLevel() == levelFilter)
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("\nNo internships match the selected filters.");
            return;
        }

        // ===== Show detail table =====
        printInternshipsTable(filtered);

        System.out.print("\n(Press ENTER to return to menu) ");
        sc.nextLine();
    }

    /**
     * Helper method to print the list of internships in a structured table format.
     * <p>
     * It uses {@link TablePrinter} to render columns for ID, Title, Company, Level, Major, Status, and Slots.
     * </p>
     *
     * @param list The list of {@link Internship} objects to display.
     */
    private void printInternshipsTable(List<Internship> list) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(160)
                .addColumn("No.",     TablePrinter.Align.RIGHT,   2,  4)
                .addColumn("ID",      TablePrinter.Align.LEFT,    6, 10)
                .addColumn("Title",   TablePrinter.Align.LEFT,   10, 30)
                .addColumn("Company", TablePrinter.Align.LEFT,    8, 22)
                .addColumn("Level",   TablePrinter.Align.CENTER,  8, 12)
                .addColumn("Major",   TablePrinter.Align.CENTER,  5,  8)
                .addColumn("Status",  TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Slots",   TablePrinter.Align.RIGHT,   5,  6)
                .build();

        int idx = 1;
        for (Internship i : list) {
            tp.addRow(
                    idx++,
                    i.getId(),
                    ConsoleHelper.safe(i.getTitle()),
                    ConsoleHelper.safe(i.getCompanyName()),
                    i.getLevel(),
                    ConsoleHelper.safe(i.getPreferredMajor()),
                    i.getStatus(),
                    i.getSlots()
            );
        }

        System.out.println(tp.render());
    }
}