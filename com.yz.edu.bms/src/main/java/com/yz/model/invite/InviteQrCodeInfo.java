package com.yz.model.invite;

public class InviteQrCodeInfo {
	private String channelId; // 渠道编号
	private String empId; // 邀约人
	private String empName; // 邀约人
	private String defaultUrl;//默认地址
	private String inviteQrcodeUrl; // 邀约二维码地址
	private String inviteUrl; // 活动地址
	private String inviteName; // 活动名称
	private String createUser; // 创建人
	private String createTime; // 创建时间
	private String remark; // 备注
	private String lookCount; // 浏览人数
	private String registerCount; // 注册人数
	private String reChargeCount; // 缴费人数
	private String createUserName;//创建人名称
	
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getInviteQrcodeUrl() {
		return inviteQrcodeUrl;
	}

	public void setInviteQrcodeUrl(String inviteQrcodeUrl) {
		this.inviteQrcodeUrl = inviteQrcodeUrl;
	}

	public String getInviteUrl() {
		return inviteUrl;
	}

	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}

	public String getInviteName() {
		return inviteName;
	}

	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLookCount() {
		return lookCount;
	}

	public void setLookCount(String lookCount) {
		this.lookCount = lookCount;
	}

	public String getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(String registerCount) {
		this.registerCount = registerCount;
	}

	public String getReChargeCount() {
		return reChargeCount;
	}

	public void setReChargeCount(String reChargeCount) {
		this.reChargeCount = reChargeCount;
	}

}
