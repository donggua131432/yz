package com.yz.core.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.constants.GlobalConstants;
import com.yz.dao.admin.BmsUserMapper;
import com.yz.dao.oa.OaSessionMapper;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.SessionCampusInfo;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.admin.SessionEmpInfo;
import com.yz.model.admin.SessionGroupInfo;

@Component
public class ISecurityService {

	@Autowired
	private BmsUserMapper userMapper;

	public BaseUser getUser(String userName) {
		BaseUser user = getBaseUser(userName);
		if (user == null)
			return null;

		if (GlobalConstants.TRUE.equals(user.getIsBlock())) {
			throw new BusinessException("E000062");// TODO 锁定异常
		}

		return user;
	}

	public BaseUser assembly(BaseUser user) {
		boolean isSuperAdmin = check(user);

		List<BmsFunc> funcs = getFunctions(user.getUserId());
		user.setFuncs(funcs);
		String empId = user.getEmpId();
		List<String> jtList = getJtList(empId);
		user.setJtList(jtList);
		List<String> roleCodeList = getRoleCodeList(user.getUserId());
		user.setRoleCodeList(roleCodeList);
		if (isSuperAdmin) {
			user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
		} else {
	
			List<SessionCampusInfo> campusList = getCampusList(empId);// 校长级别权限
			if (campusList != null && !campusList.isEmpty()) {
				user.setMyCampusList(campusList);
				user.setUserLevel(GlobalConstants.USER_LEVEL_SCHOOL);
				return user;
			}
			//校长级别权限
			if(jtList!=null&&(jtList.contains("FXZ")||jtList.contains("XZ"))) {
				campusList = new ArrayList<>();
				SessionCampusInfo campusInfo  = new SessionCampusInfo();
				campusInfo.setCampusId("");
				campusList.add(campusInfo);
				user.setMyCampusList(campusList);
				user.setUserLevel(GlobalConstants.USER_LEVEL_SCHOOL);
				return user;
			}
			List<SessionDpInfo> dpList = getDepartmentList(empId);// 校监级别权限
			if (dpList != null && !dpList.isEmpty()) {
				user.setMyDpList(dpList);
				user.setUserLevel(GlobalConstants.USER_LEVEL_DEPARTMENT);
				return user;
			}

			List<SessionEmpInfo> empList = getEmployeeList(empId);// 招生主管权限
			if (empList != null && !empList.isEmpty()) {
				user.setMyEmplyeeList(empList);
				user.setUserLevel(GlobalConstants.USER_LEVEL_GROUP);
				return user;
			}
			
			//校区助理级别权限
			if(jtList!=null&&jtList.contains("XJZL")) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_XJZL);
				return user;
			}
			//校监助理级别权限
			if(jtList!=null&&jtList.contains("XQZL")) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_XQZL);
				return user;
			}

			//400专员
			if(jtList!=null&&jtList.contains("400")) {
				user.setUserLevel(GlobalConstants.USER_LEVEL_400);
				return user;
			}
			
			user.setUserLevel(GlobalConstants.USER_LEVEL_NORMARL);
		}

		return user;
	}

	/**
	 * 查询职员职称
	 * 
	 * @param empId
	 * @return
	 */
	private List<String> getJtList(String empId) {
		return sessionMapper.getJtList(empId);
	}
	/**
	 * 查询用户角色代码
	 * @param userId
	 * @return
	 */
	private List<String> getRoleCodeList(String userId){
		return userMapper.getRoleCodeList(userId);
	}

	@Autowired
	private OaSessionMapper sessionMapper;

	private List<SessionEmpInfo> getEmployeeList(String empId) {
		List<SessionGroupInfo> groupList = sessionMapper.getGroupList(empId);
		if (groupList != null && groupList.size() > 0) {
			return sessionMapper.getEmployeeList(groupList);
		}
		return null;
	}

	private List<SessionDpInfo> getDepartmentList(String empId) {
		List<SessionDpInfo> dpList = sessionMapper.getDepartmentList(empId);
		if (dpList != null && dpList.size() > 0) {
			List<SessionDpInfo> subDpList = sessionMapper.getSubDpList(dpList);
			if (subDpList != null) {
				for (SessionDpInfo dpInfo : subDpList) {
					dpList.add(dpInfo);
				}
			}
		}
		return dpList;
	}

	private List<SessionCampusInfo> getCampusList(String empId) {
		return sessionMapper.getCampusList(empId);
	}

	private boolean check(BaseUser user) {
		int count = userMapper.isSuperAdmin(user);
		return count > 0;
	}

	/**
	 * 获取用户权限列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BmsFunc> getFunctions(String userId) {
		return userMapper.selectFuncListByUserId(userId);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userName
	 * @return
	 */
	public BaseUser getBaseUser(String userName) {
		BaseUser user = userMapper.selectBaseUserByUsername(userName);
		if (null != user) {
			user.setUserId(user.getUserId());
			user.setDepartmentId(user.getDepartmentId());
			user.setDepartmentName(user.getDepartmentName());
			user.setRealName(user.getRealName());
			user.setUsername(user.getUsername());
			user.setPassword(user.getPassword());
			user.setEmpId(user.getEmpId());
		}
		return user;
	}

}
