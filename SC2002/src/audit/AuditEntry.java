package audit;

import java.time.LocalDateTime;

/**
 * Represents a single immutable entry in the system's audit log.
 * <p>
 * Each entry captures a specific event containing the timestamp of occurrence,
 * the action performed, and the system phase (context) in which it happened.
 * </p>
 */
public class AuditEntry {
    private final LocalDateTime ts; private final String action; private final String phase;

    /**
     * Constructs a new {@code AuditEntry} with the specified details.
     *
     * @param ts     The {@link LocalDateTime} timestamp indicating when the event occurred.
     * @param action A {@code String} describing the action performed (e.g., "LOGIN", "SUBMIT_APP").
     * @param phase  A {@code String} indicating the system context or phase (e.g., "StudentMenu", "AdminOps").
     */
    public AuditEntry(LocalDateTime ts, String action, String phase){ this.ts=ts; this.action=action; this.phase=phase; }

    /**
     * Returns a string representation of this audit entry.
     * The format used is: {@code timestamp | action | phase}.
     *
     * @return A formatted string representing the audit log entry.
     */
    @Override public String toString(){ return ts + " | " + action + " | " + phase; }
}