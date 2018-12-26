CREATE TABLE `us_task_card` (
  `id` varchar(50) NOT NULL COMMENT '领取任务ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `task_id` varchar(50) NOT NULL COMMENT '任务ID',
  `task_count` varchar(10) NOT NULL COMMENT '任务数',
  `complete_count` varchar(10) DEFAULT '0' COMMENT '完成数',
  `complete_status` char(1) DEFAULT '0' COMMENT '任务完成状态 (0:未完成，1:已完成)',
  `is_award` char(1) DEFAULT '0' COMMENT '奖励是否已赠送(0:未赠送，1:已经赠送)',
  `complete_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  `create_time` datetime DEFAULT NULL COMMENT '任务领取时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_USER_TASK_ID` (`user_id`,`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户领取任务卡表';