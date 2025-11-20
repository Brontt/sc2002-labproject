package menu.CompanyRepMenu;

import menu.BaseMenuUI;

/**
 * Company Representative menu UI - extends {@link BaseMenuUI} to reduce duplication
 * and implement the template menu flow. This class is responsible only for
 * presenting the menu and delegating commands to {@link CompanyRepMenuControl}.
 */
public class CompanyRepMenuUI extends BaseMenuUI {
    private final CompanyRepMenuControl control;

    /**
     * Construct the UI with the given control.
     *
     * @param control the control that will handle menu commands
     */
    public CompanyRepMenuUI(CompanyRepMenuControl control) {
        this.control = control;
    }

    /**
     * Delegate the entered command to the control for handling.
     *
     * @param command the raw command string entered by the user
     */
    @Override
    protected void handleCommand(String command) {
        control.handle(command);
    }

    /**
     * Print the company representative's menu options to the console.
     * This method contains only presentation concerns and must not perform
     * business logic.
     */
    @Override
    protected void printMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("  ║          COMPANY REPRESENTATIVE MENU       ║");
        System.out.println("  ╚════════════════════════════════════════════╝");
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│  1) Create Internship                       │");
        System.out.println("│  2) Manage My Internships                   │");
        System.out.println("│  3) View Applications                       │");
        System.out.println("│  4) Approve/Reject Applications             │");
        System.out.println("│  0) Logout                                  │");
        System.out.println("└─────────────────────────────────────────────┘");
        System.out.print("\nSelect option: ");
    }
}