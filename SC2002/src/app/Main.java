package app;

import storage.CsvStorage;
import storage.Storage;

public class Main {
    public static void main(String[] args) {
        Storage storage = new CsvStorage(
            "data/sample_student_list.csv",
            "data/sample_staff_list.csv",
            "data/sample_company_representative_list.csv",
            "data/internships.csv",
            "data/applications.csv"
        );
        SystemCoordinator app = new SystemCoordinator(storage);
        app.loadAll();
        System.out.println("Welcome to the Internship Placement Management System!\n");
        app.entryScreen();      // Login / Register Rep
        app.saveAll();
        System.out.println("Goodbye!");
    }
}