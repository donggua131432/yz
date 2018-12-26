package com.yz.model.goods;

import java.io.Serializable;
/**
 * 活动通知
 * @author lx
 * @date 2017年8月3日 下午2:18:24
 */
public class GsSalesNotify implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4327475914980918779L;

	private String notifyId;
	private String salesType;
	private String salesName;
	private String nickName;
	private String mobile;
	private String isNotify;
	private String openId;
	
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsNotify() {
		return isNotify;
	}
	public void setIsNotify(String isNotify) {
		this.isNotify = isNotify;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
}
