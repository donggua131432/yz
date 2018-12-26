package com.yz.util;

 
import java.util.HashMap;
import java.util.Map;
 
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.http.HttpUtil;
import com.yz.http.interceptor.HttpInterceptor;
import com.yz.model.CommunicationMap;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 手机归属地查询工具
 *
 * @author Administrator
 */
public class MobileOwnershipUtil {

    private static final Logger log = LoggerFactory.getLogger(MobileOwnershipUtil.class);

    private static final String MOBILE_LOCATION_URL = "https://cx.shouji.360.cn/phonearea.php?number=";


    /**
     * 查询手机归属地
     *
     * @param mobile
     * @return map{ area ： 地址 <br/>
     * zipcode ： 邮编 }
     */
    public static Map<String, String> getLocation(String mobile,HttpInterceptor interceptor) {

        Map<String, String> areaInfo = new HashMap<String, String>();
        String result = HttpUtil.sendGet(MOBILE_LOCATION_URL + mobile,interceptor);
        if(StringUtil.hasValue(result)) {
            CommunicationMap c = JsonUtil.str2Object(result, CommunicationMap.class);
            String code = c.getString("code");
            if ("0".equals(code)) {
                Object o = c.get("data");
                if (o == null) {
                    log.error("---------------------- 手机归属地查询返回结果为空");
                }else {
                    try {
                    	JSONObject obj = JSONObject.fromObject(o);
                    	String province = obj.getString("province");
                    	String city = obj.getString("city");
                    	String sp = obj.getString("sp");
                    	
                        areaInfo.put("area", sp + "-" + province + city);
                    }
                    catch (Exception e){
                        log.error("---------------------- 手机归属地接口数据异常!",e);
                    }
                }
            }else {
                log.error("---------------------- 手机归属地查询失败!");
            }
        }
        else {
            log.error("---------------------- 手机归属地查询返回结果为空");
        }
        return areaInfo;
    }
    
    public static void main(String[] args) {
		Map<String, String> locationMap = MobileOwnershipUtil.getLocation("18025301287",null);
    	System.out.println(locationMap.get("area")+"----"+locationMap.get("zipcode"));
	}
}
