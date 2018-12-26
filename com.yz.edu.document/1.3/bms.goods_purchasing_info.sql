/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `goods_purchasing_info` (
	`id` varchar (150),
	`apply_name` varchar (60),
	`apply_reason` varchar (765),
	`annex_path` varchar (300),
	`goods_sku_id` varchar (150),
	`goods_name` varchar (765),
	`goods_num` int (10),
	`goods_price` varchar (36),
	`total_price` varchar (36),
	`receive_name` varchar (60),
	`receive_mobile` varchar (60),
	`province` varchar (150),
	`city` varchar (150),
	`district` varchar (150),
	`street` varchar (150),
	`province_name` varchar (150),
	`city_name` varchar (150),
	`district_name` varchar (150),
	`street_name` varchar (150),
	`address` varchar (150),
	`email` varchar (150),
	`jd_goods_type` varchar (150),
	`remark` varchar (765),
	`if_success` varchar (30),
	`jd_order_no` varchar (150),
	`oper_user_name` varchar (150),
	`oper_user_id` varchar (150),
	`oper_time` datetime 
); 
