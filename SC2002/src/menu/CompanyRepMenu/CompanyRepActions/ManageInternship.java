package menu.CompanyRepMenu.CompanyRepActions;

import user.CompanyRep;
import menu.MenuAction;
import repository.Repository;
import internship.Internship;
import util.TablePrinter;
import util.ConsoleHelper;

import java.util.List;
import java.util.Scanner;

/**
 * Action to manage internships created by a company representative.
 *
 * In a full implementation this would present a list of the rep's postings
 * and allow editing or closing them. The current scaffold contains a stub.
 */
public class ManageInternship implements MenuAction {
    private final CompanyRep me;
    private final Scanner sc;

    /**
     * @param me the company rep who will manage their internships
     * @param sc the {@link Scanner} used for console interaction
     */
    public ManageInternship(CompanyRep me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    /**
     * Execute the manage internship flow. Currently a placeholder message.
     */
    @Override
    public void execute() {
        System.out.println("\n--- MANAGE MY INTERNSHIPS ---");

        // 1) Get internships posted by this rep
        List<Internship> mine = Repository.findInternshipsByRepUserId(me.getExternalId());
        // If you don't have this method yet, see note below.

        if (mine == null || mine.isEmpty()) {
            System.out.println("You have not created any internships yet.");
            return;
        }

        // 2) Show them in a table
        printInternshipsTable(mine);

        // 3) Simple manage action: toggle visibility
        System.out.print("\nEnter internship No. to toggle visibility (0 to return): ");
        String input = sc.nextLine().trim();
        if ("0".equals(input)) return;

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > mine.size()) {
                System.out.println("Invalid choice. Returning to menu.");
                return;
            }

            Internship selected = mine.get(choice - 1);
            boolean newVisible = !selected.isVisible();
            selected.setVisible(newVisible);

            System.out.println("Updated visibility of [" + selected.getId() + "] "
                    + ConsoleHelper.safe(selected.getTitle())
                    + " to: " + (newVisible ? "VISIBLE" : "HIDDEN"));

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }

    private void printInternshipsTable(List<Internship> list) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(140)
                .addColumn("No.",     TablePrinter.Align.RIGHT,   2,  4)
                .addColumn("ID",      TablePrinter.Align.LEFT,    6, 10)
                .addColumn("Title",   TablePrinter.Align.LEFT,   10, 28)
                .addColumn("Level",   TablePrinter.Align.CENTER,  8, 12)
                .addColumn("Major",   TablePrinter.Align.CENTER,  5,  8)
                .addColumn("Open",    TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Close",   TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Slots",   TablePrinter.Align.RIGHT,   5,  6)
                .addColumn("Visible", TablePrinter.Align.CENTER,  7, 10)
                .addColumn("Status",  TablePrinter.Align.CENTER, 10, 12)
                .build();

        int idx = 1;
        for (Internship i : list) {
            tp.addRow(
                    idx++,
                    i.getId(),
                    ConsoleHelper.safe(i.getTitle()),
                    i.getLevel(),
                    ConsoleHelper.safe(i.getPreferredMajor()),
                    i.getOpenDate(),
                    i.getCloseDate(),
                    i.getSlotsRemaining(),
                    i.isVisible() ? "YES" : "NO",
                    i.getStatus()
            );
        }

        System.out.println(tp.render());
    }
}
