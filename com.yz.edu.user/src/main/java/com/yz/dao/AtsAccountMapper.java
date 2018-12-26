package com.yz.dao;

import java.util.List;
import java.util.Map;

import com.yz.model.AtsAwardRecord;
import org.apache.ibatis.annotations.Param;

import com.yz.model.AtsAccount;
import com.yz.model.AtsAccountSerial;

public interface AtsAccountMapper {

    int insertSelective(AtsAccount record);

	AtsAccount getAccount(AtsAccount account);

	int initAccount(AtsAccount account);
	/**
	 * 行级锁 - 查询账户
	 * @param serial
	 * @return
	 */
	AtsAccount getAccountForUpdate(AtsAccountSerial serial);
	/**
	 * 更新账户信息
	 * @param account
	 */
	int updateAccount(AtsAccount account);
	/**
	 * 查询账户列表
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> getAccountList(String userId);
	/**
	 * 查询账户详情
	 * @param param
	 * @return
	 */
	Map<String, String> getAccountDetail(Map<String, String> param);
	/**
	 * 删除账户
	 * @param accId
	 */
	void deleteAccount(String accId);
	/**
	 * 更新学员账户信息
	 * @param userId
	 * @param stdId
	 */
	void updateStdAccount(@Param("userId") String userId, @Param("stdId") String stdId);


	List<AtsAwardRecord> selectZmRepairList();
	
}