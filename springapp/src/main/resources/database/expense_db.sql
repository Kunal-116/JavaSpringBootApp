-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 12, 2025 at 07:03 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `expense_db`
CREATE DATABASE IF NOT EXISTS `expense_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `expense_db`;
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_users`
--

CREATE TABLE IF NOT EXISTS `tbl_expense_categories` (
    `category_id` BIGINT NOT NULL AUTO_INCREMENT,
    `category_name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255) NULL,
    `delete_status` enum('0','1') NOT NULL DEFAULT '0',
    PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE IF NOT EXISTS `tbl_users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `u_name` VARCHAR(100) NOT NULL,
    `u_email` VARCHAR(150) NOT NULL UNIQUE, -- Added UNIQUE
    `u_mobile` VARCHAR(10) NOT NULL,
    `u_password` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `login_status` ENUM('INACTIVE','ACTIVE') NOT NULL,
    `updated_at` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `delete_status` ENUM('0','1') NOT NULL DEFAULT '0',
    `u_role`  VARCHAR(100) NOT NULL DEFAULT 'User',
    PRIMARY KEY (`user_id`) -- <<-- THIS WAS MISSING
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- --------------------------------------------------------

--
-- Table structure for table `tbl_user_expenses`
--

CREATE TABLE IF NOT EXISTS `tbl_user_expenses` (
    `expense_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL, -- Foreign key to tbl_expense_categories
    `amount` DECIMAL(13, 2) NOT NULL, -- Increased precision for larger amounts
    `note` VARCHAR(255) NULL,
    `expense_date` DATE NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `delete_status` enum('0','1') NOT NULL DEFAULT '0',
    PRIMARY KEY (`expense_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_users`
-- --
-- ALTER TABLE `tbl_users`
--   ADD PRIMARY KEY (`user_id`),
--   ADD UNIQUE KEY `u_email` (`u_email`);

-- --
-- -- Indexes for table `tbl_user_expenses`
-- --
-- ALTER TABLE `tbl_user_expenses`
--   ADD PRIMARY KEY (`exp_id`),
--   ADD KEY `user_id` (`user_id`);

-- --
-- AUTO_INCREMENT for dumped tables
--

--
-- -- AUTO_INCREMENT for table `tbl_users`
-- --
-- ALTER TABLE `tbl_users`
--   MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT;

-- --
-- -- AUTO_INCREMENT for table `tbl_user_expenses`
-- --
-- ALTER TABLE `tbl_user_expenses`
--   MODIFY `exp_id` bigint(20) NOT NULL AUTO_INCREMENT;

-- --
-- -- Constraints for dumped tables
-- --

--
-- Constraints for table `tbl_user_expenses`
--
ALTER TABLE `tbl_user_expenses`
    ADD CONSTRAINT `fk_expense_user`
    FOREIGN KEY (`user_id`) REFERENCES `tbl_users` (`user_id`) ON DELETE CASCADE, -- If user is deleted, delete their expenses
    ADD CONSTRAINT `fk_expense_category`
    FOREIGN KEY (`category_id`) REFERENCES `tbl_expense_categories` (`category_id`) ON DELETE RESTRICT; -- Don't delete categories if expenses use them

INSERT INTO `tbl_expense_categories` (`category_name`) VALUES
('Food & Drink'),
('Transportation'),
('Housing'),
('Utilities'),
('Entertainment'),
('Shopping'),
('Healthcare'),
('Education'),
('Income');

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
