package com.yz.dao;

import java.util.List;
import java.util.Map;

import com.yz.model.AtsRechargeRecords;
import com.yz.model.AtsZhimiProduct;

public interface AtsRechargeMapper {

	List<Map<String, String>> getProductList(int size);
	
	AtsZhimiProduct getProductById(String productId);

	int insertRecords(AtsRechargeRecords records);

	AtsRechargeRecords getRecordsInfo(String recordsNo);
	
	int updateRecords(AtsRechargeRecords records);
	
	int insertRecordsNoG(AtsRechargeRecords records);

}
