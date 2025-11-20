package menu.StaffMenu.StaffActions;

import repository.Repository;
import user.CompanyRep;
import menu.MenuAction;

import java.util.List;
import java.util.Scanner;

/**
 * Action that allows career centre staff to approve pending company representatives.
 *
 * This action queries the {@link Repository} for pending reps and prompts the
 * staff member to approve each one. Approved reps are persisted via the
 * repository update method.
 */
public class ApproveRepAction implements MenuAction {
    private final Scanner sc;

    public ApproveRepAction(Scanner sc) { this.sc = sc; }

    /**
     * Execute the approval flow: list pending reps, ask for confirmation,
     * and persist approvals.
     */
    @Override
    public void execute() {
        List<CompanyRep> pending = Repository.findPendingCompanyReps();

        if (pending == null || pending.isEmpty()) {
            System.out.println("\nNo pending company representatives to approve.");
            return;
        }

        System.out.println("\n--- APPROVE COMPANY REPRESENTATIVES ---");
        System.out.println("Pending reps: " + pending.size());
        System.out.println("Type 'y' to approve, 'n' to skip, 'q' to stop.\n");

        for (CompanyRep r : pending) {
            System.out.println("Email: " + r.getUserId());
            System.out.println("Name : " + r.getName());
            System.out.print("Approve this representative? (y/n/q): ");

            String ans = sc.nextLine().trim().toLowerCase();

            // allow early exit
            if ("q".equals(ans)) {
                System.out.println("Stopping approval process.");
                return;
            }

            // validate input
            while (!"y".equals(ans) && !"n".equals(ans)) {
                System.out.print("Please enter 'y' (yes), 'n' (no) or 'q' (quit): ");
                ans = sc.nextLine().trim().toLowerCase();
                if ("q".equals(ans)) {
                    System.out.println("Stopping approval process.");
                    return;
                }
            }

            if ("y".equals(ans)) {
                r.setApproved(true);
                Repository.updateUser(r);
                System.out.println("-> Approved: " + r.getUserId() + "\n");
            } else {
                System.out.println("-> Skipped: " + r.getUserId() + "\n");
            }
        }

        System.out.println("Finished processing all pending company representatives.");
    }
}