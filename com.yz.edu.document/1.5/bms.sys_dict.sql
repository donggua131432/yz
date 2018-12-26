##########################DK###############################
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskStatus', '0', '任务卡状态', NULL, NULL, '0', '1', '2018-06-07 15:03:38', NULL, NULL, NULL, '2018-06-07 15:03:38', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskStatus.1', 'taskStatus', '未发布', '1', NULL, '1', '1', '2018-06-07 15:04:22', NULL, NULL, NULL, '2018-06-07 15:04:22', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskStatus.2', 'taskStatus', '已发布', '2', NULL, '2', '1', '2018-06-07 15:04:51', NULL, NULL, NULL, '2018-06-07 15:04:51', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskStatus.3', 'taskStatus', '已开始', '3', NULL, '3', '1', '2018-06-07 15:05:18', NULL, NULL, NULL, '2018-06-07 15:05:18', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskStatus.4', 'taskStatus', '已结束', '4', NULL, '4', '1', '2018-06-07 15:05:41', NULL, NULL, NULL, '2018-06-07 15:05:41', NULL, NULL, NULL, NULL, NULL);


INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskCardType', '0', '任务卡类型', NULL, NULL, '0', '1', '2018-06-07 15:00:45', NULL, NULL, NULL, '2018-06-07 15:00:45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('taskCardType.1', 'taskCardType', '邀约推荐任务', '1', NULL, '1', '1', '2018-06-07 15:02:22', NULL, NULL, NULL, '2018-06-07 15:02:22', NULL, NULL, NULL, NULL, NULL);


INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('isOverlap', '0', '是否叠加', NULL, NULL, '0', '1', '2018-06-08 16:27:53', NULL, NULL, NULL, '2018-06-08 16:27:53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('isOverlap.0', 'isOverlap', '非叠加', '0', NULL, '0', '1', '2018-06-08 16:28:20', NULL, NULL, NULL, '2018-06-08 16:28:20', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `bms`.`sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) 
VALUES ('isOverlap.1', 'isOverlap', '叠加', '1', NULL, '1', '1', '2018-06-08 16:28:35', NULL, NULL, NULL, '2018-06-08 16:28:35', NULL, NULL, NULL, NULL, NULL);



######################Zhuliping###########################################

insert into sys_dict(dict_id,p_id,dict_name,dict_value,description,order_num,is_enable,update_time,create_time)
VALUES('notification','0','通知情况','','','0','1',now(),now());
insert into sys_dict(dict_id,p_id,dict_name,dict_value,description,order_num,is_enable,update_time,create_time)
VALUES('notification.0','notification','未确认','0','','1','1',now(),now());
insert into sys_dict(dict_id,p_id,dict_name,dict_value,description,order_num,is_enable,update_time,create_time)
VALUES('notification.1','notification','报名','1','','2','1',now(),now());
insert into sys_dict(dict_id,p_id,dict_name,dict_value,description,order_num,is_enable,update_time,create_time)
VALUES('notification.2','notification','下次报名','2','','3','1',now(),now());
