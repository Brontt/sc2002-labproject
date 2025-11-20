package commands;

import audit.AuditLog;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages the execution and lifecycle of commands within the system.
 * <p>
 * This class acts as the <b>Invoker</b> in the Command Design Pattern. It is responsible for:
 * <ul>
 * <li>Executing commands.</li>
 * <li>Maintaining a history stack to enable functionality (Undo).</li>
 * <li>Integrating with the {@link AuditLog} to record all executed and undone actions.</li>
 * </ul>
 * </p>
 */
public class CommandManager {
    private final Deque<Command> history = new ArrayDeque<>();
    private final AuditLog audit = new AuditLog();

    /**
     * Executes a command and registers it in the history and audit log.
     * <p>
     * This method calls the {@link Command#execute()} method, pushes the command onto the
     * history stack for potential future reversal, and records an "EXECUTE" entry in the audit log.
     * </p>
     *
     * @param c The {@link Command} object to be executed.
     */
    public void run(Command c){ c.execute(); history.push(c); audit.record(c.name(), "EXECUTE"); }

    /**
     * Undoes the most recently executed command.
     * <p>
     * This method retrieves the last command from the history stack, calls its {@link Command#undo()}
     * method, and records an "UNDO" entry in the audit log.
     * </p>
     *
     * @return {@code true} if a command was successfully undone; {@code false} if the history stack was empty.
     */
    public boolean undo(){
        if (history.isEmpty()) return false;
        var c = history.pop(); c.undo(); audit.record(c.name(), "UNDO"); return true;
    }

    /**
     * Delegates to the {@link AuditLog} to print the entire history of recorded events.
     */
    public void printAudit(){ audit.print(); }
}