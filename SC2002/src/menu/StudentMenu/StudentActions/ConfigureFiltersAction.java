package menu.StudentMenu.StudentActions;

import menu.MenuAction;
import user.Student;
import filter.FilterManager;
import filter.FilterSettings;
import internship.Internship.InternshipLevel;
import util.ConsoleHelper;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Action that allows a student to configure their filtering preferences.
 *
 * This action updates the {@link FilterSettings} held by the provided
 * {@link FilterManager}. It supports keyword search, level preferences
 * (respecting year-based restrictions), major filtering and company filtering.
 */
public class ConfigureFiltersAction implements MenuAction {
    private final Student me;
    private final FilterManager filterManager;
    private final Scanner sc;

    /**
     * @param me the student configuring filters
     * @param filterManager the manager holding the student's filter settings
     * @param sc console scanner for interactive prompts
     */
    public ConfigureFiltersAction(Student me, FilterManager filterManager, Scanner sc) {
        this.me = me;
        this.filterManager = filterManager;
        this.sc = sc;
    }

    /**
     * Run the interactive configuration flow and persist the choices
     * into the {@link FilterSettings} object.
     */
    @Override
    public void execute() {
        ConsoleHelper.printSectionHeader("CONFIGURE FILTERS");

        FilterSettings settings = filterManager.getFilterSettings();

        // Keywords
        System.out.print("Enter keywords to search (title/company/description) [Enter to skip]: ");
        String keyword = sc.nextLine().trim();
        settings.setKeyword(keyword.isEmpty() ? null : keyword);

        // Internship Level (Year-based restriction)
        if (me.getYear() <= 2) {
            System.out.println("\nAs a Year " + me.getYear() + " student, you can only apply to BASIC level internships.");
            settings.setLevelPreferences(Set.of(InternshipLevel.BASIC));
        } else {
            System.out.println("\nAs a Year " + me.getYear() + " student, you can choose preferred internship levels:");
            Set<InternshipLevel> levels = askLevelPreferences();
            if (!levels.isEmpty()) {
                settings.setLevelPreferences(levels);
            } else {
                settings.setLevelPreferences(null); // all levels
            }
        }

        // Major filter
        boolean matchMajor = ConsoleHelper.askYesNo(sc, "\nOnly show internships matching your major (" + me.getMajor() + ")? (y/n): ");
        if (matchMajor) {
            settings.setPreferredMajor(me.getMajor());
        } else {
            settings.setPreferredMajor(null);
        }

        // Company filter
        System.out.print("\nFilter by specific company [Enter to skip]: ");
        String company = sc.nextLine().trim();
        settings.setCompany(company.isEmpty() ? null : company);

        System.out.println("\n✓ Filter configuration saved!");
        System.out.println("Current filters: " + settings);
        System.out.println("\nReminder: Use option 4 to toggle filter mode ON/OFF");
        ConsoleHelper.pause(sc);
    }

    /**
     * Interactive helper to ask the user for preferred internship levels.
     *
     * @return the set of selected levels (empty = all levels)
     */
    private Set<InternshipLevel> askLevelPreferences() {
        System.out.println("Select preferred internship levels (comma-separated, e.g., 1,2,3):");
        System.out.println("  1) BASIC");
        System.out.println("  2) INTERMEDIATE");
        System.out.println("  3) ADVANCED");
        System.out.println("  [Enter for all levels]");
        System.out.print("Your choice: ");
        String line = sc.nextLine().trim();
        Set<InternshipLevel> levels = new LinkedHashSet<>();
        if (line.isBlank()) return levels;
        String[] parts = line.split("\\s*,\\s*");
        for (String p : parts) {
            switch (p) {
                case "1" -> levels.add(InternshipLevel.BASIC);
                case "2" -> levels.add(InternshipLevel.INTERMEDIATE);
                case "3" -> levels.add(InternshipLevel.ADVANCED);
            }
        }
        return levels;
    }


}