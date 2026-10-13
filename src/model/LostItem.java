package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing an item reported as lost.
 */
public class LostItem {
    private int id;
    private int userId;
    private String itemName;
    private String category;
    private String location;
    private Date lostDate;
    private String description;
    private String contactPhone;
    private String status; // "ACTIVE", "CLAIMED", "RESOLVED"
    private Timestamp createdAt;

    // Additional display field
    private String reporterName;

    public LostItem() {
        this.status = "PENDING";
    }

    public LostItem(int id, int userId, String itemName, String category, String location,
                    Date lostDate, String description, String contactPhone, String status) {
        this.id = id;
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.location = location;
        this.lostDate = lostDate;
        this.description = description;
        this.contactPhone = contactPhone;
        this.status = status;
    }

    public LostItem(int userId, String itemName, String category, String location,
                    Date lostDate, String description, String contactPhone) {
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.location = location;
        this.lostDate = lostDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getLostDate() {
        return lostDate;
    }

    public void setLostDate(Date lostDate) {
        this.lostDate = lostDate;
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

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
