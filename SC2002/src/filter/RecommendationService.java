package filter;

import internship.Internship;
import internship.InternshipScore;
import user.Student;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * RecommendationService computes ranking scores for internships based on
 * student profile, filter settings and ranking preferences. The algorithm is
 * intentionally simple and parameterized via {@link RankingPreferences} and
 * {@link NonNegotiables}.
 */
public class RecommendationService {

    /**
     * Rank internships by computing a score for each and sorting descending.
     */
    public static List<InternshipScore> rankInternships(
            List<Internship> internships,
            Student student,
            FilterSettings filterSettings,
            RankingPreferences rankingPreferences,
            NonNegotiables nonNegotiables
    ) {
        List<InternshipScore> scored = new ArrayList<>();
        for (Internship i : internships) {
            int score = calculateScore(i, student, filterSettings, rankingPreferences, nonNegotiables);
            scored.add(new InternshipScore(i, score));
        }
        scored.sort((a, b) -> Integer.compare(b.score, a.score)); // descending
        return scored;
    }

    /**
     * Compute a score for a single internship using configured weights.
     */
    public static int calculateScore(
            Internship i,
            Student student,
            FilterSettings filterSettings,
            RankingPreferences rankingPreferences,
            NonNegotiables nonNegotiables
    ) {
        int score = 0;

        // Major match - give points if major matches OR internship has no preferred major
        if (i.getPreferredMajor() == null || i.getPreferredMajor().isBlank() ||
            i.getPreferredMajor().equalsIgnoreCase(student.getMajor())) {
            score += rankingPreferences.getWMajor();
        }

        // Closing soon - scale points based on days remaining until close
        if (i.getCloseDate() != null) {
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, i.getCloseDate());
            if (days >= 0) {
                // scale factor: full weight if closing today, taper to 0 at or beyond maxWindow
                final long maxWindow = 30L; // adjust window as desired
                double factor = 1.0;
                if (days >= maxWindow) {
                    factor = 0.0;
                } else {
                    factor = (double)(maxWindow - days) / (double)maxWindow; // 1.0 -> today, 0.0 -> >=maxWindow
                }
                int add = (int)Math.round(rankingPreferences.getWClosingSoon() * factor);
                score += add;
            }
        }

        // Level fit - give points if level matches ranking preferences or no preferences set
        Set<Internship.InternshipLevel> levels = rankingPreferences.getRankingLevelPreferences();
        if (levels == null || levels.isEmpty() || levels.contains(i.getLevel())) {
            score += rankingPreferences.getWLevelFit();
        }

        // Keyword match - give points if ranking keyword is in title/company/description
        String kw = rankingPreferences.getRankingKeyword();
        if (kw != null && !kw.isBlank() && keywordMatch(i, kw)) {
            score += rankingPreferences.getWTitleKeywords();
        }
        return score;
    }

    private static boolean keywordMatch(Internship i, String kw) {
        String k = kw.toLowerCase();
        return (i.getTitle() != null && i.getTitle().toLowerCase().contains(k)) ||
               (i.getCompanyName() != null && i.getCompanyName().toLowerCase().contains(k)) ||
               (i.getDescription() != null && i.getDescription().toLowerCase().contains(k));
    }
}