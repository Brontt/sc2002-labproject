package storage.loaders;

import user.Student;
import util.PasswordService;
import util.io.CsvUtils;
import util.io.EntityLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Student entities from/to CSV files.
 * Follows Single Responsibility Principle - only handles Student data.
 */
public class StudentDataLoader implements EntityLoader<Student> {
    
    private static final String DEFAULT_PASSWORD = "password";
    
    /**
     * Loads students from CSV file.
     * Expected format: StudentID, Name, Major, Year, Email
     * @param filename Path to the CSV file
     * @return List of Student objects
     */
    @Override
    public List<Student> load(String filename) {
        List<Student> students = new ArrayList<>();
        
        try (BufferedReader br = CsvUtils.openIfExists(filename)) {
            if (br == null) {
                return students;
            }
            
            CsvUtils.skipHeader(br);
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                
                String id = parts[0].trim();
                String name = parts[1].trim();
                String major = parts[2].trim();
                int year = Integer.parseInt(parts[3].trim());
                
                Student student = new Student(id, name, DEFAULT_PASSWORD, year, major);
                
                // Hash password if not already hashed
                if (!PasswordService.isHashed(student.getPassword())) {
                    student.setPassword(PasswordService.hashPassword(student.getPassword()));
                }
                
                students.add(student);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * Saves students to CSV file.
     * Format: StudentID, Name, Major, Year, Email
     * @param filename Path to the CSV file
     * @param students List of students to save
     */
    @Override
    public void save(String filename, List<Student> students) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("StudentID,Name,Major,Year,Email");
            
            for (Student s : students) {
                pw.printf("%s,%s,%s,%d,%s%n",
                    s.getUserId(), 
                    s.getName(), 
                    s.getMajor(), 
                    s.getYear(), 
                    ""  // Email not stored
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }
}