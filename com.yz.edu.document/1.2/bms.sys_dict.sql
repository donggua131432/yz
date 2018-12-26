/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.35-80.0-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroup','0','奖励规则分组信息',NULL,'奖励规则分组信息','0','1','2018-05-05 15:14:06',NULL,NULL,NULL,'2018-05-05 15:14:06',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroup.charge','ruleGroup','充值缴费规则','charge',NULL,'2','1','2018-05-05 15:16:30',NULL,NULL,NULL,'2018-05-05 15:16:30',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroup.enroll','ruleGroup','报读奖励','enroll',NULL,'3','1','2018-05-05 15:17:36',NULL,NULL,NULL,'2018-05-05 15:17:36',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroup.register','ruleGroup','注册奖励','register',NULL,'1','1','2018-05-05 15:15:43',NULL,NULL,NULL,'2018-05-05 15:15:43',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList','0','奖励规则组属性',NULL,NULL,'0','1','2018-05-07 11:13:08',NULL,NULL,NULL,'2018-05-07 11:13:08',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.invit','ruleGroupAttrList','邀约赠送','invit',NULL,'2','1','2018-05-09 17:16:32',NULL,NULL,NULL,'2018-05-09 17:16:32',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge2','ruleGroupAttrList','优惠类型(1:普通全额 2:全额奖学金 3：东莞奖学金)','scholarship',NULL,'2','1','2018-05-09 17:26:32',NULL,NULL,NULL,'2018-05-09 17:26:32',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge3','ruleGroupAttrList','招生类型(1:成教  2：国开)','recruitType',NULL,'3','1','2018-05-09 17:28:23',NULL,NULL,NULL,'2018-05-09 17:28:23',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge4','ruleGroupAttrList','费项编码（Y0 Y1 Y2 ）','itemCode',NULL,'4','1','2018-05-09 17:30:01',NULL,NULL,NULL,'2018-05-09 17:30:01',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge5','ruleGroupAttrList','学年（1：第一学年 2：第二学年）','itemYear',NULL,'5','1','2018-05-09 17:31:51',NULL,NULL,NULL,'2018-05-09 17:31:51',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge6','ruleGroupAttrList','学员届级（0：本届学员  大于0：历届学员）','lSize',NULL,'6','1','2018-05-09 17:43:53',NULL,NULL,NULL,'2018-05-09 17:43:53',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge7','ruleGroupAttrList','年级（2015：2015级  2016:2016级）','grade',NULL,'7','1','2018-05-09 17:44:49',NULL,NULL,NULL,'2018-05-09 17:44:49',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge8','ruleGroupAttrList','支付时间（如：2018-05-19 00:00:00  支付时间在这个时间之前就符合）','payDateTime',NULL,'8','1','2018-05-09 17:46:05',NULL,NULL,NULL,'2018-05-09 17:46:05',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.recharge9','ruleGroupAttrList','送给上级还是自身（0：自身     1：上级）','isParent',NULL,'9','1','2018-05-11 15:33:41',NULL,NULL,NULL,'2018-05-11 15:33:41',NULL,NULL,NULL,NULL,NULL);
insert into `sys_dict` (`dict_id`, `p_id`, `dict_name`, `dict_value`, `description`, `order_num`, `is_enable`, `update_time`, `update_user`, `update_user_id`, `create_user_id`, `create_time`, `create_user`, `ext_1`, `ext_2`, `ext_3`, `ext_4`) values('ruleGroupAttrList.register','ruleGroupAttrList','智米赠送倍数(不填或填0默认取规则赠送的数量，否则就按倍数计算)','multiple',NULL,'1','1','2018-05-07 11:16:09',NULL,NULL,NULL,'2018-05-07 11:16:09',NULL,NULL,NULL,NULL,NULL);
