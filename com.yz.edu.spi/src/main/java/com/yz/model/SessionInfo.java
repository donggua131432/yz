package com.yz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.internal.util.StringUtil;

public class SessionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7026783186081721256L;
	/** 用户编号 */
	private String userId;
	/** 登录名称/商家店名 */
	private String nickName;
	/** 头像地址 */
	private String headImg;
	/** 用户类型 */
	private List<String> iUserTypes;
	/** 真实姓名 */
	private String realName;
	/** 学员编号 */
	private String stdId;
	/** 员工编号 */
	private String empId;
	/** 异地登录标识 */
	private String jwtToken;
	/** 手机号 */
	private String mobile;
	/** 直接推介人 */
	private String pid;
	/** 性别 */
	private String sex;
	/** 微信公众对应ID */
	private String openId;
	/** 是否绑定身份证 */
	private String isBindCert;
	/** 用户类型 */
	private String userType;
	/** 上线用户类型 */
	private String pType;
	/** 上线用户是否米瓣 */
	private String pIsMb;

	/**
	 * 用户状态 1-正常 2-系统锁定 3-密码锁定 4-冻结账户
	 */
	private String userStatus;
	
	/**
	 * 用户类型 使用位运算  0|2|4|6...
	 */
	private int relation;

	/**
	 * 
	 * @param userType
	 */
	public void addiUserType(String userType) {
		if (!StringUtil.isEmpty(userType)) {
			if (iUserTypes == null) {
				iUserTypes = new ArrayList<>();
			}
			if (!iUserTypes.contains(userType)) {
				iUserTypes.add(userType);
			}
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg == null ? null : headImg.trim();
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken == null ? null : jwtToken.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid == null ? null : pid.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus == null ? null : userStatus.trim();
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName == null ? null : nickName.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null ? null : openId.trim();
	}

	public List<String> getiUserTypes() {
		return iUserTypes;
	}

	public void setiUserTypes(List<String> iUserTypes) {
		this.iUserTypes = iUserTypes;
	}

	public String getIsBindCert() {
		return isBindCert;
	}

	public void setIsBindCert(String isBindCert) {
		this.isBindCert = isBindCert == null ? null : isBindCert.trim();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType == null ? null : userType.trim();
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType == null ? null : pType.trim();
	}

	public String getpIsMb() {
		return pIsMb;
	}

	public void setpIsMb(String pIsMb) {
		this.pIsMb = pIsMb == null ? null : pIsMb.trim();
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

}