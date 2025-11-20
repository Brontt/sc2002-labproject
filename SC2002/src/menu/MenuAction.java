package menu;

/**
 * Represents a single actionable command in a menu UI.
 * Implementations encapsulate the behavior executed when a menu option
 * is selected. This simple Command-like interface keeps UI and action
 * implementations decoupled and testable.
 */
public interface MenuAction {
    /**
     * Execute the action represented by this menu entry.
     * Implementations should perform their work and handle any user
     * feedback (printing to console or updating state) as appropriate.
     */
    void execute();
}