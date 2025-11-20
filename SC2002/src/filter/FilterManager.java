package filter;

import internship.Internship;
import internship.InternshipScore;
import user.Student;
import java.util.*;

/**
 * Manages filtering and recommendation states for students.
 * Handles toggle functionality for "show all/filtered only" and "recommendations on/off"
 */
public class FilterManager {
    private final FilterSettings filterSettings;
    private final RankingPreferences rankingPreferences;
    private final NonNegotiables nonNegotiables;
    private boolean filterEnabled = false;
    private boolean recommendationEnabled = false;
    
    public FilterManager() {
        this.filterSettings = new FilterSettings();
        this.rankingPreferences = new RankingPreferences();
        this.nonNegotiables = new NonNegotiables();
    }
    
    public FilterSettings getFilterSettings() { return filterSettings; }
    public RankingPreferences getRankingPreferences() { return rankingPreferences; }
    public NonNegotiables getNonNegotiables() { return nonNegotiables; }
    
    public boolean isFilterEnabled() { return filterEnabled; }
    public void setFilterEnabled(boolean enabled) { this.filterEnabled = enabled; }
    public boolean isRecommendationEnabled() { return recommendationEnabled; }
    public void setRecommendationEnabled(boolean enabled) { this.recommendationEnabled = enabled; }
    public void toggleFilter() { this.filterEnabled = !this.filterEnabled; }
    public void toggleRecommendation() { this.recommendationEnabled = !this.recommendationEnabled; }
    
    /**
     * Apply filters to a list of internships if filter is enabled
     */
    public List<Internship> applyFilters(List<Internship> internships) {
        if (!filterEnabled || filterSettings.isEmpty()) {
            return new ArrayList<>(internships);
        }
        
        return internships.stream()
            .filter(filterSettings::matches)
            .toList();
    }
    
    /**
     * Apply ranking to internships based on student's profile and preferences.
     * If recommendations are disabled, all internships receive a score of 0.
     */
    public List<InternshipScore> applyRanking(List<Internship> internships, Student student) {
        if (!recommendationEnabled) {
            // Return with score 0
            List<InternshipScore> out = new ArrayList<>();
            for (Internship i : internships) out.add(new InternshipScore(i, 0));
            return out;
        }
        return RecommendationService.rankInternships(
                internships, student, filterSettings, rankingPreferences, nonNegotiables);
    }
    
    /**
     * Check if student is eligible for an internship level based on their year
     */
    public static boolean isLevelEligibleForStudent(Student student, Internship internship) {
        int year = student.getYear();
        if (year <= 2) {
            return internship.getLevel() == Internship.InternshipLevel.BASIC;
        }
        return true; // Y3+ can apply to all levels
    }
    
    public void clearAll() {
        filterSettings.clear();
        rankingPreferences.setWMajor(30);
        rankingPreferences.setWClosingSoon(30);
        rankingPreferences.setWLevelFit(20);
        rankingPreferences.setWTitleKeywords(20);
        rankingPreferences.setRankingKeyword(null);
        rankingPreferences.setRankingLevelPreferences(null);
        nonNegotiables.setMustMatchMajor(false);
        nonNegotiables.setOnlyOpenNow(true);
        filterEnabled = false;
        recommendationEnabled = false;
    }
    
    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filter: ").append(filterEnabled ? "ON" : "OFF");
        if (filterEnabled && !filterSettings.isEmpty()) {
            sb.append(" (").append(filterSettings.toString()).append(")");
        }
        sb.append(" | Recommendations: ").append(recommendationEnabled ? "ON" : "OFF");
        return sb.toString();
    }

    // Delegation methods for filter settings
    public void setMajor(String major) { filterSettings.setPreferredMajor(major); }
    public void setCompany(String company) { filterSettings.setCompany(company); }
    public void setLevel(Internship.InternshipLevel level) { filterSettings.setLevel(level); }
}