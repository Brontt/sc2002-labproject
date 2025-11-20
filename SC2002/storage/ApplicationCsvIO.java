package storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import internship.*;
import user.Student;
import user.User;

/**
 * Helper for loading and saving internship applications to CSV.
 *
 * The CSV format is: id,studentId,internshipId,status,withdrawalRequested
 */
public class ApplicationCsvIO {
    public static List<InternshipApp> load(String filename, List<User> users, List<Internship> internships) {
        List<InternshipApp> out = new ArrayList<>();
        try (BufferedReader br = openIfExists(filename)) {
            if (br == null) return out; String line; boolean header=true;
            while ((line = br.readLine()) != null) {
                if (header) { header=false; continue; }
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                String id=p[0].trim(), sid=p[1].trim(), iid=p[2].trim(), status=p[3].trim(), wr=p[4].trim();
                Student s = users.stream().filter(u->u instanceof Student && u.getUserId().equalsIgnoreCase(sid)).map(u->(Student)u).findFirst().orElse(null);
                Internship i = internships.stream().filter(x->x.getId().equalsIgnoreCase(iid)).findFirst().orElse(null);
                if (s==null || i==null) continue;
                InternshipApp app = new InternshipApp(id, s, i);
                if ("CONFIRMED".equalsIgnoreCase(status)) app.confirm();
                if ("WITHDRAWN".equalsIgnoreCase(status)) app.withdraw();
                app.setWithdrawalRequested("true".equalsIgnoreCase(wr));
                out.add(app);
            }
        } catch (Exception e){ System.out.println("Error loading applications: "+e.getMessage()); }
        return out;
    }

    /**
     * Persist the list of applications to CSV.
     */
    public static void save(String filename, List<InternshipApp> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("id,studentId,internshipId,status,withdrawalRequested");
            for (var a : list)
                pw.printf("%s,%s,%s,%s,%b%n", a.getId(), a.getStudent().getUserId(), a.getInternship().getId(), a.getStatus(), a.isWithdrawalRequested());
        } catch (IOException e){ System.out.println("Error saving applications: "+e.getMessage()); }
    }

    private static BufferedReader openIfExists(String filename) {
        try { Path p = Path.of(filename); if (!Files.exists(p)) return null; return Files.newBufferedReader(p); }
        catch (IOException e) { return null; }
    }
}
