package com.yz.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yz.model.sf.SFExpressRequest;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;

public class SFUtil {

	/**
	 * 顺丰下单xml赋值
	 * 
	 * @param sf
	 * @return
	 */
	public static String setSFXml2String(SFExpressRequest sf) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("template/sf-order.xml");
			Document document = reader.read(in);

			Element root = document.getRootElement();
			Element head = root.element("Head");
			head.setText(sf.getHead());
			Element body = root.element("Body");
			Element order = body.element("Order");
			order.attribute("orderid").setValue(sf.getOrderid());
			order.attribute("d_contact").setValue(sf.getdContact());
			order.attribute("d_province").setValue(sf.getdProvince());
			order.attribute("d_city").setValue(sf.getdCity());
			order.attribute("d_county").setValue(sf.getdCountry());
			order.attribute("d_tel").setValue(sf.getdTel());
			order.attribute("d_mobile").setValue(sf.getdMobile());
			order.attribute("d_address").setValue(sf.getdAddress());
			order.attribute("j_company").setValue(sf.getjCompany());
			order.attribute("j_contact").setValue(sf.getjContact());
			order.attribute("j_tel").setValue(sf.getjTel());
			order.attribute("j_province").setValue(sf.getjProvince());
			order.attribute("j_city").setValue(sf.getjCity());
			order.attribute("j_county").setValue(sf.getjCountry());
			order.attribute("j_address").setValue(sf.getjAddress());
			order.attribute("custid").setValue(sf.getCustid());
			return document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 顺丰订单轨迹 xml 赋值
	 * 
	 * @param sfNo
	 * @param sfHead
	 * @return
	 */
	public static String setSFXml2String(String sfNo, String sfHead) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("template/sf-order-query.xml");
			Document document = reader.read(in);

			Element root = document.getRootElement();
			Element head = root.element("Head");
			head.setText(sfHead);
			Element body = root.element("Body");
			Element route = body.element("RouteRequest");
			route.attribute("tracking_number").setValue(sfNo);

			return document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String loadFile(String fileName) {
		InputStream fis;
		try {
			fis = new FileInputStream(fileName);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			String res = new String(bs, "utf8");
			fis.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}

	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String encodeBase64(byte[] b) {
		return CodeUtil.base64Encode2String(b);
	}
}
