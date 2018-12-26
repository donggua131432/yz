
DROP TABLE IF EXISTS `oa_student_examcityaffirm_gk_task`;
CREATE TABLE `oa_student_examcityaffirm_gk_task` (
  `affirm_id` varchar(50) NOT NULL COMMENT '考场城市确认任务ID',
  `task_id` varchar(50) NOT NULL COMMENT '任务ID',
  `ey_id` varchar(50) DEFAULT NULL COMMENT '考试年度id-关联表bd_exam_year_gk',
  `learn_id` varchar(50) NOT NULL COMMENT '学业ID',
  `ec_id` varchar(50) DEFAULT NULL COMMENT '考场城市ID-关联表bd_exam_city_gk',
  `is_exam` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否考试 1-是 0-否',
  `is_affirm` varchar(10) DEFAULT '0' COMMENT '是否确认 1-是 0-否',
  `is_view` tinyint(4) DEFAULT '0' COMMENT '是否查看 0-未查看 1-已查看',
  `view_time` datetime DEFAULT NULL COMMENT '查看时间',
  `is_reset` tinyint(4) DEFAULT '0' COMMENT '是否重置 0-未重置 1-已重置',
  `reset_time` datetime DEFAULT NULL COMMENT '重置时间',
  `reason` varchar(1000) DEFAULT NULL COMMENT '未确认原因',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '最后更新人名称',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '最后更新人ID',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`affirm_id`),
  KEY `idx_learn_id` (`learn_id`) USING BTREE,
  KEY `idx_py_id` (`ey_id`) USING BTREE,
  KEY `idx_ey_id` (`ec_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国开考场城市确认任务';


