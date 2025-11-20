package reporting;

import internship.Internship;

/**
 * Encapsulates filtering criteria for generating internship reports.
 * Allows staff to filter internships by status, preferred major, and level.
 */
public class ReportFilter {
    private Internship.InternshipStatus status;
    private String preferredMajor;
    private Internship.InternshipLevel level;

    public ReportFilter() {}

    public ReportFilter(Internship.InternshipStatus status, String preferredMajor, Internship.InternshipLevel level) {
        this.status = status;
        this.preferredMajor = preferredMajor;
        this.level = level;
    }

    // Getters and Setters
    public Internship.InternshipStatus getStatus() {
        return status;
    }

    public void setStatus(Internship.InternshipStatus status) {
        this.status = status;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }

    public Internship.InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(Internship.InternshipLevel level) {
        this.level = level;
    }

    /**
     * Check if a filter is defined (not all null).
     */
    public boolean hasFilters() {
        return status != null || preferredMajor != null || level != null;
    }
}
