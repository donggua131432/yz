ALTER TABLE `us_base_info`
ADD COLUMN `intention_type`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '意向类型' AFTER `relation`;
