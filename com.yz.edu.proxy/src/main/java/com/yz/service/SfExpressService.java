package com.yz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.constants.CommonConstants;
import com.yz.http.HttpUtil;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.util.SFUtil;
import com.yz.util.XmlUtil;

/**
 * 顺丰接口 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年7月10日.
 *
 */
@Service
public class SfExpressService {

	@Value("${sf.orderUrl}")
	private String orderUrl;

	@Value("${sf.checkword}")
	private String checkword;

	@Value("${sf.head}")
	private String head;

	@YzService(sysBelong = "gs", methodName = "sfOrderQuery", methodRemark = "订单物流信息查询",
			useCache =true ,cacheKey = "yz.sf.${transportNo}",cacheHandler = CommonConstants.MEMORY_CACHE_HANDLER )
	/**
	 * 顺丰，订单查询
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	public Object sfOrderQuery(Header header, Body body) {
		String transportNo = body.getString("transportNo");
		String param = SFUtil.setSFXml2String(transportNo, head);
		String verifyCode = SFUtil.md5EncryptAndBase64(param + checkword);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", param));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		String  paramString=URLEncodedUtils.format(nvps,Consts.UTF_8);
		String result = HttpUtil.sendPost(orderUrl, paramString, null);
		List<Map<String, String>> resultMap = XmlUtil.readSFQueryXml(result);
		if (null != resultMap) {
			return resultMap;
		} else {
			return null;
		}
	}
}
