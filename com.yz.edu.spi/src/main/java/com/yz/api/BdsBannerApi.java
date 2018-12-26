package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsBannerApi {
	
	/**
	 * 首页轮播图
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="bannerList",methodRemark="首页轮播图",needLogin=false)
	public Object bannerList(Header header, Body body) throws IRpcException;
	
}
