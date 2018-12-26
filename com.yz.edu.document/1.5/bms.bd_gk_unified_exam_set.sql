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

/*Table structure for table `bd_gk_unified_exam_set` */

DROP TABLE IF EXISTS `bd_gk_unified_exam_set`;

CREATE TABLE `bd_gk_unified_exam_set` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `title` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `describe` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `start_time` datetime DEFAULT NULL COMMENT '报名缴费开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '报名缴费结束时间',
  `test_subject` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '考试科目',
  `operation_desc` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '报名详细操作流程',
  `file_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件路径',
  `file_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件名',
  `if_show` char(2) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否可用1:是 0:否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
  `update_user_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='国开统考设置';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
