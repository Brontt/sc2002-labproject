package util;

import java.util.Comparator;

import internship.Internship;

public final class ComparatorFactory {
    private ComparatorFactory(){}

    public static Comparator<Internship> byTitle() {
        return Comparator.comparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER);
    }
    public static Comparator<Internship> byClosingDate() {
        return Comparator.comparing(Internship::getCloseDate);
    }
    public static Comparator<Internship> byCompany() {
        return Comparator.comparing(Internship::getCompanyName, String.CASE_INSENSITIVE_ORDER);
    }
    public static Comparator<Internship> byLevelThenTitle() {
        return Comparator.comparing((Internship i) -> i.getLevel().ordinal())
                .thenComparing(Internship::getTitle, String.CASE_INSENSITIVE_ORDER);
    }
}
