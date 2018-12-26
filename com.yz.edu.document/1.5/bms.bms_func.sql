/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152826936966202386','国开统考设置','2','studentGKUnifiedExam/tolist.do','gKUnifiedExam','1754780369119127943',NULL,'2018-06-06 15:16:10','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152826941015817587','查看国开统考设置','3','','gkUnifiedExam:query','152826936966202386',NULL,'2018-06-06 15:16:51','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152835549853523852','新增国开统考设置','3','','gkUnifiedExam:insert','152826936966202386',NULL,'2018-06-07 15:11:40','刘欣','1085','100');


INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) VALUES ('152836180681746052', '任务卡', '2', '/taskCard/toList.do', 'taskCard', '25154774491990548', NULL, '2018-06-07 16:56:59', '倪宇鹏', '25131292815264224', '110');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) VALUES ('152836577575376680', '任务卡查询', '3', '', 'taskCard:query', '152836180681746052', NULL, '2018-06-07 18:03:08', '倪宇鹏', '25131292815264224', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) VALUES ('152836581053640566', '任务卡修改', '3', '', 'taskCard:update', '152836180681746052', NULL, '2018-06-07 18:03:42', '倪宇鹏', '25131292815264224', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) VALUES ('152836583798434252', '任务卡删除', '3', '', 'taskCard:delete', '152836180681746052', NULL, '2018-06-07 18:04:10', '倪宇鹏', '25131292815264224', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) VALUES ('152844288757910431', '任务卡新增', '3', '', 'taskCard:insert', '152836180681746052', NULL, '2018-06-08 15:28:20', '倪宇鹏', '25131292815264224', '100');
