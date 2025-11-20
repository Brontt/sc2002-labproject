package user;

/**
 * Represents a company representative who can post internships on behalf
 * of their company. New representatives require approval by Career Centre
 * Staff before they may access the system.
 */
public class CompanyRep extends User {
    private String companyName;
    private String department;
    private String position;
    private boolean approved;
    private String externalId;

    public String getCompanyName() { return companyName; }
    public String getDepartment()  { return department; }
    public String getPosition()    { return position; }
    public boolean isApproved()    { return approved; }

    /**
     * Mark this representative as approved or not. Approval is typically set
     * by Career Centre Staff.
     *
     * @param approved true to approve, false to revoke approval
     */
    public void setApproved(boolean approved) { this.approved = approved; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    /**
     * Construct a CompanyRep instance.
     *
     * @param userId login id (email) used as identifier
     * @param name the representative's name
     * @param password hashed or plain depending on caller expectations
     * @param companyName company this representative belongs to
     * @param department dept string
     * @param position role/position title
     * @param approved initial approval state
     */
    public CompanyRep(String userId, String name, String password,
                      String companyName, String department,
                      String position, boolean approved) {
        super(userId, name, password);

        this.companyName = companyName;
        this.department  = department;
        this.position    = position;
        this.approved    = approved;
    }

}
