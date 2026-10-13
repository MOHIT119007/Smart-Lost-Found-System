package dao;

import database.DatabaseConnection;
import model.LostItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for LostItem entity operations.
 */
public class LostItemDAO {

    /**
     * Reports a new lost item.
     */
    public boolean addLostItem(LostItem item) {
        String sql = "INSERT INTO lost_items (user_id, item_name, category, location, lost_date, description, contact_phone, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, item.getUserId());
                pstmt.setString(2, item.getItemName());
                pstmt.setString(3, item.getCategory());
                pstmt.setString(4, item.getLocation());
                pstmt.setDate(5, item.getLostDate());
                pstmt.setString(6, item.getDescription());
                pstmt.setString(7, item.getContactPhone());
                pstmt.setString(8, item.getStatus() != null ? item.getStatus() : "PENDING");

                int affected = pstmt.executeUpdate();
                if (affected > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            item.setId(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.addLostItem Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all lost items with reporter names.
     */
    public List<LostItem> getAllLostItems() {
        String sql = "SELECT l.*, u.full_name as reporter_name FROM lost_items l "
                + "JOIN users u ON l.user_id = u.id ORDER BY l.id DESC";
        return fetchItems(sql);
    }

    /**
     * Retrieves only active lost items.
     */
    public List<LostItem> getActiveLostItems() {
        String sql = "SELECT l.*, u.full_name as reporter_name FROM lost_items l "
                + "JOIN users u ON l.user_id = u.id WHERE l.status = 'ACTIVE' ORDER BY l.id DESC";
        return fetchItems(sql);
    }

    /**
     * Retrieves lost items reported by a specific user.
     */
    public List<LostItem> getLostItemsByUser(int userId) {
        String sql = "SELECT l.*, u.full_name as reporter_name FROM lost_items l "
                + "JOIN users u ON l.user_id = u.id WHERE l.user_id = ? ORDER BY l.id DESC";
        List<LostItem> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToItem(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.getLostItemsByUser Error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves a single lost item by ID.
     */
    public LostItem getLostItemById(int id) {
        String sql = "SELECT l.*, u.full_name as reporter_name FROM lost_items l "
                + "JOIN users u ON l.user_id = u.id WHERE l.id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToItem(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.getLostItemById Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates status of a lost item (ACTIVE, CLAIMED, RESOLVED).
     */
    public boolean updateStatus(int itemId, String status) {
        String sql = "UPDATE lost_items SET status = ? WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, itemId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.updateStatus Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a lost item by ID.
     */
    public boolean deleteLostItem(int itemId) {
        String sql = "DELETE FROM lost_items WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.deleteLostItem Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Search lost items by keyword, category, and status.
     */
    public List<LostItem> search(String keyword, String category, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT l.*, u.full_name as reporter_name FROM lost_items l "
                        + "JOIN users u ON l.user_id = u.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(l.item_name) LIKE ? OR LOWER(l.description) LIKE ? OR LOWER(l.location) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (category != null && !category.trim().isEmpty() && !"All Categories".equalsIgnoreCase(category)) {
            sql.append("AND l.category = ? ");
            params.add(category.trim());
        }

        if (status != null && !status.trim().isEmpty() && !"All Statuses".equalsIgnoreCase(status)) {
            sql.append("AND l.status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY l.id DESC");

        List<LostItem> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToItem(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.search Error: " + e.getMessage());
        }
        return list;
    }

    private List<LostItem> fetchItems(String sql) {
        List<LostItem> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("LostItemDAO.fetchItems Error: " + e.getMessage());
        }
        return list;
    }

    private LostItem mapResultSetToItem(ResultSet rs) throws SQLException {
        LostItem item = new LostItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setItemName(rs.getString("item_name"));
        item.setCategory(rs.getString("category"));
        item.setLocation(rs.getString("location"));
        item.setLostDate(rs.getDate("lost_date"));
        item.setDescription(rs.getString("description"));
        item.setContactPhone(rs.getString("contact_phone"));
        item.setStatus(rs.getString("status"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            item.setReporterName(rs.getString("reporter_name"));
        } catch (SQLException ignored) {
        }
        return item;
    }
}
