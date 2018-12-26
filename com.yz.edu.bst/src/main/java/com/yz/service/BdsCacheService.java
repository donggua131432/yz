package com.yz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class BdsCacheService {

	private static final Logger log = LoggerFactory.getLogger(BdsCacheService.class);

	public void refreshAll() {
		log.debug("-------------------------- Nothing to excute");

	}

//	public void refreshParam() {
//		SysParamUtil.loadAll(true);
//		log.debug("-------------------------- BDS 系统参数刷新成功");
//	}

//	public void refreshMarktingJar() {
//		String remoteConfigUrl = SysParamUtil.getString(GlobalConstants.MP_JAR_JSON);
//		MarktingJarUtil.initMarktingJar(remoteConfigUrl);
//	}

}
