package filter;

import java.util.*;

import internship.Internship;

/**
 * Holds filter settings used to filter internship listings.
 *
 * Provides a {@link #matches(Internship)} predicate that tests whether an
 * internship satisfies the configured constraints.
 */
public class FilterSettings {
    private Internship.InternshipStatus status;
    private Internship.InternshipLevel level;
    private String preferredMajor;
    private String company;
    private String keyword;
    private Set<Internship.InternshipLevel> levelPreferences = new LinkedHashSet<>();

    public boolean matches(Internship i) {
        if (status != null && i.getStatus() != status) return false;
        if (level != null && i.getLevel() != level) return false;
        if (preferredMajor != null && !preferredMajor.isBlank()) {
            if (i.getPreferredMajor()==null || !i.getPreferredMajor().equalsIgnoreCase(preferredMajor)) return false;
        }
        if (company != null && !company.isBlank()) {
            if (i.getCompanyName()==null || !i.getCompanyName().equalsIgnoreCase(company)) return false;
        }
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.toLowerCase();
            boolean matchesKeyword = (i.getTitle() != null && i.getTitle().toLowerCase().contains(kw)) ||
                                    (i.getCompanyName() != null && i.getCompanyName().toLowerCase().contains(kw)) ||
                                    (i.getDescription() != null && i.getDescription().toLowerCase().contains(kw));
            if (!matchesKeyword) return false;
        }
        if (!levelPreferences.isEmpty() && !levelPreferences.contains(i.getLevel())) {
            return false;
        }
        return true;
    }
    
    public boolean isEmpty(){ 
        return status==null && level==null && 
               (preferredMajor==null||preferredMajor.isBlank()) && 
               (company==null||company.isBlank()) &&
               (keyword==null||keyword.isBlank()) &&
               levelPreferences.isEmpty(); 
    }
    
    public void clear(){ 
        status=null; 
        level=null; 
        preferredMajor=null; 
        company=null; 
        keyword=null;
        levelPreferences.clear();
    }

    public void setStatus(Internship.InternshipStatus s){ this.status=s; }
    public void setLevel(Internship.InternshipLevel l){ this.level=l; }
    public void setPreferredMajor(String m){ this.preferredMajor=(m==null?null:m.trim()); }
    public void setCompany(String c){ this.company=(c==null?null:c.trim()); }
    public void setKeyword(String k){ this.keyword=(k==null?null:k.trim()); }
    public void setLevelPreferences(Set<Internship.InternshipLevel> prefs){ 
        this.levelPreferences = prefs == null ? new LinkedHashSet<>() : new LinkedHashSet<>(prefs); 
    }
    
    public Internship.InternshipStatus getStatus(){ return status; }
    public Internship.InternshipLevel getLevel(){ return level; }
    public String getPreferredMajor(){ return preferredMajor; }
    public String getCompany(){ return company; }
    public String getKeyword(){ return keyword; }
    public Set<Internship.InternshipLevel> getLevelPreferences(){ return new LinkedHashSet<>(levelPreferences); }

    @Override public String toString(){ 
        return "status="+status+", level="+level+", major="+preferredMajor+
               ", company="+company+", keyword="+keyword+", levelPrefs="+levelPreferences; 
    }
}
