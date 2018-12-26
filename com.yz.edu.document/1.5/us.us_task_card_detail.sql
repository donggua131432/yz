CREATE TABLE `us_task_card_detail` (
  `id` varchar(50) NOT NULL COMMENT '序号',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `task_id` varchar(50) DEFAULT NULL COMMENT '任务卡ID',
  `trigger_user_id` varchar(50) DEFAULT NULL COMMENT '被邀约人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_GROUP_ID` (`user_id`,`task_id`,`trigger_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务卡详情';