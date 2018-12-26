package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.BusinessException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsBannerService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsBannerApiImpl implements BdsBannerApi {

	@Autowired
	private BdsBannerService bannerService;

	@Override
	public Object bannerList(Header header, Body body) throws BusinessException {
		return bannerService.selectBanner();
	}

}
