
CREATE TABLE `oa_student_graduateexam_gk_task_sub` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `follow_id` varchar(50) NOT NULL COMMENT '统考跟进任务ID',
  `test_area` varchar(50) DEFAULT NULL COMMENT '考试县区',
  `is_payreg` varchar(10) DEFAULT '0' COMMENT '是否缴费报名 1-是 0-否',
  `enroll_subject` varchar(50) DEFAULT NULL COMMENT '报名科目  1-大学英语B  2-计算机应用基础',
  `test_time` datetime DEFAULT NULL COMMENT '考试时间',
  `test_address` varchar(50) DEFAULT NULL COMMENT '考试地点',
  `is_test` varchar(10) DEFAULT '0' COMMENT '是否参考 1-是 0-否',
  `is_pass` varchar(10) DEFAULT '0' COMMENT '是否合格 1-是 0-否',
  `is_ccaa` varchar(10) DEFAULT '0' COMMENT '是否考前辅导 1-是 0-否',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_follow_id` (`follow_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国开本科统考科目跟进';