ALTER TABLE bms.`bd_student_info` 
ADD COLUMN `address_edit_time` DATETIME  COLLATE utf8_general_ci NULL COMMENT '教材地址更新的时间' AFTER `address`;