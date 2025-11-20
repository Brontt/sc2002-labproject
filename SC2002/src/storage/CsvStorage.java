package storage;

import internship.*;
import repository.Repository;
import user.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV-backed storage implementation.
 *
 * Loads and saves domain entities from/to CSV files. Internally uses
 * {@link DataManager} and {@link ApplicationCsvIO} for parsing and writing.
 */
public class CsvStorage implements Storage {
    private final String studentsFile, staffFile, repsFile, internshipsFile, applicationsFile;
    private DataManager dataManager;  // Store for reuse

    private List<Student> students;
    private List<CareerCentreStaff> staff;
    private List<CompanyRep> reps;
    private List<Internship> internships;
    private List<InternshipApp> applications;

    public CsvStorage(String studentsFile, String staffFile, String repsFile, String internshipsFile, String applicationsFile) {
        this.studentsFile = studentsFile; this.staffFile = staffFile; this.repsFile = repsFile;
        this.internshipsFile = internshipsFile; this.applicationsFile = applicationsFile;
    }

    @Override
    public void setTargets(List<Student> students,
                           List<CareerCentreStaff> staff,
                           List<CompanyRep> reps,
                           List<Internship> internships,
                           List<InternshipApp> applications) {
        this.students = students;
        this.staff = staff;
        this.reps = reps;
        this.internships = internships;
        this.applications = applications;
    }

    /**
     * Load all CSV-backed data into the provided target lists and bootstrap
     * the in-memory {@link SC2002.Repository.Repository}.
     */
    @Override public void loadAll() {
        students.clear(); staff.clear(); reps.clear(); internships.clear(); applications.clear();
        
        dataManager = new DataManager();
        students.addAll(dataManager.loadStudents(studentsFile));
        staff.addAll(dataManager.loadStaff(staffFile));
        reps.addAll(dataManager.loadCompanyReps(repsFile));
        internships.addAll(dataManager.loadInternships(internshipsFile, reps));
        applications.addAll(ApplicationCsvIO.load(applicationsFile, new ArrayList<>(students) {{
            addAll(staff); addAll(reps);
        }}, internships));
        Repository.bootstrap(new ArrayList<>(students) {{ addAll(staff); addAll(reps); }},
                internships, applications);
    }

    /**
     * Persist all current entities to the configured CSV files.
     */
    @Override public void saveAll() {
        if (dataManager == null) {
            dataManager = new DataManager();
            // Need to reinitialize internship loader with current reps
            dataManager.loadInternships(internshipsFile, reps);
        }
        dataManager.saveStudents(studentsFile, students);
        dataManager.saveStaff(staffFile, staff);
        dataManager.saveCompanyReps(repsFile, reps);
        dataManager.saveInternships(internshipsFile, internships);
        ApplicationCsvIO.save(applicationsFile, applications);
    }
}