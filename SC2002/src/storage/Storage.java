package storage;

import internship.Internship;
import internship.InternshipApp;
import user.CompanyRep;
import user.CareerCentreStaff;
import user.Student;

import java.util.List;

/**
 * Storage abstraction used to load and save application data. Implementations
 * may read/write CSV files, databases or other persistence mechanisms.
 *
 * The storage implementation is supplied with mutable target lists which it
 * populates when {@link #loadAll()} is invoked. Similarly, {@link #saveAll()}
 * should persist the current contents of those lists.
 */
public interface Storage {
    /**
     * Load all data into the configured target lists.
     */
    void loadAll();

    /**
     * Persist data from the configured target lists to backing storage.
     */
    void saveAll();

    // add this so consumers can hand mutable lists to storage without needing instanceof
    void setTargets(List<Student> students,
                    List<CareerCentreStaff> staff,
                    List<CompanyRep> reps,
                    List<Internship> internships,
                    List<InternshipApp> applications);
}
