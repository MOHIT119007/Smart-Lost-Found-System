# 🚀 How to Run the Smart Lost & Found Desktop Application
### Step-by-Step Execution Guide & Portable Application Package

---

## 📌 Overview
The **Smart Lost & Found System** is packaged as a standalone portable Windows application bundled with a private Java 17 runtime and pre-configured connection to our live **Clever Cloud MySQL** database. 

> **Important:** No local installation of Java (JDK) or MySQL database server is required on your computer.

---

## 📥 Step-by-Step Instructions to Run the Application

### Step 1: Download or Locate the Portable ZIP Package
Download the pre-compiled portable application archive using any of the following direct links:

* 📦 **Primary Download (GitHub Latest Release):**  
  👉 📥 **[Download SmartLostAndFound Portable ZIP](../dist/SmartLostAndFound_Portable.zip)**
  📥 **[Download SmartLostAndFound Portable ZIP](./dist/SmartLostAndFound_Portable.zip)**
* 📁 **Local Repository Distribution Folder:**  
  If you have cloned the repository locally, the distribution package and extracted files are located in:  
  👉 **[`dist/SmartLostAndFound/`](../dist/SmartLostAndFound/)** or **[`dist/`](../dist/)**

---

### Step 2: Extract the ZIP Archive
1. Right-click on the downloaded `SmartLostAndFound.zip` file.
2. Select **"Extract All..."** (or use WinRAR / 7-Zip).
3. Choose an extraction folder on your system (e.g., Desktop or `C:\SmartLostAndFound`).
4. Click **Extract**.

---

### Step 3: Verify Internet Connectivity
* Ensure your PC or laptop is connected to the **Internet** (Wi-Fi, Ethernet, or Mobile Hotspot).
* The application automatically connects to our live 24/7 cloud database on startup to synchronize loss reports, claims, and administrative approvals in real-time across multiple laptops.

---

### Step 4: Launch the Executable
1. Open the extracted folder: `SmartLostAndFound/`
2. Look for **`SmartLostAndFound.exe`** (with the official college logo icon).
3. **Double-click `SmartLostAndFound.exe`** to launch the system.

> [!TIP]
> **Windows SmartScreen / Defender Note:**  
> Since this is an academic project executable built for evaluation, Windows may display a blue dialog: *"Windows protected your PC"*.  
> 1. Click **"More info"**.  
> 2. Click **"Run anyway"**.  
> The application will initialize in 1–2 seconds.

---

### Step 5: Log in Using Demonstration Accounts
Once the login window appears, you can sign in using either of the pre-configured academic profiles:

#### 👑 Administrator Profile (For Reviewers & Faculty)
* **Email:** `admin@campus.edu`
* **Password:** `admin123`
* **Access Level:** Full administrative privileges (review pending lost & found submissions, direct desk registrations, student directory, claim audits).

#### 🎓 Student Profile
* **Email:** `mohit.sharma@campus.edu`
* **Password:** `student123`
* **Access Level:** Student self-service portal (report lost items, report found items, view reports, in-app approval notifications, claim items).
* *(Alternative: You can click **"Register as New Student"** on the login screen to create a fresh student account).*

---

### Step 6: Graceful Cloud Database Slot Release
* When you are finished reviewing, simply click the **"X" (Close)** button on the top-right corner of the window, or click **"Logout"**.
* The application features an automated `WindowAdapter` and JVM runtime shutdown hook that immediately transmits a clean `COM_QUIT` to release the cloud database connection slot in **0 milliseconds**.

---

## 🛠️ Alternative Option: Compile & Run from Source Code

If you prefer to compile from source:

1. **Prerequisites:** Ensure **Java JDK 17+** is installed on your computer.
2. **Build:** Open Command Prompt in the project root directory and run:
   ```cmd
   build.bat
   ```
3. **Run:** Launch the packaged JAR file:
   ```cmd
   java -jar dist\SmartLostAndFound\app\SmartLostAndFound.jar
   ```

---

## 🔗 Useful Documentation Links
* 🏠 **Main Documentation:** [README.md](../README.md)
* 🏗️ **System Architecture:** [ARCHITECTURE.md](../docs/ARCHITECTURE.md)
* 📜 **Software Requirements (SRS):** [SRS.md](../docs/SRS/SRS.md)
* 📄 **Academic Project Report:** [PROJECT_REPORT.md](../docs/PROJECT_REPORT/PROJECT_REPORT.md)
* 📊 **Defense Presentation Deck (PPT):** [presentation_link.md](../docs/PRESENTATION/presentation_link.md)
* 🎥 **Video Demonstration:** [demo_video_link.md](../docs/VIDEO/demo_video_link.md)
* 📅 **13-Week Activity Logs:** [week_wise_data.md](../week_wise_data.md)
