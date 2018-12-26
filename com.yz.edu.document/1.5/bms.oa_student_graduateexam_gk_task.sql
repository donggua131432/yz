
CREATE TABLE `oa_student_graduateexam_gk_task` (
  `follow_id` varchar(50) NOT NULL COMMENT '统考跟进任务ID',
  `task_id` varchar(50) NOT NULL COMMENT '任务ID',
  `learn_id` varchar(50) NOT NULL COMMENT '学业ID',
  `notification` varchar(50) DEFAULT '0' COMMENT '通知情况--- 对应字典：notification',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `is_view` tinyint(4) DEFAULT '0' COMMENT '是否查看 0-未查看 1-已查看',
  `view_time` datetime DEFAULT NULL COMMENT '查看时间',
  `is_reset` tinyint(4) DEFAULT '0' COMMENT '是否重置 0-未重置 1-已重置',
  `reset_time` datetime DEFAULT NULL COMMENT '重置时间',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '最后更新人名称',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '最后更新人ID',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`follow_id`) USING BTREE,
  KEY `idx_learn_id` (`learn_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国开本科统考跟进';

