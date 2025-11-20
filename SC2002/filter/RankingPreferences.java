package filter;

import internship.Internship.InternshipLevel;
import java.util.*;

/**
 * Preferences used to influence ranking/scoring of internships for
 * recommendation purposes. These weights control how different factors
 * (major match, closing soon, level fit, keyword match) contribute to
 * the computed score.
 */
public class RankingPreferences {
    private int wMajor = 30, wClosingSoon = 30, wLevelFit = 20, wTitleKeywords = 20;
    private String rankingKeyword;  // Keywords for ranking boost (not filtering)
    private Set<InternshipLevel> rankingLevelPreferences = new LinkedHashSet<>();  // Levels for ranking (not filtering)
    
    public void setWMajor(int v){ wMajor = Math.max(0,v); }
    public void setWClosingSoon(int v){ wClosingSoon = Math.max(0,v); }
    public void setWLevelFit(int v){ wLevelFit = Math.max(0,v); }
    public void setWTitleKeywords(int v){ wTitleKeywords = Math.max(0,v); }

    public int getWMajor(){ return wMajor; }
    public int getWClosingSoon(){ return wClosingSoon; }
    public int getWLevelFit(){ return wLevelFit; }
    public int getWTitleKeywords(){ return wTitleKeywords; }
    
    public void setRankingKeyword(String k){ this.rankingKeyword = (k == null ? null : k.trim()); }
    public String getRankingKeyword(){ return rankingKeyword; }
    
    public void setRankingLevelPreferences(Set<InternshipLevel> prefs){ 
        this.rankingLevelPreferences = prefs == null ? new LinkedHashSet<>() : new LinkedHashSet<>(prefs); 
    }
    public Set<InternshipLevel> getRankingLevelPreferences(){ return new LinkedHashSet<>(rankingLevelPreferences); }
}