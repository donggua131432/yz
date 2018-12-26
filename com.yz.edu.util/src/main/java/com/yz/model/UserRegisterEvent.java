package com.yz.model;

 
/**
 * @desc 用户注册事件 
 * @author Administrator
 *
 */
public class UserRegisterEvent extends BaseEvent{

	private String userId; // 用户Id
	 
	private String pId ; // 推荐人ID
	
	private String pUserType ;// 推荐人用户类型
	
	private String bindType ; // 手机号码注册 微信授权注册
	
	private String idCard; // 身份证号码
	
	private int pRelation ; // 推荐人的类型 
	
	private String realName; // 名称
	
	private boolean isRegistered = false;
	
	private String mobile ; //注册手机号码 
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getMobile() {
		return mobile;
	}
	 
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getRealName() {
		return realName;
	}
	 
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getpRelation() {
		return pRelation;
	}

	public void setpRelation(int pRelation) {
		this.pRelation = pRelation;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getpUserType() {
		return pUserType;
	}

	public void setpUserType(String pUserType) {
		this.pUserType = pUserType;
	}

	public String getBindType() {
		return bindType;
	}

	public void setBindType(String bindType) {
		this.bindType = bindType;
	} 
    
}
