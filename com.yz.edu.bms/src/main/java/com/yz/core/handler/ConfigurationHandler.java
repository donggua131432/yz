package com.yz.core.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.yz.oss.OSSConfig;
import com.yz.oss.OSSUtil;

@Component
public class ConfigurationHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigurationHandler.class);
	
	
	@Value("${cacheFile.sysParam}")
	private String paramFileName;
	
	@Value("${cacheFile.dict}")
	private String dictFileName;
	
	@Bean(name="ossConfig")
	public OSSConfig ossConfig()
	{
		return new OSSConfig();
	}
	
	@Autowired 
	private OSSConfig ossConfig;

	
    @PostConstruct
	public void init() {
		OSSUtil.init(ossConfig);
		log.debug("----------------------------------- 系统缓存初始化");
		//marktingPlanService.initJarInfo();
		log.debug("----------------------------------- 营销策划工具类型初始化成功");
	}

}
