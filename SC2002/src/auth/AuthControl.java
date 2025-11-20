package auth;

import java.util.List;
import user.CompanyRep;
import user.User;

/**
 * Control class responsible for authentication logic.
 *
 * The class holds an in-memory list of users and provides helper methods
 * to find a user by id and to verify credentials.
 */
public class AuthControl {
    private final List<User> users;

    public AuthControl(List<User> users) {
        this.users = users;
    }

    /**
     * Find a user by id (case-insensitive). Returns null when no match found.
     *
     * @param id user id or email to look up
     * @return matching user or null
     */
    public User findById(String id) {
        if (id == null) return null;
        String t = id.trim();
        return users.stream()
                .filter(u -> u.getUserId() != null && u.getUserId().trim().equalsIgnoreCase(t))
                .findFirst()
                .orElse(null);
    }

    /**
     * Authenticate a user using the provided plain-text password. The method
     * delegates to {@link SC2002.user.User#login(String, String)}.
     *
     * @param user the user object to authenticate
     * @param password the plain-text password
     * @return AuthResult describing success/failure and optional message
     */
    public AuthResult authenticate(User user, String password) {
        if (user == null) return AuthResult.fail("No such user ID/email.");
        // user.login expects id and password in current codebase; pass stored id
        if (!user.login(user.getUserId(), password)) {
            System.out.println("[DEBUG] Password verification failed for " + user.getUserId());
            return AuthResult.fail("Wrong password for user '" + user.getUserId() + "'.");
        }
        if (user instanceof CompanyRep rep && !rep.isApproved()) {
            return AuthResult.fail("Rep account pending approval by Career Centre Staff.");
        }
        return AuthResult.success(user);
    }

    /**
     * Result holder used by authenticate() to report success or failure.
     */
    public static class AuthResult {
        private final boolean success;
        private final User user;
        private final String message;

        private AuthResult(boolean success, User user, String message) {
            this.success = success;
            this.user = user;
            this.message = message;
        }

        public static AuthResult success(User u) { return new AuthResult(true, u, null); }
        public static AuthResult fail(String msg) { return new AuthResult(false, null, msg); }

        public boolean isSuccess() { return success; }
        public User getUser() { return user; }
        public String getMessage() { return message; }
    }
}