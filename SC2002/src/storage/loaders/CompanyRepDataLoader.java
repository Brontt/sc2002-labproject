package storage.loaders;

import user.CompanyRep;
import util.PasswordService;
import util.io.CsvUtils;
import util.io.EntityLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves CompanyRep entities from/to CSV files.
 * Follows Single Responsibility Principle - only handles CompanyRep data.
 */
public class CompanyRepDataLoader implements EntityLoader<CompanyRep> {
    
    private static final String DEFAULT_PASSWORD = "password";
    
    /**
     * Loads company representatives from CSV file.
     * Expected format: CompanyRepID, Name, CompanyName, Department, Position, Email, Status
     * @param filename Path to the CSV file
     * @return List of CompanyRep objects
     */
    @Override
    public List<CompanyRep> load(String filename) {
        List<CompanyRep> reps = new ArrayList<>();
        
        try (BufferedReader br = CsvUtils.openIfExists(filename)) {
            if (br == null) {
                return reps;
            }
            
            CsvUtils.skipHeader(br);
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length < 7) continue;
                
                String repId = parts[0].trim();
                String name = parts[1].trim();
                String company = parts[2].trim();
                String dept = parts[3].trim();
                String pos = parts[4].trim();
                String email = parts[5].trim();
                boolean approved = parts[6].trim().equalsIgnoreCase("Approved");
                
                CompanyRep rep = new CompanyRep(
                    email,      // Use email as userId for login
                    name,
                    DEFAULT_PASSWORD,
                    company,
                    dept,
                    pos,
                    approved
                );
                
                // Hash password if not already hashed
                if (!PasswordService.isHashed(rep.getPassword())) {
                    rep.setPassword(PasswordService.hashPassword(rep.getPassword()));
                }
                
                // Allow login with repID as well
                rep.setExternalId(repId);
                
                reps.add(rep);
            }
        } catch (IOException e) {
            System.out.println("Error loading company reps: " + e.getMessage());
        }
        
        return reps;
    }
    
    /**
     * Saves company representatives to CSV file.
     * Format: CompanyRepID, Name, CompanyName, Department, Position, Email, Status
     * @param filename Path to the CSV file
     * @param reps List of company reps to save
     */
    @Override
    public void save(String filename, List<CompanyRep> reps) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            
            for (CompanyRep rep : reps) {
                String repId = rep.getExternalId() != null ? rep.getExternalId() : rep.getUserId();
                String status = rep.isApproved() ? "Approved" : "Pending";
                
                pw.printf("%s,%s,%s,%s,%s,%s,%s%n",
                    repId,
                    rep.getName(),
                    rep.getCompanyName(),
                    rep.getDepartment(),
                    rep.getPosition(),
                    rep.getUserId(),   // Email / login ID
                    status
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving company reps: " + e.getMessage());
        }
    }
}