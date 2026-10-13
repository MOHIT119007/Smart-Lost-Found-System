package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Singleton Database Connection Manager for Smart Lost & Found System.
 * Connects to MySQL and ensures automatic database initialization if needed.
 */
public class DatabaseConnection {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB = "smart_lost_found";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "root";

    private static Connection connection = null;

    private static String dbHost = DEFAULT_HOST;
    private static String dbPort = DEFAULT_PORT;
    private static String dbName = DEFAULT_DB;
    private static String dbUser = DEFAULT_USER;
    private static String dbPass = DEFAULT_PASS;

    static {
        loadConfiguration();
    }

    private DatabaseConnection() {
        // Private constructor for singleton
    }

    /**
     * Reads database configuration from db.properties if present.
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        File configFile = new File("db.properties");
        if (!configFile.exists()) {
            configFile = new File("app/db.properties");
        }
        if (!configFile.exists()) {
            configFile = new File("../db.properties");
        }
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                props.load(input);
                dbHost = props.getProperty("db.host", DEFAULT_HOST);
                dbPort = props.getProperty("db.port", DEFAULT_PORT);
                dbName = props.getProperty("db.name", DEFAULT_DB);
                dbUser = props.getProperty("db.user", DEFAULT_USER);
                dbPass = props.getProperty("db.password", DEFAULT_PASS);
            } catch (Exception e) {
                System.err.println("Could not load db.properties, using defaults: " + e.getMessage());
            }
        }
    }

    /**
     * Obtains the active database connection, opening a new one if closed.
     */
    public static synchronized Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Ensure MySQL JDBC driver is loaded
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    throw new SQLException("MySQL JDBC Driver (com.mysql.cj.jdbc.Driver) not found in classpath!", e);
                }

                String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
                        + "?createDatabaseIfNotExist=true"
                        + "&useSSL=false"
                        + "&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=UTC"
                        + "&characterEncoding=UTF-8";

                connection = DriverManager.getConnection(url, dbUser, dbPass);
                initializeTablesIfMissing(connection);
            }
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            throw e;
        }
        return connection;
    }

    /**
     * Checks and creates required tables if the database was just created.
     */
    private static void initializeTablesIfMissing(Connection conn) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "full_name VARCHAR(100) NOT NULL,"
                + "email VARCHAR(120) NOT NULL UNIQUE,"
                + "password VARCHAR(255) NOT NULL,"
                + "role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',"
                + "phone VARCHAR(20) NOT NULL,"
                + "college_id VARCHAR(50) NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB;";

        String createLostItemsTable = "CREATE TABLE IF NOT EXISTS lost_items ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "user_id INT NOT NULL,"
                + "item_name VARCHAR(150) NOT NULL,"
                + "category VARCHAR(50) NOT NULL,"
                + "location VARCHAR(150) NOT NULL,"
                + "lost_date DATE NOT NULL,"
                + "description TEXT,"
                + "contact_phone VARCHAR(20) NOT NULL,"
                + "status VARCHAR(50) NOT NULL DEFAULT 'PENDING',"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ") ENGINE=InnoDB;";

        String createFoundItemsTable = "CREATE TABLE IF NOT EXISTS found_items ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "user_id INT NOT NULL,"
                + "item_name VARCHAR(150) NOT NULL,"
                + "category VARCHAR(50) NOT NULL,"
                + "found_location VARCHAR(150) NOT NULL,"
                + "storage_location VARCHAR(150) NOT NULL,"
                + "found_date DATE NOT NULL,"
                + "description TEXT,"
                + "contact_phone VARCHAR(20) NOT NULL,"
                + "status VARCHAR(50) NOT NULL DEFAULT 'PENDING',"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ") ENGINE=InnoDB;";

        String createClaimsTable = "CREATE TABLE IF NOT EXISTS claims ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "lost_item_id INT NULL,"
                + "found_item_id INT NOT NULL,"
                + "claimant_id INT NOT NULL,"
                + "proof_details TEXT NOT NULL,"
                + "claim_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',"
                + "admin_remarks TEXT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (lost_item_id) REFERENCES lost_items(id) ON DELETE SET NULL,"
                + "FOREIGN KEY (found_item_id) REFERENCES found_items(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (claimant_id) REFERENCES users(id) ON DELETE CASCADE"
                + ") ENGINE=InnoDB;";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createLostItemsTable);
            stmt.execute(createFoundItemsTable);
            stmt.execute(createClaimsTable);

            // Ensure status column accepts PENDING, APPROVED, REJECTED
            try {
                stmt.execute("ALTER TABLE lost_items MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PENDING'");
                stmt.execute("ALTER TABLE found_items MODIFY COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PENDING'");
                stmt.execute("UPDATE lost_items SET status = 'PENDING' WHERE status = 'ACTIVE'");
                stmt.execute("UPDATE found_items SET status = 'PENDING' WHERE status = 'ACTIVE'");
            } catch (Exception ignored) {
            }

            // Seed default admin if missing
            String seedAdmin = "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (1, 'Campus Administrator', 'admin@campus.edu', 'admin123', 'ADMIN', '9876543210', 'ADM-001');";
            stmt.execute(seedAdmin);

            // Seed the 5 predefined students
            String[] students = new String[]{
                "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (2, 'Mohit Kumar Sharma', 'mohit.sharma@campus.edu', 'student123', 'STUDENT', '9829012345', '24EARAD103');",
                "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (3, 'Prabhash Kumar Chaudhary', 'prabhash.chaudhary@campus.edu', 'student123', 'STUDENT', '9829012346', '24EARAD121');",
                "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (4, 'Mitesh Nehra', 'mitesh.nehra@campus.edu', 'student123', 'STUDENT', '9829012347', '24EARAD102');",
                "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (5, 'Naitik Pathak', 'naitik.pathak@campus.edu', 'student123', 'STUDENT', '9829012348', '24EARAD106');",
                "INSERT IGNORE INTO users (id, full_name, email, password, role, phone, college_id) "
                    + "VALUES (6, 'Paramveer Singh', 'paramveer.singh@campus.edu', 'student123', 'STUDENT', '9829012349', '24EARD119');"
            };
            for (String s : students) {
                stmt.execute(s);
            }
        } catch (SQLException e) {
            System.err.println("Warning while checking tables: " + e.getMessage());
        }
    }

    /**
     * Safely closes the database connection.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
