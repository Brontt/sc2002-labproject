package policy;

import internship.Internship;
import user.Student;

public interface EligibilityPolicy {
    boolean isEligible(Student s, Internship i);
}
