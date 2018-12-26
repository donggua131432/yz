package com.yz.dao.invite;

import java.util.List;
import java.util.Map;

import com.yz.model.condition.invite.InviteUserQuery;
import org.apache.ibatis.annotations.Param;

import com.yz.core.datasource.DB;
import com.yz.core.datasource.Database;
import com.yz.model.condition.invite.InviteFansQuery;
import com.yz.model.invite.InviteAssignInfo;
import com.yz.model.invite.InviteFansList;
import com.yz.model.invite.InviteUserInfo;

public interface InviteFansMapper {

	/**
	 * 查询我的粉丝
	 * @param queryInfo
	 * @return
	 */
	List<InviteFansList> getAssignedList(InviteUserQuery queryInfo);

	/**
	 * 查询我的下属粉丝
	 * @param queryInfo
	 * @return
	 */
	List<InviteFansList> getSubFansList(InviteUserQuery queryInfo);
	/**
	 * 查询未分配粉丝
	 * @param queryInfo
	 * @return
	 */
	List<InviteFansList> getUndistributedList(InviteFansQuery queryInfo);
	/**
	 * 判断用户是否有跟进关系
	 * @param userId
	 * @return
	 */
	int countFollow(String userId);
	
	/**
	 * 更新用户跟进关系
	 */
	int updateFollow(InviteAssignInfo assignInfo);
	/**
	 * 新增用户跟进关系
	 */
	int insertFollow(InviteAssignInfo assignInfo);
	/**
	 * 查询用户信息
	 * @param userIds
	 * @return
	 */
	List<InviteUserInfo> getUser(@Param("userIds") String[] userIds);
	
	/**
	 * 绩效重新分配的时候更改跟进关系的部门和和校区
	 */
	public void updateUsFollow(@Param("list") List<String> list,@Param("campusId") String campusId,@Param("dpId") String dpId);
	
	/**
	 * 员工的所有跟进关系
	 * @param empId
	 * @return
	 */
	List<Map<String, String>> selectList(String empId);
	
	/**
	 * 清楚跟进关系
	 * @param empId
	 * @param empStatus 
	 * @return
	 */
	int clearFollow(@Param("empId") String empId, @Param("empStatus") String empStatus);
	/**
	 * 获取校监信息
	 * @param empId
	 * @return
	 */
	@DB(db=Database.BDS)
	Map<String, String> getXJInfo(String empId);
	
	/**
	 * 根据userId改变用户的身份
	 * @param userId
	 */
	void updateUserRelationByUserId(@Param("userId") String userId);
	
}
