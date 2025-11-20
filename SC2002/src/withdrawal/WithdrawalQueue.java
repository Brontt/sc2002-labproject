package withdrawal;

import java.util.*;

import internship.InternshipApp;

public class WithdrawalQueue {
    public static class Request {
        public final String id;
        public final InternshipApp app;
        public final String reason;
        public Request(String id, InternshipApp app, String reason) { this.id=id; this.app=app; this.reason=reason; }
    }
    private final Deque<Request> queue = new ArrayDeque<>();
    public void submit(InternshipApp app, String reason) { queue.addLast(new Request(UUID.randomUUID().toString(), app, reason)); }
    public boolean isEmpty(){ return queue.isEmpty(); }
    public List<Request> peekAll(){ return new ArrayList<>(queue); }
    public Request poll(){ return queue.pollFirst(); }
}
