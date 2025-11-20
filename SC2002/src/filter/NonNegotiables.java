package filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents non-negotiable constraints applied by the student when
 * considering internship recommendations (e.g., must match major,
 * only show currently open internships, required title keywords).
 */
public class NonNegotiables {
    private boolean mustMatchMajor = false;
    private boolean onlyOpenNow = true;
    private final List<String> titleKeywords = new ArrayList<>();

    public void setMustMatchMajor(boolean v){ this.mustMatchMajor = v; }
    public void setOnlyOpenNow(boolean v){ this.onlyOpenNow = v; }
    public void addKeyword(String k){ if (k!=null && !k.isBlank()) titleKeywords.add(k.trim()); }

    public boolean isMustMatchMajor(){ return mustMatchMajor; }
    public boolean isOnlyOpenNow(){ return onlyOpenNow; }
    public List<String> getTitleKeywords(){ return Collections.unmodifiableList(titleKeywords); }
}
