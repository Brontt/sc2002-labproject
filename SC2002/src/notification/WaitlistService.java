package notification;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import internship.Internship;
import user.Student;

/**
 * Lightweight waitlist service used to manage student waitlists per internship.
 *
 * Stores student IDs per internship id and provides methods to join and to
 * obtain the current waitlist (either read-only via {@link #getWaitlisted}
 * or destructive via {@link #popAll}).
 */
public class WaitlistService {
    // Use the interface type Set<String> for values to avoid concrete-type mismatches.
    private static final Map<String, Set<String>> wait = new HashMap<>();

    /**
     * Add a student to the waitlist for the given internship and notify them.
     *
     * @param s student joining the waitlist
     * @param i internship to join
     */
    public void joinWaitlist(Student s, Internship i) {
        // Keep insertion order with LinkedHashSet, but store as Set<String>
        Set<String> list = wait.computeIfAbsent(i.getId(), k -> new LinkedHashSet<>());
        list.add(s.getUserId());
        s.pushNotice("You joined the waitlist for " + i.getTitle());
    }

    /** View current waitlisted student IDs for an internship (read-only view). */
    public static Set<String> getWaitlisted(String internshipId) {
        Set<String> set = wait.get(internshipId);
        return (set == null) ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    /** Remove and return current waitlist (if you prefer one-time notifications). */
    public static Set<String> popAll(String internshipId) {
        Set<String> set = wait.remove(internshipId);
        return (set == null) ? Collections.emptySet() : set;
    }
}
