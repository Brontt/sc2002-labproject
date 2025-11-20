package user;

import internship.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import policy.DefaultEligibilityPolicy;
import policy.EligibilityPolicy;
import repository.Repository;
import util.ValidationExceptions.DuplicateApplicationException;

public class Student extends User {
    /**
     * Represents a student user with academic profile and a list of
     * internship applications. The class delegates eligibility checks to
     * an {@link SC2002.policy.EligibilityPolicy} implementation.
     */
    private int year;
    private String major;
    private final List<InternshipApp> applications = new ArrayList<>();
    private final EligibilityPolicy policy = new DefaultEligibilityPolicy();

    public Student(String userId, String name, String password, int year, String major) {
        super(userId, name, password);
        this.year = year;
        this.major = major;
    }

    public int getYear() { return year; }
    public String getMajor() { return major; }

    public void applyForInternship(Internship internship) {
        /**
         * Attempt to apply for the given internship. The method validates
         * that the internship is non-null, that the student meets the
         * eligibility policy, that the student has not already applied,
         * and that slots remain.
         *
         * @param internship the internship to apply for
         */
        if (internship == null) {
            System.out.println("Internship cannot be null.");
            return;
        }

        boolean isEligible = policy.isEligible(this, internship);
        if (!isEligible) {
            System.out.println("Not eligible based on policy.");
        }

        var existingApplications = Repository.findApplicationsByStudentId(getUserId());
        boolean isDuplicate = existingApplications.stream()
            .anyMatch(app -> Objects.equals(app.getInternship().getId(), internship.getId()));

        if (isDuplicate) {
            throw new DuplicateApplicationException("Already applied to this internship.");
        }

        // Count only active applications (not withdrawn, unsuccessful, or confirmed)
        long activeApplications = existingApplications.stream()
            .filter(app -> app.getStatus() != ApplicationStatus.WITHDRAWN &&
                          app.getStatus() != ApplicationStatus.UNSUCCESSFUL &&
                          app.getStatus() != ApplicationStatus.CONFIRMED)
            .count();

        if (activeApplications >= 3) {
            System.out.println("You have already applied to 3 internships. You cannot apply for more.");
            return;
        }

        if (internship.getSlotsRemaining() <= 0) {
            System.out.println("No slots remaining. Consider joining the waitlist.");
            return;
        }

        InternshipApp application = new InternshipApp(Repository.newApplicationId(), this, internship);
        Repository.saveApplication(application);
        applications.add(application); // Add to local list
        System.out.println("Application submitted.");
    }

    public void acceptPlacement(InternshipApp application) {
        /**
         * Accept an offered placement for this student. This will update
         * the application status to CONFIRMED, decrement internship slots,
         * and set internship status to FILLED when appropriate.
         *
         * @param application the application to accept
         */
        if (application == null || application.getStudent() == null ||
            !this.getUserId().equalsIgnoreCase(application.getStudent().getUserId())) {
            System.out.println("You can only accept offers on your own applications.");
            return;
        }

        if (application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            System.out.println("You can only accept an application that is in SUCCESSFUL state.");
            return;
        }

        Internship internship = application.getInternship();
        if (internship == null) {
            System.out.println("Invalid internship.");
            return;
        }

        if (internship.getSlotsRemaining() <= 0) {
            System.out.println("No slots left to confirm. Please contact the company/CCS.");
            return;
        }

        // Confirm the placement
        application.setStatus(ApplicationStatus.CONFIRMED);
        Repository.updateApplication(application);

        internship.decrementSlot();
        if (internship.getSlotsRemaining() == 0) {
            internship.setStatus(Internship.InternshipStatus.FILLED);
        }
        Repository.updateInternship(internship);

        System.out.println("Offer accepted for " + internship.getTitle() + " @ " + internship.getCompanyName()
                + " (Remaining slots: " + internship.getSlotsRemaining() + ")");
    }

    public void withdrawApplication(InternshipApp application) {
        /**
         * Request withdrawal for an application. This flags the application
         * and persists the change via {@link SC2002.repository.Repository}.
         *
         * @param application the application to withdraw
         */
        if (application == null || application.getStudent() == null ||
            !this.getUserId().equalsIgnoreCase(application.getStudent().getUserId())) {
            System.out.println("You can only withdraw your own applications.");
            return;
        }

        ApplicationStatus status = application.getStatus();
        if (status == ApplicationStatus.UNSUCCESSFUL || status == ApplicationStatus.WITHDRAWN) {
            System.out.println("This application is already closed (" + status + ").");
            return;
        }

        application.setWithdrawalRequested(true);
        Repository.updateApplication(application);

        System.out.println("Withdrawal request submitted for AppID " + application.getId()
                + " (" + (application.getInternship() != null ? application.getInternship().getTitle() : "-") + ").");
    }

    public void addApplication(InternshipApp application) {
        /**
         * Add an application to the student's local list. Used when bootstrapping
         * or when applications are loaded from storage.
         *
         * @param application the application to add
         */
        if (application != null) applications.add(application);
    
    }
}
