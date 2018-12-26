=================================dk=============================
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '校区组管理', '2', '/campusGroup/toCampusGroupList.do', 'campusGroup', '25131292815268608', NULL, '2018-05-23 11:07:31', '杜凯', '1209', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '校区组查询', '3', '', 'campusGroup:query', '152704487414513683', NULL, '2018-05-31 10:58:29', '杜凯', '1209', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '校区组编辑', '3', '', 'campusGroup:insert', '152704487414513683', NULL, '2018-05-31 10:58:57', '杜凯', '1209', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '校区组删除', '3', '', 'campusGroup:delete', '152704487414513683', NULL, '2018-05-31 10:59:40', '杜凯', '1209', '100');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '分配跟进人', '3', '', 'invite_user:assign', '25201389080674779', NULL, '2018-05-31 15:57:52', '杜凯', '1209', '100');

===============================liuxin==========================


insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152731942922644760','导出休学申请','3','','quitSchoolCheck:export','152724393402627582',NULL,'2018-05-26 15:23:49','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152724772611023074','执行审批','3','','quitSchoolCheck:check','152724393402627582',NULL,'2018-05-25 19:28:48','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152724407085902041','查看申请信息','3','','quitSchoolCheck:query','152724393402627582',NULL,'2018-05-25 18:27:52','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152724393402627582','休学审批','2','quitSchoolCheck/list.do','quitSchoolCheck','25131292815263385',NULL,'2018-05-25 18:25:36','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152724229227253032','撤销申请','3','','quitSchool:delete','152721843737050867',NULL,'2018-05-25 17:58:14','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152722055715870858','添加休学申请','3','','quitSchool:insert','152721843737050867',NULL,'2018-05-25 11:55:59','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152721845993476855','查看休学申请','3','','quitSchool:query','152721843737050867',NULL,'2018-05-25 11:21:01','刘欣','1085','100');
insert into `bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) values('152721843737050867','休学申请','2','/quitSchool/list.do','quitSchool','25131292815263385',NULL,'2018-05-25 11:20:39','刘欣','1085','100');


insert into bms_func(func_id,func_name,func_type,func_url,func_code,p_id,icon,update_time,update_user,update_user_id,order_num)
values(seq(),'考试科目Excel导入','3','','teachPlan:testsubjectupload','25093320472528767','',now(),'朱丽平','1754661637538743424','100');