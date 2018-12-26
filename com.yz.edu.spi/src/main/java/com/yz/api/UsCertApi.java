package com.yz.api;

import java.util.Map;

import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;

public interface UsCertApi {
	
	/**
	 * 【PUB】绑定证件
	 * @param header
	 * @param body
	 * @throws IRuntimeException
	 *//*
	Object bindCert(Header header, Body body) throws IRpcException;
	
	*//**
	 * 【PUB】是否绑定证件
	 * @param header
	 * @param body
	 * @throws IRuntimeException
	 *//*
	Object isBindCert(Header header, Body body) throws IRpcException;
	
	*//**
	 * 【PUB】查询绑定状态
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 *//*
	Object getBindStatus(Header header, Body body) throws IRpcException;*/
	/**
	 * 【PRI】创建学员与用户关系
	 * @param stdId type为STD时填写
	 * @param idCard type为STD时填写
	 * @param userId type为STD时填写
	 * 
	 * @param type STD-绑定学员身份， EMP-绑定员工身份
	 * 
	 * @param empId type为EMP时填写
	 * @param yzCode type为EMP时填写
	 */
	void createRelation(Body body) throws IRpcException;
	/**
	 * 更新学员跟进关系
	 * @param body
	 * @throws IRpcException
	 */
	Map<String, String> refreshFollow(Body body) throws IRpcException;
	/**
	 * 清理跟进关系
	 * @param empId
	 */
	void clearFollow(String empId);
}
