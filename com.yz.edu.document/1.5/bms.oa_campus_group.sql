ALTER TABLE `oa_campus_group`
ADD COLUMN `update_user`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人名称' AFTER `create_time`,
ADD COLUMN `update_user_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人' AFTER `update_user`;