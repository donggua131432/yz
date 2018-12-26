package com.yz.dao;

import com.yz.model.payment.BdFeeItem;


public interface BdFeeItemMapper {
	/**
	 * 根据收费项获取信息
	 * @param itemCode
	 * @return
	 */
	BdFeeItem selectItemInfoById(String itemCode);
}