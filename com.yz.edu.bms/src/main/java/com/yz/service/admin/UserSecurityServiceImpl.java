package com.yz.service.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.admin.BmsUserMapper;
import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsFuncResponse;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsUser;

/**
 * 用户权限Service
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserSecurityServiceImpl {
	
	private static final Logger log = LoggerFactory.getLogger(UserSecurityServiceImpl.class);

	@Autowired
	private BmsUserMapper userMapper;

	/**
	 * UserId查询用户
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public BmsUser queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	/**
	 * UserName查询用户
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public BmsUser queryUserByUserName(String username) {
		return userMapper.selectUserByUsername(username);
	}

	/**
	 * 查询用户菜单集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<BmsFuncResponse> queryMenuListByUserId(String userId) {
		return userMapper.selectMenuListByUserId(userId);
	}

	/**
	 * 查询资源集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<BmsFunc> queryFuncListByUserId(String userId) {
		return userMapper.selectFuncListByUserId(userId);
	}

	/**
	 * 查询角色集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<BmsRole> queryRoleListByuserId(String userId) {
		return userMapper.selectRoleListByUserId(userId);
	}

}
