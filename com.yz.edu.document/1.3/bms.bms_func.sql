INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '国开资料审批', '2', '/gkCheckInfo/toGkStudentList.do', 'gkCheckInfo', '1754780369119127943', NULL, '2018-05-10 19:24:32', '杜凯', '1209', '100');

INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '查看审核资料', '3', '', 'annex_check:query', '152595149364716667', NULL, '2017-06-26 17:18:51', NULL, NULL, '1');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '更新附件图片', '3', '', 'annex_check:update_annex', '152595149364716667', '', '2017-06-27 01:13:36', NULL, NULL, '2');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '删除附件图片', '3', '', 'annex_check:delete', '152595149364716667', '', '2017-06-27 01:13:36', NULL, NULL, '3');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '审核附件图片', '3', '', 'annex_check:charge', '152595149364716667', '', '2017-06-27 01:13:36', NULL, NULL, '4');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '审核国开资料', '3', '', 'annex_check:check', '152595149364716667', NULL, '2017-07-01 16:02:03', NULL, NULL, '1');
INSERT INTO `bms`.`bms_func` (`func_id`, `func_name`, `func_type`, `func_url`, `func_code`, `p_id`, `icon`, `update_time`, `update_user`, `update_user_id`, `order_num`) 
VALUES (seq(), '更新学员信息', '3', '', 'annex_check:update_info', '152595149364716667', NULL, '2017-07-01 17:33:44', NULL, NULL, '1');







---add by zhuliping --------------------------------
insert into bms_func(func_id,func_name,func_type,func_url,func_code,p_id,icon,update_time,update_user,update_user_id,order_num)
values(seq(),'教师删除','3','','teacher:delete','25154774491988440','',now(),'朱丽平','1754661637538743424','100');
insert into bms_func(func_id,func_name,func_type,func_url,func_code,p_id,icon,update_time,update_user,update_user_id,order_num)
values(seq(),'Excel导出','3','','teacher:export','25154774491988440','',now(),'朱丽平','1754661637538743424','100');

---add by zhuliping --------------------------------