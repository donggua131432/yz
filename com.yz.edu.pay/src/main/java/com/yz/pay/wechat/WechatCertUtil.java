package com.yz.pay.wechat;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.yz.pay.cert.Cert;
import com.yz.pay.cert.CertUtil;
import com.yz.util.CodeUtil;
import com.yz.util.MapUtil;

public class WechatCertUtil extends CertUtil {

	@Override
	public Map<String, String> sign(Cert cert, Map<String, String> submitFromData, String encoding) {
		String key = cert.getCertId();

		String dataStr = MapUtil.sortMap2String(submitFromData, WeChatConstants.param_sign, WeChatConstants.param_pay_sign);

		dataStr += "&key=" + key;

		String sign = CodeUtil.MD5.encrypt(dataStr).toUpperCase();
		
		Map<String, String> rt = new LinkedHashMap<String, String>();
		
		Set<String> keys = submitFromData.keySet();
		
		TreeSet<String> tempSet = new TreeSet<String>();
		tempSet.addAll(keys);
		Iterator<String> tempKeys = tempSet.iterator();
		
		while(tempKeys.hasNext()) {
			String k = tempKeys.next();
			rt.put(k, submitFromData.get(k));
		}
		
		rt.put(WeChatConstants.param_sign, sign);

		submitFromData.put(WeChatConstants.param_sign, sign);
		
		return rt;
	}

	@Override
	public boolean validate(Cert cert, Map<String, String> respData, String encoding) {
		String sign = respData.get(WeChatConstants.param_sign);

		String content = MapUtil.sortMap2String(respData, WeChatConstants.param_sign);

		String key = cert.getCertId();

		content += "&key=" + key;

		String signTemp = CodeUtil.MD5.encrypt(content).toUpperCase();

		return sign.equals(signTemp);
	}

}
