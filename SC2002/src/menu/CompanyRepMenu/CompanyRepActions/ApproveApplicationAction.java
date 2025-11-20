package menu.CompanyRepMenu.CompanyRepActions;

import internship.ApplicationStatus;
import internship.Internship;
import internship.InternshipApp;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import menu.MenuAction;
import repository.Repository;
import user.CompanyRep;
import util.ConsoleHelper;
import util.TablePrinter;

/**
 * A menu action that allows a Company Representative to review and process student applications.
 * <p>
 * This class handles the workflow for:
 * <ul>
 * <li>Fetching and displaying pending applications for internships owned by the representative.</li>
 * <li>Allowing the representative to select an application by ID.</li>
 * <li>Approving an application (if slots are available) or rejecting it.</li>
 * </ul>
 * </p>
 */
public class ApproveApplicationAction implements MenuAction {
    private final CompanyRep me;
    private final Scanner sc;

    /**
     * Constructs a new {@code ApproveApplicationAction}.
     *
     * @param me The {@link CompanyRep} currently logged in and performing the action.
     * @param sc The {@link Scanner} to use for reading user input.
     */
    public ApproveApplicationAction(CompanyRep me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    /**
     * Executes the main logic for reviewing applications.
     * <p>
     * This method retrieves all applications associated with the representative, filters for
     * those with a {@link ApplicationStatus#PENDING} status, and displays them. It then
     * prompts the user to select an application ID and choose an action (Approve or Reject).
     * </p>
     */
    @Override
    public void execute() {
        System.out.println("\n--- APPROVE/REJECT APPLICATIONS ---");

        // Get all PENDING applications for internships posted by this rep
        List<InternshipApp> allApps = Repository.findApplicationsByRepId(me.getUserId());
        List<InternshipApp> pendingApps = allApps.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingApps == null || pendingApps.isEmpty()) {
            System.out.println("No pending applications to review.");
            return;
        }

        // Display pending applications
        printApplicationsTable(pendingApps);

        // Prompt for action
        System.out.print("\nEnter Application ID to approve/reject (or 'q' to quit): ");
        String input = sc.nextLine().trim();
        
        if (input.equalsIgnoreCase("q")) {
            return;
        }

        // Find the application
        InternshipApp selectedApp = null;
        for (InternshipApp app : pendingApps) {
            if (app.getId().equalsIgnoreCase(input)) {
                selectedApp = app;
                break;
            }
        }

        if (selectedApp == null) {
            System.out.println("Invalid Application ID.");
            return;
        }

        // Prompt for approve or reject
        System.out.print("Action: (a)pprove or (r)eject? ");
        String action = sc.nextLine().trim().toLowerCase();

        if (action.equals("a") || action.equals("approve")) {
            approveApplication(selectedApp);
        } else if (action.equals("r") || action.equals("reject")) {
            rejectApplication(selectedApp);
        } else {
            System.out.println("Invalid action. No changes made.");
        }
    }

    /**
     * Processes the approval of a specific application.
     * <p>
     * It checks if the associated internship exists and if there are remaining slots.
     * If valid, it updates the application status to {@link ApplicationStatus#SUCCESSFUL}
     * and saves the change to the repository.
     * </p>
     *
     * @param app The {@link InternshipApp} to approve.
     */
    private void approveApplication(InternshipApp app) {
        // Verify the internship still has slots available
        Internship internship = app.getInternship();
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return;
        }

        if (internship.getSlotsRemaining() <= 0) {
            System.out.println("Cannot approve: No slots remaining for this internship.");
            return;
        }

        // Set application status to SUCCESSFUL
        app.setStatus(ApplicationStatus.SUCCESSFUL);
        Repository.updateApplication(app);

        System.out.println("\n✓ Application " + app.getId() + " APPROVED.");
        System.out.println("  Student: " + (app.getStudent() != null ? app.getStudent().getName() : "-"));
        System.out.println("  Internship: " + internship.getTitle());
        System.out.println("  Status: SUCCESSFUL (Student can now accept the placement)");
    }

    /**
     * Processes the rejection of a specific application.
     * <p>
     * Updates the application status to {@link ApplicationStatus#UNSUCCESSFUL} and
     * saves the change to the repository.
     * </p>
     *
     * @param app The {@link InternshipApp} to reject.
     */
    private void rejectApplication(InternshipApp app) {
        // Set application status to UNSUCCESSFUL
        app.setStatus(ApplicationStatus.UNSUCCESSFUL);
        Repository.updateApplication(app);

        System.out.println("\n✗ Application " + app.getId() + " REJECTED.");
        System.out.println("  Student: " + (app.getStudent() != null ? app.getStudent().getName() : "-"));
        System.out.println("  Status: UNSUCCESSFUL");
    }

    /**
     * Prints a formatted table of the provided internship applications.
     *
     * @param apps The list of {@link InternshipApp} objects to display.
     */
    private void printApplicationsTable(List<InternshipApp> apps) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(140)
                .addColumn("No.",        TablePrinter.Align.RIGHT,   2,  4)
                .addColumn("App ID",     TablePrinter.Align.LEFT,    6, 10)
                .addColumn("Student",    TablePrinter.Align.LEFT,   10, 20)
                .addColumn("StudentID",  TablePrinter.Align.LEFT,    8, 12)
                .addColumn("Internship", TablePrinter.Align.LEFT,   12, 30)
                .addColumn("Status",     TablePrinter.Align.CENTER, 10, 12)
                .build();

        int idx = 1;
        for (InternshipApp a : apps) {
            Internship i = a.getInternship();
            tp.addRow(
                idx++,
                a.getId(),
                a.getStudent() != null ? ConsoleHelper.safe(a.getStudent().getName()) : "-",
                a.getStudent() != null ? a.getStudent().getUserId() : "-",
                i != null ? ConsoleHelper.safe(i.getTitle()) : "-",
                a.getStatus()
            );            
        }

        System.out.println(tp.render());
    }
}