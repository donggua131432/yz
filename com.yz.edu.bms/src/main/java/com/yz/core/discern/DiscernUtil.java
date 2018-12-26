package com.yz.core.discern;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import com.yz.util.XmlUtil;

/**
 * 验证码识别
 * 
 * @author Administrator
 *
 */
public class DiscernUtil {
	
	private static final Logger log = LoggerFactory.getLogger(DiscernUtil.class);

	private static YunsuConfig config = null;

	public static void init(YunsuConfig _config) {
//		config = _config;
		config = new YunsuConfig();
		config.setPassword("Zm171123z");
		config.setUsername("yuanzhi");
		config.setSoftid("64700");
		config.setSoftkey("d36c1a3374dd4f08830aa764a41267ab");
	}

	private static String typeid = "3040";
	private static String timeout = "9500";


	/**
	 * 识别数字与字母 4位组合
	 * 
	 * @param imgByteArray
	 * @return
	 */
	public static String getCode(byte[] imgByteArray) {
		if (imgByteArray != null && imgByteArray.length > 0) {
			String result = YunSu.createByPost(config.getUsername(), config.getPassword(), typeid, timeout,
					config.getSoftid(), config.getSoftkey(), imgByteArray);
			
			if(StringUtil.isEmpty(result)) {
				log.error("---------------------------- 验证码验证失败，返回数据为空");
				return null;
			}

			Map<String, String> rt = XmlUtil.xml2Map(result);
			
			if(rt == null) {
				log.error("---------------------------- 验证码验证失败，返回数据不是xml数据");
				return null;
			}
			
			if(rt.containsKey("Error_Code")) {
				String msg = rt.get("Error");
				
				log.error("---------------------------- 验证码验证失败：[" + msg + "]");
				
				return null;
			}
			
			return rt.get("Result");
		}

		return null;
	}
}
