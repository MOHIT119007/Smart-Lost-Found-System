package model;

import java.sql.Timestamp;

/**
 * Model class representing a Claim request submitted by a student
 * against a found item in the Smart Lost & Found system.
 */
public class Claim {
    private int id;
    private Integer lostItemId;
    private int foundItemId;
    private int claimantId;
    private String proofDetails;
    private String claimStatus; // "PENDING", "APPROVED", "REJECTED"
    private String adminRemarks;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Joined fields from found_items and users
    private String itemName;
    private String itemCategory;
    private String foundLocation;
    private String storageLocation;
    private String itemDescription;
    private String claimantName;
    private String claimantEmail;
    private String claimantPhone;
    private String claimantCollegeId;

    // Joined fields from lost_items (when claim is linked to a reported lost item)
    private String lostItemName;
    private String lostCategory;
    private String lostLocation;
    private String lostDescription;
    private String lostDate;

    public Claim() {
    }

    public Claim(int foundItemId, int claimantId, String proofDetails) {
        this.foundItemId = foundItemId;
        this.claimantId = claimantId;
        this.proofDetails = proofDetails;
        this.claimStatus = "PENDING";
    }

    public Claim(Integer lostItemId, int foundItemId, int claimantId, String proofDetails) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.claimantId = claimantId;
        this.proofDetails = proofDetails;
        this.claimStatus = "PENDING";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Integer lostItemId) {
        this.lostItemId = lostItemId;
    }

    public int getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(int foundItemId) {
        this.foundItemId = foundItemId;
    }

    public int getClaimantId() {
        return claimantId;
    }

    public void setClaimantId(int claimantId) {
        this.claimantId = claimantId;
    }

    public String getProofDetails() {
        return proofDetails;
    }

    public void setProofDetails(String proofDetails) {
        this.proofDetails = proofDetails;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getFoundLocation() {
        return foundLocation;
    }

    public void setFoundLocation(String foundLocation) {
        this.foundLocation = foundLocation;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getClaimantName() {
        return claimantName;
    }

    public void setClaimantName(String claimantName) {
        this.claimantName = claimantName;
    }

    public String getClaimantEmail() {
        return claimantEmail;
    }

    public void setClaimantEmail(String claimantEmail) {
        this.claimantEmail = claimantEmail;
    }

    public String getClaimantPhone() {
        return claimantPhone;
    }

    public void setClaimantPhone(String claimantPhone) {
        this.claimantPhone = claimantPhone;
    }

    public String getClaimantCollegeId() {
        return claimantCollegeId;
    }

    public void setClaimantCollegeId(String claimantCollegeId) {
        this.claimantCollegeId = claimantCollegeId;
    }

    public String getLostItemName() {
        return lostItemName;
    }

    public void setLostItemName(String lostItemName) {
        this.lostItemName = lostItemName;
    }

    public String getLostCategory() {
        return lostCategory;
    }

    public void setLostCategory(String lostCategory) {
        this.lostCategory = lostCategory;
    }

    public String getLostLocation() {
        return lostLocation;
    }

    public void setLostLocation(String lostLocation) {
        this.lostLocation = lostLocation;
    }

    public String getLostDescription() {
        return lostDescription;
    }

    public void setLostDescription(String lostDescription) {
        this.lostDescription = lostDescription;
    }

    public String getLostDate() {
        return lostDate;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }
}
