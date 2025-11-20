package menu.StaffMenu.StaffActions;

import internship.ApplicationStatus;
import internship.InternshipApp;
import menu.MenuAction;
import repository.Repository;
import user.CareerCentreStaff;
import util.ConsoleHelper;
import util.TablePrinter;

import java.util.List;
import java.util.Scanner;

/**
 * Action for Career Center Staff to manage student withdrawal requests.
 * 
 * Staff can approve or deny withdrawal requests from students.
 * When approved, the application is marked as WITHDRAWN.
 */
public class ManageWithdrawalRequestsAction implements MenuAction {
    private final CareerCentreStaff me;
    private final Scanner sc;

    public ManageWithdrawalRequestsAction(CareerCentreStaff me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<InternshipApp> allApps = Repository.findAllApplications();
        
        // Filter for applications with withdrawal requests
        List<InternshipApp> withdrawalRequests = allApps.stream()
                .filter(InternshipApp::isWithdrawalRequested)
                .toList();

        if (withdrawalRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }

        // Display pending withdrawal requests
        System.out.println("\n=== Pending Withdrawal Requests ===");
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(150)
                .addColumn("No.", TablePrinter.Align.RIGHT, 2, 4)
                .addColumn("AppID", TablePrinter.Align.LEFT, 6, 10)
                .addColumn("Student", TablePrinter.Align.LEFT, 10, 25)
                .addColumn("Internship", TablePrinter.Align.LEFT, 10, 30)
                .addColumn("Company", TablePrinter.Align.LEFT, 8, 20)
                .addColumn("Status", TablePrinter.Align.CENTER, 10, 14)
                .build();

        int idx = 1;
        for (InternshipApp app : withdrawalRequests) {
            tp.addRow(
                    idx++,
                    app.getId(),
                    app.getStudent().getName(),
                    ConsoleHelper.safe(app.getInternship().getTitle()),
                    ConsoleHelper.safe(app.getInternship().getCompanyName()),
                    app.getStatus()
            );
        }
        System.out.println(tp.render());

        System.out.print("\nSelect withdrawal request to process (or 0 to cancel): ");
        String input = sc.nextLine().trim();
        
        if (input.equals("0")) {
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > withdrawalRequests.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            InternshipApp selectedApp = withdrawalRequests.get(choice - 1);
            
            System.out.println("\nWith drawal Request Details:");
            System.out.println("  Student: " + selectedApp.getStudent().getName());
            System.out.println("  Application: " + selectedApp.getId());
            System.out.println("  Internship: " + selectedApp.getInternship().getTitle());
            System.out.println("  Company: " + selectedApp.getInternship().getCompanyName());
            System.out.println("  Current Status: " + selectedApp.getStatus());
            
            boolean approve = ConsoleHelper.askYesNo(sc, "\nApprove withdrawal request? (y/n): ");
            
            if (approve) {
                selectedApp.withdraw();
                Repository.saveApplication(selectedApp);
                // Save internships as well since confirmedCount may have changed
                Repository.saveAllInternships();
                System.out.println("\nWithdrawal request approved.");
                System.out.println("Application status updated to WITHDRAWN.");
            } else {
                selectedApp.setWithdrawalRequested(false);
                Repository.saveApplication(selectedApp);
                System.out.println("\nWithdrawal request denied.");
                System.out.println("Student will be notified of the decision.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
