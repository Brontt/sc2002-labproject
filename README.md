📌 Internship Placement Management System
### *SC2002 Object-Oriented Programming — AY25/26*
<p align="center">
  <img src="https://img.shields.io/badge/Java-17-red">
  <img src="https://img.shields.io/badge/Status-Completed-brightgreen">
  <img src="https://img.shields.io/badge/Architecture-BCE-blue">
</p>
---

## 👥 Group Members

| Name                     | 
| ------------------------ | 
| **Tan Shi Ya Shianne**   | 
| **Tham En Yi**           | 
| **Kuek Pei Shan**        | 
| **Goh Jun Xian, Bryant** | 

---

# 📝 Project Overview

This repository contains our CLI-based **Internship Placement Management System**, built for the SC2002 OOP assignment.
The system supports **Students**, **Company Representatives**, and **Career Centre Staff**, with persistent CSV data storage, modular architecture, and extensible features implemented using **SOLID principles** and **design patterns**.

---

# 🗂 Features by User Role

## 🔐 All Users

* Login & logout
* Change password
* Default password: `password`
* Receive notifications (Inbox)
* Session-persistent filter settings

---

## 🎓 Students

* View internships filtered by:

  * Major match
  * Year eligibility
  * Internship level
  * Visibility
* Apply for up to **3 internships**
* Prevented from duplicate applications
* View application statuses (Pending / Successful / Unsuccessful / Withdrawn / Confirmed)
* Accept one offer → auto-withdraw others
* Request withdrawal (staff approval required)
* Configure **Smart Recommendation Engine** (keywords, level fit, major fit, closing soon)

---

## 🏢 Company Representatives

* Register account (requires staff approval)
* Create, edit (before approval), and manage internship postings
* Toggle visibility
* View all applications per internship
* Approve / reject applications
* Manage slots and filled status accurately

---

## 🧑‍💼 Career Centre Staff

* Approve / reject:

  * Company Representative accounts
  * Internship postings
  * Withdrawal requests
* Generate reports with filters (level, major, status, company, etc.)
* Perform **Batch Approvals**
* View **Audit Logs**
* Undo actions using **Command Pattern**

---

# 🧩 Additional Features Implemented

| Feature                                     | Description                                                                      |
| ------------------------------------------- | -------------------------------------------------------------------------------- |
| **Smart Recommendation Engine**             | Ranks internships by student preferences (level, major, keywords, closing date). |
| **Waitlist Service**                        | Manages waitlist for full internships.                                           |
| **Withdrawal Queue**                        | Staff-reviewable withdrawal processing.                                          |
| **Sorting & Pagination (Strategy Pattern)** | Sort by name/date/company, with page navigation.                                 |
| **ExportReportService**                     | CSV export for staff reports.                                                    |
| **Filter Persistence**                      | Saves filters across menus.                                                      |
| **DuplicateApplicationException**           | Prevents repeated applications.                                                  |
| **Undo + Audit Logs**                       | Admin actions reversible with traceability.                                      |
| **Role-Based Access Control**               | Secure access to restricted operations.                                          |
| **Pluggable Storage (DIP)**                 | Easily switch between CsvStorage and future Storage types.                       |
| **EligibilityPolicy (Strategy)**            | Different policies for year/major restrictions.                                  |
| **TablePrinter Utility**                    | Clean CLI table formatting.                                                      |

---

# 🧱 Architecture Overview

```
src/
│── boundary/     → Menu UIs (Student, Rep, Staff)
│── control/      → Business logic, Recommendation, Repos, Commands
│── entities/     → User, Internship, Application, Enums
│── storage/      → CsvStorage, Storage interface
│── util/         → Helpers, comparators, policies
└── App.java      → Main entry
```

---

# 🧠 SOLID Principles Applied

## ✔ Single Responsibility Principle (SRP)

`PasswordService` handles **only** hashing + validation — no I/O, no user logic.

## ✔ Open/Closed Principle (OCP)

`MenuAction` lets us add new menu actions **without modifying existing code**.

## ✔ Liskov Substitution Principle (LSP)

`User` → `Student`, `CompanyRep`, `CareerCentreStaff`
All subclasses honor the base behavior and can replace one another safely.

## ✔ Interface Segregation Principle (ISP)

Role menus depend only on the **actions they need**, not a large monolithic interface.

## ✔ Dependency Inversion Principle (DIP)

High-level modules depend on **interfaces**, not implementations:
`Storage`, `Repository`, `EligibilityPolicy`, `Command`, etc., are constructor-injected.

---

# 🧪 Testing

Comprehensive test cases include:

* Authentication
* Data persistence
* Application workflow
* Approval/withdrawal logic
* Filtering & sorting
* Recommendation accuracy
* Role-based access guard
* Error handling & edge cases

---

# 📝 Reflection

* Learned to balance extensibility vs. over-engineering
* Understood the power of **composition**, **clear interfaces**, and **design patterns**
* CSV debugging reinforced the value of early testing and logging
* Stronger grasp of maintainable, modular OO design

