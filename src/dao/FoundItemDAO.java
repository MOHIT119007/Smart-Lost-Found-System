package dao;

import database.DatabaseConnection;
import model.FoundItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for FoundItem entity operations.
 */
public class FoundItemDAO {

    /**
     * Reports a new found item.
     */
    public boolean addFoundItem(FoundItem item) {
        String sql = "INSERT INTO found_items (user_id, item_name, category, found_location, storage_location, "
                + "found_date, description, contact_phone, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, item.getUserId());
                pstmt.setString(2, item.getItemName());
                pstmt.setString(3, item.getCategory());
                pstmt.setString(4, item.getFoundLocation());
                pstmt.setString(5, item.getStorageLocation());
                pstmt.setDate(6, item.getFoundDate());
                pstmt.setString(7, item.getDescription());
                pstmt.setString(8, item.getContactPhone());
                pstmt.setString(9, item.getStatus() != null ? item.getStatus() : "PENDING");

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
            System.err.println("FoundItemDAO.addFoundItem Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all found items with finder's name.
     */
    public List<FoundItem> getAllFoundItems() {
        String sql = "SELECT f.*, u.full_name as finder_name FROM found_items f "
                + "JOIN users u ON f.user_id = u.id ORDER BY f.id DESC";
        return fetchItems(sql);
    }

    /**
     * Retrieves only active found items.
     */
    public List<FoundItem> getActiveFoundItems() {
        String sql = "SELECT f.*, u.full_name as finder_name FROM found_items f "
                + "JOIN users u ON f.user_id = u.id WHERE f.status = 'ACTIVE' ORDER BY f.id DESC";
        return fetchItems(sql);
    }

    /**
     * Retrieves found items reported by a specific user.
     */
    public List<FoundItem> getFoundItemsByUser(int userId) {
        String sql = "SELECT f.*, u.full_name as finder_name FROM found_items f "
                + "JOIN users u ON f.user_id = u.id WHERE f.user_id = ? ORDER BY f.id DESC";
        List<FoundItem> list = new ArrayList<>();
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
            System.err.println("FoundItemDAO.getFoundItemsByUser Error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves a single found item by ID.
     */
    public FoundItem getFoundItemById(int id) {
        String sql = "SELECT f.*, u.full_name as finder_name FROM found_items f "
                + "JOIN users u ON f.user_id = u.id WHERE f.id = ?";
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
            System.err.println("FoundItemDAO.getFoundItemById Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates status of a found item (ACTIVE, CLAIMED, RETURNED).
     */
    public boolean updateStatus(int itemId, String status) {
        String sql = "UPDATE found_items SET status = ? WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, itemId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("FoundItemDAO.updateStatus Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Updates found item details (Name, Category, Location, Storage Location, Description, Contact, Status).
     */
    public boolean updateFoundItemDetails(int id, String itemName, String category, String foundLocation,
                                         String storageLocation, String description, String contactPhone, String status) {
        String sql = "UPDATE found_items SET item_name = ?, category = ?, found_location = ?, "
                + "storage_location = ?, description = ?, contact_phone = ?, status = ? "
                + "WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, itemName);
                pstmt.setString(2, category);
                pstmt.setString(3, foundLocation);
                pstmt.setString(4, storageLocation);
                pstmt.setString(5, description);
                pstmt.setString(6, contactPhone);
                pstmt.setString(7, status);
                pstmt.setInt(8, id);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("FoundItemDAO.updateFoundItemDetails Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a found item by ID.
     */
    public boolean deleteFoundItem(int itemId) {
        String sql = "DELETE FROM found_items WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, itemId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("FoundItemDAO.deleteFoundItem Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Search found items by keyword, category, and status.
     */
    public List<FoundItem> search(String keyword, String category, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT f.*, u.full_name as finder_name FROM found_items f "
                        + "JOIN users u ON f.user_id = u.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(f.item_name) LIKE ? OR LOWER(f.description) LIKE ? "
                    + "OR LOWER(f.found_location) LIKE ? OR LOWER(f.storage_location) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (category != null && !category.trim().isEmpty() && !"All Categories".equalsIgnoreCase(category)) {
            sql.append("AND f.category = ? ");
            params.add(category.trim());
        }

        if (status != null && !status.trim().isEmpty() && !"All Statuses".equalsIgnoreCase(status)) {
            sql.append("AND f.status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY f.id DESC");

        List<FoundItem> list = new ArrayList<>();
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
            System.err.println("FoundItemDAO.search Error: " + e.getMessage());
        }
        return list;
    }

    private List<FoundItem> fetchItems(String sql) {
        List<FoundItem> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("FoundItemDAO.fetchItems Error: " + e.getMessage());
        }
        return list;
    }

    private FoundItem mapResultSetToItem(ResultSet rs) throws SQLException {
        FoundItem item = new FoundItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setItemName(rs.getString("item_name"));
        item.setCategory(rs.getString("category"));
        item.setFoundLocation(rs.getString("found_location"));
        item.setStorageLocation(rs.getString("storage_location"));
        item.setFoundDate(rs.getDate("found_date"));
        item.setDescription(rs.getString("description"));
        item.setContactPhone(rs.getString("contact_phone"));
        item.setStatus(rs.getString("status"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            item.setFinderName(rs.getString("finder_name"));
        } catch (SQLException ignored) {
        }
        return item;
    }
}
