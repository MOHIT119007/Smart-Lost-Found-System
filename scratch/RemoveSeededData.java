import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class RemoveSeededData {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Remove the 5 seeded lost items and 5 seeded found items added by the assistant
            int deletedLost = stmt.executeUpdate("DELETE FROM lost_items WHERE id IN (1, 2, 3, 4, 5);");
            int deletedFound = stmt.executeUpdate("DELETE FROM found_items WHERE id IN (1, 2, 3, 4, 5);");

            System.out.println("Deleted seeded lost items: " + deletedLost);
            System.out.println("Deleted seeded found items: " + deletedFound);
            System.out.println("CLEANUP SUCCESSFUL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
