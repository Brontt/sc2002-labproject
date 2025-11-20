📌 Internship Placement Management System
SC2002 Object-Oriented Programming — Semester 1 AY25/26

CLI-based internship management platform designed with BCE architecture and SOLID principles.

👥 Group Members
Name	Course	Lab Group
Tan Shi Ya Shianne	SC2002	SCED
Tham En Yi	SC2002	SCED
Kuek Pei Shan	SC2002	SCED
Goh Jun Xian, Bryant	SC2002	SCED
📚 Project Overview

This project implements a Command Line Internship Placement Management System that simulates interactions between:

Students

Company Representatives

Career Centre Staff

It is fully object-oriented following Boundary–Control–Entity (BCE) architecture, SOLID design principles, and an extensible modular structure using Strategy, Template Method, Command, and Composition patterns.

The system uses CSV-based persistence with pluggable storage and supports filtering, recommendations, waitlists, batch operations, and audit logging.

🚀 Features by User Role
🔐 1. All Users

* Login & logout

* Change password

* Default password is password

* Receive notifications (inbox)

* Access session-persistent filter & preference settings

🎓 2. Students

* View internships filtered by:

* Major match

* Year eligibility (Y1–2: Basic only; Y3–4: All levels)

* Visible postings (but can still view their own applications even if visibility is off)

* Apply for internships (max 3 active applications)

* Prevented from duplicate applications

* View application history & statuses (Pending / Successful / Unsuccessful / Withdrawn / Confirmed)

* Accept one successful offer → automatically withdraws all others

* Request withdrawals (subject to Career Centre Staff approval)

* Configure Smart Recommendation Settings (keywords, level fit, major fit, closing-soon logic)

🏢 3. Company Representatives

* Register account (pending approval by Staff)

After approval:

* Create internships (max 5 postings)

* Edit internship details (before approval)

* Toggle posting visibility

* View applications for each internship

* Approve / Reject student applications

* Automatically updates slot counts & filled status

* View internship lifecycle: Pending → Approved → Filled

🧑‍💼 4. Career Centre Staff

* Approve / reject:

* Company Representative registrations

* Internship postings

* Student withdrawal requests

* Generate internship reports with filters:

* Status, Major, Level, Company, Closing Date, etc.

* Perform Batch Approvals

* Access Audit Logs and support Undo (via Command pattern)

* Manage system-wide consistency rules

🧩 Additional Features Implemented
Feature	Description
Smart Recommendation Engine	Ranks internships using student priorities (level match, major match, closing date urgency, keywords).
Waitlist Service	Students may join waitlists when slots are full.
Withdrawal Queue	Staff can review, approve, or deny withdrawal requests.
Strategy-based Sorting & Pagination	Sort by name, date, company; paginated view for readability.
Export Report Service	Export internship & application data to CSV.
Filter Persistence	Keeps user preferences across menus during the session.
Role-Based Access Control Guard	Ensures only authorized users execute sensitive actions.
DuplicateApplicationException	Stops duplicate submissions cleanly.
Undo & Audit Logging (Command Pattern)	Rollback previous admin operations; track all staff actions.
Pluggable Storage (DIP)	Swap between CsvStorage and future SerializedStorage.
EligibilityPolicy (Strategy)	Year/major-based rules can be swapped flexibly.
TablePrinter Utility	Produces clean, aligned CLI tables.
🧱 Architecture
BCE Layering
/boundary      → CLI menus (StudentMenuUI, CompanyRepMenuUI, StaffMenuUI)
/control       → Business logic (ApplicationControl, RecommendationService, Repo, CommandManager)
/entities      → Core data models (User, Student, Internship, Application, Enums)
/storage       → CsvStorage / Storage interface
/util          → Table formatting, comparators, policies, etc.

Key Concepts Used

Encapsulation: Entities keep fields private; access through controlled getters/setters.

Composition over Inheritance: Filters, recommendation settings, and policies composed instead of deep subclassing.

Loose Coupling: Controllers depend on abstractions (Storage, Repository, EligibilityPolicy, Command).

High Cohesion: Each class has a focused responsibility (SRP).

🧠 SOLID Principles in This Project
✔ S — Single Responsibility Principle

PasswordService handles hashing, strength checking, and validation only — no authentication, no I/O.

✔ O — Open/Closed Principle

MenuAction allows adding new actions without modifying existing menus.
Recommendation weights, sorting strategies, eligibility rules are all pluggable.

✔ L — Liskov Substitution Principle

User → Student / CompanyRep / CareerCentreStaff
All subclasses maintain the contract defined by User and can be used interchangeably by polymorphic flows.

✔ I — Interface Segregation Principle

Each menu role uses only the actions it needs:

Student-only actions

Rep-only actions

Staff-only actions

No “god interface”; all implement tiny MenuAction units.

✔ D — Dependency Inversion Principle

High-level modules depend on:

Storage

Repository

EligibilityPolicy

Command

WaitlistListener

All injected via constructors → easy to replace or extend.

📁 Repository Structure
SC2002/
│── src/
│   ├── boundary/
│   ├── control/
│   ├── entities/
│   ├── storage/
│   ├── util/
│   └── App.java
│
│── data/
│   ├── sample_student_list.csv
│   ├── sample_company_list.csv
│   └── sample_staff_list.csv
│
├── diagrams/
│   ├── uml_class_diagram.png
│   └── uml_sequence_diagram.png
│
├── docs/
│   └── javadoc/
│
├── README.md
└── report.pdf

🛠️ Setup & Running the Application
Prerequisites

Java 17+

Terminal / command prompt

To Run
cd src
javac App.java
java App

To Regenerate Javadoc
javadoc -d docs/javadoc -author -private -version $(find . -name "*.java")

🧪 Testing

Test cases are listed in the /tests section and follow:

Authentication

Data persistence

Application logic

Staff approval workflows

Filtering & sorting correctness

Recommendation ranking accuracy

Edge cases (duplicate apply, waitlists, visibility toggle, withdrawn state)

Refer to full test matrix inside report.
Source test cases are based on assignment sample inputs. 

SC2002 Assignment 2025S1 (1)

📝 Reflection Summary 

Learned to balance extensibility with simplicity

Experienced the benefits of composition, clean interfaces, and strategic pattern usage

Debugging CSV-based systems taught us the importance of logging & early testing

Gained strong understanding of maintainable OO design

Future improvements:

Event bus for Observer pattern

Automated JUnit testing

More robust error handling

🔗 Report & Deliverables

Full PDF Report (Design Considerations, UML, Sequence Diagram, Reflection)

Javadoc

Data files

Source code

Additional diagrams