-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 11, 2019 at 12:40 AM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `comp3095`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userid` int(11) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `verificationkey` varchar(255) DEFAULT NULL,
  `verified` int,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `roles`(
	`roleid` int(11) NOT NULL,
	`role` varchar(20) DEFAULT NULL,
	PRIMARY KEY (`roleid`)
	)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `UserRole`(
	`userid` int(11) NOT NULL,
	`roleid` int(11) NOT NULL,
	primary key (`userid`, `roleid`)
	)ENGINE=InnoDB DEFAULT CHARSET=latin1;
	
--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userid`, `firstname`, `lastname`, `address`, `email`, `created`, `verificationkey`, `verified`, `password`) VALUES
(1, 'Admin', 'Admin', '123 Any St', 'admin@isp.net', '2019-10-09 22:33:26', '123abc', 1, '1000:1eb6e97ec6b9b1768040c42cf9689d7f:1ec970160b4104d56b98fdd911d022732ef31a37b21515d9c393b1779a422e7e0cbd49d06c98cf24a1f47dde4301c70910f840fcac6fbae5a0dcf149e6cfa0c3');
-- Sergio: Password for admin is set as P@ssword1

INSERT INTO `roles` (`roleid`, `role`) VALUES
(1, 'Admin'), (2, 'Client');

INSERT INTO `UserRole` (`userid`, `roleid`) VALUES
(1, 1); 


--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
/*ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`);
ALTER TABLE `roles`
  ADD PRIMARY KEY (`roleid`);*/
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
