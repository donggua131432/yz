package com.yz.api;

import java.util.Map;


public interface BdsUserInfoApi {

	/**
	 * 获取人员信息
	 * @param userId
	 * @return {
	 *   iUserType 人员类型 1-学员 2-员工
	 *   userId 用户ID
	 *   empId 员工ID
	 *   stdId 学员ID
	 *   dpId 员工所属部门ID
	 *   campusId 所属校区ID
	 *   dpManager 部门负责人
	 *   campusManager 校区负责人
	 * }
	 */
	public Map<String, String> getUserInfo(String userId);
	/**
	 * 根据手机号查询招生老师信息
	 * @param mobile
	 * @param mobile2 
	 * @param userId 
	 * @param pIsMb 
	 * @param idCard 
	 * @return
	 */
	public Map<String, String> getRecruit(String mobile, String idCard);
	/**
	 * 根据手机号查询招生老师信息
	 * @param mobile
	 * @param mobile2 
	 * @param userId 
	 * @param pIsMb 
	 * @param pType 
	 * @param userType 
	 * @param idCard 
	 * @return
	 */
	public void createRelation2(String pUserId, String pIsMb, String userId, String stdId, String userType, String pType);
}
