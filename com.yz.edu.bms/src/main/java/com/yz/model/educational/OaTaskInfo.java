package com.yz.model.educational;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

/**
 * 教务任务
 * @author lx
 * @date 2017年7月19日 下午3:14:17
 */
public class OaTaskInfo extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1859817286500710352L;
	
	private String taskId;
	private String taskTitle;
	private String taskContent;
	private String creater;
	private String startTime;
	private String endTime;
	private String totalCount;
	private String finishCount;
	private String taskUrl;
	private String isAllow;
	private String isNeedCheck;
	private String taskType;
	
	private String issuer;          //发布人(员工id)
	private String principal;       //负责人(员工id)
	
	private String createTime;
	
	private String eyId;         //考试年度
	private String ifCanOper;    //针对考场确认任务
	
	private String gkEyId;   //国开考试年度
	
	private String examEyIdGK; //国开城市确认考试年度
	
	private String gkUnifiedId; //国开统考
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(String finishCount) {
		this.finishCount = finishCount;
	}
	public String getTaskUrl() {
		return taskUrl;
	}
	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
	public String getIsNeedCheck() {
		return isNeedCheck;
	}
	public void setIsNeedCheck(String isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getEyId()
	{
		return eyId;
	}
	public void setEyId(String eyId)
	{
		this.eyId = eyId;
	}
	public String getIfCanOper()
	{
		return ifCanOper;
	}
	public void setIfCanOper(String ifCanOper)
	{
		this.ifCanOper = ifCanOper;
	}
	public String getGkEyId()
	{
		return gkEyId;
	}
	public void setGkEyId(String gkEyId)
	{
		this.gkEyId = gkEyId;
	}
	public String getExamEyIdGK() {
		return examEyIdGK;
	}
	public void setExamEyIdGK(String examEyIdGK) {
		this.examEyIdGK = examEyIdGK;
	}
	public String getGkUnifiedId() {
		return gkUnifiedId;
	}
	public void setGkUnifiedId(String gkUnifiedId) {
		this.gkUnifiedId = gkUnifiedId;
	}

}
