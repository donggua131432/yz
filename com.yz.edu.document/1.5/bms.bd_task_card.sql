CREATE TABLE `bd_task_card` (
  `task_id` varchar(50) NOT NULL COMMENT '任务ID',
  `task_type` varchar(3) DEFAULT NULL COMMENT '任务类型 数据字典 taskCardType',
  `task_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `task_target` varchar(10) DEFAULT NULL COMMENT '任务目标',
  `task_reward` varchar(10) DEFAULT NULL COMMENT '任务奖励',
  `task_status` char(1) DEFAULT '1' COMMENT '任务状态  数据字典 taskStatus',
  `is_overlap` char(1) DEFAULT '0' COMMENT '是否叠加 (0 不叠加  1叠加)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务卡';