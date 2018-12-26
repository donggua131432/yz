/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `bd_student_quit_apply` (
	`id` varchar (288),
	`std_id` varchar (288),
	`learn_id` varchar (288),
	`recruit_type` char (18),
	`std_stage` char (18),
	`start_time` datetime ,
	`end_time` datetime ,
	`apply_user_id` varchar (288),
	`apply_user_name` varchar (288),
	`apply_time` datetime ,
	`file_name` varchar (288),
	`file_url` varchar (288),
	`remark` varchar (2295),
	`check_status` char (18),
	`oper_user_id` varchar (288),
	`oper_user_name` varchar (288),
	`oper_time` datetime ,
	`reason` char (18),
	`reject_reason` varchar (2295)
); 
