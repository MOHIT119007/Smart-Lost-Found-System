# Software Requirements Specification (SRS)
## Smart Lost & Found System
**Standard:** IEEE Std 830-1998 / ISO/IEC/IEEE 29148  
**Department:** Department of Artificial Intelligence & Data Science  
**Institution:** Arya College of Engineering & Information Technology, Jaipur (RTU Kota)  
**Academic Year:** 2026–27 | B.Tech. AI & Data Science  

---

### 1. Introduction
#### 1.1 Purpose
This document specifies the software requirements of the **Smart Lost & Found System**, a desktop application for reporting, reviewing, and claiming lost and found belongings within a college campus.

#### 1.2 Scope
The system gives the campus a single, centralized record of lost and found items. Students register, log in, report lost and found items, and follow their status. Administrators verify reports, register items handed over at the campus desk, and view claimed items and registered users. Data is stored in a MySQL database accessed through JDBC. The system is a stand-alone Java Swing application for Windows.

---

### 2. User Roles & Permissions
* **Student:** Register, log in, report lost/found items, view own reports and approvals, accept or return an approved item, view claimed items.
* **Administrator:** Log in, view registered users, register desk found items, approve or reject lost and found reports, view claimed items.

---

### 3. Functional Requirements (FR-01 to FR-16)

| Req ID | Description | Priority |
| :--- | :--- | :---: |
| **FR-01** | Student self-registration with full name, college roll number/ID, 10-digit mobile number, college email, password, and confirmation. | High |
| **FR-02** | Validation rejection for empty fields, non-10-digit mobile numbers, malformed emails, unequal passwords, or duplicate emails. | High |
| **FR-03** | User authentication by email and password with role validation. | High |
| **FR-04** | Submission of lost-item reports with item name, category, location, date (YYYY-MM-DD), and contact phone. | High |
| **FR-05** | Submission of found-item reports with item name, category, found location, date, contact phone, and storage details. | High |
| **FR-06** | Validation of required fields, date formatting, and 10-digit contact numbers prior to persistence. | High |
| **FR-07** | Automatic assignment of `PENDING` status upon report creation. | High |
| **FR-08** | Administrator queue for reviewing pending lost and found reports with reporter contact details. | High |
| **FR-09** | Automated in-app notification when a report is approved, permitting the student to accept and claim the item. | High |
| **FR-10** | Direct registration of desk found items by campus administrators. | Medium |
| **FR-11** | Administrative view of all registered student profiles. | Medium |
| **FR-12** | Audit list of claimed items accessible to students (own items) and administrators (all items). | Medium |
| **FR-13** | Dynamic table filtering and refresh capabilities across all list views. | Low |
| **FR-14** | Rule-based lexical matching comparing lost and found records (`MatchingService.java`). | Medium |
| **FR-15** | Persistent ACID storage in MySQL with automated schema initialization on startup. | High |
| **FR-16** | Session termination and secure return to the login interface upon logout. | High |

---

### 4. Non-Functional Requirements
* **Usability:** Labeled fields, asterisk indicators for required inputs, and clear error dialogs.
* **Performance:** Screen transitions and table refreshes execute in under 1.5 seconds on standard college desktop hardware.
* **Reliability:** Graceful handling of database connectivity issues with descriptive user feedback dialogs.
* **Security:** 100% parameterized SQL via `PreparedStatement` to prevent SQL injection vulnerabilities.
* **Maintainability:** Modular separation across presentation, domain model, algorithmic service, and DAO layers.
* **Portability:** Self-contained executable execution on Windows 10/11 with zero client-side JDK or database installation requirements.
