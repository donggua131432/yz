package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.BdStudentBaseInfo;

public interface StudentAllMapper {

	/**
	 * 根据学业ID获取学员基本信息
	 * 
	 * @param learnId
	 * @return
	 */
	BdStudentBaseInfo getStudentBaseInfoByLearnId(String learnId);

	/**
	 * 查询用户ID
	 * 
	 * @param learnId
	 *            学业ID
	 * @return
	 */
	String selectUserIdByLearnId(String learnId);
	
	
	/**
	 * 根据学生ID查询用户ID
	 * @param stdId
	 * @return
	 */
	String getRelationUserIdByStdId(String stdId);

	/**
	 * 根据身份证查询学员信息
	 * 
	 * @param idCard
	 * @return
	 */
	BdStudentBaseInfo getStudentInfoByIdCard(String idCard);

	/**
	 * 查询学员绑定关系
	 * 
	 * @param stdId
	 * @return
	 */
	int countRelationBy(String stdId);

	List<String> selectAvailableRegistCouponIds();

	int insertStdCoupon(@Param("userId") String userId, @Param("couponIds") List<String> couponIds);
	/**
	 * 查询学员所属招生老师
	 * @param stdId
	 * @return
	 */
	Map<String, String> getRecruitMap(String stdId);

}
