# Smart Lost & Found System
## 12-Week Project Plan (6 July – 27 September 2026)
### Java PBL A.Y. 2026-27 | Desktop Application Track

---

## Team Members

| Name | Roll No. | Module |
|------|----------|--------|
| Mohit Kumar Sharma | 24EARAD103 | Database & DAO Layer |
| Prabhash Kumar Chaudhary | 24EARAD121 | Authentication & User Management |
| Mitesh Nehra | 24EARAD102 | Student Panel (UI + Logic) |
| Naitik Pathak | 24EARAD106 | Admin Panel (UI + Logic) |
| Paramveer Singh | 24EARAD119 | Testing, Integration & Documentation |

> 📅 Saturday = Mentor Meeting (All members attend) | ☀️ Sunday = Review / Self-Study

---
---

# 👤 MOHIT KUMAR SHARMA — 24EARAD103
## Module: Database & DAO Layer

| Day | Date | My Work for the Day |
|-----|------|----------------------|
| **WEEK 1 — Team Formation + Abstract** |
| Mon | 6 Jul | I registered our team on the project portal today and selected our Project Guide. I also set up our GitHub repository and created the basic Maven project folder structure (`src/`, `database/`, `docs/`). |
| Tue | 7 Jul | I spent time understanding the problem — what data we need to store (users, lost items, found items), who uses the system (Admin and Student), and what each role can do. I noted all this in a rough planning document. |
| Wed | 8 Jul | I researched existing lost & found systems online to understand what features are commonly included. I made a simplified feature list suited to our college project scope. |
| Thu | 9 Jul | I drafted the Project Abstract (200–300 words) covering the problem statement, our proposed solution, tech stack (Java, Swing, SQLite, Maven), and expected outcome. I shared it with the team for review. |
| Fri | 10 Jul | I finalized the abstract based on team feedback and made my first meaningful GitHub commit — initialized Maven project with correct folder structure and added the abstract to `docs/`. |
| **SAT** | **11 Jul** | **Mentor Meeting — I presented our project idea and abstract to the mentor. I noted all feedback and updated our weekly tracking sheet. GitHub commit: tracking sheet.** |
| Sun | 12 Jul | I reviewed mentor's feedback on the abstract. I planned the database table design — which tables are needed and what columns each should have. |
| **WEEK 2 — SRS Documentation** |
| Mon | 13 Jul | I wrote SRS Section 1 (Introduction) — Purpose, Scope, Definitions, and Overview of the Smart Lost & Found System following IEEE SRS format. |
| Tue | 14 Jul | I wrote SRS Section 2 (Overall Description) — product perspective (standalone desktop app), user classes (Admin, Student), and operating environment (Windows, JDK 8+, SQLite). |
| Wed | 15 Jul | I wrote the database-specific functional requirements for SRS — listed all tables needed (`users`, `lost_items`, `found_items`) and defined the status values: PENDING, APPROVED, REJECTED, CLAIMED. |
| Thu | 16 Jul | I wrote use-case descriptions for all DB operations — insertUser, insertLostItem, updateStatus. I included preconditions and postconditions for each. |
| Fri | 17 Jul | I reviewed the complete SRS with all team members, checked that everyone's module requirements are included, and made final corrections. I pushed the updated SRS to GitHub. |
| **SAT** | **18 Jul** | **Mentor Meeting — I presented the SRS to our mentor and got approval. I noted all corrections and updated the tracking sheet on GitHub.** |
| Sun | 19 Jul | I incorporated mentor's SRS feedback and finalized the document. I planned the detailed DB schema design for next week. |
| **WEEK 3 — UML Design** |
| Mon | 20 Jul | I created the ER Diagram today — defined entities (USERS, LOST_ITEMS, FOUND_ITEMS), primary keys, foreign keys, attributes, and relationships between them using draw.io. |
| Tue | 21 Jul | I contributed to the team Class Diagram — added `User`, `LostItem`, `FoundItem` model classes with all fields and key methods. Also added `UserDAO`, `LostItemDAO`, `FoundItemDAO`, `DatabaseConnection` classes. |
| Wed | 22 Jul | I created the Sequence Diagram for "Report Lost Item" — showing the flow: Student → StudentDashboard → LostItemDAO → SQLite DB. I reviewed other members' sequence diagrams for consistency. |
| Thu | 23 Jul | I reviewed all UML diagrams to ensure class names in diagrams match the actual Java class names we will use in code. I made corrections wherever names didn't match. |
| Fri | 24 Jul | I exported my ER Diagram and Class Diagram as PNG files and pushed them to `docs/UML/` on GitHub. I also helped Paramveer compile all diagrams into one PDF. |
| **SAT** | **25 Jul** | **Mentor Meeting — I presented the ER Diagram and Class Diagram to the mentor. Got approval. Updated tracking sheet on GitHub.** |
| Sun | 26 Jul | I reviewed the mentor's UML feedback. Adjusted the ER Diagram — added missing NOT NULL constraints and corrected the relationship between LOST_ITEMS and USERS. |
| **WEEK 4 — DB Design + UI Mockups** |
| Mon | 27 Jul | I wrote `database/database.sql` today — created the `users` table with columns: id, name, email, password, role, phone, roll_no. Defined all data types and constraints (NOT NULL, UNIQUE on email). |
| Tue | 28 Jul | I wrote the `lost_items` table in `database.sql` — columns: id, student_id (FK to users), item_name, description, location, date_lost, status. Tested the CREATE statement in SQLite Browser. |
| Wed | 29 Jul | I wrote the `found_items` table and added the default admin user seed data (`INSERT INTO users`). I tested the entire `database.sql` script in SQLite Browser — all tables created correctly. |
| Thu | 30 Jul | I wrote `DatabaseConnection.java` today — singleton class that establishes a JDBC SQLite connection, calls `initializeTables()` on first launch, and handles connection errors with try-catch. |
| Fri | 31 Jul | I tested `DatabaseConnection.java` by running a standalone Java program — confirmed the SQLite DB file is created, all tables exist, and the default admin is inserted correctly on first run. |
| **SAT** | **1 Aug** | **Mentor Meeting — I showed the DB schema and DatabaseConnection.java to the mentor. Got approval on the table design. Updated tracking sheet.** |
| Sun | 2 Aug | I reviewed DB feedback and planned the writing of all DAO classes starting next week. I also wrote brief inline comments for all SQL queries. |
| **WEEK 5 — Module Coding Phase 1** |
| Mon | 3 Aug | I wrote `UserDAO.java` today — implemented the `insertUser(User u)` method using PreparedStatement. Added proper SQLException handling and a message if the insert fails. |
| Tue | 4 Aug | I wrote `getUserByEmail(String email)` in `UserDAO.java` — maps the ResultSet to a `User` object and returns null if no matching user is found. |
| Wed | 5 Aug | I wrote `getAllUsers()` in `UserDAO.java` — returns a `List<User>` of all registered students, filtered by role (excluding admin). |
| Thu | 6 Aug | I wrote `LostItemDAO.java` — implemented `insertLostItem(LostItem item)`, `getAllLostItems()`, and `getPendingLostItems()` methods with PreparedStatements. |
| Fri | 7 Aug | I wrote `updateStatus(int id, String status)` and `getLostItemsByStudentId(int studentId)` in `LostItemDAO.java`. Tested both methods manually with sample data. |
| **SAT** | **8 Aug** | **Mentor Meeting — I showed UserDAO and LostItemDAO code to the mentor. Demonstrated DB operations working correctly. Updated tracking sheet.** |
| Sun | 9 Aug | I tested all DAO methods written so far using a standalone test class with sample inserts. Fixed two SQL errors in the status update query. Pushed fixes to GitHub. |
| **WEEK 6 — Module Coding Phase 2** |
| Mon | 10 Aug | I wrote `FoundItemDAO.java` — implemented `insertFoundItem()`, `getPendingFoundItems()`, and `getAllFoundItems()` methods. |
| Tue | 11 Aug | I wrote `updateStatus()` and `getFoundItemsByStudentId()` in `FoundItemDAO.java`. All FoundItemDAO methods are now complete and tested. |
| Wed | 12 Aug | I added a notification query today — wrote `getNotificationsByStudentId(int studentId)` which fetches lost items with status APPROVED for a specific student. These appear as notifications in the student panel. |
| Thu | 13 Aug | I wrote `updateNotificationResponse()` — when the student accepts, status becomes CLAIMED; when rejected, status becomes REJECTED. Tested DB state after both actions. |
| Fri | 14 Aug | I ran a full DAO test — inserted a lost item, approved it, fetched it as a notification, accepted it, and verified the CLAIMED status in the DB. All steps worked correctly. |
| **SAT** | **15 Aug** | **Mentor Meeting — I demonstrated all DAO methods working with real test data. Mentor approved the complete DAO layer. Updated tracking sheet.** |
| Sun | 16 Aug | I fixed two bugs found during this week's testing — one in the getLostItemsByStudentId query and one in the claimed status filter. Pushed corrected DAO files. |
| **WEEK 7 — Module Coding Phase 3** |
| Mon | 17 Aug | I wrote `getClaimedItemsByStudentId(int studentId)` today. I also helped Mitesh connect the student panel "My Claimed Items" section to load from this method. |
| Tue | 18 Aug | I helped Naitik connect the admin "Lost Items Approval" section to `LostItemDAO.getPendingLostItems()`. Debugged a data-loading issue where approved items were still appearing — fixed the query filter. |
| Wed | 19 Aug | I helped Mitesh wire the "Notifications" panel to `getNotificationsByStudentId()`. Verified the correct student ID is passed from SessionManager to the query. |
| Thu | 20 Aug | I added `try-with-resources` to all DAO methods today for proper closing of connections, statements, and result sets. This prevents memory leaks in long-running sessions. |
| Fri | 21 Aug | I added input validation in all DAO methods — null and empty string checks before executing any INSERT. Threw `IllegalArgumentException` for invalid inputs. |
| **SAT** | **22 Aug** | **Mentor Meeting — I showed the complete tested DAO layer to the mentor. Mentor gave sign-off on my module. Updated tracking sheet.** |
| Sun | 23 Aug | I finalized and froze the DAO code. Updated GitHub README with DB setup instructions — how to run database.sql and where the SQLite file is created. |
| **WEEK 8 — Module Coding Phase 4: Final** |
| Mon | 24 Aug | I ran the DB with 15+ sample records and tested all queries for performance. Added an index on `student_id` and `status` columns to speed up filtering queries. |
| Tue | 25 Aug | I wrote `ResetCleanDB.java` — a utility class that drops all tables and recreates them with fresh seed data. This will be useful during demos and testing. |
| Wed | 26 Aug | I finalized `database.sql` — ensured all CREATE TABLE statements, constraints, indexes, and seed data are correct and complete. This is the final schema file. |
| Thu | 27 Aug | I did a final review of all DAO files — checked for consistent method naming, no unused methods, and complete Javadoc comments on all public methods. |
| Fri | 28 Aug | I pushed the final DAO code to GitHub with commit message: "DAO layer complete - v1.0". No more structural changes to DAO files after this. |
| **SAT** | **29 Aug** | **Mentor Meeting — I showed the complete finalized application to the mentor with all modules integrated. Mentor confirmed DB module is complete. Updated tracking sheet.** |
| Sun | 30 Aug | I reviewed any remaining DB issues and prepared notes to support integration testing next week. Helped Paramveer update the project README. |
| **WEEK 9 — Integration + Testing** |
| Mon | 31 Aug | I ran the full E2E flow today with all modules together — student registers, reports a lost item, data enters the DB, and the admin can see it. Fixed one FK constraint issue found during the test. |
| Tue | 1 Sep | I tested the complete approval chain in the DB — verified PENDING → APPROVED → CLAIMED status changes are correctly persisted at each step. |
| Wed | 2 Sep | I tested edge cases — rejecting an already-rejected item, a student reporting 3 items at once. All were handled gracefully with no DB errors. |
| Thu | 3 Sep | I fixed all integration-level DB bugs found this week — incorrect status filter in one query and a FK error when deleting test data. |
| Fri | 4 Sep | I ran the DB Browser after the full E2E test — verified all records have the correct final status. No orphan records, no null foreign keys. |
| **SAT** | **5 Sep** | **Mentor Meeting — I demonstrated the fully integrated system to the mentor. All modules working together. Updated tracking sheet.** |
| Sun | 6 Sep | I reviewed integration feedback. Planned any remaining fixes for system testing week. Checked GitHub commit count — on track with 3+ commits/week. |
| **WEEK 10 — System Testing + Final Build** |
| Mon | 7 Sep | I ran the DB with 20+ records today. Verified all DAO queries perform within 1 second. No performance issues found. |
| Tue | 8 Sep | I tested DB persistence — closed the app and reopened it. All data persisted correctly. Verified the default admin is inserted only once and not duplicated on restart. |
| Wed | 9 Sep | I added a UNIQUE constraint on the user email column at the DB level as a final safety measure. Tested it rejects duplicate emails even if code-level check is bypassed. |
| Thu | 10 Sep | I declared DB code freeze today. Tagged the final commit as "DB-Final-v1.0" on GitHub. No more structural changes to any DAO or DB file. |
| Fri | 11 Sep | I helped Paramveer test the final JAR — verified that all DB operations (insert, update, select) work correctly in the packaged application. |
| **SAT** | **12 Sep** | **Mentor Meeting — I presented final system test results. Mentor gave final sign-off on the Database module. Updated tracking sheet.** |
| Sun | 13 Sep | I reviewed all my GitHub commits across 12 weeks — confirmed 3+ commits every week. Pushed any missing documentation-only commits. |
| **WEEK 11 — Project Report + PPT + Video** |
| Mon | 14 Sep | I wrote the Module 1 documentation section — DB Design explanation, ER Diagram description, explanation of the DAO pattern, and a list of all DAO methods with their purpose. |
| Tue | 15 Sep | I created the final high-quality ER Diagram and wrote a detailed explanation of each table, column, and relationship for the project report. |
| Wed | 16 Sep | I wrote the DB Implementation section for the report — explained DatabaseConnection, DAO pattern, use of PreparedStatement, and connection management. Added actual code snippets. |
| Thu | 17 Sep | I proofread my DB section of the report. Fixed two technical errors and sent the finalized section to Paramveer for inclusion in the master document. |
| Fri | 18 Sep | I prepared 3 PPT slides: Slide 1 — ER Diagram with explanation. Slide 2 — DAO Architecture diagram. Slide 3 — Key SQL queries used in the project. |
| **SAT** | **19 Sep** | **Mentor Meeting — I submitted the project report draft to the mentor along with my PPT slides. Noted final feedback. Updated tracking sheet.** |
| Sun | 20 Sep | I recorded a 2-minute video clip — explained the DB module by showing the ER diagram, explaining DAO pattern, and demonstrating a query running live. Sent clip to Paramveer. |
| **WEEK 12 — Final Documentation Polish, Code Freeze & Project Completion** |
| Mon | 21 Sep | I incorporated the mentor's final report feedback and finalized my DB section. Sent the corrected section to Paramveer for the final report. |
| Tue | 22 Sep | I conducted a complete SQL query and code audit across `DatabaseConnection.java` and all DAO classes — verified proper connection closing, parameterized queries, and consistent error handling. |
| Wed | 23 Sep | I tested the database initialization script (`database.sql`) and sample seed records on a fresh machine to ensure 100% zero-configuration auto-setup. |
| Thu | 24 Sep | I assisted the team in cross-module data validation, checking that SQLite foreign keys and cascading status updates execute cleanly under multiple operations. |
| Fri | 25 Sep | I did a final code cleanup — removed all debug print statements, added remaining Javadoc comments, and pushed the final commit: "Final Deliverable - DB Module". |
| **SAT** | **26 Sep** | **Mentor Meeting — I participated in the final project review meeting with our mentor and verified that all database deliverables and documentation are approved.** |
| **Sun** | **27 Sep** | 🚀 **FINAL PROJECT COMPLETION & DELIVERABLES READY — Completed all final project deliverables for the Database & DAO Module. Verified full database packaging, code freeze, and 100% project readiness.** |

---
---

# 👤 PRABHASH KUMAR CHAUDHARY — 24EARAD121
## Module: Authentication & User Management

| Day | Date | My Work for the Day |
|-----|------|----------------------|
| **WEEK 1 — Team Formation + Abstract** |
| Mon | 6 Jul | I joined the team today and set up my GitHub account. I joined the team repository created by Mohit. My module is Authentication — Login, Register, Session Management, and Role-based Access. |
| Tue | 7 Jul | I studied the project idea carefully — understanding how login works differently for Admin and Student. I noted all the fields needed at registration: email, password, name, roll number, phone. |
| Wed | 8 Jul | I researched Java Swing login screen best practices — how to use JPasswordField for masked password input, JOptionPane for error dialogs, and proper input validation logic. |
| Thu | 9 Jul | I wrote 2–3 sentences about the authentication module for the project abstract and shared them with Mohit. I also discussed with the team about what fields the login and register screens need. |
| Fri | 10 Jul | I created placeholder files — `LoginFrame.java`, `RegisterFrame.java`, and `User.java` — and made my first GitHub commit. I also explored the Maven folder structure Mohit set up. |
| **SAT** | **11 Jul** | **Mentor Meeting — I attended the full team meeting with the mentor. I noted all authentication-related feedback from the mentor. Helped update the tracking sheet.** |
| Sun | 12 Jul | I reviewed the mentor's feedback and read about the Maven project structure. I learned how to add my files in the correct package path under `src/main/java/`. |
| **WEEK 2 — SRS Documentation** |
| Mon | 13 Jul | I wrote the functional requirements for my auth module: FR-Auth-01: Register with name/email/password/roll no. FR-Auth-02: Email format must be validated. FR-Auth-03: Duplicate emails must be rejected. |
| Tue | 14 Jul | I wrote more FRs: FR-Auth-04: Login with email and password. FR-Auth-05: System redirects to Admin or Student dashboard based on role. FR-Auth-06: User can logout from any panel. |
| Wed | 15 Jul | I wrote non-functional requirements for my module — login response under 2 seconds, password not displayed in plain text, session maintained in memory during runtime. |
| Thu | 16 Jul | I wrote detailed use-case descriptions for "Login" and "Register" — including actor, preconditions, main flow, alternate flows (wrong password, duplicate email), and postconditions. |
| Fri | 17 Jul | I finalized my auth section of the SRS and submitted it to Paramveer for inclusion in the master document. I pushed the updated SRS to GitHub. |
| **SAT** | **18 Jul** | **Mentor Meeting — I attended and presented my auth requirements to the mentor. Noted feedback. Helped update the tracking sheet.** |
| Sun | 19 Jul | I incorporated mentor's SRS corrections. I also planned the LoginFrame and RegisterFrame UI layouts for the upcoming design week. |
| **WEEK 3 — UML Design** |
| Mon | 20 Jul | I created the Use-Case Diagram for my module today — actors: Student and Admin. Use cases: Register, Login, Logout. Added includes/extends relationships using draw.io. |
| Tue | 21 Jul | I added `LoginFrame`, `RegisterFrame`, and `SessionManager` to the team's Class Diagram. I showed their methods: `validateLogin()`, `registerUser()`, `logout()`, `getCurrentUser()`. |
| Wed | 22 Jul | I created an Activity Diagram for the Login Flow — Start → Enter Credentials → Validate → If Admin: open Admin Dashboard → If Student: open Student Dashboard → End. |
| Thu | 23 Jul | I created an Activity Diagram for the Register Flow — Fill Form → Validate Email Format → Check for Duplicate → Save to DB → Show Success Message → Redirect to Login. |
| Fri | 24 Jul | I exported all my auth UML diagrams as PNG and pushed them to `docs/UML/auth/` on GitHub. I also sent them to Paramveer for the combined UML PDF. |
| **SAT** | **25 Jul** | **Mentor Meeting — I presented my auth UML diagrams to the mentor. I adjusted the Activity Diagram based on feedback. Updated tracking sheet.** |
| Sun | 26 Jul | I reviewed mentor feedback and corrected my diagrams. I planned the LoginFrame and RegisterFrame implementation for next week. |
| **WEEK 4 — DB Design + UI Mockups** |
| Mon | 27 Jul | I wrote `User.java` model class today — added all private fields (id, name, email, password, role, phone, rollNo), parameterized and default constructors, getters, setters, and `toString()`. |
| Tue | 28 Jul | I designed the Login screen mockup — email field, password field with masking, Login button, Register link at bottom, and app title at top. Chose dark navy background with blue button colors. |
| Wed | 29 Jul | I designed the Register screen mockup — fields: Full Name, Email, Password, Confirm Password, Phone, Roll Number, Register button, and Back to Login link. |
| Thu | 30 Jul | I started `LoginFrame.java` — extended JFrame, set title and size, added JLabel for title, JTextField for email, JPasswordField for password, and JButton for login. Used GridBagLayout for alignment. |
| Fri | 31 Jul | I applied UITheme styling to `LoginFrame.java` — dark navy background `#0A0E27`, blue buttons `#2563EB`, white text. The login screen is now visually consistent with the team's design. |
| **SAT** | **1 Aug** | **Mentor Meeting — I showed the LoginFrame skeleton and RegisterFrame mockup to the mentor. Got positive feedback on the layout. Updated tracking sheet.** |
| Sun | 2 Aug | I reviewed the design feedback and planned to complete login logic starting next week. |
| **WEEK 5 — Module Coding Phase 1** |
| Mon | 3 Aug | I completed the LoginFrame UI today — all components placed correctly. Added Login button and "Don't have an account? Register" link label at the bottom. |
| Tue | 4 Aug | I coded the Login button ActionListener — reads email from JTextField, reads password from JPasswordField using `getPassword()`, and calls `UserDAO.getUserByEmail(email)`. |
| Wed | 5 Aug | I added password matching logic — compares the entered password with the stored one. Shows a JOptionPane error dialog with a clear message if the credentials don't match. |
| Thu | 6 Aug | I added role-check logic after successful login — if `user.getRole().equals("ADMIN")`, opens the admin dashboard; otherwise opens the student dashboard. LoginFrame is then closed. |
| Fri | 7 Aug | I started `RegisterFrame.java` — added all input fields (Full Name, Email, Password, Confirm Password, Phone, Roll Number) and styled them to match the team's UITheme. |
| **SAT** | **8 Aug** | **Mentor Meeting — I demonstrated the login flow live — correct credentials and wrong password scenarios. Mentor was satisfied. Updated tracking sheet.** |
| Sun | 9 Aug | I tested the complete login flow and fixed one bug where the student dashboard was opening even for admin. Pushed the fix to GitHub. |
| **WEEK 6 — Module Coding Phase 2** |
| Mon | 10 Aug | I completed the Register button logic — checks for duplicate email using `UserDAO.getUserByEmail()`. If email exists, shows "Email already registered." If new, calls `UserDAO.insertUser()`. |
| Tue | 11 Aug | I added post-registration flow — on success, shows "Registration Successful" dialog and opens LoginFrame. On failure, shows a specific error message to the user. |
| Wed | 12 Aug | I implemented `SessionManager.java` today — a static class with `currentUser` field and three methods: `setCurrentUser()`, `getCurrentUser()`, and `clearUser()`. |
| Thu | 13 Aug | I implemented the Logout feature — `SessionManager.clearUser()` clears the session, the current dashboard is disposed, and LoginFrame is reopened. Both student and admin dashboards use this. |
| Fri | 14 Aug | I added a "Show Password" toggle checkbox in LoginFrame — toggles between dots and visible characters using `setEchoChar('\0')` and `setEchoChar('•')`. Tested it works smoothly. |
| **SAT** | **15 Aug** | **Mentor Meeting — I demonstrated the complete auth module — register, login, role-check, session, logout. Mentor approved the module. Updated tracking sheet.** |
| Sun | 16 Aug | I fixed one minor bug — the "Show Password" checkbox was not resetting when the login screen reopened. Fixed and pushed. |
| **WEEK 7 — Module Coding Phase 3** |
| Mon | 17 Aug | I added full form validation to RegisterFrame today — email format check using regex, Roll Number format validation (must match `24EARAD\d{3}`), and phone number validation (exactly 10 digits). |
| Tue | 18 Aug | I added inline field validation feedback — when a field is invalid, its border turns red. When the user fixes it and moves to the next field, the border resets to normal. |
| Wed | 19 Aug | I tested edge cases — login with extra spaces in email, register with only spaces in the name field. Both are now handled gracefully with specific, clear error messages. |
| Thu | 20 Aug | I wrote Javadoc comments for all public methods in `LoginFrame.java`, `RegisterFrame.java`, `User.java`, and `SessionManager.java`. Cleaned up all unused imports. |
| Fri | 21 Aug | I tested duplicate registration 3 times in a row with the same email. Verified only one user is ever saved and the error message shows correctly each time. |
| **SAT** | **22 Aug** | **Mentor Meeting — I showed the polished auth module with all validations working. Mentor gave final sign-off on Module 2. Updated tracking sheet.** |
| Sun | 23 Aug | I froze the auth module code. Pushed all final files. No more changes to auth code from this point. |
| **WEEK 8 — Module Coding Phase 4: Final** |
| Mon | 24 Aug | I supported Mitesh today — verified that `SessionManager.getCurrentUser()` works correctly in the student panel, making sure student-specific data loads for the logged-in student only. |
| Tue | 25 Aug | I supported Naitik — verified that the admin's name displays correctly in the admin dashboard header using `SessionManager.getCurrentUser().getName()`. |
| Wed | 26 Aug | I tested edge cases like logging in and out 5 times in a row, registering 10 different students sequentially. All worked without any session or state issues. |
| Thu | 27 Aug | I did a final code review of all auth files — consistent naming conventions, no raw `System.out.println()` calls, complete exception handling throughout. |
| Fri | 28 Aug | I pushed the final auth code to GitHub with commit message: "Auth module complete - v1.0". Auth code is now frozen. |
| **SAT** | **29 Aug** | **Mentor Meeting — I participated in the full application demo with all modules. Mentor confirmed auth is fully complete and integrated. Updated tracking sheet.** |
| Sun | 30 Aug | I reviewed all GitHub commits to ensure I have 3+ commits per week. Pushed a documentation-only commit for missing inline comments. |
| **WEEK 9 — Integration + Testing** |
| Mon | 31 Aug | I ran integration tests on the auth module — verified that the student logged in via my LoginFrame can correctly see their own data in Mitesh's student panel using the session. |
| Tue | 1 Sep | I tested admin login → admin panel, student login → student panel, and confirmed that logout from both panels returns to my LoginFrame correctly. |
| Wed | 2 Sep | I helped Paramveer run auth test cases TC-01 to TC-10. All passed. Fixed one minor bug where the error dialog appeared twice on wrong password. |
| Thu | 3 Sep | I tested "back button" behavior — pressing Back on the Register screen correctly returns to the Login screen. Closing and reopening the app shows no leftover session data. |
| Fri | 4 Sep | I confirmed that the session is fully cleared on logout — a fresh login always works without any ghost data from the previous session. |
| **SAT** | **5 Sep** | **Mentor Meeting — I participated in the full integration demo. Auth working perfectly with all other modules. Updated tracking sheet.** |
| Sun | 6 Sep | I reviewed any remaining integration issues and confirmed all auth-related scenarios are fully resolved. |
| **WEEK 10 — System Testing + Final Build** |
| Mon | 7 Sep | I ran a system test with 5 different student accounts — registered all, logged in as each, submitted items, and logged out. Each student only saw their own data. |
| Tue | 8 Sep | I tested role-based access — verified that an admin account cannot access the report form and a student account cannot access admin sections, even programmatically. |
| Wed | 9 Sep | I helped Paramveer test the packaged JAR — verified that login and registration work correctly in the final build on a clean machine. |
| Thu | 10 Sep | I tagged the final auth commit as "Auth-Final-v1.0" on GitHub. Auth code is completely frozen from this point. |
| Fri | 11 Sep | I wrote the auth section of the project report — explaining the login flow, registration validation rules, role-based access, and session management. |
| **SAT** | **12 Sep** | **Mentor Meeting — Mentor gave final system-level sign-off. All auth test cases passed. Updated tracking sheet.** |
| Sun | 13 Sep | I reviewed all GitHub commits across 12 weeks. Confirmed consistent 3+ commits/week. Pushed any missing documentation commits. |
| **WEEK 11 — Project Report + PPT + Video** |
| Mon | 14 Sep | I wrote the Module 2 documentation section for the project report — Login flow explanation, registration validation logic, role-based access, and SessionManager design. |
| Tue | 15 Sep | I created a Login Flow Diagram (flowchart) showing each decision point and added a code snippet of the key login validation logic as an example. |
| Wed | 16 Sep | I wrote the Auth Implementation section — explained LoginFrame, RegisterFrame, and SessionManager with actual code snippets and inline explanations. |
| Thu | 17 Sep | I proofread my auth section of the report, corrected technical inaccuracies, and submitted the finalized section to Paramveer. |
| Fri | 18 Sep | I prepared 3 PPT slides: Slide 1 — Login screen screenshot with explanation. Slide 2 — Register screen with validation rules. Slide 3 — Role-based access flowchart. |
| **SAT** | **19 Sep** | **Mentor Meeting — I attended and noted final feedback on the report and PPT. Helped Paramveer submit the draft to the mentor.** |
| Sun | 20 Sep | I recorded a 2-minute video clip — demonstrated registering a new student, logging in, showing the student dashboard, logging out, logging in as admin, and showing the admin dashboard. Sent to Paramveer. |
| **WEEK 12 — Final Documentation Polish, Code Freeze & Project Completion** |
| Mon | 21 Sep | I incorporated the mentor's final feedback and finalized my auth section in the project report. |
| Tue | 22 Sep | I performed a comprehensive security and input-validation audit on `LoginFrame.java` and `RegisterFrame.java` — verified password masking, regex checks, and duplicate email prevention. |
| Wed | 23 Sep | I tested session management and logout behaviors across multiple login/logout cycles to ensure user credentials and session states are cleanly wiped with no leftover data. |
| Thu | 24 Sep | I collaborated with Mitesh and Naitik to verify role-based access control, ensuring students cannot access admin routes and admin credentials redirect cleanly to the Admin panel. |
| Fri | 25 Sep | I did a final code cleanup — ensured all auth files have complete Javadoc comments and clean exception handling. Pushed final commit: "Final Deliverable - Auth Module". |
| **SAT** | **26 Sep** | **Mentor Meeting — I participated in the final review meeting with our mentor. Demonstrated complete auth flow and got final mentor sign-off on the Auth module.** |
| **Sun** | **27 Sep** | 🚀 **FINAL PROJECT COMPLETION & DELIVERABLES READY — Completed all final project deliverables for the Authentication Module. Verified login/register UI, session management logic, and 100% project readiness.** |

---
---

# 👤 MITESH NEHRA — 24EARAD102
## Module: Student Panel (UI + Logic)

| Day | Date | My Work for the Day |
|-----|------|----------------------|
| **WEEK 1 — Team Formation + Abstract** |
| Mon | 6 Jul | I joined the team today and understood my module — the complete Student Panel. I will build everything the student sees and interacts with: Report Lost, Report Found, My Reports, Notifications, and My Claimed Items. |
| Tue | 7 Jul | I studied the student's journey in detail — they register, log in, report a lost item, wait for admin to approve it, get a notification, accept or reject it, and then see the item in their claimed list. |
| Wed | 8 Jul | I researched Java Swing dashboard layouts — how to create a sidebar with CardLayout for switching between multiple panels. I noted useful components: JTable, JScrollPane, JTextField, DefaultTableCellRenderer. |
| Thu | 9 Jul | I wrote 2–3 sentences about the student panel for the project abstract and shared them with Mohit. I discussed with the team what sections the student panel needs. |
| Fri | 10 Jul | I created `StudentDashboard.java` and `LostItem.java` placeholder files and made my first GitHub commit to the team repository. |
| **SAT** | **11 Jul** | **Mentor Meeting — I attended the full team meeting. I noted the mentor's feedback specifically about what student features are expected. Helped update the tracking sheet.** |
| Sun | 12 Jul | I reviewed student panel requirements. I finalized a list of 5 sections I need to build: Report Lost, Report Found, My Reports, Notifications, My Claimed Items. |
| **WEEK 2 — SRS Documentation** |
| Mon | 13 Jul | I wrote the functional requirements for my module: FR-STU-01: Student reports a lost item with name, description, location, and date. FR-STU-02: Student reports a found item with the same fields. |
| Tue | 14 Jul | I wrote FR-STU-03: Student views all their submitted reports with current status. FR-STU-04: Student receives notifications for admin-approved lost items. |
| Wed | 15 Jul | I wrote FR-STU-05: Student accepts or rejects the notification. FR-STU-06: Accepted item appears in "My Claimed Items." FR-STU-07: Student can logout from the panel. |
| Thu | 16 Jul | I wrote use-case descriptions for "Report Lost Item" and "Accept Notification" — actor, steps, alternate flows, and postconditions for each. |
| Fri | 17 Jul | I finalized my student panel SRS section and submitted it to Paramveer for the master document. Pushed the updated SRS to GitHub. |
| **SAT** | **18 Jul** | **Mentor Meeting — I attended and presented the student panel requirements to the mentor. Noted all feedback. Helped update tracking sheet.** |
| Sun | 19 Jul | I reviewed mentor's feedback on student requirements. I planned the visual layout of all 5 student sections for the design week ahead. |
| **WEEK 3 — UML Design** |
| Mon | 20 Jul | I created the Use-Case Diagram for the student panel — actor: Student. Use cases: Report Lost, Report Found, View My Reports, View Notifications, Accept Claim, Reject Claim, View Claimed Items, Logout. |
| Tue | 21 Jul | I added `StudentDashboard` to the team Class Diagram with methods: `showReportLostPanel()`, `showReportFoundPanel()`, `showMyReportsPanel()`, `showNotificationsPanel()`, `showClaimedPanel()`. |
| Wed | 22 Jul | I created the Sequence Diagram for "Report Lost Item" — Student clicks Submit → form data read → LostItemDAO.insertLostItem() called → DB saved → success dialog shown. |
| Thu | 23 Jul | I created the Sequence Diagram for "Accept Notification" — Student clicks Accept → LostItemDAO.updateStatus(id, "CLAIMED") called → DB updated → item appears in My Claimed Items. |
| Fri | 24 Jul | I exported all my student panel UML diagrams and pushed them to `docs/UML/student/` on GitHub. Also sent to Paramveer for the combined PDF. |
| **SAT** | **25 Jul** | **Mentor Meeting — I presented my student panel UML diagrams to the mentor. Adjusted the Sequence Diagram based on feedback. Updated tracking sheet.** |
| Sun | 26 Jul | I updated my diagrams with mentor corrections. I planned the implementation of the StudentDashboard skeleton for next week. |
| **WEEK 4 — DB Design + UI Mockups** |
| Mon | 27 Jul | I wrote `LostItem.java` model class — added all fields (id, studentId, itemName, description, location, dateLost, status), parameterized constructor, default constructor, and getters/setters. |
| Tue | 28 Jul | I sketched the student dashboard layout on paper — left sidebar (200px wide) with 5 menu buttons, and a right content area that changes when each button is clicked. |
| Wed | 29 Jul | I designed each of the 5 student content panels: Report Lost form (5 fields + Submit), Report Found form (same), My Reports table (6 columns), Notifications table (with buttons), Claimed Items table. |
| Thu | 30 Jul | I implemented the `StudentDashboard.java` skeleton — a JFrame with JSplitPane dividing into a left sidebar JPanel and a right CardLayout JPanel. Tested the frame opens correctly. |
| Fri | 31 Jul | I built the sidebar — added 5 JButtons with labels and styled them with the team's dark navy and blue color scheme. Wired each button to switch the correct panel using CardLayout.show(). |
| **SAT** | **1 Aug** | **Mentor Meeting — I showed the student dashboard skeleton with working sidebar navigation to the mentor. Got positive feedback. Updated tracking sheet.** |
| Sun | 2 Aug | I reviewed the dashboard feedback and planned to build all 5 content panels starting next week. |
| **WEEK 5 — Module Coding Phase 1** |
| Mon | 3 Aug | I built the "Report Lost Item" panel today — added JTextField for Item Name, JTextArea for Description, JTextField for Location and Date, and a Submit button. Applied consistent styling. |
| Tue | 4 Aug | I coded the Submit button for the Lost Item form — reads all field values, validates that none are empty, and creates a LostItem object with the current student's ID from SessionManager. |
| Wed | 5 Aug | I wired the Submit button to `LostItemDAO.insertLostItem(item)`. On success, shows "Item Reported Successfully!" On exception, shows an error dialog. After submit, all form fields are cleared. |
| Thu | 6 Aug | I built the "Report Found Item" panel — same structure as the Report Lost form but uses the FoundItem model and calls `FoundItemDAO.insertFoundItem()` on submit. |
| Fri | 7 Aug | I coded the Found Item Submit button — validated fields, created a FoundItem with student ID, called FoundItemDAO method, and showed success/error feedback. |
| **SAT** | **8 Aug** | **Mentor Meeting — I demonstrated the Report Lost and Report Found forms to the mentor. Both worked correctly. Updated tracking sheet.** |
| Sun | 9 Aug | I tested both report forms with real data — submitted 3 lost items and 2 found items. Verified all appear in the DB. Fixed one empty field validation bug. |
| **WEEK 6 — Module Coding Phase 2** |
| Mon | 10 Aug | I built the "My Reports" panel — created a JTable with columns: Type (Lost/Found), Item Name, Description, Location, Date, Status. Wrapped it in a JScrollPane. |
| Tue | 11 Aug | I loaded data into the My Reports table — fetched lost items using `LostItemDAO.getLostItemsByStudentId()` and found items using `FoundItemDAO.getFoundItemsByStudentId()`. Combined both into one list. |
| Wed | 12 Aug | I added color-coding to the Status column — PENDING in yellow text, APPROVED in green, REJECTED in red. Implemented this using a custom DefaultTableCellRenderer. |
| Thu | 13 Aug | I added a "Refresh" button to the My Reports panel — when clicked, reloads all data from the DB so the student can see status changes without restarting the app. |
| Fri | 14 Aug | I built the "Notifications" panel — a JTable showing approved lost items for the current student. Columns: Item Name, Location, Date Approved. Added "Accept" and "Reject" buttons below the table. |
| **SAT** | **15 Aug** | **Mentor Meeting — I demonstrated the My Reports panel (with color coding) and the Notifications panel. Mentor approved the design. Updated tracking sheet.** |
| Sun | 16 Aug | I tested the Notifications panel — accepted one item and rejected another. Verified the table updates correctly after each action. Fixed one table refresh bug. |
| **WEEK 7 — Module Coding Phase 3** |
| Mon | 17 Aug | I wired the Accept button — gets the selected row's item ID, calls `LostItemDAO.updateStatus(id, "CLAIMED")`, shows a "Item Claimed Successfully!" dialog, and refreshes the Notifications table. |
| Tue | 18 Aug | I wired the Reject button — calls `LostItemDAO.updateStatus(id, "REJECTED")`, shows a rejection message, and removes the item from the Notifications table immediately. |
| Wed | 19 Aug | I built the "My Claimed Items" panel — a JTable loading from `LostItemDAO.getClaimedItemsByStudentId(currentUserId)` with columns: Item Name, Description, Location, Date Reported. |
| Thu | 20 Aug | I tested the full claim flow: admin approves → I switch to the student panel → notification appears → I click Accept → item appears in My Claimed Items. All steps worked correctly. |
| Fri | 21 Aug | I added empty state handling — if Notifications is empty, it shows "No pending notifications." If My Claimed Items is empty, it shows "You have not claimed any items yet." |
| **SAT** | **22 Aug** | **Mentor Meeting — I demonstrated the full student panel flow: report → notification → accept → claimed. Mentor gave sign-off on Module 3. Updated tracking sheet.** |
| Sun | 23 Aug | I froze the student panel code and pushed all final files to GitHub. Wrote a commit message: "Student Panel - All sections complete." |
| **WEEK 8 — Module Coding Phase 4: Final** |
| Mon | 24 Aug | I added a "Welcome, [Student Name]" label at the top of the student dashboard using `SessionManager.getCurrentUser().getName()`. |
| Tue | 25 Aug | I tested all 5 student sections with 10+ records for each. Verified that each student only sees their own data — tested with 3 different student accounts. |
| Wed | 26 Aug | I polished the UI across all panels — consistent EmptyBorder padding, consistent font sizes (title 16pt bold, labels 12pt), and button hover color effect. |
| Thu | 27 Aug | I tested all form validations one final time — empty fields, fields with only spaces. All show appropriate error dialogs before allowing submission. |
| Fri | 28 Aug | I pushed the final student panel code with commit message: "Student Panel complete - v1.0". No more changes to student panel code. |
| **SAT** | **29 Aug** | **Mentor Meeting — I participated in the full application demo with all modules working together. Mentor confirmed student panel is complete. Updated tracking sheet.** |
| Sun | 30 Aug | I reviewed all GitHub commits to confirm 3+ per week. Pushed missing Javadoc comments as a documentation commit. |
| **WEEK 9 — Integration + Testing** |
| Mon | 31 Aug | I ran integration tests today — submitted a lost item from the student panel and confirmed it appeared in Naitik's admin panel. Fixed one issue where the item name wasn't loading correctly. |
| Tue | 1 Sep | I tested: admin approves the item → I switch to my student panel → the notification appeared correctly in the Notifications section. |
| Wed | 2 Sep | I tested with 3 different student accounts — each student only sees their own reports, notifications, and claimed items. No data leakage between accounts. |
| Thu | 3 Sep | I fixed the student panel integration bugs found during testing — one issue with the session ID not being correctly passed to a query. |
| Fri | 4 Sep | I did a final check — My Reports, Notifications, and My Claimed Items all show correct data in the fully integrated app. |
| **SAT** | **5 Sep** | **Mentor Meeting — I participated in the full integration demo. Student panel fully integrated. Updated tracking sheet.** |
| Sun | 6 Sep | I reviewed remaining integration feedback. Confirmed all student panel issues are resolved. |
| **WEEK 10 — System Testing + Final Build** |
| Mon | 7 Sep | I ran system tests — 3 students each submitted 3 lost items. All 9 items appeared correctly in the admin panel. |
| Tue | 8 Sep | I tested the full claimed flow for 3 students — each accepted 2 items. Verified each student's "My Claimed Items" shows exactly their own 2 items. |
| Wed | 9 Sep | I helped Paramveer test the packaged JAR — verified that the student panel works correctly (form submission, notifications, claimed items) in the final build. |
| Thu | 10 Sep | I tagged the final student panel code as "StudentPanel-Final-v1.0" on GitHub. Student panel is completely frozen. |
| Fri | 11 Sep | I wrote the student panel section of the project report — describing each of the 5 sections, the report flow, and the notification accept/reject flow. |
| **SAT** | **12 Sep** | **Mentor Meeting — Mentor gave final sign-off on the student panel. All test cases for student module passed. Updated tracking sheet.** |
| Sun | 13 Sep | I reviewed all GitHub commits across 12 weeks. Confirmed consistent 3+ commits/week. Pushed final documentation commits. |
| **WEEK 11 — Project Report + PPT + Video** |
| Mon | 14 Sep | I wrote the Module 3 documentation section for the project report — explained all 5 student sections, the CardLayout navigation, and key logic in each section. |
| Tue | 15 Sep | I created the Student Flow Diagram — Login → Report Lost/Found → My Reports → Notification → Accept/Reject → My Claimed Items. Added screenshots of each section. |
| Wed | 16 Sep | I wrote the Student Panel Implementation section with code snippets — showing the JTable loading logic, the Accept/Reject button wiring, and the empty state handling. |
| Thu | 17 Sep | I proofread my student panel section of the report, corrected two errors, and sent the finalized section to Paramveer. |
| Fri | 18 Sep | I prepared 3 PPT slides: Slide 1 — Student Dashboard overview screenshot. Slide 2 — Report Lost/Found form. Slide 3 — Notifications and My Claimed Items. |
| **SAT** | **19 Sep** | **Mentor Meeting — I attended and noted final feedback. Helped Paramveer submit the draft report to the mentor.** |
| Sun | 20 Sep | I recorded a 2-minute video clip — demonstrated the full student workflow: report lost → receive notification → accept → see item in claimed. Sent to Paramveer. |
| **WEEK 12 — Final Documentation Polish, Code Freeze & Project Completion** |
| Mon | 21 Sep | I incorporated the mentor's final feedback into my section of the project report and finalized it. |
| Tue | 22 Sep | I audited all 5 student panel sections in `StudentDashboard.java` — checked form field boundary validations, table empty state messages, and smooth CardLayout navigation. |
| Wed | 23 Sep | I tested the student notification and claim lifecycle end-to-end — verified that items approved by admin reflect in real-time and transfer to My Claimed Items upon student acceptance. |
| Thu | 24 Sep | I refined the visual styling across all student panels — adjusted table padding, column alignments, status badge colors, and button hover states for an immaculate presentation. |
| Fri | 25 Sep | I did final code cleanup — removed all debug prints, added remaining Javadoc comments, and pushed the final commit: "Final Deliverable - Student Panel". |
| **SAT** | **26 Sep** | **Mentor Meeting — I participated in the final project review meeting with our mentor. Demonstrated all 5 student panel features and got complete mentor approval.** |
| **Sun** | **27 Sep** | 🚀 **FINAL PROJECT COMPLETION & DELIVERABLES READY — Completed all final project deliverables for the Student Panel Module. Packaged all student UI components, event handlers, and verified 100% project readiness.** |

---
---

# 👤 NAITIK PATHAK — 24EARAD106
## Module: Admin Panel (UI + Logic)

| Day | Date | My Work for the Day |
|-----|------|----------------------|
| **WEEK 1 — Team Formation + Abstract** |
| Mon | 6 Jul | I joined the team today. My module is the Admin Panel — I will build all 4 sections: Registered Users, Found Items Approval, Lost Items Approval, and Claimed Items Details. |
| Tue | 7 Jul | I studied the admin's responsibilities carefully — view all registered students, approve or reject found and lost item reports, send notifications to students on approval, and track claimed items. |
| Wed | 8 Jul | I researched Java Swing table-based admin layouts — how to use JTable, DefaultTableModel, JScrollPane, and how to get a selected row's data for performing actions on it. |
| Thu | 9 Jul | I wrote 2–3 sentences about the admin panel for the project abstract and shared them with Mohit. I also discussed with Mitesh how the admin approval connects to the student notification flow. |
| Fri | 10 Jul | I created `AdminDashboard.java` and `FoundItem.java` placeholder files and made my first GitHub commit. |
| **SAT** | **11 Jul** | **Mentor Meeting — I attended with the full team. I noted the mentor's feedback about what the admin panel should be able to do. Helped update the tracking sheet.** |
| Sun | 12 Jul | I studied the 4 admin sections in detail — for each, I noted what data to show, what buttons are needed, and what DB operation each button triggers. |
| **WEEK 2 — SRS Documentation** |
| Mon | 13 Jul | I wrote my admin functional requirements: FR-ADM-01: Admin views all registered students. FR-ADM-02: Admin views all pending found item reports. FR-ADM-03: Admin approves or rejects a found item. |
| Tue | 14 Jul | I wrote FR-ADM-04: Admin views pending lost item reports. FR-ADM-05: Admin approves a lost item and sends a notification to the reporting student. FR-ADM-06: Admin rejects a lost item report. |
| Wed | 15 Jul | I wrote FR-ADM-07: After the student accepts the notification, the item moves to the Claimed Items section in the admin panel. FR-ADM-08: Admin views all claimed items with student details. |
| Thu | 16 Jul | I wrote use-case descriptions for "Approve Lost Item" and "View Claimed Items" — including actor, steps, alternate flows, and postconditions for both. |
| Fri | 17 Jul | I finalized my admin panel SRS section and submitted it to Paramveer. Pushed the updated section to GitHub. |
| **SAT** | **18 Jul** | **Mentor Meeting — I attended and presented my admin requirements to the mentor. Noted feedback for improvement. Helped update tracking sheet.** |
| Sun | 19 Jul | I reviewed mentor's SRS feedback. I planned the visual layout for all 4 admin sections for the design week ahead. |
| **WEEK 3 — UML Design** |
| Mon | 20 Jul | I created the Use-Case Diagram for the admin panel — actor: Admin. Use cases: View Registered Users, Approve/Reject Found Item, Approve Lost Item + Notify Student, Reject Lost Item, View Claimed Items. |
| Tue | 21 Jul | I added `AdminDashboard` to the team Class Diagram — showing methods: `showRegisteredUsersPanel()`, `showFoundItemsPanel()`, `showLostItemsPanel()`, `showClaimedItemsPanel()`. |
| Wed | 22 Jul | I created the Sequence Diagram for "Approve Lost Item" — Admin clicks Approve → `LostItemDAO.updateStatus(APPROVED)` called → notification entry created → student sees it in their panel. |
| Thu | 23 Jul | I created the Sequence Diagram for the Claimed Items update — student accepts → `updateStatus(CLAIMED)` → item removed from admin's Lost Items Approval → appears in Claimed Items Details. |
| Fri | 24 Jul | I exported my admin panel UML diagrams and pushed them to `docs/UML/admin/`. Sent copies to Paramveer for the combined PDF. |
| **SAT** | **25 Jul** | **Mentor Meeting — I presented the admin panel UML diagrams to the mentor. Adjusted the Sequence Diagram based on feedback. Updated tracking sheet.** |
| Sun | 26 Jul | I updated my diagrams with mentor corrections and planned the AdminDashboard implementation for next week. |
| **WEEK 4 — DB Design + UI Mockups** |
| Mon | 27 Jul | I wrote `FoundItem.java` model class — added all fields (id, studentId, itemName, description, location, dateFound, status), parameterized and default constructors, getters, setters, and `toString()`. |
| Tue | 28 Jul | I designed the admin dashboard layout on paper — left sidebar with 4 section buttons and a large right content area that shows a table for whichever section is selected. |
| Wed | 29 Jul | I designed each admin section: Section 1 (Registered Users — read-only table), Section 2 (Found Items — table + Approve/Reject buttons), Section 3 (Lost Items — table + Approve & Notify/Reject), Section 4 (Claimed Items — read-only). |
| Thu | 30 Jul | I implemented the `AdminDashboard.java` skeleton — JFrame with JSplitPane: left sidebar with 4 styled JButtons and right CardLayout area for the 4 content panels. |
| Fri | 31 Jul | I wired the sidebar — each button switches to its corresponding panel using CardLayout.show(). Tested that all 4 panels switch correctly. |
| **SAT** | **1 Aug** | **Mentor Meeting — I showed the admin dashboard skeleton with working 4-section sidebar to the mentor. Got positive feedback. Updated tracking sheet.** |
| Sun | 2 Aug | I reviewed dashboard feedback. Planned to build all 4 content panels starting next week. |
| **WEEK 5 — Module Coding Phase 1** |
| Mon | 3 Aug | I built Section 1 "Registered Users" today — created a JTable with columns: Name, Email, Roll No, Phone. Called `UserDAO.getAllUsers()` to load data. Wrapped in JScrollPane. |
| Tue | 4 Aug | I disabled editing on the Registered Users table by overriding `isCellEditable()` to always return false. Styled the table header with a blue background and white bold text. |
| Wed | 5 Aug | I built Section 2 "Found Items Approval" — a JTable showing pending found items from `FoundItemDAO.getPendingFoundItems()`. Columns: Item Name, Finder's Name, Location, Date Found. |
| Thu | 6 Aug | I added "Approve" and "Reject" buttons below the Found Items table. Coded the Approve button — gets selected row's item ID using `table.getSelectedRow()`, calls `FoundItemDAO.updateStatus(id, "APPROVED")`, and refreshes the table. |
| Fri | 7 Aug | I coded the Reject button for Found Items — calls `FoundItemDAO.updateStatus(id, "REJECTED")` and refreshes the table. Added handling for when no row is selected — shows "Please select an item first" dialog. |
| **SAT** | **8 Aug** | **Mentor Meeting — I demonstrated the Registered Users and Found Items Approval sections to the mentor. Both worked correctly. Updated tracking sheet.** |
| Sun | 9 Aug | I tested the Found Items section — approved and rejected items. Verified DB status updates. Fixed one table refresh bug. |
| **WEEK 6 — Module Coding Phase 2** |
| Mon | 10 Aug | I built Section 3 "Lost Items Approval" today — JTable loading from `LostItemDAO.getPendingLostItems()` with columns: Item Name, Student Name, Location, Date Lost. |
| Tue | 11 Aug | I coded the "Approve & Notify Student" button — calls `LostItemDAO.updateStatus(id, "APPROVED")`. This sets the status so that the student sees the item as a notification in their panel. |
| Wed | 12 Aug | I coded the Reject button for Lost Items — calls `LostItemDAO.updateStatus(id, "REJECTED")` with a confirmation dialog before the action. Refreshes the table after. |
| Thu | 13 Aug | I built Section 4 "Claimed Items Details" — JTable loading from `LostItemDAO.getClaimedItems()` with columns: Item Name, Claimed By (Student Name), Roll No, Date Claimed. Made read-only. |
| Fri | 14 Aug | I tested the complete admin flow — approved a lost item, switched to the student panel (Mitesh's module), accepted the notification, switched back to admin, and verified the item appeared in Claimed Items. |
| **SAT** | **15 Aug** | **Mentor Meeting — I demonstrated all 4 admin sections with real actions to the mentor. Mentor approved the admin panel design. Updated tracking sheet.** |
| Sun | 16 Aug | I added "Refresh" buttons to all 4 sections. Tested that refreshing reloads the current DB state correctly. Fixed one bug where Claimed Items wasn't auto-loading after a student acceptance. |
| **WEEK 7 — Module Coding Phase 3** |
| Mon | 17 Aug | I added confirmation dialogs before every approve/reject action — "Are you sure you want to approve this item?" with Yes/No buttons. This prevents accidental clicks. |
| Tue | 18 Aug | I added a count label above each table — e.g., "Total Pending: 5 items". The count updates dynamically after each approval or rejection. |
| Wed | 19 Aug | I handled the empty table state in all 4 sections — when a section has no data, it shows a "No items found" label instead of an empty table. |
| Thu | 20 Aug | I styled all admin tables — added alternating row colors (slightly different shades of dark), blue selected row highlight, and bold column headers. |
| Fri | 21 Aug | I added Javadoc comments to all public methods in `AdminDashboard.java`. Cleaned up all unused imports and debug print statements. |
| **SAT** | **22 Aug** | **Mentor Meeting — I showed the polished admin panel with all 4 sections, confirmations, count labels, and empty state handling. Mentor gave final sign-off on Module 4. Updated tracking sheet.** |
| Sun | 23 Aug | I froze the admin panel code and pushed all final files. Commit message: "Admin Panel - All 4 sections complete." |
| **WEEK 8 — Module Coding Phase 4: Final** |
| Mon | 24 Aug | I added a "Welcome, Admin" header at the top of the admin dashboard using `SessionManager.getCurrentUser().getName()`. |
| Tue | 25 Aug | I added a visual highlight to the sidebar — the currently active section button is shown in a brighter blue. |
| Wed | 26 Aug | I tested all edge cases — approve an item when no row is selected, approve an already-approved item, reject with no row selected. All handled gracefully with clear error messages. |
| Thu | 27 Aug | I did a final review of all admin panel code — consistent naming, complete error handling, no raw print statements, clean code structure. |
| Fri | 28 Aug | I pushed the final admin panel code with commit: "Admin Panel complete - v1.0". No more changes to admin code. |
| **SAT** | **29 Aug** | **Mentor Meeting — I participated in the full application demo. Mentor confirmed the admin panel is complete and fully integrated. Updated tracking sheet.** |
| Sun | 30 Aug | I reviewed all GitHub commits and confirmed 3+ per week. Pushed a Javadoc documentation commit. |
| **WEEK 9 — Integration + Testing** |
| Mon | 31 Aug | I ran integration tests — verified that when Mitesh's student panel submits a lost item, it correctly appears in my Lost Items Approval section. Fixed one query filter issue. |
| Tue | 1 Sep | I tested: I approved a lost item in my admin panel → Mitesh's student panel was opened → the notification appeared correctly. The full chain worked. |
| Wed | 2 Sep | I tested: After the student accepts → I refreshed my Claimed Items section → the item appeared with the student's name and roll number correctly. |
| Thu | 3 Sep | I fixed the last admin panel integration bug — the Claimed Items section was not loading the student's name correctly due to a JOIN query issue. Fixed with Mohit's help. |
| Fri | 4 Sep | I ran a final admin integration check — all 4 sections show correct, live data after every action in the fully integrated app. |
| **SAT** | **5 Sep** | **Mentor Meeting — I participated in the full integration demo. Admin panel fully integrated with all other modules. Updated tracking sheet.** |
| Sun | 6 Sep | I reviewed remaining integration feedback. Confirmed all admin panel issues are resolved. |
| **WEEK 10 — System Testing + Final Build** |
| Mon | 7 Sep | I ran system tests — approved 9 items from 3 different students. All appeared correctly in the Claimed Items section after each student accepted. |
| Tue | 8 Sep | I tested the admin panel with 30+ total records across all sections. Verified all tables load correctly and actions work without delay. |
| Wed | 9 Sep | I helped Paramveer test the packaged JAR — verified all 4 admin sections work correctly in the final build. |
| Thu | 10 Sep | I tagged the final admin code as "AdminPanel-Final-v1.0" on GitHub. Admin panel code is completely frozen. |
| Fri | 11 Sep | I wrote the admin panel section of the project report — describing all 4 sections, the approve/notify logic, and the claimed items tracking. |
| **SAT** | **12 Sep** | **Mentor Meeting — Mentor gave final sign-off on the admin panel. All test cases for the admin module passed. Updated tracking sheet.** |
| Sun | 13 Sep | I reviewed all GitHub commits across 12 weeks. Confirmed 3+ commits/week. Pushed final documentation commits. |
| **WEEK 11 — Project Report + PPT + Video** |
| Mon | 14 Sep | I wrote the Module 4 documentation section — explained all 4 admin sections, the approval/rejection logic, notification sending, and the claimed items tracking. |
| Tue | 15 Sep | I created the Admin Workflow Diagram — PENDING → Admin Approves → Student Notified → Student Accepts → CLAIMED. Added screenshots of all 4 admin sections. |
| Wed | 16 Sep | I wrote the Admin Panel Implementation section with actual code snippets — showing the JTable loading logic, approve button wiring, and confirmation dialog code. |
| Thu | 17 Sep | I proofread my admin panel report section, corrected two technical errors, and submitted the final section to Paramveer. |
| Fri | 18 Sep | I prepared 4 PPT slides: Slide 1 — Registered Users section. Slide 2 — Found Items Approval. Slide 3 — Lost Items Approval. Slide 4 — Claimed Items Details. |
| **SAT** | **19 Sep** | **Mentor Meeting — I attended and noted final feedback. Helped Paramveer submit the draft report to the mentor.** |
| Sun | 20 Sep | I recorded a 2-minute video clip — demonstrated the admin panel: viewed users, approved a found item, approved a lost item, and showed the Claimed Items section updating. Sent to Paramveer. |
| **WEEK 12 — Final Documentation Polish, Code Freeze & Project Completion** |
| Mon | 21 Sep | I incorporated the mentor's final feedback into my report section and finalized it. |
| Tue | 22 Sep | I audited all 4 admin dashboard sections (Registered Users, Found Items Approval, Lost Items Approval, Claimed Items Details) — checked table models and read-only protections. |
| Wed | 23 Sep | I tested admin approval and rejection workflows against multi-record datasets — verified dynamic pending counter updates and prompt confirmation dialogs. |
| Thu | 24 Sep | I verified synchronization between admin approvals and student notification generation alongside Mitesh to ensure zero dropped status changes. |
| Fri | 25 Sep | I did final code cleanup — complete Javadoc comments, removed debug prints, and pushed the final commit: "Final Deliverable - Admin Panel". |
| **SAT** | **26 Sep** | **Mentor Meeting — I participated in the final review meeting with our mentor. Demonstrated all 4 admin panels with live operations and received full mentor sign-off.** |
| **Sun** | **27 Sep** | 🚀 **FINAL PROJECT COMPLETION & DELIVERABLES READY — Completed all final project deliverables for the Admin Panel Module. Verified all administrative controls, tables, and 100% project readiness.** |

---
---

# 👤 PARAMVEER SINGH — 24EARAD119
## Module: Testing, Integration & Documentation

| Day | Date | My Work for the Day |
|-----|------|----------------------|
| **WEEK 1 — Team Formation + Abstract** |
| Mon | 6 Jul | I joined the team today. My module is Testing, Integration, and Documentation. I will be responsible for writing the SRS, test cases, project report, PPT, demo video, and managing GitHub and tracking sheets for all 12 weeks. |
| Tue | 7 Jul | I studied the complete system workflow from both perspectives — a student registering, reporting, and claiming an item, and an admin approving items and tracking claims. |
| Wed | 8 Jul | I researched software testing methodologies — unit testing, integration testing, system testing, and UAT. I also looked at IEEE SRS format for structuring our documentation. |
| Thu | 9 Jul | I contributed 2–3 sentences about testing and documentation for the abstract. I also created the project's `testing/` and `docs/` folder structure on GitHub. |
| Fri | 10 Jul | I set up GitHub repository management — added all 5 team members as collaborators. Created the `TestPlan.md` template with columns: Test ID, Steps, Expected Result, Actual Result, Status. |
| **SAT** | **11 Jul** | **Mentor Meeting — I attended as the official note-taker. I recorded all mentor feedback, created the weekly tracking sheet, and made the first official tracking sheet commit to GitHub.** |
| Sun | 12 Jul | I reviewed all mentor feedback notes. I planned the full test case list based on all features discussed in the team. Updated the test plan with initial test scenarios. |
| **WEEK 2 — SRS Documentation** |
| Mon | 13 Jul | I started compiling the full SRS document today — created the master document structure: Title Page, Table of Contents, Section 1 (Introduction), collecting input from all members. |
| Tue | 14 Jul | I wrote SRS Section 1 — Purpose (manage lost/found items for a college), Scope (desktop app for students and admin), Definitions (DAO, JDBC, Role-based access), and Overview. |
| Wed | 15 Jul | I compiled all functional requirements from all 4 modules — auth (Prabhash), student panel (Mitesh), admin panel (Naitik), and database (Mohit). Assigned sequential FR IDs (FR-01, FR-02…). |
| Thu | 16 Jul | I wrote the system constraints section — must run on Windows with JDK 8+, SQLite DB, minimum 512MB RAM. Added assumptions and dependencies. Wrote the glossary defining all technical terms. |
| Fri | 17 Jul | I finalized the complete SRS document — formatted consistently (Times New Roman 12pt, proper headings, page numbers, table of contents). Pushed SRS PDF to GitHub `docs/`. |
| **SAT** | **18 Jul** | **Mentor Meeting — I officially submitted the SRS draft to the mentor. Noted all corrections. Updated tracking sheet on GitHub.** |
| Sun | 19 Jul | I incorporated all mentor corrections into the SRS. Pushed the corrected final SRS to GitHub. |
| **WEEK 3 — UML Design** |
| Mon | 20 Jul | I started compiling all UML diagrams from all team members into the `docs/UML/` folder. I also created the System Architecture Diagram — showing UI Layer → Logic Layer → DAO Layer → DB Layer. |
| Tue | 21 Jul | I compiled and reviewed the combined Use-Case Diagram — all actors (Admin, Student) and all use cases across both modules in one consistent diagram. |
| Wed | 22 Jul | I reviewed all Sequence Diagrams submitted by the members — verified they correctly show DAO method calls, and that class names match the actual Java code. |
| Thu | 23 Jul | I created the Deployment Diagram — shows User Machine → Java Runtime → Swing Application → JDBC Driver → SQLite DB file. |
| Fri | 24 Jul | I compiled all UML diagrams (ER, Use-Case, Class, Sequence, Activity, Deployment) into one `docs/UML_Diagrams.pdf` and pushed it to GitHub. |
| **SAT** | **25 Jul** | **Mentor Meeting — I presented the complete compiled UML set to the mentor. Got approval on all diagrams. Updated tracking sheet.** |
| Sun | 26 Jul | I updated the UML PDF with all mentor corrections from all modules. Pushed the final version. |
| **WEEK 4 — Maven + Build Setup** |
| Mon | 27 Jul | I set up the Maven `pom.xml` today — added the SQLite JDBC dependency (`org.xerial:sqlite-jdbc:3.45.3.0`), set Java version to 1.8, and configured `maven-jar-plugin` for building a runnable JAR. |
| Tue | 28 Jul | I configured the JAR manifest in `pom.xml` — set the `mainClass` and ensured the SQLite JDBC driver is included. Tested `mvn clean compile` — project compiled successfully. |
| Wed | 29 Jul | I wrote `build.bat` — a script that runs `mvn clean package` and copies the output JAR to `dist/SmartLostAndFound.jar`. Tested it successfully. |
| Thu | 30 Jul | I wrote `run.bat` — a script that runs `java -jar dist/SmartLostAndFound.jar`. Tested that it opens the application. |
| Fri | 31 Jul | I pushed `pom.xml`, `build.bat`, and `run.bat` to GitHub. Updated `README.md` with project description, tech stack, and build/run instructions. |
| **SAT** | **1 Aug** | **Mentor Meeting — I demonstrated the working build and run scripts to the mentor. Showed the JAR being built and launched. Updated tracking sheet.** |
| Sun | 2 Aug | I reviewed all module progress so far. Updated the test plan with new test scenarios discovered this week. |
| **WEEK 5 — Test Case Writing** |
| Mon | 3 Aug | I wrote TC-01 to TC-10 — Auth test cases: correct login, wrong password, unregistered email, valid registration, duplicate email, empty email, empty password, password mismatch, invalid email format, logout. |
| Tue | 4 Aug | I wrote TC-11 to TC-20 — Student Panel test cases: report lost item successfully, submit with empty Item Name, report found item, My Reports shows correct status, refresh works, notification appears after admin approval. |
| Wed | 5 Aug | I wrote TC-21 to TC-30 — Admin Panel test cases: registered users loads, found items loads, approve found item, reject found item, lost items loads, approve lost item + notify, reject lost item, claimed items loads, no-selection error, refresh works. |
| Thu | 6 Aug | I wrote TC-31 to TC-35 — Integration test cases: student reports → admin sees it, admin approves → student notified, student accepts → admin claimed list updates, student rejects → item returns, full E2E test. |
| Fri | 7 Aug | I pushed the complete test case file (`testing/TestCases.md`) to GitHub. Started running TC-01 to TC-10 against Prabhash's current code and recorded PASS/FAIL results. |
| **SAT** | **8 Aug** | **Mentor Meeting — I showed the complete test plan and first set of results to the mentor. Mentor approved the testing approach. Updated tracking sheet.** |
| Sun | 9 Aug | I re-ran failed test cases after Prabhash's fixes and updated the test result sheet. |
| **WEEK 6 — Executing Test Cases** |
| Mon | 10 Aug | I ran TC-11 to TC-20 against Mitesh's current student panel code. Recorded PASS/FAIL results. Marked some as BLOCKED because notifications aren't built yet. |
| Tue | 11 Aug | I ran TC-21 to TC-30 against Naitik's current admin panel code. Recorded results. Reported 2 failures (approve with no selection not handled, refresh not working). |
| Wed | 12 Aug | I re-ran all previously failed auth test cases after Prabhash's fixes. All 10 auth test cases now PASS. Updated the test result sheet. |
| Thu | 13 Aug | I compiled a formal Bug Report with all open issues: Bug ID, Module, Description, Steps to Reproduce, Severity (High/Medium/Low), Assigned To. Shared with the team. |
| Fri | 14 Aug | I ran TC-31 partially — tested "student reports → admin sees it" — PASS. Other integration tests are still BLOCKED waiting for notification and claimed features to be built. |
| **SAT** | **15 Aug** | **Mentor Meeting — I presented the test results and bug report to the mentor. Mentor reviewed the testing approach positively. Updated tracking sheet.** |
| Sun | 16 Aug | I updated the test result sheet with this week's results. Re-ran newly fixed test cases. |
| **WEEK 7 — Integration Testing** |
| Mon | 17 Aug | I ran TC-32 today — "admin approves → student notified" — PASS. Ran TC-33 — "student accepts → claimed list updates" — PASS. Both integration flows working. |
| Tue | 18 Aug | I ran TC-34 — "student rejects → item returned to admin" — PASS. Ran TC-35 — full E2E test — PASS. All integration test cases now passing. |
| Wed | 19 Aug | I re-ran all 35 test cases on the integrated full system. 33 PASS, 2 FAIL (edge cases in admin no-selection handling and duplicate submit). Reported to Naitik and Mitesh. |
| Thu | 20 Aug | I helped Naitik and Mitesh fix the 2 failing edge case tests. Re-ran both — now PASS. All 35 test cases PASS. |
| Fri | 21 Aug | I documented all integration test results in `testing/IntegrationTestReport.md`. Marked all resolved bugs as CLOSED in the bug report. Pushed to GitHub. |
| **SAT** | **22 Aug** | **Mentor Meeting — I presented the integration test report with all 35 PASS results to the mentor. Updated tracking sheet.** |
| Sun | 23 Aug | I updated the GitHub README with final screenshots of the application. Reviewed all module progress before the final coding week. |
| **WEEK 8 — UAT + Final Build** |
| Mon | 24 Aug | I ran UAT (User Acceptance Testing) today — simulated a real student: registered a new account, logged in, reported a lost item, waited for approval, accepted the claim, and checked My Claimed Items. All perfect. |
| Tue | 25 Aug | I ran admin UAT — logged in as admin, viewed all 4 sections, approved items for 3 different students, verified all claimed items appeared correctly. |
| Wed | 26 Aug | I fixed the last critical UAT bug found — the claimed items count label in the admin panel wasn't updating dynamically. Coordinated with Naitik and got it fixed. |
| Thu | 27 Aug | I signed off on all testing — marked the final test result sheet as complete. All 35 test cases PASS, UAT passed. Final bug report shows 0 open bugs. |
| Fri | 28 Aug | I ran `mvn clean package` to build the final JAR. Tested `SmartLostAndFound.jar` on a different user profile. Everything works — the app opens, login works, all features function. |
| **SAT** | **29 Aug** | **Mentor Meeting — I demonstrated the final working JAR running on a fresh machine. Mentor confirmed it's submission-ready. Updated tracking sheet.** |
| Sun | 30 Aug | I updated the GitHub README — added final screenshots from all sections, team member details, build and run instructions, and project description. Created GitHub Release tag `v1.0-final`. |
| **WEEK 9 — Final Integration Check** |
| Mon | 31 Aug | I ran the complete E2E test one final time with 3 students and 9 items. All scenarios passed. Recorded results in the final Integration Test Report. |
| Tue | 1 Sep | I verified that all 5 members have 3+ GitHub commits per week across all 8 weeks so far. Sent a reminder to one member who was short — they pushed the missing commits. |
| Wed | 2 Sep | I coordinated final integration fixes with the team — helped debug one last JOIN query issue in the Claimed Items section. |
| Thu | 3 Sep | I ran all 35 test cases one final time post-fixes. All 35 PASS. Pushed the final test result sheet to GitHub. |
| Fri | 4 Sep | I created the final project folder structure: `src/`, `docs/`, `database/`, `dist/SmartLostAndFound.jar`, `build.bat`, `run.bat`, `README.md`. Pushed the clean structure. |
| **SAT** | **5 Sep** | **Mentor Meeting — I presented the complete final integration with all modules. Mentor gave integration sign-off. Updated tracking sheet.** |
| Sun | 6 Sep | I reviewed all open items. Confirmed all 35 test cases pass, all bugs closed, all code frozen. Planned the report writing week. |
| **WEEK 10 — System Testing + Final JAR** |
| Mon | 7 Sep | I ran the final system test — 30+ records across all tables. All sections load correctly. All actions (approve, reject, accept, claim) work without errors. |
| Tue | 8 Sep | I confirmed GitHub commit counts for all members — everyone has maintained 3+ commits/week. Verified GitHub history is clean and well-organized. |
| Wed | 9 Sep | I tested the packaged JAR on 2 different machines. The app runs correctly on both without any setup issues (DB file auto-creates on first launch). |
| Thu | 10 Sep | I created the GitHub Release `v1.0-final` — tagged the final code, uploaded the JAR, and added a release description. |
| Fri | 11 Sep | I locked all code — confirmed with all members that no more code changes will be made. Started planning the project report structure. |
| **SAT** | **12 Sep** | **Mentor Meeting — I presented the final system test results. All modules confirmed complete. Mentor gave final project sign-off. Updated tracking sheet.** |
| Sun | 13 Sep | I reviewed all GitHub contributions and confirmed everything is in order for the report writing week. |
| **WEEK 11 — Project Report + PPT + Video** |
| Mon | 14 Sep | I started compiling the final project report — assembled all module documentation from all 5 members into one structured document: Title Page, Abstract, Index, Introduction, Tech Stack, Architecture. |
| Tue | 15 Sep | I added all UML Diagrams, System Architecture Diagram, and ER Diagram to the report. Formatted all images consistently with captions and proper sizing. |
| Wed | 16 Sep | I wrote the Testing chapter — included the complete 35-case test result table, integration test results, UAT report, and the final bug report summary (all resolved). |
| Thu | 17 Sep | I wrote the Conclusion and Future Scope section — what the project achieved and what can be added in the future (email notifications, mobile app integration, barcode-based item tracking). |
| Fri | 18 Sep | I compiled the final PPT — 16 slides: Title, Team, Abstract, Problem Statement, Tech Stack, System Architecture, Module 1–4 slides, Testing, Demo, Conclusion. Sent to all members for review. |
| **SAT** | **19 Sep** | **Mentor Meeting — I officially submitted the project report draft and PPT to the mentor. Noted all final corrections. Updated tracking sheet.** |
| Sun | 20 Sep | I compiled all 5 members' video clips into one 10-minute demo video — added a title screen, module transitions, and exported the final video. Pushed to GitHub and `docs/`. |
| **WEEK 12 — Final Documentation Polish, Code Freeze & Project Completion** |
| Mon | 21 Sep | I incorporated all mentor corrections into the final project report. Exported the final version as `docs/Final_Report.pdf` and pushed to GitHub. |
| Tue | 22 Sep | I completed the final verification of test logs — confirmed all 35 test cases PASS, bug tracking log shows 0 open issues, and signed off the final Integration and UAT test reports. |
| Wed | 23 Sep | I finalized the 16-slide project presentation deck (PPT) and completed the final export of the 10-minute master demonstration video showcasing all project modules. |
| Thu | 24 Sep | I conducted a team-wide repository and build audit — verified consistent commit histories across all 5 members, validated clean Maven builds, and confirmed zero unresolved warnings. |
| Fri | 25 Sep | I assembled the master release package — verified standalone runnable JAR (`SmartLostAndFound.jar`), packaged `build.bat`, `run.bat`, `database.sql`, and pushed the final release commit. |
| **SAT** | **26 Sep** | **Mentor Meeting — I led the final project review presentation with our mentor. Submitted the complete deliverable suite (JAR, Report, PPT, Video, Source Code). Mentor officially approved project completion.** |
| **Sun** | **27 Sep** | 🚀 **FINAL PROJECT COMPLETION & DELIVERABLES READY — Successfully concluded all 12 weeks of project work! All deliverables (runnable JAR, full source code, comprehensive report PDF, PPT deck, demo video, and release package) are 100% complete, verified, and ready.** |

---

## Evaluation Checklist

| Component | Marks | Deliverable | Responsible |
|-----------|-------|-------------|-------------|
| Idea & Abstract | 05 | Abstract PDF | Mohit |
| SRS / Documentation | 10 | SRS Document | Paramveer |
| Design (Use-case, Class, ER) | 10 | UML Diagrams PDF | All Members |
| Weekly Progress | 25 | Tracking Sheet (12 weeks) | Paramveer |
| Implementation & Code Quality | 20 | GitHub Source Code | All Members |
| Testing | 10 | 35 Test Cases + Results | Paramveer |
| Presentation | 10 | PPT + Demo Video | Paramveer |
| Final Packaging & Deliverables | 10 | Complete Package & Sign-off | All Members |
| **Total** | **100** | | |

---

*Smart Lost & Found System | Java PBL A.Y. 2026-27 | 6 July – 27 September 2026 | 12 Weeks | 84 Days*
