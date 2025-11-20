package menu.CompanyRepMenu.CompanyRepActions;

import internship.Internship;
import internship.InternshipIds;
import java.time.LocalDate;
import java.util.Scanner;
import menu.MenuAction;
import repository.Repository;
import user.CompanyRep;
import util.ConsoleHelper;
import util.TablePrinter;

/**
 * Action to create a new {@link SC2002.internship.Internship} posting.
 *
 * Current scaffold prints a placeholder message. In a full implementation
 * this action would collect internship details from the console and persist
 * the new internship via the repository.
 */
public class CreateInternshipAction implements MenuAction {
    private final CompanyRep me;
    private final Scanner sc;

    /**
     * @param me the company representative creating the internship
     * @param sc the console {@link Scanner} used to read input
     */
    public CreateInternshipAction(CompanyRep me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    /**
     * Execute the create-internship flow. Currently a stub that instructs
     * future implementers where to hook persistence logic.
     */
    @Override
    public void execute() {
        System.out.println("\n--- CREATE INTERNSHIP ---");

        try {
            System.out.print("Title: ");
            String title = sc.nextLine().trim();

            System.out.print("Description: ");
            String desc = sc.nextLine().trim();

            System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED): ");
            String level = sc.nextLine().trim().toUpperCase();

            System.out.print("Preferred Major (optional): ");
            String major = sc.nextLine().trim();

            System.out.print("Application Open Date (YYYY-MM-DD): ");
            String openInput = sc.nextLine().trim();
            LocalDate openDate = LocalDate.parse(openInput);

            System.out.print("Application Close Date (YYYY-MM-DD): ");
            String closeInput = sc.nextLine().trim();
            LocalDate closeDate = LocalDate.parse(closeInput);

            System.out.print("Number of Slots: ");
            int slots = Integer.parseInt(sc.nextLine().trim());

            // Validate
            if (title.isEmpty() || desc.isEmpty() || level.isEmpty() || slots <= 0) {
                System.out.println("Invalid input. Internship not created.");
                return;
            }

            // Check if slots exceed maximum of 10
            if (slots > 10) {
                System.out.println("Error: Maximum number of slots allowed is 10. You entered " + slots + ".");
                return;
            }

            // Check if rep has already created 5 internships (limit)
            var myInternships = Repository.findInternshipsByRepUserId(me.getUserId());
            if (myInternships.size() >= 5) {
                System.out.println("You have reached the maximum limit of 5 internships. Cannot create more.");
                return;
            }

            // Build Internship entity
            Internship internship = new Internship(
                    InternshipIds.next(),
                    title,
                    desc,
                    level,
                    major.isEmpty() ? null : major,
                    me.getCompanyName(),
                    me,
                    slots,
                    true,
                    openDate.toString(),
                    closeDate.toString(),
                    "PENDING"
            );

            // Save into repository
            Repository.saveInternship(internship, me.getExternalId());

            System.out.println("\nInternship created successfully!");
            System.out.println("Generated Internship ID: " + internship.getId());
            // 👉 show as table
            printCreatedInternship(internship);

        } catch (Exception e) {
            System.out.println("Error creating internship: " + e.getMessage());
        }
    }

    private void printCreatedInternship(Internship i) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(140)
                .addColumn("ID",      TablePrinter.Align.LEFT,   6, 10)
                .addColumn("Title",   TablePrinter.Align.LEFT,  10, 28)
                .addColumn("Company", TablePrinter.Align.LEFT,   8, 22)
                .addColumn("Level",   TablePrinter.Align.CENTER, 8, 12)
                .addColumn("Major",   TablePrinter.Align.CENTER, 5,  8)
                .addColumn("Open",    TablePrinter.Align.CENTER,10, 12)
                .addColumn("Close",   TablePrinter.Align.CENTER,10, 12)
                .addColumn("Slots",   TablePrinter.Align.RIGHT,  5,  6)
                .build();

        tp.addRow(
                i.getId(),
                ConsoleHelper.safe(i.getTitle()),
                ConsoleHelper.safe(i.getCompanyName()),
                i.getLevel(),
                ConsoleHelper.safe(i.getPreferredMajor()),
                i.getOpenDate(),
                i.getCloseDate(),
                i.getSlots()
        );

        System.out.println(tp.render());
    }
}
