package com.yz.dubbo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 
 * @author Administrator
 *
 */
@ConfigurationProperties(prefix="dubbo")
public class YzDubboBean implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String scanPackage;
	
	private String owner="yzApp";

	private String application; //应用名称
	
	private String registryProtocol="zookeeper";//注册中心地址
	
	private int port;//端口号
	
	private String registryAddress;//注册中心的协议
	
	private String dispatcher="all";//
	
	private String threadpool="limited";
	
	private int threads=300;
	
	private String cluster="failover";
	
	private String loadbalance="random";
	
	private int retries=0;//
	
	private boolean validation=true;//
	
	private boolean check=false;
	
	private String serialization="hessian2";
	
	private String protocolName="dubbo";
	
	private String monitorAddress;
	
	
	public void setMonitorAddress(String monitorAddress) {
		this.monitorAddress = monitorAddress;
	}
	
	public String getMonitorAddress() {
		return monitorAddress;
	}
	
	public void setScanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
	}
	
	public String getScanPackage() {
		return scanPackage;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwner() {
		return owner;
	}
	

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	
	public String getProtocolName() {
		return protocolName;
	}
	
	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}
	
	public String getSerialization() {
		return serialization;
	}
	
	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getRegistryProtocol() {
		return registryProtocol;
	}

	public void setRegistryProtocol(String registryProtocol) {
		this.registryProtocol = registryProtocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(String dispatcher) {
		this.dispatcher = dispatcher;
	}

	public String getThreadpool() {
		return threadpool;
	}

	public void setThreadpool(String threadpool) {
		this.threadpool = threadpool;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public boolean getValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
