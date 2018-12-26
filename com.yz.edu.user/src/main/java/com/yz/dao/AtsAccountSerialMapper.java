package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.AtsAccountSerial;

public interface AtsAccountSerialMapper {

	int initSerial(AtsAccountSerial serial);

	int updateSerial(AtsAccountSerial serial);

	List<Map<String, String>> getAccountSerials(@Param("userId") String userId, @Param("action") String action,@Param("accType") String accType);
	

	int copy(@Param("srcAccId") String srcAccId, @Param("destAccId") String destAccId, @Param("userId") String userId,
			@Param("stdId") String stdId);
}