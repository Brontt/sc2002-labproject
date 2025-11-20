package storage.loaders;

import internship.Internship;
import user.CompanyRep;
import util.io.CsvUtils;
import util.io.EntityLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Internship entities from/to CSV files.
 * Follows Single Responsibility Principle - only handles Internship data.
 */
public class InternshipDataLoader implements EntityLoader<Internship> {
    
    private final List<CompanyRep> companyReps;
    
    /**
     * Creates a new InternshipDataLoader.
     * Requires company reps list to resolve internship creator references.
     * @param companyReps List of company representatives
     */
    public InternshipDataLoader(List<CompanyRep> companyReps) {
        this.companyReps = companyReps;
    }
    
    /**
     * Loads internships from CSV file.
     * Expected format: id,title,desc,level,major,company,openDate,closeDate,slots,status,visible,repId
     * @param filename Path to the CSV file
     * @return List of Internship objects
     */
    @Override
    public List<Internship> load(String filename) {
        List<Internship> internships = new ArrayList<>();
        
        try (BufferedReader br = CsvUtils.openIfExists(filename)) {
            if (br == null) {
                return internships;
            }
            
            CsvUtils.skipHeader(br);
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length < 11) continue;
                
                String id = parts[0].trim();
                String title = parts[1].trim();
                String desc = parts[2].trim();
                String level = parts[3].trim();
                String major = CsvUtils.emptyToNull(parts[4]);
                String company = parts[5].trim();
                String open = parts[6].trim();
                String close = parts[7].trim();
                int slots = Integer.parseInt(parts[8].trim());
                String status = parts[9].trim();
                boolean visible = CsvUtils.parseBoolean(parts[10]);
                String repId = parts.length > 11 ? parts[11].trim() : "";
                
                // Find the company rep by email or repID
                CompanyRep postedBy = findCompanyRep(repId);
                
                Internship internship = new Internship(
                    id, title, desc, level, major, company,
                    postedBy, slots, visible, open, close, status
                );
                
                internships.add(internship);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading internships: " + e.getMessage());
        }
        
        return internships;
    }
    
    /**
     * Saves internships to CSV file.
     * Format: id,title,desc,level,major,company,openDate,closeDate,slots,status,visible,repId
     * @param filename Path to the CSV file
     * @param internships List of internships to save
     */
    @Override
    public void save(String filename, List<Internship> internships) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("id,title,desc,level,major,company,openDate,closeDate,slots,status,visible,repId");
            
            for (Internship i : internships) {
                String repId = i.getPostedBy() != null ? i.getPostedBy().getUserId() : "";
                
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%b,%s%n",
                    i.getId(),
                    CsvUtils.csvSafe(i.getTitle()),
                    CsvUtils.csvSafe(i.getDescription()),
                    i.getLevel().name(),
                    CsvUtils.csvSafe(i.getPreferredMajor()),
                    CsvUtils.csvSafe(i.getCompanyName()),
                    i.getOpenDate().toString(),
                    i.getCloseDate().toString(),
                    i.getSlots(),
                    i.getStatus().name(),
                    i.isVisible(),
                    repId
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving internships: " + e.getMessage());
        }
    }
    
    /**
     * Finds a company rep by ID or external ID.
     * @param repId The rep ID to search for
     * @return CompanyRep if found, null otherwise
     */
    private CompanyRep findCompanyRep(String repId) {
        if (repId == null || repId.isBlank()) {
            return null;
        }
        
        return companyReps.stream()
            .filter(r -> repId.equalsIgnoreCase(r.getUserId()) ||
                        (r.getExternalId() != null && repId.equalsIgnoreCase(r.getExternalId())))
            .findFirst()
            .orElse(null);
    }
}