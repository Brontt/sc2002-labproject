package internship;

import user.Student;

/**
 * Represents a student's application to an internship.
 */
public class InternshipApp {
    private final String id;
    private final Student student;
    private final Internship internship;
    private ApplicationStatus status = ApplicationStatus.PENDING;
    private boolean withdrawalRequested = false;

    public InternshipApp(String id, Student s, Internship i) { 
        this.id=id; this.student=s; this.internship=i; }

    public String getId(){ return id; }
    public Student getStudent(){ return student; }
    public Internship getInternship(){ return internship; }
    public ApplicationStatus getStatus(){ return status; }
    public boolean isWithdrawalRequested(){ return withdrawalRequested; }
    public void setWithdrawalRequested(boolean r){ this.withdrawalRequested=r; }

    /**
     * Mark the application as confirmed (student accepted placement).
     */
    public void confirm(){ status = ApplicationStatus.CONFIRMED; }

    /**
     * Withdraw the application.
     * If the application was confirmed, this frees up a slot in the internship.
     */
    public void withdraw(){ 
        // If this was a confirmed placement, increment the slot count to free it up
        if (status == ApplicationStatus.CONFIRMED && internship != null) {
            internship.incrementSlot();
        }
        status = ApplicationStatus.WITHDRAWN; 
        withdrawalRequested=false; 
    }

    /**
     * Update application status to the provided value if non-null.
     *
     * @param newStatus new application status
     */
    public void setStatus(ApplicationStatus newStatus) {
        if (newStatus == null) return; 
        this.status = newStatus;
    }
}
