# Host: 127.0.0.1  (Version: 5.6.7-rc)
# Date: 2013-01-09 16:30:45
# Generator: MySQL-Front 5.3  (Build 1.27)

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;

DROP DATABASE IF EXISTS `intalker`;
CREATE DATABASE `intalker` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `intalker`;

#
# Source for table "book"
#

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(20) DEFAULT NULL,
  `ownerid` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `preview` varchar(32) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `publiclevel` enum('all','myfriends','specified','onlyme') DEFAULT 'all',
  `status` enum('available',' unavailable') DEFAULT 'available',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Source for table "bookinfo"
#

DROP TABLE IF EXISTS `bookinfo`;
CREATE TABLE `bookinfo` (
  `isbn` varchar(20) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `publisher` varchar(50) DEFAULT NULL,
  `pagecount` int(11) DEFAULT NULL,
  `language` varchar(32) DEFAULT NULL,
  `reflink` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Source for table "openid"
#

DROP TABLE IF EXISTS `openid`;
CREATE TABLE `openid` (
  `localid` int(11) DEFAULT NULL,
  `source` enum('weibo','tencent') DEFAULT NULL,
  `externalid` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Source for table "session"
#

DROP TABLE IF EXISTS `session`;
CREATE TABLE `session` (
  `id` varchar(36) NOT NULL DEFAULT '',
  `uid` int(11) DEFAULT NULL,
  `datetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Source for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(45) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `registertime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `permission` enum('normal','admin','block') NOT NULL DEFAULT 'normal',
  `localpwd` varchar(32) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
