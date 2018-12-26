CREATE TABLE `us_student_remark` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户id',
  `std_id` varchar(50) DEFAULT NULL COMMENT '学员id',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-学员扩展信息表';
