package dao;

import database.DatabaseConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for User entity operations.
 */
public class UserDAO {

    /**
     * Authenticates a user by email and password.
     * Returns User object if credentials match, null otherwise.
     */
    public User authenticate(String email, String password) {
        String sql = "SELECT id, full_name, email, password, role, phone, college_id, created_at "
                + "FROM users WHERE LOWER(email) = LOWER(?) AND password = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email.trim());
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setRole(rs.getString("role"));
                        user.setPhone(rs.getString("phone"));
                        user.setCollegeId(rs.getString("college_id"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.authenticate Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registers a new user.
     * Returns true if registered successfully.
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (full_name, email, password, role, phone, college_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, user.getFullName());
                pstmt.setString(2, user.getEmail().trim().toLowerCase());
                pstmt.setString(3, user.getPassword());
                pstmt.setString(4, user.getRole() != null ? user.getRole() : "STUDENT");
                pstmt.setString(5, user.getPhone());
                pstmt.setString(6, user.getCollegeId());

                int affected = pstmt.executeUpdate();
                if (affected > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.register Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an email is already registered.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE LOWER(email) = LOWER(?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email.trim());
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.emailExists Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves a user by their ID.
     */
    public User getUserById(int id) {
        String sql = "SELECT id, full_name, email, password, role, phone, college_id, created_at "
                + "FROM users WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setRole(rs.getString("role"));
                        user.setPhone(rs.getString("phone"));
                        user.setCollegeId(rs.getString("college_id"));
                        user.setCreatedAt(rs.getTimestamp("created_at"));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.getUserById Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all registered users (students and admins).
     */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, full_name, email, password, role, phone, college_id, created_at FROM users ORDER BY id ASC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setPhone(rs.getString("phone"));
                    user.setCollegeId(rs.getString("college_id"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.getAllUsers Error: " + e.getMessage());
        }
        return list;
    }
}
