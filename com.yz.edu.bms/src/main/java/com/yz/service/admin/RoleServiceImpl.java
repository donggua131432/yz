package com.yz.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.admin.BmsRoleMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsRoleResponse;
import com.yz.model.common.IPageInfo;

/**
 * 
 * Description: 角色业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
@Transactional
public class RoleServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private BmsRoleMapper roleMapper;

	/**
	 * 分页查询角色
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryRolesByPage(int start, int length, BmsRole role) {
		PageHelper.offsetPage(start, length);
		List<BmsRole> roles = roleMapper.selectAllRoles(role);
		return new IPageInfo((Page) roles);
	}

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @return
	 */
	public void updateRole(BmsRole role, String[] permissions) {
		if(null == permissions || permissions.length <=0){
			throw new BusinessException("E000090");	// 未选择任何资源
		}
		roleMapper.updateByPrimaryKeySelective(role);
		roleMapper.updateRoleFunc(role.getRoleId(), permissions);
	}

	/**
	 * 增加角色
	 * 
	 * @param role
	 * @param funcIds
	 * @return
	 */
	public int insertRole(BmsRole role, String[] funcIds) {
		
		if(null == funcIds || funcIds.length <=0){
			throw new BusinessException("E000090");	// 未选择任何资源
		}
		Map<String, Object> roleMap = new HashMap<String, Object>();
		role.setRoleId(IDGenerator.generatorId());
		roleMapper.insert(role);
		roleMap.put("roleId", role.getRoleId());
		roleMap.put("funcs", funcIds);
		return roleMapper.inserRoleFuncs(roleMap);
	}

	/**
	 * 查询角色所有资源
	 * 
	 * @param roleId
	 * @return
	 */
	public List<BmsFunc> queryRolesFunc(String roleId) {

		return roleMapper.selectFuncsByRoleId(roleId);
	}

	/**
	 * 查询角色，包括资源
	 * 
	 * @param roleId
	 * @return
	 */
	public BmsRoleResponse queryRole(String roleId) {
		return roleMapper.selectRoleAndFunc(roleId);
	}

	public int deleteRole(String roleId) {
		return roleMapper.deleteByPrimaryKey(roleId);
	}

	/**
	 * 通过角色名查询角色
	 * 
	 * @param roleName
	 * @return
	 */
	public int isRoleNameExist(String roleName) {
		return roleMapper.selectCountByRoleName(roleName);
	}

	/**
	 * 查询角色编码是否存在
	 * 
	 * @param roleName
	 * @return
	 */
	public int isRoleCodeExist(String roleCode) {
		return roleMapper.selectCountByRoleCode(roleCode);
	}

	/**
	 * 查询所有角色
	 */
	public List<BmsRole> queryAllRoles() {

		return roleMapper.selectAllRoles(null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findAllKeyValue(String sName,int rows,int page){
		PageHelper.startPage(page, rows);
		List<BmsRole> list = roleMapper.findAllKeyValue(sName);
		return new IPageInfo((Page) list);
	}
}
