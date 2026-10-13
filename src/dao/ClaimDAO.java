package dao;

import database.DatabaseConnection;
import model.Claim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Claim operations in the Smart Lost & Found System.
 */
public class ClaimDAO {

    /**
     * Submits a new claim request for a found item.
     */
    public boolean submitClaim(Claim claim) {
        String sql = "INSERT INTO claims (lost_item_id, found_item_id, claimant_id, proof_details, claim_status) "
                + "VALUES (?, ?, ?, ?, 'PENDING')";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (claim.getLostItemId() != null && claim.getLostItemId() > 0) {
                    pstmt.setInt(1, claim.getLostItemId());
                } else {
                    pstmt.setNull(1, Types.INTEGER);
                }
                pstmt.setInt(2, claim.getFoundItemId());
                pstmt.setInt(3, claim.getClaimantId());
                pstmt.setString(4, claim.getProofDetails());
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.submitClaim Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all claims with joined found item, claimant user details, and linked lost item.
     */
    public List<Claim> getAllClaims() {
        String sql = "SELECT c.*, f.item_name, f.category as item_category, f.found_location, f.storage_location, f.description as item_desc, "
                + "u.full_name as claimant_name, u.email as claimant_email, u.phone as claimant_phone, u.college_id as claimant_college_id, "
                + "l.item_name as lost_item_name, l.category as lost_category, l.location as lost_location, l.description as lost_desc, l.lost_date "
                + "FROM claims c "
                + "JOIN found_items f ON c.found_item_id = f.id "
                + "JOIN users u ON c.claimant_id = u.id "
                + "LEFT JOIN lost_items l ON c.lost_item_id = l.id "
                + "ORDER BY c.id DESC";
        return fetchClaims(sql, null);
    }

    /**
     * Retrieves claims submitted by a specific student.
     */
    public List<Claim> getClaimsByUser(int userId) {
        String sql = "SELECT c.*, f.item_name, f.category as item_category, f.found_location, f.storage_location, f.description as item_desc, "
                + "u.full_name as claimant_name, u.email as claimant_email, u.phone as claimant_phone, u.college_id as claimant_college_id, "
                + "l.item_name as lost_item_name, l.category as lost_category, l.location as lost_location, l.description as lost_desc, l.lost_date "
                + "FROM claims c "
                + "JOIN found_items f ON c.found_item_id = f.id "
                + "JOIN users u ON c.claimant_id = u.id "
                + "LEFT JOIN lost_items l ON c.lost_item_id = l.id "
                + "WHERE c.claimant_id = ? "
                + "ORDER BY c.id DESC";
        return fetchClaims(sql, userId);
    }

    /**
     * Updates claim status (APPROVED/REJECTED) and admin remarks.
     * When APPROVED: marks the found item as 'CLAIMED' and lost item as 'CLAIMED'.
     */
    public boolean updateClaimStatus(int claimId, String status, String adminRemarks) {
        String sql = "UPDATE claims SET claim_status = ?, admin_remarks = ? WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setString(2, adminRemarks);
                pstmt.setInt(3, claimId);
                int updated = pstmt.executeUpdate();

                if (updated > 0) {
                    if ("APPROVED".equalsIgnoreCase(status)) {
                        // Mark found item as CLAIMED
                        String updateFoundSql = "UPDATE found_items SET status = 'CLAIMED' "
                                + "WHERE id = (SELECT found_item_id FROM claims WHERE id = ?)";
                        try (PreparedStatement fiStmt = conn.prepareStatement(updateFoundSql)) {
                            fiStmt.setInt(1, claimId);
                            fiStmt.executeUpdate();
                        }
                        // Mark lost item as CLAIMED if linked
                        String updateLostSql = "UPDATE lost_items SET status = 'CLAIMED' "
                                + "WHERE id = (SELECT lost_item_id FROM claims WHERE id = ? AND lost_item_id IS NOT NULL)";
                        try (PreparedStatement liStmt = conn.prepareStatement(updateLostSql)) {
                            liStmt.setInt(1, claimId);
                            liStmt.executeUpdate();
                        }
                    } else if ("REJECTED".equalsIgnoreCase(status)) {
                        // Reset found item to ACTIVE
                        String resetFoundSql = "UPDATE found_items SET status = 'ACTIVE' "
                                + "WHERE id = (SELECT found_item_id FROM claims WHERE id = ?)";
                        try (PreparedStatement fiStmt = conn.prepareStatement(resetFoundSql)) {
                            fiStmt.setInt(1, claimId);
                            fiStmt.executeUpdate();
                        }
                        // Reset lost item to ACTIVE
                        String resetLostSql = "UPDATE lost_items SET status = 'ACTIVE' "
                                + "WHERE id = (SELECT lost_item_id FROM claims WHERE id = ? AND lost_item_id IS NOT NULL)";
                        try (PreparedStatement liStmt = conn.prepareStatement(resetLostSql)) {
                            liStmt.setInt(1, claimId);
                            liStmt.executeUpdate();
                        }
                    }
                }
                return updated > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.updateClaimStatus Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Called when student clicks "Confirm Receipt" (item found and handed over).
     * Marks claim as CONFIRMED, found_item as RETURNED, and lost_item as RESOLVED.
     */
    public boolean studentConfirmReceipt(int claimId, int studentId) {
        String sql = "UPDATE claims SET claim_status = 'CONFIRMED', "
                + "admin_remarks = CONCAT(IFNULL(admin_remarks, ''), ' [Verified & Received by Student]') "
                + "WHERE id = ? AND claimant_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, claimId);
                pstmt.setInt(2, studentId);
                int updated = pstmt.executeUpdate();
                if (updated > 0) {
                    // Update found item to RETURNED
                    String updateFound = "UPDATE found_items SET status = 'RETURNED' "
                            + "WHERE id = (SELECT found_item_id FROM claims WHERE id = ?)";
                    try (PreparedStatement fi = conn.prepareStatement(updateFound)) {
                        fi.setInt(1, claimId);
                        fi.executeUpdate();
                    }
                    // Update lost item to RESOLVED
                    String updateLost = "UPDATE lost_items SET status = 'RESOLVED' "
                            + "WHERE id = (SELECT lost_item_id FROM claims WHERE id = ? AND lost_item_id IS NOT NULL)";
                    try (PreparedStatement li = conn.prepareStatement(updateLost)) {
                        li.setInt(1, claimId);
                        li.executeUpdate();
                    }
                }
                return updated > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.studentConfirmReceipt Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Called when student clicks "Reject (Not My Item)".
     * Marks claim as REJECTED, restores found item to ACTIVE, and keeps lost item ACTIVE.
     */
    public boolean studentRejectReceipt(int claimId, int studentId, String reason) {
        String sql = "UPDATE claims SET claim_status = 'REJECTED', "
                + "admin_remarks = CONCAT(IFNULL(admin_remarks, ''), ' [Student: Not My Item]') "
                + "WHERE id = ? AND claimant_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, claimId);
                pstmt.setInt(2, studentId);
                int updated = pstmt.executeUpdate();
                if (updated > 0) {
                    // Return found item back to ACTIVE inventory
                    String resetFound = "UPDATE found_items SET status = 'ACTIVE' "
                            + "WHERE id = (SELECT found_item_id FROM claims WHERE id = ?)";
                    try (PreparedStatement fi = conn.prepareStatement(resetFound)) {
                        fi.setInt(1, claimId);
                        fi.executeUpdate();
                    }
                    // Return lost item back to ACTIVE search
                    String resetLost = "UPDATE lost_items SET status = 'ACTIVE' "
                            + "WHERE id = (SELECT lost_item_id FROM claims WHERE id = ? AND lost_item_id IS NOT NULL)";
                    try (PreparedStatement li = conn.prepareStatement(resetLost)) {
                        li.setInt(1, claimId);
                        li.executeUpdate();
                    }
                }
                return updated > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.studentRejectReceipt Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Allows Admin to match an active Lost Item with a Found Item directly,
     * creating an approved claim waiting for student confirmation.
     */
    public boolean createAdminMatch(int lostItemId, int foundItemId, int claimantId, String remarks) {
        String sql = "INSERT INTO claims (lost_item_id, found_item_id, claimant_id, proof_details, claim_status, admin_remarks) "
                + "VALUES (?, ?, ?, 'System/Admin Match: Item found in custody matches reported lost item.', 'APPROVED', ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, lostItemId);
                pstmt.setInt(2, foundItemId);
                pstmt.setInt(3, claimantId);
                pstmt.setString(4, (remarks != null && !remarks.isEmpty())
                        ? remarks
                        : "Admin verified match. Please confirm receipt and collect with College ID.");
                int inserted = pstmt.executeUpdate();
                if (inserted > 0) {
                    String updateFoundSql = "UPDATE found_items SET status = 'CLAIMED' WHERE id = ?";
                    try (PreparedStatement fi = conn.prepareStatement(updateFoundSql)) {
                        fi.setInt(1, foundItemId);
                        fi.executeUpdate();
                    }
                    String updateLostSql = "UPDATE lost_items SET status = 'CLAIMED' WHERE id = ?";
                    try (PreparedStatement li = conn.prepareStatement(updateLostSql)) {
                        li.setInt(1, lostItemId);
                        li.executeUpdate();
                    }
                }
                return inserted > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.createAdminMatch Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a claim record by its ID.
     */
    public boolean deleteClaim(int claimId) {
        String sql = "DELETE FROM claims WHERE id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, claimId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.deleteClaim Error: " + e.getMessage());
            return false;
        }
    }

    private List<Claim> fetchClaims(String sql, Integer userId) {
        List<Claim> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (userId != null) {
                    pstmt.setInt(1, userId);
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Claim c = new Claim();
                        c.setId(rs.getInt("id"));
                        int lostId = rs.getInt("lost_item_id");
                        if (!rs.wasNull()) {
                            c.setLostItemId(lostId);
                        }
                        c.setFoundItemId(rs.getInt("found_item_id"));
                        c.setClaimantId(rs.getInt("claimant_id"));
                        c.setProofDetails(rs.getString("proof_details"));
                        c.setClaimStatus(rs.getString("claim_status"));
                        c.setAdminRemarks(rs.getString("admin_remarks"));
                        c.setCreatedAt(rs.getTimestamp("created_at"));
                        c.setUpdatedAt(rs.getTimestamp("updated_at"));

                        c.setItemName(rs.getString("item_name"));
                        c.setItemCategory(rs.getString("item_category"));
                        c.setFoundLocation(rs.getString("found_location"));
                        c.setStorageLocation(rs.getString("storage_location"));
                        c.setItemDescription(rs.getString("item_desc"));

                        c.setClaimantName(rs.getString("claimant_name"));
                        c.setClaimantEmail(rs.getString("claimant_email"));
                        c.setClaimantPhone(rs.getString("claimant_phone"));
                        c.setClaimantCollegeId(rs.getString("claimant_college_id"));

                        try {
                            c.setLostItemName(rs.getString("lost_item_name"));
                            c.setLostCategory(rs.getString("lost_category"));
                            c.setLostLocation(rs.getString("lost_location"));
                            c.setLostDescription(rs.getString("lost_desc"));
                            java.sql.Date ld = rs.getDate("lost_date");
                            if (ld != null) {
                                c.setLostDate(ld.toString());
                            }
                        } catch (SQLException ignored) {
                        }

                        list.add(c);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ClaimDAO.fetchClaims Error: " + e.getMessage());
        }
        return list;
    }
}
