package com.yz.pay;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

 
import com.yz.constants.GlobalConstants;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.pay.allinpay.AllinpayConstants;
import com.yz.pay.cert.Cert;
import com.yz.pay.cert.CertUtil;
import com.yz.pay.constants.PayConstants;
import com.yz.pay.factory.AllinPayFactory;
import com.yz.pay.model.AllinpayRuoteInfo; 
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import com.yz.util.XmlUtil;

@Service
@Transactional
public class AllinPayRouteService extends AbstractTranService {

	private static final Logger log = LoggerFactory.getLogger(AllinPayRouteService.class);

	private static SSLHandler sslHanderl = new SSLHandler();

	private static SSLSocketFactory sslFactory;
	
	@Value(value="${payment.allinpay.route.payment.url:''}")
	private String url;
	
	@Autowired
	private YzPayCertService payCertService;

	@Override
	public Map<String, String> addOrder(Map<String, String> postData) {
		Map<String, String> result = new HashMap<String, String>();
		Cert cert = payCertService.getCert(PayConstants.ALLINPAY_ROUTE_CERTS);
		if (cert == null)
			throw new BusinessException("");

		String amount = postData.get("amount");

		String merchantId = cert.getMerchantId();
		String cardNo = postData.get("cardNo");
		String belong = postData.get("belong");
		String cardType = postData.get("cardType");
		String timeStr = postData.get("timeStr");

		String ctNo = merchantId + "_" + System.currentTimeMillis();

		AllinpayRuoteInfo routeInfo = new AllinpayRuoteInfo();

		routeInfo.set_TRX_CODE(AllinpayConstants.SINGLE_RUOTE_TX_CODE);
		routeInfo.set_VERSION("04");
		routeInfo.set_DATA_TYPE("2");
		routeInfo.set_LEVEL("5");
		routeInfo.set_USER_NAME(cert.getUserName());
		routeInfo.set_USER_PASS(cert.getUserPwd());
		routeInfo.set_REQ_SN(ctNo);
		// routeInfo.set_SIGNED_MSG("");
		BigDecimal b_amount = new BigDecimal(StringUtil.hasValue(amount) ? amount : "0.00");
		b_amount = b_amount.multiply(new BigDecimal("100"));

		routeInfo.set_BUSINESS_CODE(AllinpayConstants.DAI_SHOU_B_CODE);
		routeInfo.set_MERCHANT_ID(merchantId);
		routeInfo.set_SUBMIT_TIME(timeStr);
		routeInfo.set_ACCOUNT_NO(cardNo);
		routeInfo.set_ACCOUNT_NAME(belong);
		routeInfo.set_ACCOUNT_PROP(cardType);

		routeInfo.set_AMOUNT(b_amount.setScale(0, BigDecimal.ROUND_DOWN).toString());

		String xmlStr = getXml(routeInfo);

		log.debug("-------------------------- 待签XML : " + xmlStr);

		String sign = sign(xmlStr, cert);

		log.debug("------------------------------- sign : " + sign);

		routeInfo.set_SIGNED_MSG(sign);

		xmlStr = getXml(routeInfo);

		log.debug(" ------------------------------- 请求XML：" + xmlStr);

		String resultStr = null;

		try {
			resultStr = send(url, xmlStr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("isOk", GlobalConstants.FALSE);
			result.put("eMsg", e.getMessage());
		}

		log.debug("----------------------------- 返回结果：" + resultStr);

		if (StringUtil.hasValue(resultStr)) {

			Map<String, String> infoMap = getReturnInfo(resultStr);

			String rtCode = infoMap.get("RET_CODE");

			if ("0000".equals(rtCode)) {

				boolean isSuccess = verfiy(resultStr, cert, infoMap);

				if (isSuccess) {
					Map<String, String> transet = getReturnTranset(resultStr);
					if ("0000".equals(transet.get("RET_CODE"))) {
						result.put("isOk", GlobalConstants.TRUE);
						result.put("ctNo", ctNo);
					} else {
						result.put("isOk", GlobalConstants.FALSE);
						result.put("eMsg", transet.get("ERR_MSG"));
					}
				} else {
					result.put("isOk", GlobalConstants.FALSE);
					result.put("eMsg", "返回报文验签失败");
				}
			} else {
				result.put("isOk", GlobalConstants.FALSE);
				result.put("eMsg", infoMap.get("ERR_MSG"));
			}
		}

		log.debug("---------------------------- 账户：[" + cardNo + "]" + " | 账户所属人：[" + belong + "] 分账返回结果："
				+ JsonUtil.object2String(result));

		return result;
	}

	@Override
	public CertUtil getCertUtil() {
		return null;
	}

	/**
	 * 签名
	 * 
	 * @param ss
	 * @param cert
	 * @return
	 */
	public String sign(String ss, Cert cert) {
		Signature sign = null;
		try {
			sign = Signature.getInstance("SHA1withRSA", AllinPayFactory.getProvider());
			sign.initSign(cert.getPrivateKey());
			sign.update(ss.getBytes("GBK"));
			byte signed[] = sign.sign();
			byte sign_asc[] = new byte[signed.length * 2];
			Hex2Ascii(signed.length, signed, sign_asc);

			return new String(sign_asc);
		} catch (SignatureException | UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		throw new CustomException("签名失败");
	}

	/**
	 * 转ASC2码
	 * 
	 * @param len
	 * @param data_in
	 * @param data_out
	 */
	private void Hex2Ascii(int len, byte data_in[], byte data_out[]) {
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; i++) {
			temp1[0] = data_in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0x0f);
			temp2[0] = data_in[i];
			temp2[0] = (byte) (temp2[0] & 0x0f);
			if (temp1[0] >= 0x00 && temp1[0] <= 0x09) {
				(data_out[j]) = (byte) (temp1[0] + '0');
			} else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f) {
				(data_out[j]) = (byte) (temp1[0] + 0x57);
			}

			if (temp2[0] >= 0x00 && temp2[0] <= 0x09) {
				(data_out[j + 1]) = (byte) (temp2[0] + '0');
			} else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f) {
				(data_out[j + 1]) = (byte) (temp2[0] + 0x57);
			}
			j += 2;
		}
	}

	private String getXml(AllinpayRuoteInfo routeInfo) {
		LinkedHashMap<String, String> INFO = routeInfo.getInfo();
		LinkedHashMap<String, String> TRANS = routeInfo.getTrans();

		String infoStr = XmlUtil.map2NoCDATAXml(INFO, "INFO").substring(38);
//		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//		Matcher m = p.matcher(infoStr);
		// infoStr = m.replaceAll("");
		// log.debug("-------------------------- INFO xml : " + infoStr);
		String transStr = XmlUtil.map2NoCDATAXml(TRANS, "TRANS").substring(38);
//		m = p.matcher(transStr);
		// transStr = m.replaceAll("");
		// log.debug("-------------------------- TRANS xml : " + transStr);

		String xmlStr = null;

		String header = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		String h_flag = "<AIPG>";
		String e_flag = "</AIPG>";
		xmlStr = header + h_flag + infoStr + transStr + e_flag;

		return xmlStr;
	}

	public String send(String url, String xml) throws Exception {

		byte[] postData;
		postData = xml.getBytes("GBK");
		URLConnection request = null;
		request = createRequest(url, "POST");

		request.setRequestProperty("Content-type", "application/tlt-notify");
		request.setRequestProperty("Content-length", String.valueOf(postData.length));
		request.setRequestProperty("Keep-alive", "false");

		OutputStream reqStream = request.getOutputStream();
		reqStream.write(postData);
		reqStream.close();

		ByteArrayOutputStream ms = null;
		String respText;

		InputStream resStream = request.getInputStream();
		ms = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int count;
		while ((count = resStream.read(buf, 0, buf.length)) > 0) {
			ms.write(buf, 0, count);
		}
		resStream.close();
		respText = new String(ms.toByteArray(), "GBK");
		return respText;
	}

	// protected static Logger log=Logger.getLogger(TestMain.class);
	private URLConnection createRequest(String strUrl, String strMethod) throws Exception {
		URL url = new URL(strUrl);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		if (conn instanceof HttpsURLConnection) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
			httpsConn.setRequestMethod(strMethod);
			httpsConn.setSSLSocketFactory(getSSLSF());
			httpsConn.setHostnameVerifier(sslHanderl);
		} else if (conn instanceof HttpURLConnection) {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod(strMethod);
		}
		return conn;
	}

	private static class SSLHandler implements X509TrustManager, HostnameVerifier {
		private SSLHandler() {
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	public static synchronized SSLSocketFactory getSSLSF() throws Exception {
		if (sslFactory != null)
			return sslFactory;
		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, new TrustManager[] { sslHanderl }, null);
		sslFactory = sc.getSocketFactory();
		return sslFactory;
	}

	private boolean verfiy(String resultStr, Cert cert, Map<String, String> resultMap) {
		try {
			String merSign = null;
			int iStart = resultStr.indexOf("<SIGNED_MSG>");
			if (iStart == -1)
				throw new CustomException("XML报文中不存在<SIGNED_MSG>");
			int end = resultStr.indexOf("</SIGNED_MSG>");
			if (end == -1)
				throw new CustomException("XML报文中不存在</SIGNED_MSG>");
			
			merSign = resultStr.substring(0, iStart) + resultStr.substring(end + 13);
			
			String ss = sign(merSign, cert).toLowerCase();
			
			log.debug("-------------------------- 【验签】待签名字符串：" + merSign);
			
			String signMsg = resultMap.get("SIGNED_MSG").toLowerCase();
			log.debug("-------------------------- 【验签】返回的签名：" + ss);
			
			Signature verify = Signature.getInstance("SHA1withRSA", AllinPayFactory.getProvider());
			
			verify.initVerify(cert.getPublicKey());
			
			byte signeddata[] = new byte[ss.length() / 2];
			
			Ascii2Hex(ss.length(), ss.getBytes("GBK"), signeddata);
			
			verify.update(merSign.getBytes("GBK"));
			
			if (verify.verify(signeddata)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * 将ASCII字符串转换成十六进制数据
	 * 
	 * @param len
	 *            ASCII字符串长度
	 * @param data_in
	 *            待转换的ASCII字符串
	 * @param data_out
	 *            已转换的十六进制数据
	 */
	private static void Ascii2Hex(int len, byte data_in[], byte data_out[]) {
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; j++) {
			temp1[0] = data_in[i];
			temp2[0] = data_in[i + 1];
			if (temp1[0] >= '0' && temp1[0] <= '9') {
				temp1[0] -= '0';
				temp1[0] = (byte) (temp1[0] << 4);

				temp1[0] = (byte) (temp1[0] & 0xf0);

			} else if (temp1[0] >= 'a' && temp1[0] <= 'f') {
				temp1[0] -= 0x57;
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xf0);
			}

			if (temp2[0] >= '0' && temp2[0] <= '9') {
				temp2[0] -= '0';

				temp2[0] = (byte) (temp2[0] & 0x0f);

			} else if (temp2[0] >= 'a' && temp2[0] <= 'f') {
				temp2[0] -= 0x57;

				temp2[0] = (byte) (temp2[0] & 0x0f);
			}
			data_out[j] = (byte) (temp1[0] | temp2[0]);

			i += 2;
		}

	}

	private String getReturnInfoXml(String resultXml) {
		int iStart = resultXml.indexOf("<INFO>");
		if (iStart == -1)
			return null;
		int end = resultXml.indexOf("</INFO>");
		if (end == -1)
			return null;
		String infoXml = resultXml.substring(iStart, end + 7);

		return infoXml;
	}

	private Map<String, String> getReturnInfo(String resultXml) {
		String infoXml = getReturnInfoXml(resultXml);
		return XmlUtil.xml2Map(infoXml);
	}

	private Map<String, String> getReturnTranset(String resultXml) {
		int iStart = resultXml.indexOf("<TRANSRET>");
		if (iStart == -1)
			return null;
		int end = resultXml.indexOf("</TRANSRET>");
		if (end == -1)
			return null;
		String infoXml = resultXml.substring(iStart, end + 11);

		return XmlUtil.xml2Map(infoXml);
	}

	public void yanqian(String xml) {
	 
		Cert cert = null;

		Map<String, String> info = getReturnInfo(xml);

		System.out.println(verfiy(xml, cert, info));
	}

}
