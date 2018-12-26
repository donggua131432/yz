package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.AtsAccount;

public interface AtsAccountMapper {

	AtsAccount getAccount(AtsAccount account);

	/**
	 * 查询账户列表
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> getAccountList(String userId);

	/**
	 * 查询账户
	 *
	 * @param userId
	 * @return
	 */
	AtsAccount getAccountByAccType(@Param("accType") String accType, @Param("userId") String userId);

	/**
	 * 查询账户流水
	 * 
	 * @param accType
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> getAccountSerial(@Param("accType") String accType, @Param("userId") String userId,
			@Param("stdId") String stdId);

	/**
	 * 查询学员账户流水
	 * 
	 * @param accType
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> getStudentAccountSerial(@Param("accType") String accType, @Param("userId") String userId,
			@Param("stdId") String stdId);

	/**
	 * 查询学员所有流水
	 * 
	 * @param userId
	 * @param stdId
	 * @return
	 */
	List<Map<String, String>> getStudentAllAccountSerial(@Param("userId") String userId, @Param("stdId") String stdId);

	/**
	 * 查询账户流水
	 * 
	 * @param accType
	 * @param userId
	 * @param stdId
	 * @return
	 */
	/*
	 * List<BdAccountSerialResponse> getAccountSerialModel(@Param("accType")
	 * String accType, @Param("userId") String userId,
	 * 
	 * @Param("stdId") String stdId);
	 */

}