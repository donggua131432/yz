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

/*Table structure for table `rep_channel_qrcode` */

DROP TABLE IF EXISTS `rep_channel_qrcode`;

CREATE TABLE `rep_channel_qrcode` (
  `channel_id` varchar(50) NOT NULL COMMENT '渠道编号',
  `emp_id` varchar(50) DEFAULT NULL COMMENT '邀约人',
  `invite_qrcode_url` varchar(200) NOT NULL COMMENT '二维码地址',
  `invite_url` varchar(200) NOT NULL COMMENT '邀约地址',
  `invite_name` varchar(50) NOT NULL COMMENT '邀约名称(inviteQrType)',
  `defaultUrl` varchar(200) NOT NULL COMMENT '邀约活动的默认地址',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `is_del` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
