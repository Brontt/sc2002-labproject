package notification;

import repository.Repository;
import internship.Internship;
import user.User;

import java.util.Set;

/**
 * NotificationCenter contains static helpers for broadcasting system-wide
 * notifications (for example when slots free up on an internship).
 */
public class NotificationCenter {

    /**
     * Called by staff flow when a withdrawal frees a slot. Notifies all
     * waitlisted users that a slot has opened for the given internship.
     *
     * @param i internship that freed a slot
     */
    public static void slotFreed(Internship i) {
        if (i == null) return;
        Set<String> waitlisted = WaitlistService.getWaitlisted(i.getId()); // or popAll(i.getId()) if one-time
        if (waitlisted.isEmpty()) return;
        for (String sid : waitlisted) {
            Object u = Repository.findUserById(sid);
            if (u instanceof User user) {
                user.pushNotice("A slot just opened for " + i.getTitle() + " @ " + i.getCompanyName());
            }
        }
    }
}
