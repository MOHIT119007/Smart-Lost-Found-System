# Academic Project Report
## Smart Lost & Found System
**Foundation Course (Tinkering & Elements of Design)**  
**Department of Artificial Intelligence & Data Science**  
**Arya College of Engineering & Information Technology, Jaipur (RTU Kota)**  
**Academic Session:** 2026–27  

* **Project Guide:** Er. Ram Babu Buri (Assistant Professor)
* **HOD:** Dr. Ashok Kajla (Professor)
* **Submitted By:**
  * Mitesh Nehra (`24EARAD102`)
  * Mohit Kumar Sharma (`24EARAD103`)
  * Naitik Pathak (`24EARAD106`)
  * Paramveer Singh (`24EARAD119`)
  * Prabhash Kumar Chaudhary (`24EARAD121`)

---

### Executive Summary
The **Smart Lost & Found System** provides an efficient digital solution for managing lost and found belongings within a college campus. Developed using **Core Java (JDK 17)** and **Java Swing**, it replaces informal notice boards and social media messaging with a structured, role-based workflow.

### Key Capabilities
1. **Student Self-Service Panel:** Report lost items, report found items, track submission status in real-time with color-coded status badges, receive administrative approval alerts, and claim belongings.
2. **Administrative Console:** View registered students, inspect pending lost/found submission queues, execute single-click approvals, and maintain an audit trail of claimed items.
3. **Smart Matching Engine (`MatchingService.java`):** Rule-based lexical algorithm that compares categories and title token overlap to suggest potential matches between reported lost items and recovered inventory.
4. **Resilient Data Architecture:** Uses MySQL Server with InnoDB engine, parameterized JDBC queries via Data Access Objects, and auto-bootstrapping database initialization.
5. **Portability:** Packaged with `jpackage` as a self-contained Windows executable bundle (`SmartLostAndFound.exe`) with an embedded Java runtime and cloud MySQL connectivity.
