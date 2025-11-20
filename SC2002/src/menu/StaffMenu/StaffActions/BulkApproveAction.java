package menu.StaffMenu.StaffActions;

import internship.Internship;
import internship.Internship.InternshipStatus;
import menu.MenuAction;
import repository.Repository;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bulk-approve action used by staff to approve multiple applications or reps
 * in a single operation. The scaffold currently contains only a placeholder
 * implementation — replace with batch logic as needed.
 */
public class BulkApproveAction implements MenuAction {
    private final Scanner sc;

    public BulkApproveAction(Scanner sc) { this.sc = sc; }

    /**
     * Execute the bulk approve workflow. Currently prints a stub message.
     */
    @Override
    public void execute() {
        while (true) {
            System.out.println("\n--- BULK APPROVE INTERNSHIP OPPORTUNITIES ---");
            System.out.println("1) Manually approve pending internships");
            System.out.println("2) Bulk approve ALL pending internships except selected ones");
            System.out.println("0) Back");
            System.out.print("Select option: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> manualApprove();
                case "2" -> bulkApproveWithExceptions();
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    private List<Internship> getPendingInternships() {
        // adapt if you have a dedicated method in Repository
        return Repository.findAllInternships().stream()
                .filter(i -> i.getStatus() == InternshipStatus.PENDING)
                .collect(Collectors.toList());
    }

    /** Option 1: approve one by one */
    private void manualApprove() {
        List<Internship> pending = getPendingInternships();
        if (pending.isEmpty()) {
            System.out.println("No pending internships to approve.");
            return;
        }

        System.out.println("\nPending internships:");
        for (Internship i : pending) {
            System.out.println(i.getId() + " | " + i.getTitle() + " | " + i.getCompanyName());
            System.out.print("Approve this internship? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            if ("y".equals(ans)) {
                i.setStatus(InternshipStatus.APPROVED);
                i.setVisible(true);
                Repository.updateInternship(i); // or saveAll later
                System.out.println("  -> Approved " + i.getId());
            }
        }
    }

    /** Option 2: bulk approve all except some IDs */
    private void bulkApproveWithExceptions() {
        List<Internship> pending = getPendingInternships();
        if (pending.isEmpty()) {
            System.out.println("No pending internships to approve.");
            return;
        }

        System.out.println("\nPending internships:");
        for (Internship i : pending) {
            System.out.println(" - " + i.getId() + " | " + i.getTitle() + " | " + i.getCompanyName());
        }

        System.out.print("\nEnter internship IDs to EXCLUDE (comma-separated), or press ENTER for none: ");
        String line = sc.nextLine().trim();

        Set<String> exclude = new HashSet<>();
        if (!line.isEmpty()) {
            for (String part : line.split(",")) {
                exclude.add(part.trim().toUpperCase());
            }
        }

        int count = 0;
        for (Internship i : pending) {
            if (exclude.contains(i.getId().toUpperCase())) {
                System.out.println("Skipping " + i.getId());
                continue;
            }
            i.setStatus(InternshipStatus.APPROVED);
            i.setVisible(true);
            Repository.updateInternship(i);
            count++;
        }

        System.out.println("\nBulk approval complete. Approved " + count + " internships.");
    }
}
