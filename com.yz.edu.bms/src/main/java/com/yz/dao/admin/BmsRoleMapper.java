package com.yz.dao.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsRoleResponse;

public interface BmsRoleMapper {
	int deleteByPrimaryKey(String roleId);

	int insert(BmsRole record);

	int insertSelective(BmsRole record);

	BmsRole selectByPrimaryKey(String roleId);

	int updateByPrimaryKeySelective(BmsRole record);

	int updateByPrimaryKey(BmsRole record);

	List<BmsRole> selectAllRoles(BmsRole role);

	List<BmsFunc> selectFuncsByRoleId(String roleId);

	int inserRoleFuncs(Map<String, Object> roleFunc);

	BmsRoleResponse selectRoleAndFunc(String roleId);

	int selectCountByRoleName(String roleName);

	int selectCountByRoleCode(String roleCode);

	int deleteRoleFuncId(String roleId);

	int updateRoleFunc(@Param(value = "roleId") String roleId, @Param(value = "permissions") String[] permissions);
	
	List<BmsRole> findAllKeyValue(@Param("rName") String rName);

}