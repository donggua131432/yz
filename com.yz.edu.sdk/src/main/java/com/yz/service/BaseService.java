package com.yz.service;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yz.cache.handler.BccCacheHandler;
import com.yz.cache.handler.ParameterCacheHandler;
import com.yz.cache.rule.RedisCacheRule;

public class BaseService {

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected DataSource dataSource;

	@Autowired
	protected BccCacheHandler bccCacheHandler;
	
	@Autowired
	protected ParameterCacheHandler parameterCacheHandler;

	protected RedisCacheRule cacheRule = null;
	
	protected NamedParameterJdbcTemplate jdbcTemplate;
}
