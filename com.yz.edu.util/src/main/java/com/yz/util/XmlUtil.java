package com.yz.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.reflect.FieldUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;

public class XmlUtil {

	private static XmlMapper xml = new XmlMapper();

	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	/**
	 * 将参数转换为xml格式
	 * 
	 * @param data
	 * @return
	 */
	public static String map2NoCDATAXml(Map<String, String> data, String rootName) {
		Document document = DocumentHelper.createDocument();
		Set<String> keys = data.keySet();

		Iterator<String> it = keys.iterator();

		Element root = document.addElement(rootName);

		while (it.hasNext()) {
			String key = it.next();
			String value = data.get(key);
			if (StringUtil.isEmpty(value))
				continue;
			Element e = root.addElement(key);
			e.addText(value);
		}
		return document.asXML();
	}

	/**
	 * 将参数转换为xml格式
	 * 
	 * @param data
	 * @return
	 */
	public static String map2Xml(Map<String, String> data, String rootName) {
		Document document = DocumentHelper.createDocument();
		Set<String> keys = data.keySet();

		Iterator<String> it = keys.iterator();

		Element root = document.addElement(rootName);

		while (it.hasNext()) {
			String key = it.next();
			String value = data.get(key);
			if (!StringUtil.hasValue(value))
				continue;
			Element e = root.addElement(key);
			e.addCDATA(value);
		}
		return document.asXML();
	}

	/**
	 * 将参数转换为xml格式
	 * 
	 * @param data
	 * @return
	 */
	public static Document map2XmlObjet(Map<String, String> data, String rootName) {
		Document document = DocumentHelper.createDocument();

		Set<String> keys = data.keySet();

		Iterator<String> it = keys.iterator();

		Element root = document.addElement(rootName);

		while (it.hasNext()) {
			String key = it.next();
			String value = data.get(key);
			if (!StringUtil.hasValue(value))
				continue;
			Element e = root.addElement(key);
			e.addCDATA(value);
		}
		return document;
	}

	public static Map<String, String> xml2Map(String xmlString, String charset) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = new ByteArrayInputStream(xmlString.getBytes(charset));
			Document document = reader.read(in);

			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			Map<String, String> result = new HashMap<String, String>();
			for (Element e : elements) {
				String key = e.getName();
				String value = e.getText();
				result.put(key, value);
			}
			return result;
		} catch (UnsupportedEncodingException | DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 顺丰响应XML解析
	 * 
	 * @param xmlString
	 * @param element
	 * @return
	 */
	public static Map<String, String> readSFXml(String xmlString, String charset) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = new ByteArrayInputStream(xmlString.getBytes(charset));
			Document document = reader.read(in);

			Map<String, String> result = new HashMap<String, String>();

			Element root = document.getRootElement();
			Element head = root.element("Head");
			result.put("Head", head.getText());
			if ("ERR".equals(head.getText())) {
				Element error = root.element("ERROR");
				result.put("errorCode", error.attribute("code").getValue());
				result.put("errorMsg", error.getText());
				return result;
			}

			Element body = root.element("Body");
			Element ele = body.element("OrderResponse");
			for (Iterator it = ele.attributeIterator(); it.hasNext();) {
				Attribute conAttr = (Attribute) it.next();
				String conTxt = conAttr.getValue();
				String conAttrName = conAttr.getName();
				result.put(conAttrName, conTxt);
			}
			return result;
		} catch (UnsupportedEncodingException | DocumentException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * javaBean转化为xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String objToXml(Object obj) {
		try {
			return xml.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("objToXml.error:{}", ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 将xml转化为Map集合
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> xmlToMap(InputStream ins) {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(ins);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		try {
			ins.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return map;
	}

	/**
	 * 顺丰响应XML解析
	 * 
	 * @param xmlString
	 * @param element
	 * @return
	 */
	public static Map<String, String> readSFXml(String xmlString) {
		return readSFXml(xmlString, "UTF-8");
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	public static Map<String, String> xml2Map(String xmlString) {
		return xml2Map(xmlString, "UTF-8");
	}

	/**
	 * 
	 * @param xmlString
	 * @param cls
	 *            目标类型
	 * @return
	 */
	public static <T> T xml2Obj(String xmlString, Class<?> cls) {
		try {
			if (StringUtil.isBlank(xmlString)) {
				logger.error("xmlString is null !");
				return null;
			}
			return (T) xml.readValue(xmlString, cls);
		} catch (Exception e) {
			logger.error("xml2Obj.error:{}", ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 
	 * @param xmlString
	 * @param cls
	 *            目标类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> xml2List(String xmlString, Class<?> cls) {
		if (StringUtil.isBlank(xmlString)) {
			logger.error("xmlString is null !");
			return null;
		}
		try {
			Document doc = DocumentHelper.parseText(xmlString);
			List<T> list = Lists.newArrayList();
			List<Element> data = doc.getRootElement().elements();
			for (Element el : data) {
				Object target = cls.newInstance();
				List<Element> els = el.elements();
				for (Element e : els) {
					ReflectionUtils.setFieldValue(target, e.getName(), e.getText());
				}
				list.add((T) target);
			}
			return list;
		} catch (Exception e) {
			logger.error("xml2List.error:{}", ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List<Map<String, String>> readSFQueryXml(String xmlString) {
		return readSFQueryXml(xmlString, "UTF-8");
	}

	/**
	 * 
	 * @param xmlString
	 * @param charset
	 * @return
	 */
	public static List<Map<String, String>> readSFQueryXml(String xmlString, String charset) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = new ByteArrayInputStream(xmlString.getBytes(charset));
			Document document = reader.read(in);

			Element root = document.getRootElement();
			Element head = root.element("Head");
			if ("ERR".equals(head.getText())) {
				return null; // 直接返回空,错误信息不展示给用户
			}

			Element body = root.element("Body");
			Element ele = body.element("RouteResponse");
			List<Map<String, String>> maps = new ArrayList<>();
			if (null != ele) {
				List<Element> route = ele.elements("Route");

				if (null != route && route.size() > 0) {
					for (Element element : route) {
						Map<String, String> map = new HashMap<>();
						map.put("accept_time", element.attributeValue("accept_time"));
						map.put("accept_address", element.attributeValue("accept_address"));
						map.put("remark", element.attributeValue("remark"));
						map.put("opcode", element.attributeValue("opcode"));

						maps.add(map);

					}
				}
			}

			return maps;
		} catch (UnsupportedEncodingException | DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
