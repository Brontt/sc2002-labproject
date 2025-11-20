package menu.StaffMenu;

import java.util.Scanner;

/**
 * UI boundary for Career Centre Staff functionality.
 *
 * Presents staff-specific menu options and delegates command handling to the
 * {@link StaffMenuControl}. This class is intentionally focused on presentation
 * concerns only (printing menus and reading user input).
 */
public class StaffMenuUI {
    private final StaffMenuControl control;
    private final Scanner sc = new Scanner(System.in);

    /**
     * Construct the staff menu UI.
     *
     * @param control the control that will handle selected commands
     */
    public StaffMenuUI(StaffMenuControl control) {
        this.control = control;
    }

    /**
     * Run the interactive menu loop until the user chooses to logout.
     * This method reads user input from the console and delegates actions
     * to the control.
     */
    public void run() {
        while (true) {
            printMenu();
            String cmd = sc.nextLine().trim();
            if ("0".equals(cmd)) return;
            control.handle(cmd);
        }
    }

    /**
     * Print the staff menu options. This method performs only display work
     * and must not execute any business logic.
     */
    private void printMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         CAREER CENTRE STAFF MENU           ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│  1) Approve Company Representatives         │");
        System.out.println("│  2) Handle Withdrawal Requests              │");
        System.out.println("│  3) Manage Internships (Overview)           │");
        System.out.println("│  4) Bulk Approve Internship Opportunities   │");
        System.out.println("│  5) Generate Reports                        │");
        System.out.println("│  0) Logout                                  │");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.print("\nSelect option: ");
    }
}
