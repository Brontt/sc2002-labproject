package commands;

/**
 * Defines the contract for all command objects in the system.
 * <p>
 * This interface follows the Command Design Pattern, encapsulating a request as an object.
 * It provides standard methods for executing logic, reversing that logic (undo),
 * and identifying the command for logging or auditing purposes.
 * </p>
 */
public interface Command {

    /**
     * Executes the logic associated with this command.
     * <p>
     * This method contains the specific business logic to be performed when the
     * command is invoked.
     * </p>
     */
    void execute();

    /**
     * Reverses the effects of the {@link #execute()} method.
     * <p>
     * This method restores the state of the system to what it was before the
     * command was executed. It relies on state saved during the execution phase.
     * </p>
     */
    void undo();

    /**
     * Returns the unique identifier or name of this command.
     * <p>
     * This string is typically used for writing to the audit log to track
     * which operations have been performed.
     * </p>
     *
     * @return A {@code String} representing the name of the command.
     */
    String name();
}