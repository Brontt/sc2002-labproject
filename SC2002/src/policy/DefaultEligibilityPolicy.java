package policy;

import internship.Internship;
import user.Student;

public class DefaultEligibilityPolicy implements EligibilityPolicy {
    @Override public boolean isEligible(Student s, Internship i) {
        String m = i.getPreferredMajor();
        if (m != null && !m.isBlank() && !m.equalsIgnoreCase(s.getMajor())) return false;
        return true;
    }
}
