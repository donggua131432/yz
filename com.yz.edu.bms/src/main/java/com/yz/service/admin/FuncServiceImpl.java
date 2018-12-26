package com.yz.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.admin.BmsFuncMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsFuncResponse;
import com.yz.model.common.IPageInfo;

/**
 * 
 * Description: 资源业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
@Transactional
public class FuncServiceImpl {

	@Autowired
	private BmsFuncMapper funcMapper;

	/**
	 * 分页查询资源
	 * 
	 * @param pageSize
	 * @param pageIndex
	 * @param func
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryFuncsByPage(int start, int length, BmsFunc func) {
		PageHelper.offsetPage(start, length);
		List<BmsFunc> funcs = funcMapper.selectAllFunc(func);
		return new IPageInfo((Page) funcs);
	}

	/**
	 * 修改
	 * 
	 * @param func
	 * @return
	 */
	public int editFunc(BmsFunc func) {
		return funcMapper.updateByPrimaryKeySelective(func);
	}

	/**
	 * 查询资源
	 * 
	 * @param funcId
	 * @return
	 */
	public BmsFunc queryFunc(String funcId) {
		return funcMapper.selectByPrimaryKey(funcId);
	}

	/**
	 * 查询所有资源
	 */
	public List<BmsFunc> queryAllFuncs() {
		return funcMapper.selectAllFunc(null);
	}

	public List<BmsFuncResponse> queryMenuAndFunc() {
		return funcMapper.selectMenuAndFuncs();
	}

	/**
	 * 增加权限
	 */
	public int addFunc(BmsFunc func) {
		BaseUser user = SessionUtil.getUser();
		func.setUpdateUser(user.getRealName());
		func.setUpdateUserId(user.getUserId());
		func.setFuncId(IDGenerator.generatorId());
		return funcMapper.insert(func);
	}

	public boolean isFuncNameExist(String funcName, String oldFuncName) {
		if (funcName.equalsIgnoreCase(oldFuncName)) {
			return true;
		}
		if (!oldFuncName.equalsIgnoreCase("undefined")
				&& funcMapper.selectFuncNameIsExist(funcName, oldFuncName) <= 0) {
			return true;
		}
		return false;
	}

	public boolean isRoleCodeExist(String funcCode, String oldFuncCode) {
		if (funcCode.equalsIgnoreCase(oldFuncCode)) {
			return true;
		}
		if (!oldFuncCode.equalsIgnoreCase("undefined")
				&& funcMapper.selectFuncCodeIsExist(funcCode, oldFuncCode) <= 0) {
			return true;
		}
		return false;
	}

	public int deleteFunc(String funcId) {
		return funcMapper.deleteByPrimaryKey(funcId);
	}

}
