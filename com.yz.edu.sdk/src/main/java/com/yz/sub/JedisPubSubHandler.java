package com.yz.sub;

import redis.clients.jedis.JedisPubSub;

/**
 * @desc redis 订阅 发布的基类
 * @author Administrator
 *
 */
public abstract class JedisPubSubHandler<T> extends JedisPubSub implements Comparable<JedisPubSubHandler> {
	
	private int seq; // 序列 
	
	private String name; // 名称

	private String channel; // redis channel

	private String convert; // 转化handler

	private Class<?> target; // 目标类

	private boolean enable; // 是否启用

	private byte pubType; // 类型

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public int getSeq() {
		return seq;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getConvert() {
		return convert;
	}

	public void setConvert(String convert) {
		this.convert = convert;
	}

	public Class<?> getTarget() {
		return target;
	}

	public void setTarget(Class<?> target) {
		this.target = target;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public byte getPubType() {
		return pubType;
	}

	public void setPubType(byte pubType) {
		this.pubType = pubType;
	}
	
	
	@Override
	public int compareTo(JedisPubSubHandler o) {
		return this.seq<=o.getSeq()?1:-1;
	}
	
	public abstract void execute(T obj);
}
