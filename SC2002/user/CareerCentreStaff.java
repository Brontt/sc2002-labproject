package user;

/**
 * Represents a career centre staff user who can perform administrative tasks
 * such as approving company representatives and managing internships.
 */
public class CareerCentreStaff extends User {
    private final String department;
    public CareerCentreStaff(String userId, String name, String password, String department) {
        super(userId, name, password); this.department = department;
    }
    public String getDepartment() { return department; }
}
