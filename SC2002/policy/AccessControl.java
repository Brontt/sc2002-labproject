package policy;

import internship.Internship;
import user.CompanyRep;
import util.ValidationExceptions.UnauthorizedActionException;

public class AccessControl {
    public static void ensureRepOwns(CompanyRep rep, Internship i) {
        if (rep == null || i == null || i.getPostedBy() == null) throw new UnauthorizedActionException("Not allowed.");
        if (!rep.getUserId().equalsIgnoreCase(i.getPostedBy().getUserId()))
            throw new UnauthorizedActionException("You do not own this internship.");
    }
}
