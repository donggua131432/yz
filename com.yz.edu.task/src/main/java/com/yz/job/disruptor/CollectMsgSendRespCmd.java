package com.yz.job.disruptor;

import java.io.Serializable;
import java.util.Date;


public class CollectMsgSendRespCmd implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8336846296223651906L;
	
	private String mappingId;    // 对应的业务id
	
	private String srId;        //接受消息主键
	
	private String receiverId;   // 消息接收人id
	
	private String receiverName; //消息接收人
	
	private String sender;       //消息发送人

	private Date createDate; // 创建时间
	
	public CollectMsgSendRespCmd()
	{
		
	}

	public CollectMsgSendRespCmd(String mappingId) {
		this.mappingId = mappingId;
		this.createDate = new Date();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSrId() {
		return srId;
	}

	public void setSrId(String srId) {
		this.srId = srId;
	}
	
}
