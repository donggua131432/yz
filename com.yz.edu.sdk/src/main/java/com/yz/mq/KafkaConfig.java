package com.yz.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author lingdian
 */
@SuppressWarnings("serial")
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig implements java.io.Serializable {

	private String bootstrapServers; // 服务器地址

	private String acks = "0";// 是否act

	private int retries = 0; // 重试

	private int batchSize = 16*1024; // 批量

	private int bufferMemory = 16 * 1024 * 1024; // 缓存冲
	 
	private int mqPartition = 4 ; // MQ topic分区数

	private Boolean kafkaDerail  = false;  //  kafka开关 false

	public Boolean getKafkaDerail() {
		return kafkaDerail;
	}

	public void setKafkaDerail(Boolean kafkaDerail) {
		this.kafkaDerail = kafkaDerail;
	}

	public void setMqPartition(int mqPartition) {
		this.mqPartition = mqPartition;
	}
	
	public int getMqPartition() {
		return mqPartition;
	}
	
	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		this.acks = acks;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(int bufferMemory) {
		this.bufferMemory = bufferMemory;
	}
}
