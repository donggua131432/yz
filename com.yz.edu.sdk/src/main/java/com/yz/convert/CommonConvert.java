package com.yz.convert;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yz.constants.CommonConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * @author Administrator
 */
@Component(value = CommonConstants.COMMON_CONVERT)
public class CommonConvert implements YzDataConvert {

    private static Logger logger = LoggerFactory.getLogger(CommonConvert.class);

    @Override
    public Object convert(String redisStr, Class<?> clz) {
        if (!StringUtil.isBlank(redisStr)) {
            return JsonUtil.str2Object(redisStr, clz);
        }
        logger.error("redisStr is null,please check config !");
        return null;
    }

}
