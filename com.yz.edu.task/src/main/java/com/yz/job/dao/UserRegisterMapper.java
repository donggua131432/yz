package com.yz.job.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.job.model.AtsAwardRecord;

public interface UserRegisterMapper {

	/**
	 * @desc 初始化系统系统的账号
	 * @param serial
	 * @return
	 */
	public void initAccount(@Param(value = "accountList") List<AtsAccount> serial);

	/**
	 * @desc 保存用户的奖励记录
	 * @param record
	 */
	public void saveAwardRecord(AtsAwardRecord record);

	/**
	 * @description 根据用户Id查询用户的智米账号
	 * @param userId
	 * @return
	 */
	public AtsAccount getAccountInfo(@Param(value = "userId") String userId, @Param(value = "accType") String acc_type);

	/**
	 * @desc 更新账号余额
	 * @param account
	 */
	public void updateAccount(AtsAccount account);

	/**
	 * @desc 保存账号帐变流水
	 * @param serial
	 */
	public void saveSerial(AtsAccountSerial serial);

	/**
	 * @desc 派券
	 * @param userId
	 */
	public void sendCoupon(@Param(value = "userId") String userId);



}