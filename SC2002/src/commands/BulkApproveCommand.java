package commands;

import internship.Internship;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A concrete implementation of the {@link Command} interface that handles the bulk approval of internships.
 * <p>
 * This command iterates through a list of target internships and approves them, while allowing
 * specific internships to be excluded via a {@link Predicate}. It also maintains a history of the
 * internships' previous states (visibility and status) to support the {@link #undo()} operation.
 * </p>
 */
public class BulkApproveCommand implements Command {
    private final List<Internship> target;
    private final Predicate<Internship> except;
    private final List<Boolean> prevVisible = new ArrayList<>();
    private final List<Internship.InternshipStatus> prevStatus = new ArrayList<>();

    /**
     * Constructs a new {@code BulkApproveCommand}.
     *
     * @param target The list of {@link Internship} objects to be processed for approval.
     * @param except A {@link Predicate} defining the condition for exclusion.
     * Internships for which this predicate returns {@code true} will be skipped.
     */
    public BulkApproveCommand(List<Internship> target, java.util.function.Predicate<Internship> except) {
        this.target = target; this.except = except;
    }

    /**
     * Executes the bulk approval logic.
     * <p>
     * This method first clears any previous history. Then, for each internship in the target list:
     * <ol>
     * <li>It saves the current visibility and status to the history lists.</li>
     * <li>It checks the {@code except} predicate. If the predicate returns {@code true}, the internship is skipped.</li>
     * <li>Otherwise, the internship status is set to {@code APPROVED} and visibility is set to {@code true}.</li>
     * </ol>
     * </p>
     */
    @Override public void execute() {
        prevVisible.clear(); prevStatus.clear();
        for (var i : target) {
            prevVisible.add(i.isVisible());
            prevStatus.add(i.getStatus());
            if (except.test(i)) continue;
            i.setStatus(Internship.InternshipStatus.APPROVED);
            i.setVisible(true);
        }
    }

    /**
     * Reverts the changes made by the {@link #execute()} method.
     * <p>
     * This restores every internship in the target list to its exact state (visibility and status)
     * recorded prior to the execution of the command.
     * </p>
     */
    @Override public void undo() {
        for (int idx=0; idx<target.size(); idx++) {
            target.get(idx).setVisible(prevVisible.get(idx));
            target.get(idx).setStatus(prevStatus.get(idx));
        }
    }

    /**
     * Returns the unique identifier name for this command.
     *
     * @return The string "BulkApproveInternships".
     */
    @Override public String name(){ return "BulkApproveInternships"; }
}