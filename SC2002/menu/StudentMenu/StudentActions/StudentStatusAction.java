package menu.StudentMenu.StudentActions;

import internship.ApplicationStatus;
import internship.Internship;
import internship.InternshipApp;
import java.util.List;
import java.util.Scanner;
import menu.MenuAction;
import repository.Repository;
import user.Student;
import util.ConsoleHelper;
import util.TablePrinter;

/**
 * Action that displays the student's current internship applications and statuses.
 *
 * Fetches application records from {@link Repository} and renders them using
 * {@link TablePrinter} for a readable console display.
 */
public class StudentStatusAction implements MenuAction {
    private final Student me;
    private final Scanner sc;

    /**
     * @param me the student whose applications will be displayed
     * @param sc the scanner for reading user input
     */
    public StudentStatusAction(Student me, Scanner sc) { 
        this.me = me; 
        this.sc = sc;
    }

    /**
     * Execute the status-reporting flow — query applications and render them.
     */
    @Override
    public void execute() {
        List<InternshipApp> apps = Repository.findApplicationsByStudentId(me.getUserId());
        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

    // Build table
        
        var tp = TablePrinter.builder()
            .unicodeBorders(true)
            .maxTableWidth(150)
            .addColumn("No.",       TablePrinter.Align.RIGHT,  2,  4)
            .addColumn("AppID",     TablePrinter.Align.LEFT,   6, 10)
            .addColumn("Internship",TablePrinter.Align.LEFT,   10, 30)
            .addColumn("Company",   TablePrinter.Align.LEFT,    8, 20)
            .addColumn("Status",    TablePrinter.Align.CENTER, 10, 14)
            .addColumn("W.Request", TablePrinter.Align.CENTER, 8, 10)
            .build();

        int idx = 1;
    

        for (InternshipApp a : apps) {
            Internship i = a.getInternship();
            String withdrawalStatus = a.isWithdrawalRequested() ? "Pending" : "-";
            tp.addRow(
                    idx++,
                    a.getId(),
                    (i != null ? ConsoleHelper.safe(i.getTitle()) : "-"),
                    (i != null ? ConsoleHelper.safe(i.getCompanyName()) : "-"),
                    a.getStatus(),
                    withdrawalStatus
          );
        }

        System.out.println(tp.render());
        
        // Show action menu
        showActionMenu(apps);
    }
    
    /**
     * Display options for accepting placements or requesting withdrawals
     */
    private void showActionMenu(List<InternshipApp> apps) {
        boolean hasSuccessful = apps.stream()
                .anyMatch(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL);
        boolean hasActive = apps.stream()
                .anyMatch(app -> app.getStatus() != ApplicationStatus.WITHDRAWN &&
                               app.getStatus() != ApplicationStatus.UNSUCCESSFUL);
        
        System.out.println("\n--- Options ---");
        if (hasSuccessful) {
            System.out.println("1) Accept Placement (from successful applications)");
        }
        if (hasActive) {
            System.out.println("2) Request Withdrawal from Application");
        }
        System.out.println("0) Back to Menu");
        
        if (!hasSuccessful && !hasActive) {
            return;
        }
        
        System.out.print("\nSelect option: ");
        String input = sc.nextLine().trim();
        
        switch (input) {
            case "1":
                if (hasSuccessful) {
                    new AcceptPlacementAction(me, sc).execute();
                }
                break;
            case "2":
                if (hasActive) {
                    new RequestWithdrawalAction(me, sc).execute();
                }
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}