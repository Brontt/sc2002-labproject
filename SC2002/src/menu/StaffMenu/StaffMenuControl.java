package menu.StaffMenu;

import user.CareerCentreStaff;
import menu.StaffMenu.StaffActions.*;
import menu.MenuAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Control class for the Career Centre Staff menu.
 *
 * Responsibilities:
 * - Register available staff actions and map them to menu keys.
 * - Delegate incoming menu selections to the corresponding {@link MenuAction}.
 *
 * This class is intentionally a thin router to keep UI code decoupled
 * from action implementations and to make testing easier.
 */
public class StaffMenuControl {
    private final CareerCentreStaff me;
    private final Map<String, MenuAction> actions = new HashMap<>();

    /**
     * Create a control instance for the specified staff member.
     * The constructor registers all staff-facing actions and binds them
     * to their numeric menu keys.
     *
     * @param me the current {@link CareerCentreStaff} using the menu
     * @param sc the shared {@link Scanner} used by actions requiring console input
     */
    public StaffMenuControl(CareerCentreStaff me, Scanner sc) {
        this.me = me;
        actions.put("1", new ApproveRepAction(sc));
        actions.put("2", new HandleWithdrawalsAction(sc));
        actions.put("3", new ManageInternshipsStaffAction(sc));
        actions.put("4", new BulkApproveAction(sc));
        actions.put("5", new GenerateReportsAction(sc));
    }

    /**
     * Handle a user-entered key by locating and executing the bound action.
     * If the key is unknown a friendly message is printed to the console.
     *
     * @param key the menu selection entered by the user
     */
    public void handle(String key) {
        MenuAction act = actions.get(key);
        if (act != null) act.execute();
        else System.out.println("Invalid option.");
    }
}