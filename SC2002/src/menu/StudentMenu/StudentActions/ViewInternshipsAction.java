package menu.StudentMenu.StudentActions;

import filter.FilterManager;
import internship.Internship;
import internship.InternshipScore;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import menu.MenuAction;
import menu.StudentMenu.StudentMenuControl;
import repository.Repository;
import user.Student;
import util.ConsoleHelper;
import util.TablePrinter;

/**
 * A menu action for students to view a list of available internships.
 * <p>
 * This class implements the core functionality of displaying internship opportunities.
 * It handles the following logic:
 * <ul>
 * <li>Retrieving all internships from the repository.</li>
 * <li>Filtering out closed internships or those the student is ineligible for (e.g., based on academic level).</li>
 * <li>Applying dynamic filters configured by the student via the {@link FilterManager}.</li>
 * <li>Calculating relevance scores for each internship and sorting the list accordingly.</li>
 * <li>Displaying the final list in a formatted table.</li>
 * </ul>
 * </p>
 */
public class ViewInternshipsAction implements MenuAction {
    private final Student me;
    private final FilterManager filterManager;
    private final Scanner sc;
    private final StudentMenuControl control;

    /**
     * Constructs a new {@code ViewInternshipsAction}.
     *
     * @param me            The {@link Student} viewing the internships.
     * @param filterManager The {@link FilterManager} responsible for filtering and scoring logic.
     * @param sc            The {@link Scanner} for reading user input (e.g., for prompts).
     * @param control       The {@link StudentMenuControl} managing the student's menu flow.
     */
    public ViewInternshipsAction(Student me, FilterManager filterManager, Scanner sc, StudentMenuControl control) {
        this.me = me;
        this.filterManager = filterManager;
        this.sc = sc;
        this.control = control;
    }

    /**
     * Executes the logic to filter, rank, and display internships.
     * <p>
     * This method performs a pipeline of operations:
     * <ol>
     * <li>Fetches all internships.</li>
     * <li>Filters for eligibility (open date and student level).</li>
     * <li>Applies user-defined filters. If the result is empty, it offers to show all eligible internships.</li>
     * <li>Applies ranking/scoring based on student preferences.</li>
     * <li>Prints the results.</li>
     * </ol>
     * </p>
     */
    @Override
    public void execute() {
        List<Internship> all = Repository.findAllInternships();
        LocalDate today = LocalDate.now();
        List<Internship> eligible = all.stream()
                .filter(i -> i.isOpenFor(today))
                .filter(i -> FilterManager.isLevelEligibleForStudent(me, i))
                .collect(Collectors.toList());

        List<Internship> filtered = eligible;
        if (filterManager.isFilterEnabled()) {
            filtered = filterManager.applyFilters(eligible);
            if (filtered.isEmpty()) {
                System.out.println("\n(No internships match your filter criteria.)");
                boolean showAll = ConsoleHelper.askYesNo(sc, "Show all eligible internships instead? (y/n): ");
                if (!showAll) {
                    ConsoleHelper.pause(sc);
                    return;
                }
                filtered = eligible;
            }
        }

        // Delegate ranking to FilterManager
        List<InternshipScore> scoredList = filterManager.applyRanking(filtered, me);

        ConsoleHelper.printSectionHeader("AVAILABLE INTERNSHIPS");
        printInternships(scoredList);
        ConsoleHelper.pause(sc);
    }

    /**
     * Helper method to format and print the list of scored internships.
     * <p>
     * Uses {@link TablePrinter} to create a readable table with columns for ID, Title, Company,
     * Level, Major, Closing Date, Slots, and Relevance Score.
     * </p>
     *
     * @param list The list of {@link InternshipScore} objects containing the internship and its score.
     */
    private void printInternships(List<InternshipScore> list) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(140)
                .addColumn("No.", TablePrinter.Align.RIGHT, 2, 4)
                .addColumn("ID", TablePrinter.Align.LEFT, 6, 10)
                .addColumn("Title", TablePrinter.Align.LEFT, 10, 28)
                .addColumn("Company", TablePrinter.Align.LEFT, 8, 22)
                .addColumn("Level", TablePrinter.Align.CENTER, 8, 12)
                .addColumn("Major", TablePrinter.Align.CENTER, 5, 8)
                .addColumn("Close Date", TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Slots", TablePrinter.Align.RIGHT, 5, 6)
                .addColumn("Score", TablePrinter.Align.RIGHT, 4, 6)
                .build();

        int idx = 1;
        for (InternshipScore is : list) {
            Internship i = is.internship;
            tp.addRow(
                    idx++,
                    i.getId(),
                    ConsoleHelper.safe(i.getTitle()),
                    ConsoleHelper.safe(i.getCompanyName()),
                    i.getLevel(),
                    ConsoleHelper.safe(i.getPreferredMajor()),
                    i.getCloseDate(),
                    i.getSlotsRemaining(),
                    is.score
            );
        }
        System.out.println(tp.render());
    }
}