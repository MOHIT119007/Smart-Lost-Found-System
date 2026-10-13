import database.DatabaseConnection;
import java.sql.*;

public class InspectDB {
    public static void main(String[] args) throws Exception {
        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        
        System.out.println("--- USERS ---");
        ResultSet rs = stmt.executeQuery("SELECT id, full_name, email, phone, college_id FROM users");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " | " + rs.getString(4) + " | " + rs.getString(5));
        }
        
        System.out.println("--- LOST ITEMS ---");
        rs = stmt.executeQuery("SELECT id, user_id, item_name, category, location, status FROM lost_items");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | u:" + rs.getInt(2) + " | " + rs.getString(3) + " | " + rs.getString(4) + " | " + rs.getString(5) + " | " + rs.getString(6));
        }

        System.out.println("--- FOUND ITEMS ---");
        rs = stmt.executeQuery("SELECT id, user_id, item_name, category, found_location, status FROM found_items");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " | u:" + rs.getInt(2) + " | " + rs.getString(3) + " | " + rs.getString(4) + " | " + rs.getString(5) + " | " + rs.getString(6));
        }
    }
}
