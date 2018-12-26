/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.6.35-80.0-log : Database - us
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`us` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `us`;

/*Table structure for table `yz_bad_payment` */

DROP TABLE IF EXISTS `yz_bad_payment`;

CREATE TABLE `yz_bad_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_no` varchar(50) NOT NULL COMMENT '支付单号',
  `trans_no` varchar(50) NOT NULL COMMENT '第三方交易单号',
  `amount` varchar(10) NOT NULL COMMENT '支付金额',
  `pay_type` varchar(10) NOT NULL COMMENT '支付方式',
  `deal_type` varchar(50) NOT NULL COMMENT '处理类型',
  `deal_addr` varchar(50) DEFAULT NULL,
  `error_msg` varchar(200) DEFAULT NULL COMMENT '错误消息',
  `execute_count` int(11) NOT NULL DEFAULT '0' COMMENT '处理次数',
  `last_execute_date` datetime DEFAULT NULL COMMENT '最后一次执行时间',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
