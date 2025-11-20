package menu.StaffMenu.StaffActions;

import user.CareerCentreStaff;
import menu.MenuAction;
import repository.Repository;
import java.util.Scanner;

/**
 * Action that prompts a career centre staff member to change their password.
 *
 * Reads a new password from the console and delegates to
 * {@link CareerCentreStaff#changePassword}. Any validation exceptions are caught and
 * presented to the user. The password is then persisted to the CSV file.
 */
public class ChangePasswordAction implements MenuAction {
    private final CareerCentreStaff me;
    private final Scanner sc;

    /**
     * @param me current career centre staff member
     * @param sc console scanner for password input
     */
    public ChangePasswordAction(CareerCentreStaff me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    /**
     * Execute the change password flow and print status to the console.
     */
    @Override
    public void execute() {
        System.out.print("Enter new password: ");
        String pw = sc.nextLine().trim();
        try {
            me.changePassword(pw);
            // Save password change to CSV
            Repository.saveAllStaff();
            System.out.println("Password changed successfully.");
        } catch (Exception e) {
            System.out.println("Failed to change password: " + e.getMessage());
        }
    }
}
