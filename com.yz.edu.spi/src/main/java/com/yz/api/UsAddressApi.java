package com.yz.api;

import java.util.Map;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface UsAddressApi {

	/**
	 * 【PUB】修改收货地址信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="editAddress",methodRemark="添加/修改/删除收货地址",needLogin=true)
	void editAddress(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】查询我的收货地址
	 * @param header
	 * @param body
	 * @return
	 * @throws IRuntimeException
	 */
	@YzService(sysBelong="us",methodName="myAddress",methodRemark="我的收货地址",needLogin=true)
	Object getAddressList(Header header, Body body) throws IRpcException;
	
	/**
	 * 某个收货地址的信息
	 * @param saId
	 * @return
	 * @throws IRpcException
	 */
	Map<String, String> getAddressDetailById(String saId) throws IRpcException;
	
	/**
	 * 查询地址详情
	 * @param saId
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="us",methodName="getAddress",methodRemark="查询地址详情",needLogin=true)
	Object getAddress(Header header, Body body) throws IRpcException;
}
