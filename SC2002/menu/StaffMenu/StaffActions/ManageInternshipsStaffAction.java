package menu.StaffMenu.StaffActions;

import menu.MenuAction;
import repository.Repository;
import internship.Internship;
import util.TablePrinter;
import util.ConsoleHelper;

import java.util.List;
import java.util.Scanner;
/**
 * Staff action to show an overview of internships across the platform.
 *
 * Placeholder in the scaffold; replace with aggregated reporting logic
 * (counts per company, open positions, etc.) as needed.
 */
public class ManageInternshipsStaffAction implements MenuAction {
    private final Scanner sc;
    /**
     * @param sc scanner used for any interactive prompts
     */
    public ManageInternshipsStaffAction(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Execute the overview/reporting flow. Currently a stub.
     */
    @Override
    public void execute() {
        System.out.println("\n--- INTERNSHIP OVERVIEW (STAFF) ---");

        List<Internship> all = Repository.findAllInternships();
        if (all == null || all.isEmpty()) {
            System.out.println("No internships in the system.");
            return;
        }

        printInternshipsTable(all);

        System.out.print("\n(Press ENTER to return to menu) ");
        sc.nextLine();
    }

    private void printInternshipsTable(List<Internship> list) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(160)
                .addColumn("No.",     TablePrinter.Align.RIGHT,   2,  4)
                .addColumn("ID",      TablePrinter.Align.LEFT,    6, 10)
                .addColumn("Title",   TablePrinter.Align.LEFT,   10, 30)
                .addColumn("Company", TablePrinter.Align.LEFT,    8, 22)
                .addColumn("Level",   TablePrinter.Align.CENTER,  8, 12)
                .addColumn("Major",   TablePrinter.Align.CENTER,  5,  8)
                .addColumn("Open",    TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Close",   TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Slots",   TablePrinter.Align.RIGHT,   5,  6)
                .addColumn("Visible", TablePrinter.Align.CENTER,  7,  9)
                .addColumn("Status",  TablePrinter.Align.CENTER, 10, 12)
                .build();

        int idx = 1;
        for (Internship i : list) {
            tp.addRow(
                    idx++,
                    i.getId(),
                    ConsoleHelper.safe(i.getTitle()),
                    ConsoleHelper.safe(i.getCompanyName()),
                    i.getLevel(),
                    ConsoleHelper.safe(i.getPreferredMajor()),
                    i.getOpenDate(),
                    i.getCloseDate(),
                    i.getSlots(),
                    i.isVisible() ? "YES" : "NO",
                    i.getStatus()
            );
        }

        System.out.println(tp.render());
    }
}
