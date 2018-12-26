package com.yz.service;
 
import java.util.Map;

import javax.annotation.PostConstruct;
 
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yz.cache.rule.RedisCacheRuleFactory;
import com.yz.constants.CommonConstants;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

/**
 * 
 * Description: 异常信息业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service(value = "yzSysErrorMessageService")
public class SysErrorMessageService extends BaseService {

	public static String App_ERROR_SQL = "select app_msg from common.sys_error_message where error_code=:errorCode limit 1 ";
	public static String SYS_ERROR_SQL = "select sys_msg from common.sys_error_message where error_code=:errorCode limit 1 ";

	@PostConstruct
	public void initService() {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		cacheRule = RedisCacheRuleFactory.getInstance().getRedisCacheRule(CommonConstants.COMMON_CACHE_RULE);
	}

	/**
	 * 获取app异常信息
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	public String getErrorMsg(String errorCode) {
		Map<String, Object> param = Maps.newHashMap();   
		param.put("errorCode", errorCode);
		String cacheKey = cacheRule.getCacheName("yz-errorcode-${errorCode}", param);
		String sysParameter = StringUtil.obj2String(bccCacheHandler.getCache("common", cacheKey, String.class));
		if (StringUtil.isBlank(sysParameter)) {
			try {
				sysParameter = jdbcTemplate.queryForObject(App_ERROR_SQL, param, String.class);
				bccCacheHandler.setCache("common", cacheKey, sysParameter, 3600 * 24);
			} catch (Exception ex) {
				logger.error("getString.sysBelong:{},paramName:{},error:{}", errorCode,
						ExceptionUtil.getStackTrace(ex));
			}
		}
		return sysParameter;

	}
	
	/**
	 * 获取Sys异常信息
	 * 
	 * @param errorCode
	 *            错误代码
	 * @return
	 */
	public String getSysErrorMsg(String errorCode) {
		Map<String, Object> param = Maps.newHashMap();   
		param.put("errorCode", errorCode);
		String cacheKey = cacheRule.getCacheName("yz-syserrorcode-${errorCode}", param);
		String sysParameter = StringUtil.obj2String(bccCacheHandler.getCache("common", cacheKey, String.class));
		if (StringUtil.isBlank(sysParameter)) {
			try {
				sysParameter = jdbcTemplate.queryForObject(SYS_ERROR_SQL, param, String.class);
				bccCacheHandler.setCache("common", cacheKey, sysParameter, 3600 * 24);
			} catch (Exception ex) {
				logger.error("getString.sysBelong:{},paramName:{},error:{}", errorCode,
						ExceptionUtil.getStackTrace(ex));
			}
		}
		return sysParameter;

	}
	
	

}
