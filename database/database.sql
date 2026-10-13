-- ===================================================
-- Smart Lost & Found System Database Schema
-- Database: smart_lost_found
-- ===================================================

CREATE DATABASE IF NOT EXISTS smart_lost_found
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE smart_lost_found;

-- Drop existing tables to refresh with clean predefined records
DROP TABLE IF EXISTS claims;
DROP TABLE IF EXISTS found_items;
DROP TABLE IF EXISTS lost_items;
DROP TABLE IF EXISTS users;

-- ---------------------------------------------------
-- 1. Table: users
-- ---------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    phone VARCHAR(20) NOT NULL,
    college_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------
-- 2. Table: lost_items
-- ---------------------------------------------------
CREATE TABLE lost_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    location VARCHAR(150) NOT NULL,
    lost_date DATE NOT NULL,
    description TEXT,
    contact_phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------
-- 3. Table: found_items
-- ---------------------------------------------------
CREATE TABLE found_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    found_location VARCHAR(150) NOT NULL,
    storage_location VARCHAR(150) NOT NULL,
    found_date DATE NOT NULL,
    description TEXT,
    contact_phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------
-- 4. Table: claims (Retained for backwards compatibility)
-- ---------------------------------------------------
CREATE TABLE IF NOT EXISTS claims (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lost_item_id INT NULL,
    found_item_id INT NOT NULL,
    claimant_id INT NOT NULL,
    proof_details TEXT NOT NULL,
    claim_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    admin_remarks TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lost_item_id) REFERENCES lost_items(id) ON DELETE SET NULL,
    FOREIGN KEY (found_item_id) REFERENCES found_items(id) ON DELETE CASCADE,
    FOREIGN KEY (claimant_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------
-- Insert EXACTLY the 5 Predefined Students + 1 Campus Admin
-- ---------------------------------------------------
INSERT INTO users (id, full_name, email, password, role, phone, college_id)
VALUES 
    (1, 'Campus Administrator', 'admin@campus.edu', 'admin123', 'ADMIN', '9876543210', 'ADM-001'),
    (2, 'Mohit Kumar Sharma', 'mohit.sharma@campus.edu', 'student123', 'STUDENT', '9829012345', '24EARAD103'),
    (3, 'Prabhash Kumar Chaudhary', 'prabhash.chaudhary@campus.edu', 'student123', 'STUDENT', '9829012346', '24EARAD121'),
    (4, 'Mitesh Nehra', 'mitesh.nehra@campus.edu', 'student123', 'STUDENT', '9829012347', '24EARAD102'),
    (5, 'Naitik Pathak', 'naitik.pathak@campus.edu', 'student123', 'STUDENT', '9829012348', '24EARAD106'),
    (6, 'Paramveer Singh', 'paramveer.singh@campus.edu', 'student123', 'STUDENT', '9829012349', '24EARD119');

-- ---------------------------------------------------
-- Initial Sample Lost Items
-- ---------------------------------------------------
INSERT INTO lost_items (id, user_id, item_name, category, location, lost_date, description, contact_phone, status) VALUES
    (1, 2, 'iPhone 13 (Black)', 'Electronics', 'Library (2nd Floor)', '2026-10-04', 'Black iPhone 13 with clear silicone case, small scratch near camera.', '9829012345', 'PENDING'),
    (2, 3, 'Blue Wallet', 'Accessories', 'Sports Complex', '2026-10-06', 'Leather bi-fold wallet containing student ID card and bus pass.', '9829012346', 'PENDING'),
    (3, 4, 'Dell Charger 65W', 'Electronics', 'Computer Center Lab 3', '2026-10-02', 'Dell Type-C original 65W fast charger with braided cable.', '9829012347', 'APPROVED'),
    (4, 5, 'Earbuds', 'Electronics', 'HOD Office', '2026-10-05', 'White wireless earbuds in charging case.', '9876543210', 'APPROVED'),
    (5, 6, 'Titan Black Leather Watch', 'Accessories', 'Basketball Court', '2026-10-01', 'Analog quartz watch with black textured strap and silver bezel.', '9829012349', 'REJECTED');

-- ---------------------------------------------------
-- Initial Sample Found Items
-- ---------------------------------------------------
INSERT INTO found_items (id, user_id, item_name, category, found_location, storage_location, found_date, description, contact_phone, status) VALUES
    (1, 3, 'Blue Wallet', 'Accessories', 'Sports Complex', 'Security Desk Gate 1', '2026-10-06', 'Blue leather wallet found near sports complex pavilion.', '9829012346', 'PENDING'),
    (2, 2, 'iPhone (Black)', 'Electronics', 'Computer Center', 'Proctor Office Room 102', '2026-10-03', 'Black smartphone found near terminal 22.', '9829012345', 'PENDING'),
    (3, 4, 'Mobile Phone', 'Electronics', 'Block A Corridor', 'Admin Helpdesk Counter', '2026-10-02', 'Dark colour phone in case handed over by lab assistant.', '9829012347', 'APPROVED'),
    (4, 5, 'Scientific Calculator', 'Electronics', 'Mechanical Workshop', 'Proctor Office', '2026-10-04', 'Casio scientific calculator found on table.', '9829012348', 'APPROVED'),
    (5, 6, 'Dell Charger', 'Electronics', 'Central Library', 'Security Desk Gate 1', '2026-10-02', 'Dell 65W laptop adapter.', '9829012349', 'REJECTED');
