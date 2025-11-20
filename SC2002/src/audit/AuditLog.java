package audit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of audit entries for the system.
 * <p>
 * This class serves as a centralized log, maintaining a chronological list of
 * {@link AuditEntry} objects. It provides functionality to record new events
 * with automatic timestamping and to display the entire log history.
 * </p>
 */
public class AuditLog {
    private final List<AuditEntry> entries = new ArrayList<>();

    /**
     * Records a new action in the audit log.
     * <p>
     * This method creates a new {@link AuditEntry} using the current system time
     * ({@link LocalDateTime#now()}) and adds it to the internal record list.
     * </p>
     *
     * @param action A {@code String} description of the action performed.
     * @param phase  A {@code String} description of the system phase or context.
     */
    public void record(String action, String phase){ entries.add(new AuditEntry(LocalDateTime.now(), action, phase)); }

    /**
     * Prints all recorded audit entries to the standard output.
     * <p>
     * This outputs a header followed by the string representation of every entry
     * currently stored in the log.
     * </p>
     */
    public void print(){
        System.out.println("\n=== Audit Log ===");
        for (var e : entries) System.out.println(e);
    }
}