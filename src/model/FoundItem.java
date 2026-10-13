package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing an item reported as found on campus.
 */
public class FoundItem {
    private int id;
    private int userId;
    private String itemName;
    private String category;
    private String foundLocation;
    private String storageLocation;
    private Date foundDate;
    private String description;
    private String contactPhone;
    private String status; // "ACTIVE", "CLAIMED", "RETURNED"
    private Timestamp createdAt;

    // Additional display field
    private String finderName;

    public FoundItem() {
        this.status = "PENDING";
    }

    public FoundItem(int id, int userId, String itemName, String category, String foundLocation,
                     String storageLocation, Date foundDate, String description,
                     String contactPhone, String status) {
        this.id = id;
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.foundLocation = foundLocation;
        this.storageLocation = storageLocation;
        this.foundDate = foundDate;
        this.description = description;
        this.contactPhone = contactPhone;
        this.status = status;
    }

    public FoundItem(int userId, String itemName, String category, String foundLocation,
                     String storageLocation, Date foundDate, String description,
                     String contactPhone) {
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.foundLocation = foundLocation;
        this.storageLocation = storageLocation;
        this.foundDate = foundDate;
        this.description = description;
        this.contactPhone = contactPhone;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Date getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(Date foundDate) {
        this.foundDate = foundDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }
}
