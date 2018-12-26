package com.yz.model.graduate;

import com.yz.model.common.PageCondition;
/**
 * 毕业申请-查询条件
 * @author lx
 * @date 2017年7月13日 上午11:48:15
 */
public class GraduateApplyQuery extends PageCondition {
	
	  private String unvsId;
	  private String recruitType;
	  private String grade;
	  private String pfsnName;
	  private String stdName;
	  private String idCard;
	  private String mobile;
	  
	  private String gCheckType;
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getgCheckType() {
		return gCheckType;
	}
	public void setgCheckType(String gCheckType) {
		this.gCheckType = gCheckType;
	}
}
