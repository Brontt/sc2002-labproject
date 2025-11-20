package menu.StudentMenu.StudentActions;

import menu.MenuAction;
import user.Student;
import internship.Internship;
import repository.Repository;
import util.ValidationExceptions;

import java.util.Scanner;

/**
 * Action that allows a student to apply for an internship by ID.
 *
 * This action reads an internship id from the console, verifies existence via
 * the {@link Repository} and invokes {@link Student#applyForInternship}.
 * It catches the domain-specific duplicate application exception and prints
 * friendly messages for the user.
 */
public class ApplyInternshipAction implements MenuAction {
    private final Student me;
    private final Scanner sc;

    /**
     * @param me the student performing the application
     * @param sc the console scanner used to read the internship id
     */
    public ApplyInternshipAction(Student me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    /**
     * Execute the apply flow: read internship id, validate, and submit
     * the application via {@link Student#applyForInternship}.
     */
    @Override
    public void execute() {
        System.out.print("Enter internship ID to apply: ");
        String id = sc.nextLine().trim();
        Internship i = Repository.findInternshipById(id);
        if (i == null) {
            System.out.println("No such internship.");
            return;
        }
        try {
            me.applyForInternship(i);
        } catch (ValidationExceptions.DuplicateApplicationException e) {
            System.out.println("Already applied to this internship.");
        } catch (Exception e) {
            System.out.println("Application failed: " + e.getMessage());
        }
    }
}