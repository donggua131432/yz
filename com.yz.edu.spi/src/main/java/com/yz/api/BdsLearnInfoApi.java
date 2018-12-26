package com.yz.api;

import java.util.Map;

import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsLearnInfoApi {

	/**
	 * 基础信息
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="stdLearnInfo",methodRemark="学员基础信息",needLogin=true)
	public Object stdLearnInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 新增报读信息
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	public Map<String, String> addLearnInfo(Body body) throws IRpcException;
	
	/**
	 * 获取学员报读或缴费状态
	 * @param userId
	 * @return
	 * @throws IRpcException
	 */
	public Map<String, String> getLearnStatus(String userId) throws IRpcException;
	
	/**
	 * 根据学业获取某个具体的学业信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getLearnInfoByLearnId",methodRemark="获取某个具体学业信息息",needLogin=true)
	public Object getLearnInfoByLearnId(Header header, Body body) throws IRpcException;
}
