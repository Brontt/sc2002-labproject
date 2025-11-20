package user;

import filter.FilterSettings;
import util.PasswordService;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all user types.
 * Refactored to use PasswordService for password operations (SRP, DRY).
 */
/**
 * Abstract base class for all user types in the system.
 * <p>
 * Stores common user attributes such as id, name and password, plus
 * a saved filter settings object and an inbox for notifications.
 * Concrete subclasses include {@link Student}, {@link CompanyRep} and
 * {@link CareerCentreStaff}.
 */
public abstract class User {
    private String userId;
    private String name;
    private String password;

    protected FilterSettings savedFilters = new FilterSettings();
    protected final List<String> inbox = new ArrayList<>();

    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public FilterSettings getSavedFilters() { return savedFilters; }
    public void setSavedFilters(FilterSettings f) { if (f != null) this.savedFilters = f; }

    public List<String> getInbox() { return inbox; }
    public void pushNotice(String msg) { if (msg != null && !msg.isBlank()) inbox.add(msg); }
    public void clearInbox() { inbox.clear(); }

    /**
     * Validates user login credentials.
     * @param inputId User ID entered
     * @param inputPassword Password entered
     * @return true if credentials are valid
     */
    /**
     * Validate login credentials against this user's stored id and password.
     * The password comparison uses {@link SC2002.util.PasswordService#hashPassword(String)}.
     *
     * @param inputId the id entered by the user
     * @param inputPassword the password entered by the user (plain text)
     * @return true if credentials match, false otherwise
     */
    public boolean login(String inputId, String inputPassword) {
        return userId.equalsIgnoreCase(inputId)
            && password.equals(PasswordService.hashPassword(inputPassword));
    }

    /**
     * Change the user's password after validating the new password via
     * {@link SC2002.util.PasswordService#validatePasswordChange(String, String)} and store
     * the hashed form.
     *
     * @param newPassword the new plain-text password
     * @throws IllegalArgumentException if the new password does not pass validation
     */
    public void changePassword(String newPassword) {
        PasswordService.validatePasswordChange(userId, newPassword);
        this.password = PasswordService.hashPassword(newPassword);
    }

    /**
     * Static method for hashing passwords.
     * Delegates to PasswordService.
     * @param plain Plain text password
     * @return Hashed password
     * @deprecated Use PasswordService.hashPassword() directly
     */
    @Deprecated
    public static String hashPassword(String plain) {
        return PasswordService.hashPassword(plain);
    }
}