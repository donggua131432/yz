ALTER TABLE `bd_learn_info`
MODIFY COLUMN `is_data_check`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已审核考前资料 0-否 1-是 2-已驳回  3:国开初审 4:国开二审  5:国开三审' AFTER `is_data_completed`;
