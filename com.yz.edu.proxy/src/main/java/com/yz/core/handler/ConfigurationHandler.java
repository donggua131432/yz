package com.yz.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yz.oss.OSSConfig;
import com.yz.oss.OSSUtil;


public class ConfigurationHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigurationHandler.class);
	
	@Autowired
	private OSSConfig ossConfig;

	public void init() {
		OSSUtil.init(ossConfig);
		
		log.debug("----------------------------------- 第三方配置信息加载完成");
	}
}
