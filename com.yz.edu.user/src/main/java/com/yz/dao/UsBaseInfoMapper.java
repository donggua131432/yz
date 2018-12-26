package com.yz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.UsBaseInfo; 

public interface UsBaseInfoMapper {

	/**
	 * 新增用户信息
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(UsBaseInfo record);

	/**
	 * 根据userId查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	UsBaseInfo selectByPrimaryKey(String userId);

	/**
	 * 根据微信OpenId查询用户信息
	 * 
	 * @param openId
	 * @return
	 */
	UsBaseInfo selectByBind(@Param("bindId") String bindId, @Param("bindType") String bindType);

	/**
	 * 更新用户信息
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(UsBaseInfo record);

	/**
	 * 根据手机号查询用户
	 * 
	 * @param mobile
	 * @return
	 */
	UsBaseInfo selectByMobile(String mobile);

	/**
	 * 查询手机号是否被注册
	 * 
	 * @param mobile
	 * @return
	 */
	int countByMobile(String mobile);

	/**
	 * 查询手机号与第三方平台绑定关系
	 * 
	 * @param mobile
	 * @param openId
	 * @return
	 */
	int countByBind(@Param("bindId") String bindId, @Param("bindType") String bindType);

	/**
	 * 获取附属信息
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, String> getOtherInfo(String userId);

	/**
	 * 获取我的粉丝列表
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, String>> getMyFans(String userId);

	/**
	 * 根据远智编号查询
	 * 
	 * @param yzCode
	 * @return
	 */
	UsBaseInfo selectByYzCode(String yzCode);


	/**
	 * 通过userId 获取openId
	 * 
	 * @param userId
	 * @return
	 */
	public String getOpenIdByUserId(String userId);


	/**
	 * 根据empId查询openId
	 * 
	 * @param empId
	 * @return
	 */
	String getOpenIdByEmpId(String empId);

	/**
	 * 获取批量openid
	 * @param userIds	用户id集合
	 * @return
	 */
	List<String> getOpenIdsByUserIds(@Param("userIds") List<String> userIds);
 
	/**
	 * 根据微信编码获取用户
	 * @param openId
	 * @return
	 */
	int countByOpenId(String openId);


	List<HashMap> selectNewRegList();
	
	
	/**
	 * 根据手机号或者学员id查询用户个数
	 * @param stdId
	 * @param mobile
	 * @return
	 */
	int getCountByStdIdOrMobile(@Param("stdId")String stdId,@Param("mobile")String mobile);
}