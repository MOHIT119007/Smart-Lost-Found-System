package model;

import java.sql.Timestamp;

/**
 * Model class representing a User (Student or Administrator)
 * in the Smart Lost & Found system.
 */
public class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String role; // "STUDENT" or "ADMIN"
    private String phone;
    private String collegeId;
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String fullName, String email, String password, String role, String phone, String collegeId) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.collegeId = collegeId;
    }

    public User(String fullName, String email, String password, String role, String phone, String collegeId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.collegeId = collegeId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return fullName + " (" + role + " - " + collegeId + ")";
    }
}
