package menu.StudentMenu.StudentActions;

import internship.ApplicationStatus;
import internship.InternshipApp;
import menu.MenuAction;
import repository.Repository;
import user.Student;
import util.ConsoleHelper;

import java.util.List;
import java.util.Scanner;

/**
 * Action that allows a student to request withdrawal from an internship application.
 * 
 * Students can request withdrawal before or after placement confirmation.
 * The withdrawal is subject to approval from Career Center Staff.
 */
public class RequestWithdrawalAction implements MenuAction {
    private final Student me;
    private final Scanner sc;

    public RequestWithdrawalAction(Student me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<InternshipApp> apps = Repository.findApplicationsByStudentId(me.getUserId());
        
        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        // Filter for applications that can be withdrawn (not already withdrawn or unsuccessful)
        List<InternshipApp> withdrawable = apps.stream()
                .filter(app -> app.getStatus() != ApplicationStatus.WITHDRAWN &&
                             app.getStatus() != ApplicationStatus.UNSUCCESSFUL)
                .toList();

        if (withdrawable.isEmpty()) {
            System.out.println("No active applications to withdraw from.");
            return;
        }

        // Display withdrawable applications
        System.out.println("\n=== Your Active Applications ===");
        for (int i = 0; i < withdrawable.size(); i++) {
            InternshipApp app = withdrawable.get(i);
            System.out.printf("%d) [%s] %s @ %s (Status: %s)%n",
                    i + 1,
                    app.getId(),
                    app.getInternship().getTitle(),
                    app.getInternship().getCompanyName(),
                    app.getStatus());
        }

        System.out.print("\nSelect application to withdraw from (or 0 to cancel): ");
        String input = sc.nextLine().trim();
        
        if (input.equals("0")) {
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > withdrawable.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            InternshipApp selectedApp = withdrawable.get(choice - 1);
            
            // Check if it's a confirmed placement
            if (selectedApp.getStatus() == ApplicationStatus.CONFIRMED) {
                boolean confirm = ConsoleHelper.askYesNo(sc, 
                    "This is your confirmed placement. Are you sure you want to withdraw? (y/n): ");
                if (!confirm) {
                    return;
                }
            }

            // Mark withdrawal as requested
            selectedApp.setWithdrawalRequested(true);
            // No need to save - in-memory references are updated automatically
            System.out.println("\nWithdrawal request submitted: " + selectedApp.getInternship().getTitle());
            System.out.println("Status: Pending approval from Career Center Staff");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
