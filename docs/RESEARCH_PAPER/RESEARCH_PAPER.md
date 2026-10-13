# Research Paper: Smart Lost & Found System
**Foundation Course (Tinkering & Elements of Design)**  
**Department of Artificial Intelligence & Data Science, ACEIT Jaipur (RTU Kota)**  
**Authors:** Mitesh Nehra, Mohit Kumar Sharma, Naitik Pathak, Paramveer Singh, Prabhash Kumar Chaudhary  
**Supervisor:** Er. Ram Babu Buri, Assistant Professor  

---

### Abstract
Losing personal belongings such as identity cards, books, wallets, keys, and electronic devices is a routine problem on a college campus. Recovery usually depends on notice boards, word of mouth, and messaging groups, which lead to fragmented information and vulnerable verification. This paper presents the **Smart Lost & Found System**, a stand-alone Java Swing desktop application backed by a MySQL database. The system uses a layered architecture of presentation, domain model, algorithmic service, Data Access Object (DAO), and JDBC persistence classes, enforcing parameterized SQL for all input. Role-specific dashboards are provided for students and administrators with an explicit status lifecycle (`PENDING` $\rightarrow$ `APPROVED`/`REJECTED` $\rightarrow$ `CLAIMED` $\rightarrow$ `RETURNED`/`RESOLVED`). A transparent rule-based `MatchingService` compares lost and found articles using category equality and keyword overlap. The study presents the software architecture, database design, status lifecycle, worked examples of the matching algorithm, and an evaluation of campus deployment readiness.

---

### Key Contributions
1. A layered, DAO-based architecture for campus lost-and-found operations with isolated student and administrator dashboards.
2. A status-driven workflow where submissions are verified by administrators, students receive automated in-app notifications, and items are claimed with an audit trail.
3. A transparent rule-based lexical matching algorithm with worked examples of true and false matches.
4. A candid review of implementation characteristics including security, SQL injection defenses, and cloud database synchronization.
