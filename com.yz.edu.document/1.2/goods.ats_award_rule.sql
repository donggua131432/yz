/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `ats_award_rule` (
	`rule_group` varchar (90),
	`rule_code` varchar (96),
	`rule_type` varchar (15),
	`rule_desc` varchar (765),
	`is_allow` char (3),
	`zhimi_count` varchar (36),
	`exp_count` varchar (36),
	`start_time` datetime ,
	`end_time` datetime ,
	`is_mutex` tinyint (4),
	`is_repeat` tinyint (4),
	`sort` smallint (6),
	`update_user` varchar (150),
	`update_user_id` varchar (150),
	`update_time` datetime ,
	`create_user` varchar (150),
	`create_user_id` varchar (150),
	`create_time` datetime 
); 
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_1','1','邀请2017年11月2号以后录入的被邀约人缴费','1','0','0','2017-11-02 00:00:00','2019-04-01 00:00:00','1','1','1','张光红','457743676863409845','2018-05-11 17:26:14','张光红','457743676863409845','2018-05-09 17:48:59');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_10','1','2016/2017级学员缴费，按照缴费金额1:3赠送智米','1','0','0','2017-12-15 00:00:00','2099-12-30 23:59:59','1','1','10','刘欣','1085','2018-05-11 15:55:06','张光红','457743676863409845','2018-05-10 09:46:12');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_11','1','2017级以后学员缴费，按照缴费金额1:1赠送智米','1','0','0','2017-12-15 00:00:00','2099-12-30 23:59:59','1','1','11','张光红','457743676863409845','2018-05-11 16:02:17','张光红','457743676863409845','2018-05-10 09:52:41');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_2','1','推荐2016年8月17日中午12时以后录入的被推荐人，报读成教普通全额并缴纳辅导费后','1','5000','0','2016-08-17 12:00:00','2017-11-01 00:00:00','1','1','2','张光红','457743676863409845','2018-05-10 09:19:20','张光红','457743676863409845','2018-05-10 09:19:20');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_3','1','推荐2016年8月17日中午12时以后录入的被推荐人，报读成教奖学金并缴纳辅导费后','1','5000','0','2016-08-17 12:00:00','2017-11-01 00:00:00','1','1','3','张光红','457743676863409845','2018-05-10 09:25:34','张光红','457743676863409845','2018-05-10 09:25:34');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_4','1','推荐2016年8月17日中午12时以后录入的被推荐人，报读国家开放大学 并缴纳第一年费用','1','5000','0','2016-08-17 12:00:00','2017-11-01 00:00:00','1','1','4','张光红','457743676863409845','2018-05-10 09:32:43','张光红','457743676863409845','2018-05-10 09:28:21');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_5','1','邀请2017年6月份以后录入的被邀约人，报读成教普通全额并缴纳辅导费','1','5000','0','2017-05-31 00:00:00','2017-11-01 00:00:00','1','1','5','张光红','457743676863409845','2018-05-11 17:25:57','张光红','457743676863409845','2018-05-10 09:31:11');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_6','1','邀请2017年6月以后录入的被邀约人，报读成教圆梦计划并缴纳辅导费后','1','5000','0','2017-05-31 00:00:00','2017-11-01 00:00:00','1','1','6','张光红','457743676863409845','2018-05-10 09:33:32','张光红','457743676863409845','2018-05-10 09:32:26');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_7','1','邀请2017年6月以后录入的被推荐人，报读成教奖学金并缴纳辅导费后','1','5000','0','2017-05-31 00:00:00','2017-11-01 00:00:00','1','1','7','张光红','457743676863409845','2018-05-10 09:35:05','张光红','457743676863409845','2018-05-10 09:35:05');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_8','1','邀约2017年6月以后录入的被邀约人，报读国家开放大学 并缴纳第一年费用','1','5000','0','2017-05-31 00:00:00','2017-11-01 00:00:00','1','1','8','张光红','457743676863409845','2018-05-10 09:36:49','张光红','457743676863409845','2018-05-10 09:36:49');
insert into `ats_award_rule` (`rule_group`, `rule_code`, `rule_type`, `rule_desc`, `is_allow`, `zhimi_count`, `exp_count`, `start_time`, `end_time`, `is_mutex`, `is_repeat`, `sort`, `update_user`, `update_user_id`, `update_time`, `create_user`, `create_user_id`, `create_time`) values('charge','recharge_9','1','邀请在2017年08月21日之后2017年09月10日之前录入的被邀约人，报读成教考前冲刺并缴纳第一年学费','1','50000','0','2017-08-21 00:00:00','2017-09-10 23:59:59','1','1','9','张光红','457743676863409845','2018-05-10 09:40:05','张光红','457743676863409845','2018-05-10 09:40:05');
