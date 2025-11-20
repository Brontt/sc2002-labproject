package util;

public class ValidationExceptions {
    public static class DuplicateApplicationException extends RuntimeException {
        public DuplicateApplicationException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedActionException extends RuntimeException {
        public UnauthorizedActionException(String message) {
            super(message);
        }
    }

    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}