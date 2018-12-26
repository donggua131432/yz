package com.yz.model.graduate;

import java.io.Serializable;
/**
 * 图像采集
 * @author lx
 * @date 2017年7月17日 下午2:23:32
 */
public class BdGraduateCollectInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7333520595665756659L;

	private String confirmLocation;
	private String confirmTime;
	private String confirmResult;
	private String xinHua;
	private String information;
	private String remark;
	
	public String getConfirmLocation() {
		return confirmLocation;
	}
	public void setConfirmLocation(String confirmLocation) {
		this.confirmLocation = confirmLocation;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getConfirmResult() {
		return confirmResult;
	}
	public void setConfirmResult(String confirmResult) {
		this.confirmResult = confirmResult;
	}
	public String getXinHua() {
		return xinHua;
	}
	public void setXinHua(String xinHua) {
		this.xinHua = xinHua;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
