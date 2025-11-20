package menu.StaffMenu.StaffActions;

import internship.Internship;
import java.util.Scanner;
import menu.MenuAction;
import reporting.ReportFilter;
import reporting.ReportGenerator;

/**
 * Implements the {@link MenuAction} for Career Centre Staff to generate detailed reports on internships.
 * <p>
 * This action allows staff members to view a list of internships filtered by specific criteria:
 * <ul>
 * <li><b>Status:</b> (e.g., PENDING_REVIEW, APPROVED, FILLED)</li>
 * <li><b>Preferred Major:</b> (String match)</li>
 * <li><b>Level:</b> (e.g., BASIC, ADVANCED)</li>
 * </ul>
 * The results are fetched from the repository and displayed with comprehensive statistics.
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
     * <li>Creates a ReportFilter with the provided criteria.</li>
     * <li>Generates a comprehensive report using ReportGenerator.</li>
     * <li>Displays the report with summary overview and filtered internships.</li>
     * </ol>
     * </p>
     */
    @Override
    public void execute() {
        System.out.println("\n--- GENERATE INTERNSHIP REPORT ---");

        // ===== Ask for filters =====
        System.out.print("Filter by Status (ALL/PENDING_REVIEW/APPROVED/REJECTED/FILLED): ");
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

        // ===== Create filter object and generate report =====
        ReportFilter filter = new ReportFilter(statusFilter, majorFilter, levelFilter);
        ReportGenerator reportGenerator = new ReportGenerator(filter);
        reportGenerator.generateFullReport();

        System.out.print("\n(Press ENTER to return to menu) ");
        sc.nextLine();
    }
}
