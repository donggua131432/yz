package com.yz.edu.model;

import java.lang.reflect.Method;
import java.util.Date;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.yz.edu.constant.ObservatoryStarConstant.ObserStarEnum;
import com.yz.util.DateUtil; 

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

@SuppressWarnings("serial")
public abstract class BaseEsTraceObj implements java.io.Serializable {

	private static Logger logger = LoggerFactory.getLogger(BaseEsTraceObj.class);

	private transient ObserStarEnum alarm;

	/**
	 * @desc 讲对象转化为XContentBuilder
	 * @return
	 */
	public XContentBuilder toXContentBuilder() {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")&&!key.equals("alarm")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(this);
					if (value instanceof Date) {
						value = DateUtil.formatDate((Date) value, DateUtil.YYYYMMDDHHMMSS_SPLIT);
					}
					builder.field(key, value);
				}
			}
			builder.endObject();
			return builder;
		} catch (Exception e) {
			logger.error("toXContentBuilder.error:{}", e);
			return null;
		}
	}

	public void setAlarm(ObserStarEnum alarm) {
		this.alarm = alarm;
	}

	public ObserStarEnum getAlarm() {
		return alarm;
	}
	
	public abstract String getId();

	public abstract String indexName();
}
