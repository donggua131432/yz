package com.yz.model;

/**
 * 离线数据采集
 * 
 * @ClassName: InviteDataAcquisitionEvent
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月19日
 *
 */
public class InviteDataAcquisitionEvent extends BaseEvent {
	private String remote_addr;// 请求IP地址
	private String user_req;// 请求目标
	private String req_time;// 耗时
	private String timestamp;// 请求时间
	private InviteDataLocation location;// 请求IP地址

	private String channelId;// 渠道id
	private String channelType;// 类型

	private String userId; // 邀约人Id
	
	public InviteDataAcquisitionEvent()
	{
		setTrace(false);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getChannel_id() {
		return channelId;
	}

	public void setChannel_id(String channelId) {
		this.channelId = channelId;
	}

	public String getRemote_addr() {
		return remote_addr;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public String getUser_req() {
		return user_req;
	}

	public void setUser_req(String user_req) {
		this.user_req = user_req;
	}

	public String getReq_time() {
		return req_time;
	}

	public void setReq_time(String req_time) {
		this.req_time = req_time;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public InviteDataLocation getLocation() {
		return location;
	}

	public void setLocation(InviteDataLocation location) {
		this.location = location;
	}

}
