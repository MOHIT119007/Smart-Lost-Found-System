import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class ResetCleanDB {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            
            // Clean up unwanted test items
            stmt.execute("DELETE FROM claims;");
            stmt.execute("DELETE FROM lost_items;");
            stmt.execute("DELETE FROM found_items;");
            stmt.execute("DELETE FROM users WHERE id > 6;");

            // Ensure valid users 1 to 6
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (1, 'Campus Administrator', 'admin@campus.edu', 'admin123', 'ADMIN', '9876543210', 'ADM-001');");
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (2, 'Mohit Kumar Sharma', 'mohit.sharma@campus.edu', 'student123', 'STUDENT', '9829012345', '24EARAD103');");
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (3, 'Prabhash Kumar Chaudhary', 'prabhash.chaudhary@campus.edu', 'student123', 'STUDENT', '9829012346', '24EARAD121');");
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (4, 'Mitesh Nehra', 'mitesh.nehra@campus.edu', 'student123', 'STUDENT', '9829012347', '24EARAD102');");
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (5, 'Naitik Pathak', 'naitik.pathak@campus.edu', 'student123', 'STUDENT', '9829012348', '24EARAD106');");
            stmt.execute("INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) " +
                    "VALUES (6, 'Paramveer Singh', 'paramveer.singh@campus.edu', 'student123', 'STUDENT', '9829012349', '24EARD119');");

            // Seed clean, realistic Lost Items
            stmt.execute("INSERT INTO lost_items (id, user_id, item_name, category, location, lost_date, description, contact_phone, status) VALUES " +
                    "(1, 2, 'iPhone 13 (Black)', 'Electronics', 'Library (2nd Floor)', '2026-10-04', 'Black iPhone 13 with clear silicone case, small scratch near camera.', '9829012345', 'PENDING'), " +
                    "(2, 3, 'Blue Wallet', 'Accessories', 'Sports Complex', '2026-10-06', 'Leather bi-fold wallet containing student ID card and bus pass.', '9829012346', 'PENDING'), " +
                    "(3, 4, 'Dell Charger 65W', 'Electronics', 'Computer Center Lab 3', '2026-10-02', 'Dell Type-C original 65W fast charger with braided cable.', '9829012347', 'APPROVED'), " +
                    "(4, 5, 'Earbuds', 'Electronics', 'HOD Office', '2026-10-05', 'White wireless earbuds in charging case.', '9876543210', 'APPROVED'), " +
                    "(5, 6, 'Titan Black Leather Watch', 'Accessories', 'Basketball Court', '2026-10-01', 'Analog quartz watch with black textured strap and silver bezel.', '9829012349', 'REJECTED');");

            // Seed clean, realistic Found Items
            stmt.execute("INSERT INTO found_items (id, user_id, item_name, category, found_location, storage_location, found_date, description, contact_phone, status) VALUES " +
                    "(1, 3, 'Blue Wallet', 'Accessories', 'Sports Complex', 'Security Desk Gate 1', '2026-10-06', 'Blue leather wallet found near sports complex pavilion.', '9829012346', 'PENDING'), " +
                    "(2, 2, 'iPhone (Black)', 'Electronics', 'Computer Center', 'Proctor Office Room 102', '2026-10-03', 'Black smartphone found near terminal 22.', '9829012345', 'PENDING'), " +
                    "(3, 4, 'Mobile Phone', 'Electronics', 'Block A Corridor', 'Admin Helpdesk Counter', '2026-10-02', 'Dark colour phone in case handed over by lab assistant.', '9829012347', 'APPROVED'), " +
                    "(4, 5, 'Scientific Calculator', 'Electronics', 'Mechanical Workshop', 'Proctor Office', '2026-10-04', 'Casio scientific calculator found on table.', '9829012348', 'APPROVED'), " +
                    "(5, 6, 'Dell Charger', 'Electronics', 'Central Library', 'Security Desk Gate 1', '2026-10-02', 'Dell 65W laptop adapter.', '9829012349', 'REJECTED');");

            System.out.println("DATABASE CLEANED AND RESET TO REALISTIC DATA SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
