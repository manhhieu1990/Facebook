-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 11, 2020 at 08:37 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `facebook`
--

-- --------------------------------------------------------

--
-- Table structure for table `facebook_account`
--

CREATE TABLE `facebook_account` (
  `user_ID` varchar(100) NOT NULL COMMENT 'Số định danh cho người dùng',
  `first_name` varchar(100) NOT NULL COMMENT 'Tên ',
  `last_name` varchar(100) NOT NULL COMMENT 'Họ',
  `email` varchar(100) NOT NULL COMMENT 'email',
  `password` varchar(100) NOT NULL COMMENT 'mật khẩu',
  `secq1_ID` varchar(100) NOT NULL COMMENT 'Số định danh của câu hỏi bảo mật 1',
  `a_secq1_ID` varchar(100) NOT NULL COMMENT 'Số định danh của câu trả lời cho câu hỏi bảo mật 1',
  `secq2_ID` varchar(100) NOT NULL COMMENT 'Số định danh cho câu hỏi bảo mật 2',
  `a_secq2_ID` varchar(100) NOT NULL COMMENT 'Số định danh cho câu trả lời của câu hỏi bảo mật 2',
  `join_date` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Ngày tham gia'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Bảng tài khoản người dùng';

--
-- Dumping data for table `facebook_account`
--

INSERT INTO `facebook_account` (`user_ID`, `first_name`, `last_name`, `email`, `password`, `secq1_ID`, `a_secq1_ID`, `secq2_ID`, `a_secq2_ID`, `join_date`) VALUES
('Boonghuilau', 'Phuong', 'Xa', 'huilau@gmail.com', '12345678', '4', '1', '5', 'hanoi', '2019-10-12 00:00:00'),
('hahip1962', 'Ha', 'Nguyen ', 'hahip@yahoo.com', 'hahip1234', '4', '1', '1', 'fiat', '2019-10-26 21:09:07'),
('hoa1234', 'Hoa', 'Le An', 'hoa@gmail.com', '123456', '1', 'honda', '4', '8', '2019-12-05 22:51:56'),
('tuyet123', 'Tuyet', 'Le Thi', 'tuyet123@yahoo.com', '6789', '1', 'kia', '5', 'hoa binh', '2019-12-05 11:51:21'),
('xaboong1997', 'Hieu Le', 'Nguyen', 'nguyenlemanhhieu@yahoo.com', '123456', '1', 'Honda', '2', 'toet', '2019-10-12 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `facebook_follow`
--

CREATE TABLE `facebook_follow` (
  `user_ID` varchar(100) NOT NULL COMMENT 'ID of current user',
  `follow_user_ID` varchar(100) NOT NULL COMMENT 'ID of user whom current user is following'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facebook_follow`
--

INSERT INTO `facebook_follow` (`user_ID`, `follow_user_ID`) VALUES
('Boonghuilau', 'hahip1962'),
('xaboong1997', 'Boonghuilau'),
('Boonghuilau', 'xaboong1997');

-- --------------------------------------------------------

--
-- Table structure for table `facebook_securityquestions`
--

CREATE TABLE `facebook_securityquestions` (
  `secq_ID` int(100) NOT NULL COMMENT 'Số định danh của câu hỏi bảo mật',
  `question` varchar(2000) CHARACTER SET utf32 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facebook_securityquestions`
--

INSERT INTO `facebook_securityquestions` (`secq_ID`, `question`) VALUES
(1, 'What is the brand of your first car?'),
(2, 'what is your mum nickname?'),
(3, 'what is your dad nickname?'),
(4, 'how many siblings do you have?'),
(5, 'what is the name of your hometown?');

-- --------------------------------------------------------

--
-- Table structure for table `facebook_status`
--

CREATE TABLE `facebook_status` (
  `status_ID` int(11) NOT NULL COMMENT 'status_ID',
  `share_ID` varchar(100) NOT NULL,
  `user_ID` varchar(100) NOT NULL,
  `status_message` varchar(1000) NOT NULL COMMENT 'status ',
  `status_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT 'time user posts his or her status'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facebook_status`
--

INSERT INTO `facebook_status` (`status_ID`, `share_ID`, `user_ID`, `status_message`, `status_time`) VALUES
(102, ' ', 'xaboong1997', 'this is my first post!', '2019-12-07 01:25:53'),
(103, ' ', 'Boonghuilau', 'this is my first post!', '2019-12-07 01:27:55'),
(104, '103', 'xaboong1997', 'this is my first post!', '2019-12-07 01:35:49'),
(105, ' ', 'xaboong1997', '#Vinataba', '2019-12-08 15:58:36'),
(106, ' ', 'xaboong1997', 'no smoking #Vinataba', '2019-12-08 15:58:54'),
(107, ' ', 'xaboong1997', '#manbro', '2019-12-08 16:01:54'),
(108, ' ', 'xaboong1997', 'haha', '2020-02-13 17:04:12');

-- --------------------------------------------------------

--
-- Table structure for table `facebook_trending`
--

CREATE TABLE `facebook_trending` (
  `trending_topic` varchar(200) NOT NULL,
  `trending_count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `facebook_trending`
--

INSERT INTO `facebook_trending` (`trending_topic`, `trending_count`) VALUES
('#Vinataba', 2),
('#manbro', 1);

-- --------------------------------------------------------

--
-- Table structure for table `status_like`
--

CREATE TABLE `status_like` (
  `like_status_ID` varchar(100) NOT NULL COMMENT 'ID of liked status',
  `like_user_ID` varchar(100) NOT NULL COMMENT 'ID of user who like this status'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `status_like`
--

INSERT INTO `status_like` (`like_status_ID`, `like_user_ID`) VALUES
('107', 'xaboong1997'),
('105', 'xaboong1997');

-- --------------------------------------------------------

--
-- Table structure for table `status_reply`
--

CREATE TABLE `status_reply` (
  `reply_status_ID` varchar(100) NOT NULL,
  `reply_user_ID` varchar(100) NOT NULL,
  `reply_message` varchar(100) NOT NULL,
  `reply_timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `status_reply`
--

INSERT INTO `status_reply` (`reply_status_ID`, `reply_user_ID`, `reply_message`, `reply_timestamp`) VALUES
('102', 'Boonghuilau', 'hello xaboong1997', '2019-12-06 18:26:38'),
('102', 'Boonghuilau', 'hello xaboong1997', '2019-12-06 18:26:44'),
('106', 'xaboong1997', 'no smoking #manbro', '2019-12-08 09:01:40'),
('107', 'xaboong1997', '#manbro', '2019-12-08 09:11:30'),
('107', 'xaboong1997', '#manbro', '2019-12-08 09:14:45'),
('107', 'xaboong1997', 'manbro', '2019-12-08 09:14:52'),
('103', 'xaboong1997', 'hello boong', '2019-12-08 09:15:25'),
('103', 'xaboong1997', 'good afternoon boong', '2019-12-08 09:17:07'),
('103', 'xaboong1997', 'good afternoon boong', '2019-12-08 09:27:22'),
('103', 'xaboong1997', '', '2019-12-08 09:28:09'),
('107', 'xaboong1997', 'good afternoon boong', '2019-12-08 09:28:26'),
('103', 'xaboong1997', 'good afternoon boong', '2019-12-08 09:28:41'),
('103', 'xaboong1997', 'good afternoon boong', '2019-12-08 09:29:08'),
('107', 'xaboong1997', 'good night', '2019-12-08 09:33:07'),
('107', 'xaboong1997', 'halo', '2019-12-08 09:33:18'),
('103', 'xaboong1997', 'halo', '2019-12-08 09:33:31'),
('108', 'xaboong1997', 'haha', '2020-02-13 10:04:24');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `facebook_account`
--
ALTER TABLE `facebook_account`
  ADD PRIMARY KEY (`user_ID`);

--
-- Indexes for table `facebook_securityquestions`
--
ALTER TABLE `facebook_securityquestions`
  ADD PRIMARY KEY (`secq_ID`);

--
-- Indexes for table `facebook_status`
--
ALTER TABLE `facebook_status`
  ADD PRIMARY KEY (`status_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `facebook_status`
--
ALTER TABLE `facebook_status`
  MODIFY `status_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'status_ID', AUTO_INCREMENT=109;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
