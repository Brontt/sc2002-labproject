package menu.StudentMenu;

import menu.MenuAction;
import menu.StudentMenu.StudentActions.*;
import user.Student;
import filter.FilterManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control class for the Student menu.
 *
 * Responsibilities:
 * - Register and route menu keys to {@link MenuAction} implementations.
 * - Provide small helper operations that actions may call back into (e.g.,
 *   toggling filters and recommendations).
 *
 * The class holds a {@link FilterManager} instance to manage the student's
 * filtering and recommendation preferences.
 */
public class StudentMenuControl {
    private final Student me;
    private final FilterManager filterManager;
    private final Map<String, MenuAction> actions = new HashMap<>();

    /**
     * Construct a student menu control and register available actions.
     *
     * @param me the currently logged-in {@link Student}
     * @param sc the {@link Scanner} used by actions requiring console input
     */
    public StudentMenuControl(Student me, Scanner sc) {
        this.me = me;
        this.filterManager = new FilterManager();
        // register actions
        actions.put("1", new ViewInternshipsAction(me, filterManager, sc, this));
        actions.put("2", new ConfigureFiltersAction(me, filterManager, sc));
        actions.put("3", new ConfigureRecommendation(me, sc, this));
        actions.put("4", () -> toggleFilter());
        actions.put("5", () -> toggleRecommendation());
        actions.put("6", () -> clearAllSettings(sc));
        actions.put("7", new ApplyInternshipAction(me, sc));
        actions.put("8", new StudentStatusAction(me));
        actions.put("9", new ChangePasswordAction(me, sc));
        // Add other actions as needed
    }

    /**
     * Route the given key to the registered action and execute it.
     * Unknown keys produce a user-friendly message.
     *
     * @param key the menu key entered by the user
     */
    public void handle(String key) {
        MenuAction action = actions.get(key);
        if (action != null) action.execute();
        else System.out.println("Invalid option.");
    }

    /** @return whether the filtering mode is currently enabled */
    public boolean isFilterEnabled() { return filterManager.isFilterEnabled(); }
    /** @return whether recommendation mode is currently enabled */
    public boolean isRecommendationEnabled() { return filterManager.isRecommendationEnabled(); }
    /** @return the {@link FilterManager} managing this student's settings */
    public FilterManager getFilterManager() { return filterManager; }

    /**
     * Toggle the filter mode and inform the user of the new state.
     */
    public void toggleFilter() {
        filterManager.toggleFilter();
        System.out.println("Filter mode is now " + (filterManager.isFilterEnabled() ? "ON" : "OFF"));
    }

    /**
     * Toggle recommendation mode and inform the user of the new state.
     */
    public void toggleRecommendation() {
        filterManager.toggleRecommendation();
        System.out.println("Recommendation mode is now " + (filterManager.isRecommendationEnabled() ? "ON" : "OFF"));
    }

    /**
     * Clear all filter and recommendation settings after a confirmation prompt.
     *
     * @param sc the {@link Scanner} to read confirmation input from
     */
    public void clearAllSettings(Scanner sc) {
        System.out.print("Clear all settings? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (ans.equals("y") || ans.equals("yes")) {
            filterManager.clearAll();
            System.out.println("All settings cleared.");
        } else {
            System.out.println("Cancelled.");
        }
    }
}