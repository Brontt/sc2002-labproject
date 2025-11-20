package app;

import auth.AuthControl;
import auth.AuthUI;
import internship.*;
import java.util.*;
import menu.CompanyRepMenu.CompanyRepMenuControl;
import menu.CompanyRepMenu.CompanyRepMenuUI;
import menu.StaffMenu.StaffMenuControl;
import menu.StaffMenu.StaffMenuUI;
import menu.StudentMenu.StudentMenuControl;
import menu.StudentMenu.StudentMenuUI;
import notification.NotificationService;
import repository.Repository;
import storage.Storage;
import user.*;
import withdrawal.WithdrawalQueue;

public class SystemCoordinator {
    /**
     * Central composition root and application coordinator. Wires storage,
     * repository, authentication and top-level menu UIs for different user roles.
     */
    private final Storage storage;
    private final Scanner sc = new Scanner(System.in);
    private Repository repository;

    private final List<Student> students = new ArrayList<>();
    private final List<CareerCentreStaff> staff = new ArrayList<>();
    private final List<CompanyRep> reps = new ArrayList<>();
    private final List<Internship> internships = new ArrayList<>();
    private final List<InternshipApp> applications = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    private final WithdrawalQueue withdrawalQueue = new WithdrawalQueue();
    private final NotificationService notices = new NotificationService();

    private final AuthUI auth;

    public SystemCoordinator(Storage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Storage cannot be null.");
        }
        this.storage = storage;

        // Set storage targets
        storage.setTargets(students, staff, reps, internships, applications);

        // Initialize authentication
        AuthControl authControl = new AuthControl(users);
        this.auth = new AuthUI(sc, students, staff, reps, users, internships, applications, authControl);
    }

    public void loadAll() {
                /**
         * Load all data from configured storage implementations and bootstrap the
         * in-memory repository.
         */
        if (storage == null) {
            System.err.println("[ERROR] Storage is not initialized.");
            return;
        }
        storage.loadAll();
        rebuildUsers();
        Repository.bootstrap(users, internships, applications);

        long stu = users.stream().filter(u -> u instanceof Student).count();
        long stf = users.stream().filter(u -> u instanceof CareerCentreStaff).count();
        long rep = users.stream().filter(u -> u instanceof CompanyRep).count();
        System.out.printf("[DEBUG] Loaded: %d students, %d staff, %d reps, %d internships, %d applications%n",
                stu, stf, rep, internships.size(), applications.size());
    }

    public void saveAll() {
        if (storage == null) {
            System.err.println("[ERROR] Storage is not initialized.");
            return;
        }
        // Sync all Repository data back to SystemCoordinator before saving
        // Sync users (students, staff, reps)
        students.clear();
        staff.clear();
        reps.clear();
        for (User u : Repository.findAllUsers()) {
            if (u instanceof Student) students.add((Student) u);
            else if (u instanceof CareerCentreStaff) staff.add((CareerCentreStaff) u);
            else if (u instanceof CompanyRep) reps.add((CompanyRep) u);
        }
        // Rebuild users list
        rebuildUsers();
        // Sync internships and fix their postedBy references
        internships.clear();
        for (Internship i : Repository.findAllInternships()) {
            if (i.getPostedBy() != null) {
                CompanyRep newRep = reps.stream()
                    .filter(r -> r.getUserId().equalsIgnoreCase(i.getPostedBy().getUserId()))
                    .findFirst()
                    .orElse(i.getPostedBy());
                i.setPostedBy(newRep);
            }
            internships.add(i);
        }
        // Sync applications
        applications.clear();
        applications.addAll(Repository.findAllApplications());
        
        storage.saveAll();
    }

    private void rebuildUsers() {
        users.clear();
        users.addAll(students);
        users.addAll(staff);
        users.addAll(reps);
    }

    public void entryScreen() {
        while (true) {
            System.out.println("1) Login");
            System.out.println("2) Register as Company Representative");
            System.out.println("0) Exit");
            System.out.print("Select: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1" -> handleLogin();
                case "2" -> handleRegisterRep();
                case "0" -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void handleLogin() {
        User authenticated = auth.loginFlow();
        if (authenticated != null) {
            AppState.get().setCurrentUser(authenticated);
            System.out.println("Welcome, " + authenticated.getName());
            if (!authenticated.getInbox().isEmpty()) {
                System.out.println("\nNotifications:");
                authenticated.getInbox().forEach(m -> System.out.println(" - " + m));
            }

            // Handle user-specific menus
            if (authenticated instanceof Student s) {
                StudentMenuControl control = new StudentMenuControl(s, sc);
                new StudentMenuUI(control).run();
                // Sync applications in case student applied for internships
                applications.clear();
                applications.addAll(Repository.findAllApplications());
            } else if (authenticated instanceof CompanyRep r) {
                CompanyRepMenuControl control = new CompanyRepMenuControl(r, sc);
                new CompanyRepMenuUI(control).run();
                // Sync internships and fix their postedBy references to the current rep objects
                internships.clear();
                for (Internship i : Repository.findAllInternships()) {
                    if (i.getPostedBy() != null) {
                        // Find the matching rep in the reps list and update the reference
                        CompanyRep newRep = reps.stream()
                            .filter(rep -> rep.getUserId().equalsIgnoreCase(i.getPostedBy().getUserId()))
                            .findFirst()
                            .orElse(i.getPostedBy()); // fallback if not found
                        i.setPostedBy(newRep);
                    }
                    internships.add(i);
                }
            } else if (authenticated instanceof CareerCentreStaff ccs) {
                StaffMenuControl control = new StaffMenuControl(ccs, sc);
                new StaffMenuUI(control).run();
                // Sync all users back from Repository in case staff approved new reps or changed user data
                students.clear();
                staff.clear();
                reps.clear();
                for (User u : Repository.findAllUsers()) {
                    if (u instanceof Student) students.add((Student) u);
                    else if (u instanceof CareerCentreStaff) staff.add((CareerCentreStaff) u);
                    else if (u instanceof CompanyRep) reps.add((CompanyRep) u);
                }
                // Rebuild users with synced data
                rebuildUsers();
                // Sync internships and fix their postedBy references to point to the new rep objects
                internships.clear();
                for (Internship i : Repository.findAllInternships()) {
                    if (i.getPostedBy() != null) {
                        // Find the matching rep in the new reps list and update the reference
                        CompanyRep newRep = reps.stream()
                            .filter(r -> r.getUserId().equalsIgnoreCase(i.getPostedBy().getUserId()))
                            .findFirst()
                            .orElse(i.getPostedBy()); // fallback to old ref if not found
                        i.setPostedBy(newRep);
                    }
                    internships.add(i);
                }
            }

            AppState.get().clearCurrentUser();
            System.out.println("Logged out.");
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }

    private void handleRegisterRep() {
        CompanyRep created = auth.registerRepFlow();
        if (created != null) {
            rebuildUsers();
            System.out.println("Registration successful. Welcome, " + created.getName());
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private User findUser(String id) {
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }
}