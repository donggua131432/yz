package com.yz.model.gk;

import java.io.Serializable;

/**
 * 国开统考信息
 * @author lx
 * @date 2018年6月6日 上午10:24:48
 */
public class BdStudentGkUnifiedExamInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3973522946249130249L;
	
	private String id;               //主键
	private String title;            //标题
	private String describe;         //描述
	private String startTime;       //报名缴费开始时间
	private String endTime;         //报名缴费结束时间
	private String testSubject;      //考试科目
	private String operationDesc;    //操作描述
	private String fileUrl;          //文件路径
	private String fileName;         //文件名
	private String ifShow;           //是否显示
	private String createUserId;
	private String createTime;
	private String createUserName;
	private String updateUserId;
	private String updateUserName;
	
	private String isPhotoChange;        //是否修改
	private Object annexUrl;             //
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getTestSubject() {
		return testSubject;
	}
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
	public String getOperationDesc() {
		return operationDesc;
	}
	public void setOperationDesc(String operationDesc) {
		this.operationDesc = operationDesc;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getIfShow() {
		return ifShow;
	}
	public void setIfShow(String ifShow) {
		this.ifShow = ifShow;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	public Object getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(Object annexUrl) {
		this.annexUrl = annexUrl;
	}
	
}
