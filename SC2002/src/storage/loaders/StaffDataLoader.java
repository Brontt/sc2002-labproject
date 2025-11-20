package storage.loaders;

import user.CareerCentreStaff;
import util.PasswordService;
import util.io.CsvUtils;
import util.io.EntityLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves CareerCentreStaff entities from/to CSV files.
 * Follows Single Responsibility Principle - only handles Staff data.
 */
public class StaffDataLoader implements EntityLoader<CareerCentreStaff> {
    
    private static final String DEFAULT_PASSWORD = "password";
    
    /**
     * Loads staff from CSV file.
     * Expected format: StaffID, Name, Role, Department, Email
     * @param filename Path to the CSV file
     * @return List of CareerCentreStaff objects
     */
    @Override
    public List<CareerCentreStaff> load(String filename) {
        List<CareerCentreStaff> staff = new ArrayList<>();
        
        try (BufferedReader br = CsvUtils.openIfExists(filename)) {
            if (br == null) {
                return staff;
            }
            
            CsvUtils.skipHeader(br);
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                
                String id = parts[0].trim();
                String name = parts[1].trim();
                String department = parts[3].trim();
                
                CareerCentreStaff staffMember = new CareerCentreStaff(
                    id, name, DEFAULT_PASSWORD, department
                );
                
                // Hash password if not already hashed
                if (!PasswordService.isHashed(staffMember.getPassword())) {
                    staffMember.setPassword(PasswordService.hashPassword(staffMember.getPassword()));
                }
                
                staff.add(staffMember);
            }
        } catch (IOException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
        
        return staff;
    }
    
    /**
     * Saves staff to CSV file.
     * Format: StaffID, Name, Role, Department, Email
     * @param filename Path to the CSV file
     * @param staff List of staff to save
     */
    @Override
    public void save(String filename, List<CareerCentreStaff> staff) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("StaffID,Name,Role,Department,Email");
            
            for (CareerCentreStaff st : staff) {
                pw.printf("%s,%s,%s,%s,%s%n",
                    st.getUserId(),
                    st.getName(),
                    "Career Center Staff",
                    st.getDepartment(),
                    ""  // Email not stored
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving staff: " + e.getMessage());
        }
    }
}