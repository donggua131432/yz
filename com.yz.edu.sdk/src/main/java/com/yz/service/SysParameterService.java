package com.yz.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct; 

import org.apache.commons.lang.math.NumberUtils;  
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.yz.cache.rule.RedisCacheRuleFactory;
import com.yz.constants.CommonConstants;
import com.yz.constants.SystemParamConstants;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

@Service(value = "yzSysParameterService")
public class SysParameterService extends BaseService{

	public static String SYS_PARAMETER_SQL = "select param_value as paramValue from common.sys_parameter where param_name=:paramName and sys_belong=:sysBelog limit 1 ";
	public static String SYS_PARAMETER_SQL_ALL = "select param_name as paramName,param_value as paramValue from common.sys_parameter ";

	@PostConstruct
	public void initService() {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		cacheRule = RedisCacheRuleFactory.getInstance().getRedisCacheRule(CommonConstants.COMMON_CACHE_RULE);
	}

	/**
	 * @desc 获取bms参数
	 * @param paramName
	 * @return
	 */
	public String getString(String paramName) {
		return this.getString(SystemParamConstants.BMS_BELONG_PARAM,paramName);
	}
	
	
	/**
	 * 
	 * @param paramName
	 * @return
	 */
	public String getString(String sysBelong, String paramName) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("sysBelog", sysBelong);
		param.put("paramName", paramName);
		String cacheKey = cacheRule.getCacheName("yz-${sysBelog}-${paramName}", param);
		String sysParameter =StringUtil.obj2String(bccCacheHandler.getCache("common", cacheKey, String.class));
		if (StringUtil.isBlank(sysParameter)) {
			try
			{
				sysParameter = jdbcTemplate.queryForObject(SYS_PARAMETER_SQL, param, String.class);
				bccCacheHandler.setCache("common", cacheKey, sysParameter, 3600 * 24);
			}catch(Exception ex)
			{
				logger.error("getString.sysBelong:{},paramName:{},error:{}",sysBelong,paramName,ExceptionUtil.getStackTrace(ex));
			} 
		}
		return sysParameter;
	}

	/**
	 * 
	 * @param paramName
	 * @return
	 */
	public int getInt(String sysBelong, String paramName) {
		return NumberUtils.toInt(getString(sysBelong, paramName));
	}
	
	/**
	 * 得到所有系统参数
	 * @return
	 */
	public List<Map<String, Object>> sellectAll() {
		Map<String, String> param = null;
		String cacheKey="yz-common-parameterAll";
		List<Map<String, Object>> parameterList =(List<Map<String, Object>>) parameterCacheHandler.getCache("common", cacheKey,Map.class);
		if (parameterList==null||parameterList.size()==0) {
			try
			{
				parameterList =( List<Map<String, Object>> ) jdbcTemplate.queryForList(SYS_PARAMETER_SQL_ALL, param);
				parameterCacheHandler.setCache("common", cacheKey, parameterList, 3600 * 24);
				return parameterList;
			}catch(Exception ex)
			{
				logger.error("error:{}",ExceptionUtil.getStackTrace(ex));
			} 
		}
		
		return parameterList;
	}

}
