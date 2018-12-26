package com.yz.dao.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsFuncResponse;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;

public interface BmsUserMapper {
	int deleteByPrimaryKey(String userId);

	int insert(BmsUser record);

	int insertSelective(BmsUser record);

	BmsUser selectByPrimaryKey(String userId);

	int updateByPrimaryKeySelective(BmsUser record);

	int updateByPrimaryKey(BmsUser record);

	BmsUser selectUserByUsername(String username);
	
	BaseUser selectBaseUserByUsername(String username);

	List<BmsFuncResponse> selectMenuListByUserId(@Param(value = "userId") String userId);

	List<BmsFunc> selectFuncListByUserId(String userId);

	List<BmsRole> selectRoleListByUserId(String userId);

	List<BmsUserResponse> selectUserListByPage(BmsUser user);

	int insertUserRoles(Map<String, Object> roleInfo);

	void updateUserRoles(@Param(value = "userId") String userId, @Param(value = "roleIds") String[] roleIds);

	int selectUserNameIsExist(@Param(value = "userName") String userName,
			@Param(value = "oldUserName") String oldUserName);

	BmsUserResponse selectUserById(String userId);

	int isSuperAdmin(BaseUser user);
	
	BaseUser selectBaseUserByUserId(String userId);
	
	
	public List<String> getRoleCodeList(String userId);

}