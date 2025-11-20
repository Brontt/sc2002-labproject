package recommendation;

import java.util.Locale;

import internship.Internship;

public class SearchSpecification {
    private final String keyword;
    public SearchSpecification(String keyword) {
        this.keyword = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
    }
    public boolean matches(Internship i) {
        if (keyword.isBlank()) return true;
        String t = (i.getTitle()==null?"":i.getTitle()).toLowerCase();
        String d = (i.getDescription()==null?"":i.getDescription()).toLowerCase();
        String c = (i.getCompanyName()==null?"":i.getCompanyName()).toLowerCase();
        String m = (i.getPreferredMajor()==null?"":i.getPreferredMajor()).toLowerCase();
        return t.contains(keyword) || d.contains(keyword) || c.contains(keyword) || m.contains(keyword);
    }
}
