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
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.admin.BmsUserMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;
import com.yz.model.common.IPageInfo;
import com.yz.util.CodeUtil;

/**
 * 
 * Description: 用户业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
@Transactional
public class UserServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private BmsUserMapper userMapper;

	/**
	 * 分页查询
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo searchUserByPage(int start, int length, BmsUser user) {
		PageHelper.offsetPage(start, length);
		List<BmsUserResponse> users = userMapper.selectUserListByPage(user);
		return new IPageInfo((Page) users);
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @param roleIds
	 * @return
	 */
	public int insertUser(BmsUser user, String[] roleIds) {
		user.setUserPwd(CodeUtil.MD5.encrypt("123456"));
		user.setUserId(IDGenerator.generatorId());
		userMapper.insert(user);

		log.debug("--------------------------------  插入新用户，ID为：" + user.getUserId());

		Map<String, Object> roleInfo = new HashMap<String, Object>();
		roleInfo.put("userId", user.getUserId());
		roleInfo.put("roleIds", roleIds);
		return userMapper.insertUserRoles(roleInfo);
	}

	public void editUserPwd(String password, String newpassword, String userId) {
		BmsUser userInfo = userMapper.selectByPrimaryKey(userId);
		String originPwd = userInfo.getUserPwd();
		password = CodeUtil.MD5.encrypt(password);
		if(!originPwd.equals(password)){
			throw new BusinessException("E000074");	//旧密码错误
		}
		
		String newPwd = CodeUtil.MD5.encrypt(newpassword);
		BmsUser user = new BmsUser();
		user.setUserId(userId);
		user.setUserPwd(newPwd);
		
		editUser(user);

		// 清除用户缓存
		SessionUtil.clearUser(userId);
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	public int editUser(BmsUser user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 查询用户角色
	 * 
	 * @param userId
	 * @return
	 */
	public List<BmsRole> queryUserRoles(String userId) {
		return userMapper.selectRoleListByUserId(userId);
	}

	/**
	 * 查询用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public BmsUserResponse queryUserById(String userId) {
		return userMapper.selectUserById(userId);
	}

	/**
	 * 更新用户信息、角色信息
	 * 
	 * @param user
	 * @param roleIds
	 */
	public void updateUserAndRoles(BmsUser user, String[] roleIds) {
		userMapper.updateByPrimaryKey(user);
		userMapper.updateUserRoles(user.getUserId(), roleIds);
	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param userName
	 * @param oldUserName
	 * @return
	 */
	public boolean isUserNameExist(String userName, String oldUserName) {
		if (userName.equalsIgnoreCase(oldUserName)) {
			return true;
		}
		if (userMapper.selectUserNameIsExist(userName, oldUserName) <= 0) {
			return true;
		}
		return false;
	}

	public int deleteUser(String userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

}
