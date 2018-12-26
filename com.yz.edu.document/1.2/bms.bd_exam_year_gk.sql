
DROP TABLE IF EXISTS `bd_exam_year_gk`;
CREATE TABLE `bd_exam_year_gk` (
  `ey_id` varchar(50) NOT NULL COMMENT '年度ID',
  `exam_year` varchar(32) DEFAULT NULL COMMENT '考场年度',
  `tips` text COMMENT '温馨提示',
  `status` char(1) DEFAULT NULL COMMENT '状态  1-启用  2-禁用',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '最后更新人名称',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '最后更新人ID',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`ey_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into bd_exam_year_gk(ey_id,exam_year)VALUES(seq(),'2017[国秋]');
insert into bd_exam_year_gk(ey_id,exam_year)VALUES(seq(),'2018[国春]');

