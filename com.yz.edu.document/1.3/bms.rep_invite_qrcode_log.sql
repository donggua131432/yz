/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log : Database - bms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bms` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bms`;

/*Table structure for table `rep_invite_qrcode_log` */

DROP TABLE IF EXISTS `rep_invite_qrcode_log`;

CREATE TABLE `rep_invite_qrcode_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel_id` varchar(50) DEFAULT NULL COMMENT '渠道',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机号',
  `type` varchar(5) DEFAULT NULL COMMENT '类型:1:浏览 2:注册 3:缴费',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `qrcode_log` varchar(500) DEFAULT NULL COMMENT '采集日志',
  `qrcode_url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
