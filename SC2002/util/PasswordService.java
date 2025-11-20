package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class for password-related operations.
 * Centralizes password hashing and validation logic (SRP).
 */
public class PasswordService {
    
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    /**
     * Hashes a plain text password using SHA-256.
     * @param plainPassword The plain text password to hash
     * @return Hexadecimal string representation of the hash
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
    
    /**
     * Validates password strength.
     * Password must:
     * - Be at least 6 characters long
     * - Contain at least one uppercase letter
     * - Contain at least one lowercase letter
     * - Contain at least one digit
     * 
     * @param password The password to validate
     * @return true if password meets strength requirements, false otherwise
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Checks if a string appears to be a hashed password (SHA-256 hex format).
     * @param password The string to check
     * @return true if string matches SHA-256 hex pattern (64 hex characters)
     */
    public static boolean isHashed(String password) {
        return password != null && password.matches("^[0-9a-fA-F]{64}$");
    }
    
    /**
     * Validates password change request with business rules.
     * @param userId The user's ID
     * @param newPassword The new password to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validatePasswordChange(String userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
        if (newPassword.equalsIgnoreCase(userId)) {
            throw new IllegalArgumentException("Password cannot be the same as your user ID.");
        }
        if (!isStrongPassword(newPassword)) {
            throw new IllegalArgumentException(
                "Password must contain at least " + MIN_PASSWORD_LENGTH + 
                " characters, one uppercase letter, one lowercase letter, and one number."
            );
        }
    }
}