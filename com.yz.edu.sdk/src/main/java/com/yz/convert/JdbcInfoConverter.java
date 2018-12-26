package com.yz.convert;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.yz.datasource.SubJdbcConfig;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

 

 

@Component
@ConfigurationPropertiesBinding
public class JdbcInfoConverter implements Converter<String, SubJdbcConfig> {

	private Logger logger =LoggerFactory.getLogger(RedisInfoConverter.class);
	
	
	@Override
	public SubJdbcConfig convert(String source) {
		logger.info("source:{}",source);
		String[] arr = StringUtil.split(source);
		Map<String,String>map = new HashMap<>();
		for(String str:arr)
		{
			//String[]obj =StringUtil.split(str,":");
			map.put(StringUtil.substringBefore(str, ":"), StringUtil.substringAfter(str, ":"));
		}
		return JsonUtil.map2Object(map, SubJdbcConfig.class);
	}

}
