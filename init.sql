-- ============================================
-- PEMS Database Initialization
-- ============================================

CREATE DATABASE IF NOT EXISTS pems_db;
USE pems_db;

-- ============================================
-- TABLES
-- ============================================

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
    );

CREATE TABLE IF NOT EXISTS expense_categories (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  name VARCHAR(100) NOT NULL UNIQUE
    , active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS income_categories (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 name VARCHAR(100) NOT NULL UNIQUE
    , active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS expenses (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        user_id BIGINT NOT NULL,
                                        category_id BIGINT NOT NULL,
                                        amount DECIMAL(12,2) NOT NULL,
    expense_date DATE NOT NULL,
    expense_time TIME,
    description VARCHAR(500),
    payment_method VARCHAR(255),
    currency VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES expense_categories(id)
    );

CREATE TABLE IF NOT EXISTS incomes (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       income_category_id BIGINT NOT NULL,
                                       amount DECIMAL(12,2) NOT NULL,
    income_date DATE NOT NULL,
    income_time TIME,
    description VARCHAR(500),
    payment_method VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (income_category_id) REFERENCES income_categories(id)
    );

-- ============================================
-- DEFAULT DATA
-- ============================================

INSERT IGNORE INTO expense_categories (name, active) VALUES
('Food', TRUE),
('Travel', TRUE),
('Shopping', TRUE),
('Bills', TRUE),
('Entertainment', TRUE),
('Health', TRUE),
('Education', TRUE),
('Rent', TRUE);

INSERT IGNORE INTO income_categories (name, active) VALUES
('Salary', TRUE),
('Freelance', TRUE),
('Business', TRUE),
('Investment', TRUE),
('Gift', TRUE),
('Other', TRUE);
