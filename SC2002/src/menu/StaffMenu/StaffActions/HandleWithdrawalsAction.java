package menu.StaffMenu.StaffActions;

import menu.MenuAction;
import repository.Repository;
import internship.Internship;
import internship.InternshipApp;

import java.util.List;
import java.util.Scanner;

/**
 * Action for staff to handle withdrawal requests from students.
 *
 * The current scaffold is a placeholder; in a complete system this would
 * interact with the {@code withdrawal} package to accept/deny student
 * withdrawal requests and update placements accordingly.
 */
public class HandleWithdrawalsAction implements MenuAction {
    private final Scanner sc;

    /**
     * @param sc scanner used to read staff decisions from the console
     */
    public HandleWithdrawalsAction(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Execute the withdrawals handling flow. Currently prints a placeholder.
     */
    @Override
    public void execute() {
        System.out.println("\n--- HANDLE WITHDRAWAL REQUESTS ---");

        // 1) Get all apps where withdrawalRequested = true and status = CONFIRMED
        List<InternshipApp> pending = Repository.findPendingWithdrawals();
        if (pending == null || pending.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }

        for (InternshipApp app : pending) {
            Internship i = app.getInternship();

            System.out.println("\nApplication ID : " + app.getId());
            System.out.println("Student ID     : " + app.getStudent().getUserId());
            System.out.println("Internship     : " +
                    (i != null ? i.getId() + " | " + i.getTitle() : "-"));
            System.out.println("Current Status : " + app.getStatus());
            System.out.print("Approve withdrawal? (y/n/q): ");

            String ans = sc.nextLine().trim().toLowerCase();

            // allow early exit
            if ("q".equals(ans)) {
                System.out.println("Stopping withdrawal handling.");
                return;
            }

            // basic validation
            while (!"y".equals(ans) && !"n".equals(ans)) {
                System.out.print("Please enter 'y' (yes), 'n' (no) or 'q' (quit): ");
                ans = sc.nextLine().trim().toLowerCase();
                if ("q".equals(ans)) {
                    System.out.println("Stopping withdrawal handling.");
                    return;
                }
            }

            if ("y".equals(ans)) {
                // ✅ Approve: mark withdrawn + free a slot
                app.withdraw();          // sets status WITHDRAWN and clears withdrawalRequested
                if (i != null) {
                    i.incrementSlot();   // one confirmed student left → slot freed
                }
                Repository.updateApplication(app);

                System.out.println("-> Withdrawal approved for " + app.getId());
                // (Optional) notify waitlisted students here via WaitlistService / NotificationService
            } else {
                // ❌ Reject: keep status, just clear the flag
                app.setWithdrawalRequested(false);
                Repository.updateApplication(app);
                System.out.println("-> Withdrawal rejected for " + app.getId());
            }
        }

        System.out.println("\nFinished processing all pending withdrawal requests.");
    }
}
