# SC2002
SC2002 Object Oriented Programming Project 

(APP)
main.java 
- bootstrap the app (construct dependencies, start/stop)

SystemCoordinator.java 
- main functions: orchestrate runtime behaviour (menus, routing, services)
- coordinates login/CR registration
- set/clear current user in AppState for preferences/notifications 

DONT MERGE MAIN & SYSTEM.COORDINATOR (will violate SRP)

AppState.java 
- SINGLETON in-memory state holder for running application
- tracks current logged in users 
- keeps user nonNegotiables and RankingPreferences (Additional feature: Recommendation System) so they persist across menus during this run 



(BOUNDARY)
CompanyRepMenu.java
- create internship (autoassigned IC as logged in CR)
- list/search their own internships 
- approve internships

StudentMenu.java
- view internships (manual sort/pagination; recommendation system (AF))
- filter options: set nonnegotiables, ranking weights, saved filters
- apply, view status, accept offers, request withdrawals
- join waitlist and view notifications 

StaffMenu.java 
- approve reps 
- review withdrawal queue (free slots and ping waitlisted)
- generate advanced report, export CSV
- run bulk approve with undo and view audit log 



(ENTITIES/USERS)
User.java
- userId, name, password, inbox(notifications from waitlist AF), savedFilters
- login(), changePassword() w basic validation 

Student.java 
- year, major 
- applyForInternship()
- withdrawApplication()
- acceptPlacement()

CompanyRep.java
- companyName, department, position, approved
- approval gated by Staff

CareerCentreStaff.java
- department


(ENTITIES/INTERNSHIP)
Internship.java 
- Fields: id/title/description/level/major/company/postedBy/slots/visible/open/close/status.
- Tracks confirmed count → FILLED status when full.
- isOpenFor(date) check; increment/decrement slots.
- Auto-assigns postedBy (rep-in-charge) on creation.

InternshipApp.java
- A student’s application to an internship: Links Student + Internship.
- ApplicationStatus (PENDING/SUCCESSFUL/UNSUCCESSFUL/CONFIRMED/WITHDRAWN).
- Flags withdrawal requests → staff reviews in queue.

ApplicationStatus.java
- Enum of valid application states.

InternshipIds.java
- Generates human-readable IDs like INT-0001.


(ENTITIES/FILTER)
FilterSettings.java
- Saved per-user filter (spec requirement):
- Optional status, level, major, company.
- matches(internship) to filter lists.
- Stored in User.savedFilters.

FilterCriteria.java
- Ad-hoc filter DTO (optional: if you want to compose complex searches). Not required by menus but useful for future extensions.

NonNegotiables.java
- Smart-matching “musts”:
- mustMatchMajor, onlyOpenNow, and title keywords list.

RankingPreferences.java
- Smart-matching weights:
- wMajor, wClosingSoon, wLevelFit, wTitleKeywords (ints). Used by recommender.



(POLICY)
EligibilityPolicy.java
- Interface for “who can apply” logic.

DefaultEligibilityPolicy.java
- Default rule: if internship has preferredMajor, student must match major; else ok.

AccessControl.java
- Guards authorisation (e.g., a rep can modify only their internship).



(REPO) (in-memory data access)
Repository.java
- Simple in-memory DAO:
- Bootstrapped from storage.
- Find/save/update Internship, InternshipApp, and look up users.
- Central place menus/services use to fetch/modify data.



(SERVICES) (business services)
RecommendationService.java
- Smart ranking engine:
- Applies NonNegotiables as hard filter.
- Scores by major match, closing soon, level proximity to student year, and keyword hits (including extra search keyword).
- Sorts by score desc + tie-breakers.

SearchSpecification.java
- Simple keyword match across title/desc/company/major.

AdvancedReportBuilder.java
- Composes textual report sections (counts by status/level/major + fill rate), and can emit CSV rows.

ReportExporter.java
- Writes report to CSV/TXT files.

Paginator.java
- Splits lists into pages for CLI.

sorting/ComparatorFactory.java
- Strategy set of comparators: by title, closing date, company, or level+title.

NotificationService.java
- Pushes messages to a user’s inbox.

NotificationCenter.java
- Broadcast hook: when a slot frees (slotFreed()), notify only students who joined that internship’s waitlist.

WaitlistService.java
- Manages waitlists per internship (joinWaitlist, getWaitlisted, popAll). Stores per internship ID a Set of student IDs.

WithdrawalQueue.java
- Queue of pending withdrawal requests for staff to review/approve.




(STORAGE) (CSV I/O)

Storage.java
- Pluggable storage interface (loadAll, saveAll).

CsvStorage.java
- Concrete CSV storage:
- Loads students, staff, company reps, internships, applications from CSVs.
- Saves them back on exit.
- Injects loaded lists into Repository.

DataManager.java
- CSV parsing/writing for:
- students (school format: StudentID,Name,Major,Year,Email).
- staff (school format: StaffID,Name,Role,Department,Email).
- company reps (school format: CompanyRepID,Name,CompanyName,Department,Position,Email,Status).
- internships (internal format; includes repId to link creator).

ApplicationCsvIO.java
- CSV parsing/writing for applications:
- id,studentId,internshipId,status,withdrawalRequested.



(COMMANDS) (Command pattern + undo)
Command.java
- Command interface: execute, undo, name.

BulkApproveCommand.java
- Sets many internships to APPROVED + visible, with snapshot so undo restores previous states. Supports “except” predicate.

CommandManager.java
- Runs commands, keeps history stack, supports undo, records to audit log.



(AUDIT) (simple audit trail)
AuditLog.java
- Collects AuditEntry items and prints them.

AuditEntry.java
- Timestamp + action + phase (EXECUTE/UNDO).

(VALIDATION) (custom exceptions)
InvalidInputException.java
- Throw for validation errors.

UnauthorizedActionException.java
- Throw for access violations.

DuplicateApplicationException.java
- Throw on repeated application to same internship.



(UTILS)
Ansi.java
- Optional CLI color helpers (green/red). Use if your terminal supports ANSI.


How the whole thing flows (typical run)
- Main → SystemCoordinator.loadAll() (CSV → memory → Repository).
- User logs in → AppState.setCurrentUser.
- Routed to role menu:
-- Student: sets filters/weights (saved in AppState), views list (manual sort or reco ranking), applies/accepts/withdraws, joins waitlist, reads notifications.
-- Rep: creates internships (rep auto-assigned), lists/searches own postings.
-- Staff: approves reps, processes withdrawal queue (slot freed → notify waitlisted), builds & exports reports, runs bulk approve → undo → view audit.
- Exit → SystemCoordinator.saveAll() (memory → CSV).