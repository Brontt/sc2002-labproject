package reporting;

import internship.Internship;
import internship.InternshipApp;
import internship.ApplicationStatus;
import repository.Repository;
import util.ConsoleHelper;
import util.TablePrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive report generator for internship management system.
 * Provides detailed statistics, summaries, and filtered internship listings.
 */
public class ReportGenerator {
    private final List<Internship> allInternships;
    private final List<InternshipApp> allApplications;
    private final ReportFilter filter;
    private final LocalDateTime reportGeneratedTime;

    public ReportGenerator(ReportFilter filter) {
        this.allInternships = Repository.findAllInternships();
        this.allApplications = Repository.findAllApplications();
        this.filter = filter;
        this.reportGeneratedTime = LocalDateTime.now();
    }

    /**
     * Generate the complete report with header, summary, and filtered internships.
     */
    public void generateFullReport() {
        printReportHeader();
        printSummaryOverview();
        printFilteredInternships();
    }

    /**
     * Print the report header with generation time and applied filters.
     */
    private void printReportHeader() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String generatedAt = reportGeneratedTime.format(formatter);

        System.out.println("\n" + "=".repeat(67) + " INTERNSHIP REPORT " + "=".repeat(68));
        System.out.println("Generated on: " + generatedAt);
        
        System.out.println("Filters Applied:");
        if (filter == null || !filter.hasFilters()) {
            System.out.println("   Status: ALL");
            System.out.println("   Preferred Major: ALL");
            System.out.println("   Level: ALL");
        } else {
            System.out.println("   Status: " + (filter.getStatus() != null ? filter.getStatus().name() : "ALL"));
            System.out.println("   Preferred Major: " + (filter.getPreferredMajor() != null ? filter.getPreferredMajor() : "ALL"));
            System.out.println("   Level: " + (filter.getLevel() != null ? filter.getLevel().name() : "ALL"));
        }
        System.out.println("=".repeat(153));
    }

    /**
     * Print the summary overview section with counts and breakdowns.
     */
    private void printSummaryOverview() {
        System.out.println("\n" + "-".repeat(49) + " SUMMARY OVERVIEW " + "-".repeat(49));

        // Total internship counts
        long totalInternships = allInternships.size();
        long visibleInternships = allInternships.stream().filter(Internship::isVisible).count();
        long hiddenInternships = totalInternships - visibleInternships;

        System.out.println("Total Internships: " + totalInternships);
        System.out.println("Internships (Visible to Students): " + visibleInternships);
        System.out.println("Internships (Hidden / Visibility OFF): " + hiddenInternships);

        // Internship status breakdown
        System.out.println("\nInternship Status Breakdown:");
        Map<Internship.InternshipStatus, Long> statusCounts = allInternships.stream()
                .collect(Collectors.groupingBy(Internship::getStatus, Collectors.counting()));
        
        for (Internship.InternshipStatus status : Internship.InternshipStatus.values()) {
            long count = statusCounts.getOrDefault(status, 0L);
            System.out.println(String.format("   • %-20s: %3d", status.name(), count));
        }

        // Application status breakdown
        System.out.println("\nApplication Status Breakdown:");
        Map<ApplicationStatus, Long> appStatusCounts = allApplications.stream()
                .collect(Collectors.groupingBy(InternshipApp::getStatus, Collectors.counting()));
        
        for (ApplicationStatus status : ApplicationStatus.values()) {
            long count = appStatusCounts.getOrDefault(status, 0L);
            System.out.println(String.format("   • %-20s: %3d", status.name(), count));
        }

        // Application volume statistics
        System.out.println("\nApplication Volume:");
        long totalApplications = allApplications.size();
        long uniqueStudents = allApplications.stream()
                .map(app -> app.getStudent().getUserId())
                .distinct()
                .count();
        double avgAppsPerInternship = totalInternships == 0 ? 0 : 
                (double) totalApplications / totalInternships;

        System.out.println("   • Total Applications Submitted: " + totalApplications);
        System.out.println("   • Unique Students Applied: " + uniqueStudents);
        System.out.println(String.format("   • Average Applications per Internship: %.1f", avgAppsPerInternship));

        // Company/Internship statistics
        System.out.println("\nCompany/Internship Statistics:");
        long uniqueCompanies = allInternships.stream()
                .map(Internship::getCompanyName)
                .distinct()
                .count();
        
        // Company with most applications
        var companyAppCount = allApplications.stream()
                .filter(app -> app.getInternship() != null)
                .collect(Collectors.groupingBy(
                        app -> app.getInternship().getCompanyName(),
                        Collectors.counting()
                ));
        
        String companyMostApps = companyAppCount.isEmpty() ? "N/A" :
                companyAppCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(e -> e.getKey() + " (" + e.getValue() + " apps)")
                        .orElse("N/A");

        // Internship with highest demand
        var internshipAppCount = allApplications.stream()
                .filter(app -> app.getInternship() != null)
                .collect(Collectors.groupingBy(
                        app -> app.getInternship().getTitle(),
                        Collectors.counting()
                ));
        
        String internshipHighestDemand = internshipAppCount.isEmpty() ? "N/A" :
                internshipAppCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(e -> e.getKey() + " (" + e.getValue() + " apps)")
                        .orElse("N/A");

        System.out.println("   • Number of Companies Posting: " + uniqueCompanies);
        System.out.println("   • Company With Most Applications: " + companyMostApps);
        System.out.println("   • Internship With Highest Demand: " + internshipHighestDemand);

        System.out.println("-".repeat(115));
    }

    /**
     * Print filtered internships in a formatted table.
     */
    private void printFilteredInternships() {
        List<Internship> filtered = getFilteredInternships();

        if (filtered.isEmpty()) {
            System.out.println("\nNo internships match the selected filters.");
            return;
        }

        System.out.println("\n" + "-".repeat(48) + " FILTERED INTERNSHIPS " + "-".repeat(47));

        var tp = TablePrinter.builder()
                .unicodeBorders(true)
                .maxTableWidth(160)
                .addColumn("ID",       TablePrinter.Align.CENTER,  4,  6)
                .addColumn("Internship Title", TablePrinter.Align.LEFT, 20, 26)
                .addColumn("Company",  TablePrinter.Align.LEFT,   8, 12)
                .addColumn("Level",    TablePrinter.Align.CENTER, 7,  9)
                .addColumn("PrefMajor",TablePrinter.Align.CENTER, 9, 11)
                .addColumn("Status",   TablePrinter.Align.CENTER, 10, 12)
                .addColumn("Slots",    TablePrinter.Align.RIGHT,  7,  9)
                .build();

        for (Internship i : filtered) {
            String slotInfo = i.getConfirmedCount() + "/" + i.getSlots();
            tp.addRow(
                    i.getId(),
                    ConsoleHelper.safe(i.getTitle()),
                    ConsoleHelper.safe(i.getCompanyName()),
                    i.getLevel().name(),
                    ConsoleHelper.safe(i.getPreferredMajor()),
                    i.getStatus().name(),
                    slotInfo
            );
        }

        System.out.println(tp.render());
        System.out.println("Showing " + filtered.size() + " of " + allInternships.size() + " internships");
        System.out.println("-".repeat(115));
    }

    /**
     * Get the filtered list of internships based on filter criteria.
     */
    private List<Internship> getFilteredInternships() {
        if (filter == null || !filter.hasFilters()) {
            return allInternships;
        }

        return allInternships.stream()
                .filter(i -> filter.getStatus() == null || i.getStatus() == filter.getStatus())
                .filter(i -> filter.getPreferredMajor() == null ||
                        (i.getPreferredMajor() != null && 
                         i.getPreferredMajor().equalsIgnoreCase(filter.getPreferredMajor())))
                .filter(i -> filter.getLevel() == null || i.getLevel() == filter.getLevel())
                .collect(Collectors.toList());
    }
}
