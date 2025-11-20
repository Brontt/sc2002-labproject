package internship;

import user.CompanyRep;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model for an internship posting.
 * <p>
 * Contains metadata about the internship and a list of applications.
 */
public class Internship {

    public enum InternshipLevel { BASIC, INTERMEDIATE, ADVANCED }
    public enum InternshipStatus { PENDING, APPROVED, REJECTED, FILLED }

    private final String id;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferredMajor;
    private String companyName;
    private CompanyRep postedBy;
    private int slots;
    private boolean visible;
    private LocalDate openDate;
    private LocalDate closeDate;
    private InternshipStatus status;
    private int confirmedCount = 0;
    private final List<InternshipApp> apps = new ArrayList<>();

    /**
     * Construct an internship from CSV-like string fields.
     */
    public Internship(String id, String title, String description, String levelStr, String preferredMajor,
                      String companyName, CompanyRep postedBy, int slots, boolean visible,
                      String openDateStr, String closeDateStr, String statusStr) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = InternshipLevel.valueOf(levelStr.toUpperCase());
        this.preferredMajor = preferredMajor;
        this.companyName = companyName;
        this.postedBy = postedBy;
        this.slots = slots;
        this.visible = visible;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.openDate = LocalDate.parse(openDateStr, fmt);
        this.closeDate = LocalDate.parse(closeDateStr, fmt);
        this.status = InternshipStatus.valueOf(statusStr.toUpperCase());
    }

    public String getId(){ return id; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public InternshipLevel getLevel(){ return level; }
    public String getPreferredMajor(){ return preferredMajor; }
    public String getCompanyName(){ return companyName; }
    public CompanyRep getPostedBy(){ return postedBy; }
    public void setPostedBy(CompanyRep rep){ this.postedBy = rep; }
    public int getSlots(){ return slots; }
    public int getSlotsRemaining(){ return Math.max(0, slots - confirmedCount); }
    public boolean isVisible(){ return visible; }
    public LocalDate getOpenDate(){ return openDate; }
    public LocalDate getCloseDate(){ return closeDate; }
    public InternshipStatus getStatus(){ return status; }
    public void setStatus(InternshipStatus s){ this.status = s; }
    public void setVisible(boolean v){ this.visible = v; }
    public List<InternshipApp> getApps(){ return apps; }

    /**
     * Add an application to this internship's list.
     *
     * @param app application to add
     */
    public void addApp(InternshipApp app){ if (app != null) apps.add(app); }

    /**
     * Increment the confirmed count (a slot has been taken). When confirmed
     * count reaches configured slots the internship becomes FILLED.
     */
    public void decrementSlot(){ confirmedCount++; if (confirmedCount >= slots) status = InternshipStatus.FILLED; }
    public void incrementSlot(){ confirmedCount = Math.max(0, confirmedCount-1); }

    /**
     * Check if this internship is open on the provided date.
     */
    public boolean isOpenFor(LocalDate today) {
        return visible && !today.isBefore(openDate) && !today.isAfter(closeDate) && status == InternshipStatus.APPROVED;
    }

    public boolean isClosingSoon() {
        // Define "soon" as within 7 days (customize as needed)
        LocalDate today = LocalDate.now();
        return closeDate != null && !closeDate.isBefore(today) && !closeDate.isAfter(today.plusDays(7));
    }

    @Override public String toString() {
        return String.format("%s (%s) = %s | %d slots | %s", title, companyName, level, slots, status);
    }
}
