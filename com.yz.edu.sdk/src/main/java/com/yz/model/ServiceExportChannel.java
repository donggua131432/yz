package com.yz.model;

/**
 * 
 * @author Administrator
 *
 */
public class ServiceExportChannel implements java.io.Serializable{

	private int type= 0 ; // 0 export 1 unexport
	
	private YzServiceInfo serviceInfo;
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setServiceInfo(YzServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
	public YzServiceInfo getServiceInfo() {
		return serviceInfo;
	}
}
