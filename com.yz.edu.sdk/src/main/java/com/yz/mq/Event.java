package com.yz.mq;

import com.google.common.collect.Maps;
import com.yz.serializ.FstSerializer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable; 
import java.util.Map;

/**
 *
 * @desc mq发送event
 * @auth lingdian
 */
public class Event implements Serializable {
	private static final long serialVersionUID = -1448863431536362140L;
	private transient static final Logger logger = LoggerFactory.getLogger(Event.class);
	private String traceId; // TraceId
	private String topic; // 发送的topic
	private Object event;  // 发送的内容 
	private byte[] body; 
	private Map<String, String> properties = Maps.newHashMap();
	private static FstSerializer fstSerializer = FstSerializer.getInstance();

	public Event(){

	}

	public Event(String topic, Object event) {
		this.topic = topic;
		this.event = event;
	}

	public Event(String topic, Object event, Map<String, String> properties) {
		this.topic = topic;
		this.event = event;
		this.properties = properties;
	}

	public Event(String topic, byte[] body) {
		this.topic = topic;
		this.body = body == null ? null : body.clone();
	}

	public Event(String topic, byte[] body, Map<String, String> properties) {
		this.topic = topic;
		this.body = body == null ? null : body.clone();
		this.properties = properties;
	}

	public <T> T getEvent(Class<T> clazz) {
		if (this.body == null) {
			throw new NullPointerException("event body is null.");
		}
		this.event = fstSerializer.deserialize(this.body, clazz);
		return cast(this.event, clazz);
	}

	public <T> T cast(Object event, Class<T> clazz) {
		if (event == null) {
			throw new IllegalArgumentException("event is null.");
		}
		try {
			return clazz.cast(event);
		} catch (ClassCastException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

 

	public String getTraceId() {
		return this.traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getEvent() {
		return this.event;
	}

	public void setEvent(Object event) {
		this.event = event;
	}

	public byte[] getBody() {
		return this.body == null ? null : body.clone();
	}

	public void setBody(byte[] body) {
		this.body = body == null ? null : body.clone();
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void putProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public String getProperty(String key) {
		logger.debug("properties info : {}, key : {} ", this.properties, key);
		if (key == null) {
			logger.error("key cant not be null");
			return null;
		}
		if (this.properties == null) {
			logger.error("the properties is null!");
			return null;
		}
		return (String) this.properties.get(key);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SIMPLE_STYLE);
	}
}
