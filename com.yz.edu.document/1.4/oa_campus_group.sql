CREATE TABLE `oa_campus_group` (
  `id` varchar(50) NOT NULL COMMENT '序号',
  `campus_group_id` varchar(3) DEFAULT NULL COMMENT '分组ID',
  `campus_id` varchar(50) DEFAULT NULL COMMENT '校区ID',
  `ext1` varchar(100) DEFAULT NULL COMMENT '扩展字段',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='校区分组';