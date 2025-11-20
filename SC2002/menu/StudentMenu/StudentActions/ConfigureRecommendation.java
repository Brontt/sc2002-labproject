package menu.StudentMenu.StudentActions;

import menu.MenuAction;
import user.Student;
import menu.StudentMenu.StudentMenuControl;
import filter.FilterManager;
import filter.FilterSettings;
import filter.RankingPreferences;
import filter.NonNegotiables;
import internship.Internship.InternshipLevel;
import util.ConsoleHelper;

import java.util.*;

/**
 * Action to configure recommendation ranking preferences for a student.
 *
 * This action exposes interactive prompts to set keywords, level preferences,
 * major/non-negotiable filters and ranking weights. The preferences are stored
 * in the student's {@link FilterManager} and affect how recommendations are
 * ranked by the {@code RecommendationService}.
 */
public class ConfigureRecommendation implements MenuAction {
    private final Student me;
    private final Scanner sc;
    private final StudentMenuControl control;

    /**
     * @param me the student configuring recommendations
     * @param sc the console scanner for interactive prompts
     * @param control the student menu control providing access to the filter manager
     */
    public ConfigureRecommendation(Student me, Scanner sc, StudentMenuControl control) {
        this.me = me;
        this.sc = sc;
        this.control = control;
    }

    /**
     * Entry point for the configure recommendation action.
     */
    @Override
    public void execute() {
        configureRecommendations();
    }

    /**
     * Interactive flow that updates ranking preferences and non-negotiables.
     */
    private void configureRecommendations() {
        ConsoleHelper.printSectionHeader("CONFIGURE RECOMMENDATION RANKING");

        FilterManager filterManager = control.getFilterManager();
        RankingPreferences rankingPreferences = filterManager.getRankingPreferences();
        NonNegotiables nonNegotiables = filterManager.getNonNegotiables();

        // Keywords for matching
        System.out.print("Keywords for ranking boost (title/company) [Enter to skip]: ");
        String keywords = sc.nextLine().trim();
        if (!keywords.isEmpty()) {
            rankingPreferences.setRankingKeyword(keywords);
        } else {
            rankingPreferences.setRankingKeyword(null);
        }

        // Level preferences for ranking
        Set<InternshipLevel> levels;
        if (me.getYear() >= 3) {
            System.out.println("\nSelect levels to consider for ranking:");
            levels = askLevelPreferences();
            rankingPreferences.setRankingLevelPreferences(levels);
        } else {
            levels = Set.of(InternshipLevel.BASIC);
            rankingPreferences.setRankingLevelPreferences(levels);
        }

        // Major preference
        boolean majorMatch = ConsoleHelper.askYesNo(sc, "\nPrioritize internships matching your major? (y/n): ");
        nonNegotiables.setMustMatchMajor(majorMatch);

        // Closing soon preference
        boolean closingSoon = ConsoleHelper.askYesNo(sc, "Prioritize applications closing soon? (y/n): ");
        nonNegotiables.setOnlyOpenNow(closingSoon);

        // Ranking weights
        System.out.println("\n--- Set Ranking Weights ---");
        System.out.println("Assign weight values (higher = more important)");
        System.out.println("Default weights: Major=30, ClosingSoon=30, LevelFit=20, Keywords=20");
        
        if (ConsoleHelper.askYesNo(sc, "\nCustomize weights? (y/n): ")) {
            rankingPreferences.setWMajor(askWeight("Major match"));
            rankingPreferences.setWClosingSoon(askWeight("Closing soon"));
            rankingPreferences.setWLevelFit(askWeight("Level fit"));
            rankingPreferences.setWTitleKeywords(askWeight("Title/keywords"));
        }

        System.out.println("\n✓ Recommendation ranking configured!");
        System.out.println("Keywords: " + (keywords.isEmpty() ? "(none)" : keywords));
        System.out.println("Levels: " + (levels.isEmpty() ? "(all)" : levels));
        System.out.println("Major match: " + (majorMatch ? "Yes" : "No"));
        System.out.println("Closing soon: " + (closingSoon ? "Yes" : "No"));
        System.out.println("Weights: Major=" + rankingPreferences.getWMajor() + 
                         ", ClosingSoon=" + rankingPreferences.getWClosingSoon() + 
                         ", LevelFit=" + rankingPreferences.getWLevelFit() + 
                         ", Keywords=" + rankingPreferences.getWTitleKeywords());
        System.out.println("\nReminder: Use option 5 to toggle recommendation mode ON/OFF");
        ConsoleHelper.pause(sc);
    }

    /**
     * Helper to select preferred internship levels for ranking.
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

    private int askWeight(String criterion) {
        return ConsoleHelper.readInt(sc, "Weight for " + criterion + " (0-100): ", 0, 100);
    }
}