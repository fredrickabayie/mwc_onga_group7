-- phpMyAdmin SQL Dump
-- version 4.5.0.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 13, 2015 at 10:12 PM
-- Server version: 10.0.17-MariaDB
-- PHP Version: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ashesi_university_college_mobileweb`
--

-- --------------------------------------------------------

--
-- Table structure for table `onga_mwc_meals`
--

CREATE TABLE `onga_mwc_meals` (
  `meal_id` int(11) NOT NULL,
  `meal_name` varchar(100) NOT NULL,
  `meal_price` double(3,2) NOT NULL,
  `meal_status` enum('1','0') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `onga_mwc_orders`
--

CREATE TABLE `onga_mwc_orders` (
  `order_id` int(11) NOT NULL,
  `meal_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `order_date` datetime NOT NULL,
  `order_status` enum('ready','not') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `onga_mwc_users`
--

CREATE TABLE `onga_mwc_users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(250) NOT NULL,
  `user_type` enum('customer','cafeteria') NOT NULL,
  `user_image` blob NOT NULL,
  `user_balance` double(3,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `onga_mwc_meals`
--
ALTER TABLE `onga_mwc_meals`
  ADD PRIMARY KEY (`meal_id`);

--
-- Indexes for table `onga_mwc_orders`
--
ALTER TABLE `onga_mwc_orders`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `onga_mwc_users`
--
ALTER TABLE `onga_mwc_users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `onga_mwc_orders`
--
ALTER TABLE `onga_mwc_orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
