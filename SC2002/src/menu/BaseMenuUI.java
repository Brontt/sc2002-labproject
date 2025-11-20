package menu;

import java.util.Scanner;

/**
 * Abstract base class for menu UI classes.
 * Implements Template Method pattern to reduce duplication in menu handling.
 * Follows Open/Closed Principle - subclasses extend without modifying base behavior.
 */
public abstract class BaseMenuUI {
    protected final Scanner sc = new Scanner(System.in);
    
    /**
     * Runs the menu loop. Template method that defines the menu flow.
     */
    public void run() {
        while (true) {
            printMenu();
            String cmd = sc.nextLine().trim();
            if ("0".equals(cmd)) {
                return;
            }
            handleCommand(cmd);
        }
    }
    
    /**
     * Prints the menu options.
     * Subclasses must implement this to show their specific menu.
     */
    protected abstract void printMenu();
    
    /**
     * Handles the command entered by the user.
     * Subclasses must implement this to process their specific commands.
     * @param command The command string entered by the user
     */
    protected abstract void handleCommand(String command);
}