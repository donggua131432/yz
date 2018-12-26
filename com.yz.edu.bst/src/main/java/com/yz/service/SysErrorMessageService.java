package com.yz.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.SysErrorMessageMapper;
import com.yz.model.system.SysErrorMessage;

/**
 * 
 * Description: 异常信息业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
public class SysErrorMessageService {
	
	@Autowired
	private SysErrorMessageMapper errorMsgMapper;
	
	/**
	 * 获取异常信息
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	public SysErrorMessage getErrorMsg(String errorCode) {
		return errorMsgMapper.selectByPrimaryKey(errorCode);
	}
	
	/**
	 * 更新异常信息
	 * 
	 * @param errorMsg
	 */
	public void updateErrorMsg(SysErrorMessage errorMsg) {
		errorMsgMapper.updateByPrimaryKeySelective(errorMsg);
	}
	
	/**
	 * 删除异常信息
	 * 
	 * @param errorCode
	 */
	public void deleteErrorMsg(String errorCode) {
		errorMsgMapper.deleteErrorMsg(errorCode);
	}
	
	/**
	 * 增加异常信息
	 * 
	 * @param errorMsg
	 */
	public void addErrorMsg(SysErrorMessage errorMsg) {
		errorMsgMapper.insertSelective(errorMsg);
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<SysErrorMessage> sellectAll() {
		return errorMsgMapper.selectAll();
	}

	public void deleteAllSysErrorMessage(String[] idArray) {
		// TODO Auto-generated method stub
		errorMsgMapper.deleteAllSysErrorMessage(idArray);
	}

	public List<SysErrorMessage> selectAll(SysErrorMessage sysErrorMessage) {
		// TODO Auto-generated method stub
		return errorMsgMapper.selectAll(sysErrorMessage);
	}
}
