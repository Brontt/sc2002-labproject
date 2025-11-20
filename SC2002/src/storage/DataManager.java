package storage;

import internship.Internship;
import internship.InternshipIds;
import user.*;
import storage.loaders.*;

import java.util.List;

/**
 * DataManager delegates CSV loading/saving responsibilities to specific
 * loader classes (student, staff, company rep, internship). This keeps
 * parsing logic separated per entity type.
 */
public class DataManager {

    private final StudentDataLoader studentLoader;
    private final StaffDataLoader staffLoader;
    private final CompanyRepDataLoader companyRepLoader;
    private InternshipDataLoader internshipLoader;

    public DataManager() {
        this.studentLoader = new StudentDataLoader();
        this.staffLoader = new StaffDataLoader();
        this.companyRepLoader = new CompanyRepDataLoader();
    }

    // STUDENTS
    public List<Student> loadStudents(String filename) {
        return studentLoader.load(filename);
    }

    public void saveStudents(String filename, List<Student> students) {
        studentLoader.save(filename, students);
    }

    // STAFF
    public List<CareerCentreStaff> loadStaff(String filename) {
        return staffLoader.load(filename);
    }

    public void saveStaff(String filename, List<CareerCentreStaff> staff) {
        staffLoader.save(filename, staff);
    }

    // COMPANY REPS
    public List<CompanyRep> loadCompanyReps(String filename) {
        return companyRepLoader.load(filename);
    }

    public void saveCompanyReps(String filename, List<CompanyRep> reps) {
        companyRepLoader.save(filename, reps);
    }

    // INTERNSHIPS
    public List<Internship> loadInternships(String filename, List<CompanyRep> reps) {
        // Create loader with company reps for reference resolution
        if (internshipLoader == null) {
            internshipLoader = new InternshipDataLoader(reps);
        }
        List<Internship> internships = internshipLoader.load(filename);
        
        // Initialize InternshipIds sequence to avoid ID conflicts
        int maxId = internships.stream()
            .map(i -> i.getId())
            .filter(id -> id.startsWith("INT-"))
            .map(id -> {
                try {
                    return Integer.parseInt(id.substring(4));
                } catch (NumberFormatException e) {
                    return 0;
                }
            })
            .max(Integer::compare)
            .orElse(0);
        InternshipIds.initializeFrom(maxId);
        
        return internships;
    }

    public void saveInternships(String filename, List<Internship> internships) {
        if (internshipLoader != null) {
            internshipLoader.save(filename, internships);
        }
    }
}