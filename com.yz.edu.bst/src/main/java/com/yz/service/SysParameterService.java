package com.yz.service;

import java.util.List;

import com.yz.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.dao.SysParameterMapper;
import com.yz.model.SysParameter;

/**
 * 
 * Description: 系统参数业务 
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
public class SysParameterService {
	
	private static final Logger log = LoggerFactory.getLogger(SysParameterService.class);
	
	@Autowired
	private SysParameterMapper paramMapper;

	/**
	 * 获取参数
	 * @param paramName 参数名
	 * @return
	 */
	public String getString(String paramName) {
		SysParameter parameter =  paramMapper.selectByPrimaryKey(paramName);
		if(parameter==null)
		{
			log.error("not found paramName:{}",paramName);
			return StringUtil.EMPTY;
		}
		return parameter.getParamValue();
	}


	/**
	 * 获取参数
	 * @param paramName 参数名
	 * @return
	 */
	public SysParameter getParam(String paramName) {
		return paramMapper.selectByPrimaryKey(paramName);
	}
	
	/**
	 * 修改参数
	 * @param param
	 */
	public void updateParam(SysParameter param) {
		paramMapper.updateByPrimaryKeySelective(param);
		//SysParamUtil.replaceParam(param);
	}
	
	/**
	 * 删除参数
	 * @param paramName
	 */
	public void deleteParam(String paramName) {
		paramMapper.deleteByPrimaryKey(paramName);
		//SysParamUtil.deleteParam(paramName);
	}
	
	/**
	 * 增加参数
	 * @param param
	 */
	public void addParam(SysParameter param) {
		paramMapper.insertSelective(param);
		//SysParamUtil.replaceParam(param);
	}

	/**
	 * 查询所有参数
	 * @return
	 */
	public List<SysParameter> sellectAll(String paramName, String paramValue) {
		return paramMapper.selectAll(paramName,paramValue);
	}

}
