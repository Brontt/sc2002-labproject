package menu.StudentMenu.StudentActions;

import menu.MenuAction;
import user.Student;
import util.ConsoleHelper;
import util.TablePrinter;
import internship.Internship;
import internship.InternshipApp;
import repository.Repository;

import java.util.List;

/**
 * Action that displays the student's current internship applications and statuses.
 *
 * Fetches application records from {@link Repository} and renders them using
 * {@link TablePrinter} for a readable console display.
 */
public class StudentStatusAction implements MenuAction {
    private final Student me;

    /**
     * @param me the student whose applications will be displayed
     */
    public StudentStatusAction(Student me) { this.me = me; }

    /**
     * Execute the status-reporting flow — query applications and render them.
     */
    @Override
    public void execute() {
        List<InternshipApp> apps = Repository.findApplicationsByStudentId(me.getUserId());
        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

    // Build table
        
        var tp = TablePrinter.builder()
            .unicodeBorders(true)
            .maxTableWidth(140)
            .addColumn("No.",       TablePrinter.Align.RIGHT,  2,  4)
            .addColumn("AppID",     TablePrinter.Align.LEFT,   6, 10)
            .addColumn("Internship",TablePrinter.Align.LEFT,   10, 30)
            .addColumn("Company",   TablePrinter.Align.LEFT,    8, 20)
            .addColumn("Status",    TablePrinter.Align.CENTER, 10, 14)
            .build();

        int idx = 1;
    

        for (InternshipApp a : apps) {
            Internship i = a.getInternship();
            tp.addRow(
                    idx++,
                    a.getId(),
                    (i != null ? ConsoleHelper.safe(i.getTitle()) : "-"),
                    (i != null ? ConsoleHelper.safe(i.getCompanyName()) : "-"),
                    a.getStatus()
          );
        }

        System.out.println(tp.render());
    }

}