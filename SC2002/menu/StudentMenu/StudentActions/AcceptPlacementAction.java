package menu.StudentMenu.StudentActions;

import internship.ApplicationStatus;
import internship.Internship;
import internship.InternshipApp;
import menu.MenuAction;
import repository.Repository;
import user.Student;
import util.ConsoleHelper;

import java.util.List;
import java.util.Scanner;

/**
 * Action that allows a student to accept an internship placement.
 * 
 * A student can only accept one placement. Once accepted, all other applications
 * are automatically withdrawn.
 */
public class AcceptPlacementAction implements MenuAction {
    private final Student me;
    private final Scanner sc;

    public AcceptPlacementAction(Student me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    @Override
    public void execute() {
        List<InternshipApp> apps = Repository.findApplicationsByStudentId(me.getUserId());
        
        // Check if student already has a confirmed placement
        boolean hasConfirmed = apps.stream()
                .anyMatch(app -> app.getStatus() == ApplicationStatus.CONFIRMED);
        if (hasConfirmed) {
            System.out.println("You have already confirmed a placement. Only 1 placement can be accepted.");
            return;
        }

        // Filter for successful applications
        List<InternshipApp> successful = apps.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SUCCESSFUL)
                .toList();

        if (successful.isEmpty()) {
            System.out.println("No successful applications found.");
            return;
        }

        // Display successful applications
        System.out.println("\n=== Successful Applications ===");
        for (int i = 0; i < successful.size(); i++) {
            InternshipApp app = successful.get(i);
            System.out.printf("%d) [%s] %s @ %s%n",
                    i + 1,
                    app.getId(),
                    app.getInternship().getTitle(),
                    app.getInternship().getCompanyName());
        }

        System.out.print("\nSelect application to accept (or 0 to cancel): ");
        String input = sc.nextLine().trim();
        
        if (input.equals("0")) {
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > successful.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            InternshipApp selectedApp = successful.get(choice - 1);
            Internship selectedInternship = selectedApp.getInternship();
            
            // Confirm this application
            selectedApp.confirm();
            // Decrease the available slots in the internship
            selectedInternship.decrementSlot();
            
            // Withdraw all other applications (PENDING, SUCCESSFUL, etc.)
            for (InternshipApp app : apps) {
                if (!app.getId().equals(selectedApp.getId())) {
                    // Withdraw all non-terminal applications
                    if (app.getStatus() != ApplicationStatus.WITHDRAWN &&
                        app.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
                        app.withdraw();
                    }
                }
            }
            
            // Save both internships and applications to persist changes
            Repository.saveAllInternships();
            Repository.saveAllApplications();
            
            System.out.println("Placement accepted: " + selectedInternship.getTitle());
            System.out.println("All other applications have been automatically withdrawn.");
            
            // Check if this internship is now at capacity and auto-withdraw other applications for it
            if (selectedInternship.getSlotsRemaining() <= 0) {
                int withdrawnCount = autoWithdrawOtherApplicationsForInternship(selectedInternship);
                if (withdrawnCount > 0) {
                    System.out.println("\n! Internship " + selectedInternship.getTitle() + " is now at full capacity.");
                    System.out.println("! " + withdrawnCount + " other application(s) automatically withdrawn.");
                    Repository.saveAllApplications();
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Automatically withdraw all pending/successful applications for the given internship.
     * Called when an internship reaches capacity due to a student accepting a placement.
     *
     * @param internship The internship that has reached capacity.
     * @return The number of applications that were withdrawn.
     */
    private int autoWithdrawOtherApplicationsForInternship(Internship internship) {
        List<InternshipApp> allApps = Repository.findApplicationsByInternshipId(internship.getId());
        int withdrawnCount = 0;
        
        for (InternshipApp app : allApps) {
            if (app.getStatus() == ApplicationStatus.PENDING || 
                app.getStatus() == ApplicationStatus.SUCCESSFUL) {
                app.setStatus(ApplicationStatus.WITHDRAWN);
                withdrawnCount++;
            }
        }
        
        return withdrawnCount;
    }
}
