import database.DatabaseConnection;
import ui.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Main application entry point for Smart Lost & Found System.
 * Initializes Look & Feel, checks database connectivity, and presents Login.
 */
public class Main {

    public static void main(String[] args) {
        // Set modern Look & Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName()) || "Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }
        }

        // Global font scale and rendering hints
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            // Verify database connectivity in background
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn != null && !conn.isClosed()) {
                    System.out.println("Smart Lost & Found: Database connected successfully.");
                }
            } catch (Exception e) {
                System.err.println("Database Warning: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Warning: Could not connect to MySQL database on localhost:3306.\n"
                                + "Details: " + e.getMessage() + "\n\n"
                                + "Please ensure MySQL Server is running with database 'smart_lost_found' and user 'root'.",
                        "Database Connection Notice", JOptionPane.WARNING_MESSAGE);
            }

            // Launch Application
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
