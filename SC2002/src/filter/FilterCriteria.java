package filter;

import internship.Internship;

/**
 * Simple DTO used to transport basic filter criteria.
 */
public class FilterCriteria {
    public Internship.InternshipStatus status;
    public Internship.InternshipLevel level;
    public String major;
    public String company;
    public String keyword;

    public boolean isEmpty() {
        return status == null && level == null &&
                (major == null || major.isBlank()) &&
                (company == null || company.isBlank()) &&
                (keyword == null || keyword.isBlank());
    }
}
