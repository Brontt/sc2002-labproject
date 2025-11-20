package menu.CompanyRepMenu;

import menu.CompanyRepMenu.CompanyRepActions.*;
import menu.MenuAction;
import user.CompanyRep;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control class for the Company Representative menu.
 *
 * Responsibilities:
 * - Wire menu action keys to concrete {@link MenuAction} implementations.
 * - Delegate command handling from the UI to the appropriate action.
 *
 * This class contains only simple routing logic and keeps the UI layer
 * free of action construction details.
 */
public class CompanyRepMenuControl {
    private final CompanyRep me;
    private final Map<String, MenuAction> actions = new HashMap<>();

    /**
     * Create a new control for the given company representative.
     * The constructor registers the available menu actions and binds them
     * to their command keys.
     *
     * @param me the currently logged-in {@link CompanyRep}
     * @param sc the shared {@link Scanner} used by actions that need console input
     */
    public CompanyRepMenuControl(CompanyRep me, Scanner sc) {
        this.me = me;
        actions.put("1", new CreateInternshipAction(me, sc));
        actions.put("2", new ManageInternship(me, sc));
        actions.put("3", new ViewApplicationsAction(me, sc));
        actions.put("4", new ApproveApplicationAction(me, sc));
    }

    /**
     * Handle a menu key selection by looking up the registered action
     * and executing it. If the key is not registered the method prints a
     * friendly error to the console.
     *
     * @param key the menu selection key entered by the user
     */
    public void handle(String key) {
        MenuAction a = actions.get(key);
        if (a != null) a.execute();
        else System.out.println("Invalid option.");
    }
}