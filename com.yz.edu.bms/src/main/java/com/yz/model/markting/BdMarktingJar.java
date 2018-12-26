/**
 * @author C
 *
 */
package com.yz.model.markting;

import com.yz.model.common.PubInfo;

public class BdMarktingJar extends PubInfo {
	private String jarName;

	private String jarUrl;

	private String chargeClass;

	private String stageClass;

	private String registerClass;
	
	private String iChargeClass;

	private String isConfirm;

	private String isAllow;

	private String uploadUser;

	private String uploadUserId;

	private String updateUser;

	private String updateUserId;

	private String updateTime;

	private String uploadTime;

	private String jarDesc;
	
	private Object jar;
	
	private String confirmUser;

    private String confirmUserContacts;
    
    private String flowId;

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName == null ? null : jarName.trim();
	}

	public String getJarUrl() {
		return jarUrl;
	}

	public void setJarUrl(String jarUrl) {
		this.jarUrl = jarUrl == null ? null : jarUrl.trim();
	}

	public String getChargeClass() {
		return chargeClass;
	}

	public void setChargeClass(String chargeClass) {
		this.chargeClass = chargeClass == null ? null : chargeClass.trim();
	}

	public String getStageClass() {
		return stageClass;
	}

	public void setStageClass(String stageClass) {
		this.stageClass = stageClass == null ? null : stageClass.trim();
	}

	public String getRegisterClass() {
		return registerClass;
	}

	public void setRegisterClass(String registerClass) {
		this.registerClass = registerClass == null ? null : registerClass.trim();
	}

	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm == null ? null : isConfirm.trim();
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow == null ? null : isAllow.trim();
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser == null ? null : uploadUser.trim();
	}

	public String getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(String uploadUserId) {
		this.uploadUserId = uploadUserId == null ? null : uploadUserId.trim();
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getJarDesc() {
		return jarDesc;
	}

	public void setJarDesc(String jarDesc) {
		this.jarDesc = jarDesc == null ? null : jarDesc.trim();
	}

	public Object getJar() {
		return jar;
	}

	public void setJar(Object jar) {
		this.jar = jar;
	}
	
	public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser == null ? null : confirmUser.trim();
    }

    public String getConfirmUserContacts() {
        return confirmUserContacts;
    }

    public void setConfirmUserContacts(String confirmUserContacts) {
        this.confirmUserContacts = confirmUserContacts == null ? null : confirmUserContacts.trim();
    }

	/**
	 * @return the flowId
	 */
	public String getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(String flowId) {
		this.flowId = flowId == null ? null : flowId.trim();
	}

	/**
	 * @return the iChargeClass
	 */
	public String getiChargeClass() {
		return iChargeClass;
	}

	/**
	 * @param iChargeClass the iChargeClass to set
	 */
	public void setiChargeClass(String iChargeClass) {
		this.iChargeClass = iChargeClass == null ? null : iChargeClass.trim();
	}
}