package com.yz.job.dao;

import com.yz.job.model.YzBadPayment;  

public interface YzBadPaymentMapper {
  

	/**
	 * @desc 保存支付异常订单
	 * @param record
	 */
    void saveBadPayment(YzBadPayment record);
 
}