USE `bms` ;
#修改子流水优惠券信息字段格式
ALTER TABLE `bd_sub_serial` 
  CHANGE `coupon_id` `coupon_id` TEXT COLLATE utf8_general_ci NULL COMMENT '优惠券编码' AFTER `sub_serial_status`,
  CHANGE `coupon_name` `coupon_name` TEXT COLLATE utf8_general_ci NULL COMMENT '学员优惠券名称' AFTER `coupon_id`,
  CHANGE `sc_id` `sc_id` TEXT COLLATE utf8_general_ci NULL COMMENT '学员优惠券编码' AFTER `payee_id` ;

