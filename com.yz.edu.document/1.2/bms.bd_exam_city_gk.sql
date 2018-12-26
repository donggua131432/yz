DROP TABLE IF EXISTS `bd_exam_city_gk`;

CREATE TABLE `bd_exam_city_gk` (
  `ec_id` varchar(50) NOT NULL COMMENT '考场城市ID',
  `ec_name` varchar(50) DEFAULT NULL COMMENT '考场城市名称',
  `status` varchar(2) DEFAULT '1' COMMENT '1-启用 2-禁用',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '最后更新人名称',
  `update_user_id` varchar(50) DEFAULT NULL COMMENT '最后更新人ID',
  `create_user_id` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `ext_1` varchar(100) DEFAULT NULL,
  `ext_2` varchar(100) DEFAULT NULL,
  `ext_3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国开考场城市表';

insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'惠州市辖');
insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'阳江市辖');
insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'河源源城');
insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'河源龙川');
insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'梅州市辖');
insert into bd_exam_city_gk(ec_id,ec_name)VALUES(seq(),'东莞市辖');
