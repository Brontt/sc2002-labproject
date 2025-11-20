package menu.StudentMenu;

import java.util.Scanner;

/**
 * UI boundary for Student interactions.
 *
 * Responsible for rendering the student-facing menu and delegating user
 * selections to {@link StudentMenuControl}. This class focuses purely on
 * presentation and reading user input; it does not perform business logic.
 */
public class StudentMenuUI {
    private final StudentMenuControl control;
    private final Scanner sc = new Scanner(System.in);

    /**
     * Create a new Student menu UI bound to the provided control.
     *
     * @param control the control that handles student menu commands
     */
    public StudentMenuUI(StudentMenuControl control) {
        this.control = control;
    }

    /**
     * Run the interactive menu loop until the student logs out.
     */
    public void run() {
        while (true) {
            printMenu();
            String c = sc.nextLine().trim();
            if ("0".equals(c)) return;
            control.handle(c);
        }
    }

    /**
     * Print the formatted student menu. All dynamic state (filter/recommendation
     * toggles) is read from the control to avoid duplicating logic here.
     */
    private void printMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          STUDENT MENU                      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("Status: Filter: " + (control.isFilterEnabled() ? "ON" : "OFF") +
                           " | Recommendations: " + (control.isRecommendationEnabled() ? "ON" : "OFF"));
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│  1) View Internships                        │");
        System.out.println("│  2) Configure Filters                       │");
        System.out.println("│  3) Configure Recommendations               │");
        System.out.println("│  4) Toggle Filter Mode (Currently: " + (control.isFilterEnabled() ? "ON" : "OFF") + ")     │");
        System.out.println("│  5) Toggle Recommendation Mode (" + (control.isRecommendationEnabled() ? "ON" : "OFF") + ")│");
        System.out.println("│  6) Clear All Filters & Settings            │");
        System.out.println("│  7) Apply for Internship                    │");
        System.out.println("│  8) Show Application Status / Manage Offers │");
        System.out.println("│  9) Change Password                         │");
        System.out.println("│  0) Logout                                  │");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.print("\nSelect option: ");
    }
}
