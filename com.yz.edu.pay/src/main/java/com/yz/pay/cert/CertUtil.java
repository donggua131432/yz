package com.yz.pay.cert;

import java.util.Map;
 
import com.yz.pay.constants.PayConstants;
import com.yz.pay.model.RequestInfo;
import com.yz.util.MapUtil;

/**
 * 证书工具
 * @author cyf
 *
 */
public abstract class CertUtil {

	/**
	 * 请求参数签名
	 * @param cert 证书信息
	 * @param postData 请求参数
	 * @return
	 */
	public Map<String, String> signData(Cert cert, RequestInfo postData) {
		return signData(cert, postData, PayConstants.DEFAULT_ENCODING);
	}

	/**
	 * 请求参数签名
	 * 
	 * @param mertName
	 * @param postData
	 * @param encoding
	 * @return
	 */
	public Map<String, String> signData(Cert cert, RequestInfo postData, String encoding) {
		// 过滤空参数
		postData.signBefore();
		
		postData = (RequestInfo) MapUtil.filterBlank(postData);
		
		Map<String, String> rt = sign(cert, postData, encoding);// 签名

		return rt;
	}
	/**
	 * 获取签名
	 * @param mertName
	 * @param submitFromData
	 * @param encoding
	 * @return
	 */
	public abstract Map<String, String> sign(Cert cert, Map<String, String> submitFromData, String encoding);
	/**
	 * 验证签名
	 * @param mertName
	 * @param respData
	 * @param encoding
	 * @return
	 */
	public abstract boolean validate(Cert cert, Map<String, String> respData, String encoding);

	/**
	 * 验证返回参数签名
	 * 
	 * @param respData
	 * @return
	 */
	public boolean validate(Cert cert, Map<String, String> respData) {
		return validate(cert, respData,  PayConstants.DEFAULT_ENCODING);
	}
}
