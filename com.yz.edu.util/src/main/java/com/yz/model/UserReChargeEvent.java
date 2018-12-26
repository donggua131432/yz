package com.yz.model;

import java.util.Date;
import java.util.List;

/**
 * 用户充值事件
 * 
 * @ClassName: UserReChargeEvent
 * @Description: 处理用户充值，推荐人能获得的智米奖励
 * @author zhanggh
 * @date 2018年5月4日
 *
 */
public class UserReChargeEvent extends BaseEvent {

	private String userId; // 用户Id

	private String pId; // 推荐人ID

	private String payable;// 支付金额

	private String mappingId;// 映射ID

	private Date createTime;// 创建时间

	private Date payDateTime;// 支付时间

	private String lSize; // 是否是往届学员 0：本届学员 大于0:往届学员

	private List<String> itemCode;// 学年ID组 Y0 Y1 Y2 Y3

	private List<String> itemYear;// 学年 1,2,3,4

	private String scholarship;// 优惠类型 1：全额奖学金

	private String recruitType;// 招生类型 1：成教 2：国开

	private String grade;// 年级 2015级 2016级
	
	private String stdStage;//学员状态
	
	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return grade;
	}

	/**
	 * 是否是往届学员 0：本届学员 大于0:往届学员
	 * 
	 * @return
	 */
	public String getlSize() {
		return lSize;
	}

	/**
	 * 是否是往届学员
	 * 
	 * @param lSize
	 *            0：本届学员 大于0:往届学员
	 */
	public void setlSize(String lSize) {
		this.lSize = lSize;
	}

	public List<String> getItemCode() {
		return itemCode;
	}

	public void setItemCode(List<String> itemCode) {
		this.itemCode = itemCode;
	}

	public List<String> getItemYear() {
		return itemYear;
	}

	public void setItemYear(List<String> itemYear) {
		this.itemYear = itemYear;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayDateTime() {
		return payDateTime;
	}

	public void setPayDateTime(Date payDateTime) {
		this.payDateTime = payDateTime;
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

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

}
