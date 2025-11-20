package notification;

import user.User;

/**
 * Simple notification service that delivers textual messages to user inboxes.
 */
public class NotificationService {
    /**
     * Push a notification message to a user's inbox (if user not null).
     *
     * @param u user to notify
     * @param message message content
     */
    public void notify(User u, String message) {
        if (u != null) u.pushNotice(message);
    }
}
