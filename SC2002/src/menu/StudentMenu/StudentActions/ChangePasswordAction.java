package menu.StudentMenu.StudentActions;

import user.Student;
import menu.MenuAction;
import java.util.Scanner;

/**
 * Action that prompts the student to change their password.
 *
 * Reads a new password from the console and delegates to
 * {@link Student#changePassword}. Any validation exceptions are caught and
 * presented to the user.
 */
public class ChangePasswordAction implements MenuAction {
    private final Student me;
    private final Scanner sc;

    /**
     * @param me current student
     * @param sc console scanner for password input
     */
    public ChangePasswordAction(Student me, Scanner sc) {
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
            System.out.println("Password changed.");
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }
}