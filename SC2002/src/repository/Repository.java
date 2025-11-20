package repository;

import internship.*;
import user.CompanyRep;
import user.User;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory repository used by the application to store users, internships
 * and applications. Methods are static for convenience in this simple design.
 */
public class Repository {
    private static final List<User> users = new ArrayList<>();
    private static final List<Internship> internships = new ArrayList<>();
    private static final List<InternshipApp> apps = new ArrayList<>();
    private static final AtomicInteger appSeq = new AtomicInteger(0);

    /* -------- Bootstrap -------- */
    /**
     * Bootstrap in-memory lists from the provided collections (typically
     * populated by the storage layer).
     */
    public static void bootstrap(List<User> u, List<Internship> i, List<InternshipApp> a) {
        users.clear(); if (u != null) users.addAll(u);
        internships.clear(); if (i != null) internships.addAll(i);
        apps.clear(); if (a != null) apps.addAll(a);
    }

    /* -------- Users -------- */
    /** Minimal add: needed by StaffMenu to list all users (e.g., pending reps). */
    public static List<User> findAllUsers() {
        return new ArrayList<>(users);
    }

    /** Keep signature but return concrete type for convenience. */
    public static User findUserById(String id) {
        if (id == null) return null;

        for (var u : users) {
    
            // 1) normal student/staff login by userId
            if (u.getUserId() != null && u.getUserId().equalsIgnoreCase(id)) {
                return u;
            }

            // 2) allow CompanyRep login using external repID
            if (u instanceof CompanyRep cr) {
                String ext = cr.getExternalId();
                if (ext != null && ext.equalsIgnoreCase(id)) {
                    return cr;
                }
            }
        }
    
        return null;
    }    

    /** Minimal add: used by StaffMenu after mutating a user (in-memory no-op). */
    public static void updateUser(User u){
        // In-memory structure holds live references, so nothing required.
        // If you later switch to detached copies, replace list item by ID here.
    }

    /* -------- Internships -------- */
    public static List<Internship> findAllInternships(){ return new ArrayList<>(internships); }

    public static Internship findInternshipById(String id){
        if (id == null) return null;
        for (var i: internships) {
            if (i.getId() != null && i.getId().equalsIgnoreCase(id)) return i;
        }
        return null;
    }

    public static List<Internship> findInternshipsByRep(String repId) {
        List<Internship> result = new ArrayList<>();
        if (repId == null) return result;
    
        for (Internship i : internships) {
            CompanyRep cr = i.getPostedBy();
            if (cr != null
                    && cr.getUserId() != null
                    && cr.getUserId().equalsIgnoreCase(repId)) {
                result.add(i);
            }
        }
        return result;
    }            
    
    public static List<Internship> findInternshipsByRepUserId(String email) {
        if (email == null) return List.of();
        return findAllInternships().stream()
            .filter(i -> i.getPostedBy() != null
                      && email.equalsIgnoreCase(i.getPostedBy().getUserId()))
            .toList();
    }    

    public static String newApplicationId(){ return "APP-" + String.format("%05d", appSeq.incrementAndGet()); }

    public static String newInternshipId(){ return InternshipIds.next(); }

    public static void saveInternship(Internship i, String repId){ 
        if (i != null) internships.add(i); 
    }

    public static void updateInternship(Internship i){ 
        // In-memory: no-op. If you move to detached objects, replace by ID here.
    }

    /* -------- Applications -------- */
    public static List<InternshipApp> findApplicationsByStudentId(String sid){
        if (sid == null) return List.of();
        return apps.stream()
                .filter(a -> a.getStudent()!=null
                          && a.getStudent().getUserId()!=null
                          && a.getStudent().getUserId().equalsIgnoreCase(sid))
                .collect(Collectors.toList());
    }

    /** Minimal add: used by StudentMenu for accept/withdraw actions. */
    public static List<InternshipApp> findApplicationsByRepId(String repId) {
        if (repId == null) return List.of();
    
        return apps.stream()
                .filter(a -> {
                    if (a.getInternship() == null) return false;
                    var rep = a.getInternship().getPostedBy();
                    return rep != null
                            && rep.getUserId() != null
                            && rep.getUserId().equalsIgnoreCase(repId);
                })
                .collect(Collectors.toList());
    }
    

    public static void saveApplication(InternshipApp a){ if (a != null) apps.add(a); }

    public static List<InternshipApp> findAllApplications() {
        return new ArrayList<>(apps);
    }

    public static void updateApplication(InternshipApp a){ 
        // In-memory: no-op.
    }

    public static List<CompanyRep> findPendingCompanyReps() {
        List<CompanyRep> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof CompanyRep rep && !rep.isApproved()) {
                result.add(rep);
            }
        }
        return result;
    }

    public static List<InternshipApp> findPendingWithdrawals() {
    return apps.stream()
            .filter(a -> a != null
                      && a.isWithdrawalRequested()              // student has asked to withdraw
                      && a.getStatus() == ApplicationStatus.CONFIRMED) // currently confirmed placement
            .collect(Collectors.toList());
            }
}
