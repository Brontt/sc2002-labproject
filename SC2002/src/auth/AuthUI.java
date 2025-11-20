package auth;

import internship.Internship;
import internship.InternshipApp;
import java.util.List;
import java.util.Scanner;
import repository.Repository;
import user.*;
import util.PasswordService;

/**
 * Boundary/UI class responsible for authenticating users and handling
 * registration of company representatives. Interacts with the
 * {@link AuthControl} to perform authentication.
 */
public class AuthUI {
    private final Scanner sc;
    private final List<Student> students;
    private final List<CareerCentreStaff> staff;
    private final List<CompanyRep> reps;
    private final List<User> users;
    private final List<Internship> internships;
    private final List<InternshipApp> applications;
    private final AuthControl authControl;

    public AuthUI(Scanner sc,
                  List<Student> students,
                  List<CareerCentreStaff> staff,
                  List<CompanyRep> reps,
                  List<User> users,
                  List<Internship> internships,
                  List<InternshipApp> applications,
                  AuthControl authControl) {
        this.sc = sc;
        this.students = students;
        this.staff = staff;
        this.reps = reps;
        this.users = users;
        this.internships = internships;
        this.applications = applications;
        this.authControl = authControl;
    }

    /**
     * Prompt for ID/password, authenticate and return the authenticated
     * {@link SC2002.user.User} or null on failure.
     *
     * @return authenticated user or null
     */
    public User loginFlow() {
        System.out.print("ID/Email: ");
        String rawId = sc.nextLine();
        String id = rawId == null ? "" : rawId.trim();

        // Boundary asks control to find user first
        User byId = authControl.findById(id);
        if (byId == null) {
            System.out.println("No such user ID/email: '" + id + "'.");
            System.out.println("Student ID should start with U, Career Centre Staff ID is their NTU Account, Company Rep email is used as ID.");
            return null;
        }

        // Prompt password only after id validated
        System.out.print("Password: ");
        String pw = sc.nextLine();

        AuthControl.AuthResult res = authControl.authenticate(byId, pw);
        if (!res.isSuccess()) {
            System.out.println(res.getMessage());
            return null;
        }

        return res.getUser();
    }

    /**
     * Interactive flow for registering a new CompanyRep. Returns the
     * created CompanyRep or null if registration failed.
     *
     * @return created CompanyRep or null
     */
    public CompanyRep registerRepFlow() {
        System.out.println("\n=== Rep Registration ===");
        System.out.print("Email: "); String email = sc.nextLine().trim();
        if (users.stream().anyMatch(u -> u.getUserId().equalsIgnoreCase(email))) {
            System.out.println("Account exists.");
            return null;
        }
        System.out.print("Name: "); String name = sc.nextLine().trim();
        // Use default password - company rep must change it after account creation
        String defaultPassword = "password";
        System.out.println("(Default password set to 'password'. You must change it after account creation.)");
        System.out.print("Company: "); String company = sc.nextLine().trim();
        System.out.print("Department: "); String dept = sc.nextLine().trim();
        System.out.print("Position: "); String pos = sc.nextLine().trim();

        CompanyRep rep = new CompanyRep(email, name, PasswordService.hashPassword(defaultPassword), company, dept, pos, false);
        rep.setApproved(false);
        reps.add(rep);

        // re-bootstrap repository so other components see new user
        users.clear();
        users.addAll(students);
        users.addAll(staff);
        users.addAll(reps);
        Repository.bootstrap(users, internships, applications);

        System.out.println("Submitted. Await CCS approval.");
        return rep;
    }
}