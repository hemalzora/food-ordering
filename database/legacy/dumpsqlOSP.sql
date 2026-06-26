/*
SQLyog Enterprise - MySQL GUI v8.02 RC
MySQL - 5.5.24-log : Database - trainingsummer
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`trainingsummer` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `trainingsummer`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `admid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(25) NOT NULL,
  `phno` varchar(11) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`admid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Table structure for table `blog` */

DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog` (
  `blogid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(25) NOT NULL,
  `description` mediumtext NOT NULL,
  `cusid` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `dateofadding` date NOT NULL,
  PRIMARY KEY (`blogid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

/*Table structure for table `cart` */

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
  `custid` int(11) NOT NULL,
  `menuitemid` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `price` decimal(9,2) NOT NULL,
  `total` decimal(9,2) NOT NULL,
  `tempid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `itemname` varchar(25) NOT NULL,
  PRIMARY KEY (`tempid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `cusid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(25) NOT NULL,
  `phno` varchar(11) DEFAULT NULL,
  `gender` varchar(10) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cusid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

/*Table structure for table `feedback` */

DROP TABLE IF EXISTS `feedback`;

CREATE TABLE `feedback` (
  `fedid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(23) NOT NULL,
  `description` mediumtext NOT NULL,
  `resid` int(11) NOT NULL,
  `cusid` int(11) NOT NULL,
  PRIMARY KEY (`fedid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `menuitemid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `type` varchar(25) NOT NULL,
  `price` decimal(9,2) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `resid` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`menuitemid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

/*Table structure for table `orderdetails` */

DROP TABLE IF EXISTS `orderdetails`;

CREATE TABLE `orderdetails` (
  `tempid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `menuitemid` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` decimal(9,2) DEFAULT NULL,
  `orderid` int(11) DEFAULT NULL,
  `total` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`tempid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `orderid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cusid` int(11) NOT NULL,
  `totalamount` decimal(9,2) NOT NULL,
  `orderstatus` varchar(15) NOT NULL DEFAULT 'pending',
  `datetimeoforder` date NOT NULL,
  `address` varchar(1000) NOT NULL,
  `modeofpayment` varchar(20) NOT NULL,
  `resid` int(11) DEFAULT NULL,
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Table structure for table `restaurant` */

DROP TABLE IF EXISTS `restaurant`;

CREATE TABLE `restaurant` (
  `resid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(25) NOT NULL,
  `phno` varchar(11) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`resid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
