package menu.CompanyRepMenu.CompanyRepActions;

import internship.Internship;
import internship.InternshipApp;
import java.util.List;
import java.util.Scanner;
import menu.MenuAction;
import repository.Repository;
import user.CompanyRep;
import util.ConsoleHelper;
import util.TablePrinter;
public class ViewApplicationsAction implements MenuAction {
    private final CompanyRep me;
    private final Scanner sc;

    /**
     * @param me the company representative whose applications will be viewed
     * @param sc the scanner (currently unused in scaffold but kept for parity)
     */
    public ViewApplicationsAction(CompanyRep me, Scanner sc) {
        this.me = me;
        this.sc = sc;
    }

    @Override
    public void execute() {
        System.out.println("\n--- VIEW APPLICATIONS FOR MY INTERNSHIPS ---");

        // 1) Get all applications for internships posted by this rep
        // (see Repository helper below)
        List<InternshipApp> apps = Repository.findApplicationsByRepId(me.getUserId());

        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications yet for your internships.");
            return;
        }

        // 2) Print as table
        printApplicationsTable(apps);

        System.out.print("\n(Press ENTER to return to menu) ");
        sc.nextLine();
    }

    private void printApplicationsTable(List<InternshipApp> apps) {
        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(160)
                .addColumn("No.",       TablePrinter.Align.RIGHT,   2,  4)
                .addColumn("App ID",    TablePrinter.Align.LEFT,    6, 10)
                .addColumn("StudentID", TablePrinter.Align.LEFT,    8, 12)
                .addColumn("Name",      TablePrinter.Align.LEFT,   10, 20)
                .addColumn("Year",      TablePrinter.Align.CENTER,  4,  6)
                .addColumn("Major",     TablePrinter.Align.CENTER,  5,  8)
                .addColumn("Internship",TablePrinter.Align.LEFT,   12, 25)
                .addColumn("Status",    TablePrinter.Align.CENTER, 10, 12)
                .build();

        int idx = 1;
        for (InternshipApp a : apps) {
            Internship i = a.getInternship();
            var student = a.getStudent();
            tp.addRow(
                idx++,
                a.getId(),
                student != null ? student.getUserId() : "-",
                student != null ? ConsoleHelper.safe(student.getName()) : "-",
                student != null ? student.getYear() : "-",
                student != null ? ConsoleHelper.safe(student.getMajor()) : "-",
                i != null ? ConsoleHelper.safe(i.getTitle()) : "-",
                a.getStatus()
            );            
        }

        System.out.println(tp.render());
    }
}
