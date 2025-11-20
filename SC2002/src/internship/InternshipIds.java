package internship;

import java.util.concurrent.atomic.AtomicInteger;

public class InternshipIds {
    private static final AtomicInteger seq = new AtomicInteger(0);
    public static String next() { return "INT-" + String.format("%04d", seq.incrementAndGet()); }
    
    /**
     * Initialize the sequence to start from the given number.
     * Used after loading internships to ensure new IDs don't conflict.
     * @param startFrom the number to start the sequence from
     */
    public static void initializeFrom(int startFrom) {
        seq.set(startFrom);
    }
}
