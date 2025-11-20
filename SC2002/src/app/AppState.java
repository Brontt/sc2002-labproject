package app;

import filter.NonNegotiables;
import filter.RankingPreferences;
import java.util.HashMap;
import java.util.Map;
import user.User;

/**
 * Lightweight global state holder (per run).
 * Keeps current user and per-user smart-matching preferences.
 */
public class AppState {
    private static final AppState INSTANCE = new AppState();
    public static AppState get() { return INSTANCE; }

    private User currentUser;

    // Per-user saved smart-matching preferences (persist within session)
    private final Map<String, NonNegotiables> nonNegByUser = new HashMap<>();
    private final Map<String, RankingPreferences> rankByUser = new HashMap<>();

    private AppState() {}

    public void setCurrentUser(User u) { this.currentUser = u; }
    public User getCurrentUser() { return currentUser; }
    public void clearCurrentUser() { this.currentUser = null; }

    public NonNegotiables getNonNegotiables(String userId) {
        return nonNegByUser.computeIfAbsent(userId, k -> new NonNegotiables());
    }

    public RankingPreferences getRankingPreferences(String userId) {
        return rankByUser.computeIfAbsent(userId, k -> new RankingPreferences());
    }

    // Optional: allow external update objects to be stored back (if you recreate objects)
    public void setNonNegotiables(String userId, NonNegotiables nn) {
        if (nn != null) nonNegByUser.put(userId, nn);
    }
    public void setRankingPreferences(String userId, RankingPreferences rp) {
        if (rp != null) rankByUser.put(userId, rp);
    }
}
