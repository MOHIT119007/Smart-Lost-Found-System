# Smart Lost & Found System

A desktop-based Campus Property Recovery & Management System built using **Java Swing, JDBC, and MySQL**.

---

## 👥 Project Team

* **Project Guide:** Er. Ram Babu Buri
* **Academic Year:** 2026–27 | Desktop Application Track

| Name | Enrollment No. | Email | Module |
| :--- | :--- | :--- | :--- |
| **Mohit Kumar Sharma** | 24EARAD103 | mohit.sharma@campus.edu | Database & DAO Layer |
| **Prabhash Kumar Chaudhary** | 24EARAD121 | prabhash.chaudhary@campus.edu | Authentication & User Management |
| **Mitesh Nehra** | 24EARAD102 | mitesh.nehra@campus.edu | Student Panel |
| **Naitik Pathak** | 24EARAD106 | naitik.pathak@campus.edu | Admin Panel |
| **Paramveer Singh** | 24EARAD119 | paramveer.singh@campus.edu | Testing & Integration |

---

## 📌 Project Overview

Misplacing personal belongings (student IDs, laptops, chargers, keys, wallets, notebooks) is a frequent issue across academic campuses. Traditional notice boards and informal WhatsApp groups lack accountability, privacy, and centralized status tracking.

The **Smart Lost & Found System** provides a secure, centralized desktop management solution:
* **Students:** Report lost and found items, view submission status, receive instant alerts upon administrator approval, and securely claim recovered belongings.
* **Administrators:** Review submission queues, verify and approve claims, audit student profiles, and maintain a permanent archive of returned items.
* **Smart Matching:** An automated multi-attribute similarity algorithm suggests matches between reported lost items and newly found inventory based on item title, category, and location.

---

## 🛠️ Technology Stack & Architecture

* **Language:** Java (JDK 17+)
* **User Interface:** Java Swing (`JFrame`, `CardLayout`, custom modern dark theme)
* **Database:** MySQL Server 8.0+
* **Connectivity:** JDBC with `mysql-connector-j.jar` (prepared statements to prevent SQL injection)
* **Architecture:** Layered Architecture with Data Access Object (DAO) Pattern

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│        (LoginFrame, DashboardFrame, RegisterDialog)         │
└──────────────────────────────┬──────────────────────────────┘
                               │ User Actions / Events
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                   Business Logic Layer                      │
│             (MatchingService, Validation Rules)             │
└──────────────────────────────┬──────────────────────────────┘
                               │ Model Objects
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                  Data Access Object (DAO)                   │
│         (UserDAO, LostItemDAO, FoundItemDAO, ClaimDAO)      │
└──────────────────────────────┬──────────────────────────────┘
                               │ JDBC (PreparedStatements)
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                     MySQL Database                          │
│                   (smart_lost_found)                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 💻 System Requirements (For Any Windows Laptop)

Before running the application on any computer (e.g., student or evaluator/teacher laptop), ensure the following prerequisites are installed:

1. **Operating System:** Windows 10 or Windows 11 (64-bit)
2. **Java Runtime:** Java 17 or newer
   * Check in terminal: `java -version`
   * *If Java is not installed:* Right-click `setup_jdk.ps1` and select **Run with PowerShell** to automatically download and configure portable JDK 17.
3. **Database Server:** MySQL 8.0+ running locally (default port: `3306`)
   * MySQL must be running as a local service or via XAMPP / MySQL Workbench.

---

## 📂 Project Structure

```text
SmartLostAndFound/
├── src/                                 # Complete Java Source Code
│   ├── Main.java                        # System entry point
│   ├── dao/                             # Data Access Object pattern classes
│   │   ├── ClaimDAO.java
│   │   ├── FoundItemDAO.java
│   │   ├── LostItemDAO.java
│   │   └── UserDAO.java
│   ├── database/
│   │   └── DatabaseConnection.java      # Singleton JDBC manager & schema auto-init
│   ├── model/                           # Domain POJO entities
│   │   ├── Claim.java
│   │   ├── FoundItem.java
│   │   ├── LostItem.java
│   │   └── User.java
│   ├── service/
│   │   └── MatchingService.java         # Multi-attribute similarity algorithm
│   ├── ui/                              # Swing GUI components
│   │   ├── DashboardFrame.java          # Student & Admin dashboards
│   │   ├── LoginFrame.java              # User login screen
│   │   ├── RegisterDialog.java          # Student registration dialog
│   │   └── UITheme.java                 # Styling palette, fonts, and icons
│   └── resources/
│       └── logo.png                     # Application branding graphic
│
├── database/
│   └── database.sql                     # Table DDL schema & sample records
├── lib/
│   └── mysql-connector-j.jar            # Official MySQL JDBC Driver
├── release/                             # Application Distribution & Executable Package
│   ├── SmartLostAndFound.exe            # Native Windows executable launcher
│   ├── SmartLostAndFound.jar            # Self-contained runnable application JAR
│   └── db.properties.example            # Database configuration template for release
├── db.properties.example                # Safe database configuration template
├── build.bat                            # Rebuild Java sources & update JAR in release/
├── setup_jdk.ps1                        # Automatic JDK 17 setup script
├── LICENSE                              # MIT License
└── README.md                            # Project documentation
```

---

## 🚀 Setup & Launch Guide (Step-by-Step)

### Step 1: Database Setup
1. Open MySQL Workbench, MySQL Command Line Client, or phpMyAdmin.
2. Execute the setup script located at `database/database.sql`:
   ```bash
   mysql -u root -p < database/database.sql
   ```
   *This creates the `smart_lost_found` database, required tables (`users`, `lost_items`, `found_items`, `claims`), and initial demo records.*

### Step 2: Configure Database Credentials
1. Copy `db.properties.example` to `db.properties` in the project root (or inside `release/`):
   ```cmd
   copy db.properties.example db.properties
   ```
2. Open `db.properties` in any text editor and enter your local MySQL password:
   ```properties
   db.host=localhost
   db.port=3306
   db.name=smart_lost_found
   db.user=root
   db.password=YOUR_MYSQL_PASSWORD
   ```

### Step 3: Launch the Application
You can launch the application in either of two ways:

* **Method 1 (Recommended — Native Windows App):**
  Open the **`release/`** folder and double-click **`SmartLostAndFound.exe`**.
  *(The EXE detects your installed Java runtime and launches seamlessly).*

* **Method 2 (Runnable JAR):**
  Inside the **`release/`** folder, double-click **`SmartLostAndFound.jar`** or run from PowerShell:
  ```powershell
  java -jar release/SmartLostAndFound.jar
  ```

---

## 🔑 Predefined Accounts for Demonstration

The database is pre-populated with ready-to-use accounts for immediate testing:

| Role | Email | Password | Name | Academic ID |
| :--- | :--- | :--- | :--- | :--- |
| **Administrator** | `admin@campus.edu` | `admin123` | Campus Administrator | `ADM-001` |
| **Student** | `mohit.sharma@campus.edu` | `student123` | Mohit Kumar Sharma | `24EARAD103` |
| **Student** | `prabhash.chaudhary@campus.edu` | `student123` | Prabhash Kumar Chaudhary | `24EARAD121` |
| **Student** | `mitesh.nehra@campus.edu` | `student123` | Mitesh Nehra | `24EARAD102` |
| **Student** | `naitik.pathak@campus.edu` | `student123` | Naitik Pathak | `24EARAD106` |
| **Student** | `paramveer.singh@campus.edu` | `student123` | Paramveer Singh | `24EARD119` |

> *New students can also register anytime using the **"Register as New Student"** button on the login screen.*

---

## 🛠️ Recompiling from Source

If you modify any Java source files inside `src/`:
1. Double-click **`build.bat`**.
2. It will automatically clean, recompile all Java source files, and repackage **`SmartLostAndFound.jar`**.

---

## ❓ Troubleshooting

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| **"Communications link failure"** | MySQL server is not running or incorrect password. | Ensure MySQL service is running and verify `db.user` and `db.password` in `db.properties`. |
| **"Java Required" popup when clicking EXE** | Java 17+ is not in PATH or user profile. | Install OpenJDK 17+ or run `setup_jdk.ps1` via PowerShell. |
| **"Cannot find SmartLostAndFound.jar"** | The JAR file was moved out of the root folder. | Keep `SmartLostAndFound.exe` and `SmartLostAndFound.jar` in the same directory. |
| **"Access denied for user 'root'@'localhost'"** | Password mismatch in `db.properties`. | Set your actual MySQL root password in `db.properties`. |

---

## 📄 License

This project is open-source software licensed under the [MIT License](LICENSE).
