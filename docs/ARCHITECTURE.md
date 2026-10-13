# System Architecture & Technical Design Specification
## Smart Lost & Found System

### 1. High-Level Architecture
The Smart Lost & Found System follows a decoupled, 5-layer modular architectural pattern:

```text
┌─────────────────────────────────────────────────────────────┐
│                 1. Presentation Layer (Swing)               │
│      LoginFrame | RegisterDialog | DashboardFrame           │
│      UITheme (Custom Dark Palette & Component Helpers)      │
└──────────────────────────────┬──────────────────────────────┘
                               │ User Interactions
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 2. Domain Model Entities (POJO)             │
│            User | LostItem | FoundItem | Claim              │
└──────────────────────────────┬──────────────────────────────┘
                               │ Data Binding
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 3. Algorithmic Service Layer                │
│       MatchingService.java (Multi-Attribute Lexical Rule)   │
└──────────────────────────────┬──────────────────────────────┘
                               │ Evaluates Candidates
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 4. Data Access Object (DAO)                 │
│         UserDAO | LostItemDAO | FoundItemDAO | ClaimDAO     │
└──────────────────────────────┬──────────────────────────────┘
                               │ Parameterized JDBC Calls
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                 5. Persistence & Database Layer             │
│    DatabaseConnection.java (Singleton Connection Manager)   │
│         MySQL Server 8.0 / Clever Cloud Production DB       │
└─────────────────────────────────────────────────────────────┘
```

---

### 2. Core Modules Breakdown

#### 2.1 Database & Persistence Module (`Mohit Kumar Sharma - 24EARAD103`)
* **`DatabaseConnection.java`**: Implements the Singleton pattern to supply thread-safe JDBC connections. Dynamically loads `db.properties` from multiple fallback paths (`./db.properties`, `app/db.properties`, `../db.properties`).
* Automatically initializes the database schema (`CREATE TABLE IF NOT EXISTS`) and seeds the default administrator (`admin@campus.edu`) along with 5 student demonstration accounts on initial connection.
* Supports both local MySQL and live cloud-hosted instances (e.g., Clever Cloud, Aiven) with configurable SSL flags.

#### 2.2 Authentication & User Security Module (`Prabhash Kumar Chaudhary - 24EARAD121`)
* **`LoginFrame.java`**: Entry point screen offering role-based authentication (`STUDENT` vs `ADMIN`). Protects against unauthorized role spoofing.
* **`RegisterDialog.java`**: Modal dialog for student self-registration enforcing regex verification on 10-digit mobile phone numbers and academic email formatting.
* **`UserDAO.java`**: Encapsulates user queries using `PreparedStatement` to eliminate SQL injection vulnerabilities.

#### 2.3 Student Self-Service Module (`Mitesh Nehra - 24EARAD102`)
* **`DashboardFrame.java` (Student View)**: Utilizes `CardLayout` for instant sidebar tab transitions across:
  * *Report Lost Item:* Capture item title, category, campus location, loss date, and contact number.
  * *Report Found Item:* Report recovered belongings with handover storage details.
  * *My Reports:* Consolidated table of submitted lost and found reports with color-coded status badges.
  * *Notifications & Approvals:* Real-time alert feed displaying items verified and approved by administration.
  * *My Claimed Items:* Personal audit log of successfully claimed belongings.

#### 2.4 Administrative Console Module (`Naitik Pathak - 24EARAD106`)
* **`DashboardFrame.java` (Admin View)**: Provides centralized governance across campus operations:
  * *Registered Users Details:* Read-only directory of all student profiles.
  * *Register Found Items & Approval:* Review pending found reports and desk entries.
  * *Campus Desk Found Item Entry:* Direct entry modal for articles handed directly to the security or reception desk.
  * *Lost Items Approval:* Review queue for evaluating lost-item claims and triggering student notifications.
  * *Claimed Items Details:* Permanent audit trail of resolved claims.

#### 2.5 Testing, Integration & Packaging Module (`Paramveer Singh - 24EARAD119`)
* Implemented formal 35-case test suite (`TC-01` to `TC-35`) verifying 100% pass status across functional workflows.
* Automated build pipelines via `build.bat` using JDK 17 compiler targets.
* Packaged portable self-contained Windows distributions using `jpackage` app-image with an embedded private JRE.

---

### 3. Database Entity Relationship (3NF)

* **`users` Table:** Primary identity store with unique constraint on `email`.
* **`lost_items` Table:** Stores lost reports; foreign key linked to `users(id)` with `ON DELETE CASCADE`.
* **`found_items` Table:** Stores found reports; foreign key linked to `users(id)` with `ON DELETE CASCADE`.
* **`claims` Table:** Relational junction entity linking `found_item_id`, `claimant_id`, and optional `lost_item_id`.
