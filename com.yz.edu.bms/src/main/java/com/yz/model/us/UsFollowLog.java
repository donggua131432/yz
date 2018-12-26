package com.yz.model.us;

import java.util.Date;

public class UsFollowLog {
    private String recrodsNo;

    private String oldEmpId;

    private String oldDpId;

    private String oldCampusId;

    private String empId;

    private String dpId;

    private String campusId;

    private Date createTime;

    private String userId;

    private String createUser;

    private String createUserId;
    
    private String createTimeStr;
    
    private String empChange;
    
    private String dpChange;
    
    private String campusChange;

    private String drType;

    private String drId;

    private String drName;

    private String remark;

    public String getRecrodsNo() {
        return recrodsNo;
    }

    public void setRecrodsNo(String recrodsNo) {
        this.recrodsNo = recrodsNo == null ? null : recrodsNo.trim();
    }

    public String getOldEmpId() {
        return oldEmpId;
    }

    public void setOldEmpId(String oldEmpId) {
        this.oldEmpId = oldEmpId == null ? null : oldEmpId.trim();
    }

    public String getOldDpId() {
        return oldDpId;
    }

    public void setOldDpId(String oldDpId) {
        this.oldDpId = oldDpId == null ? null : oldDpId.trim();
    }

    public String getOldCampusId() {
        return oldCampusId;
    }

    public void setOldCampusId(String oldCampusId) {
        this.oldCampusId = oldCampusId == null ? null : oldCampusId.trim();
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId == null ? null : empId.trim();
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId == null ? null : dpId.trim();
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId == null ? null : campusId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getEmpChange() {
		return empChange;
	}

	public void setEmpChange(String empChange) {
		this.empChange = empChange;
	}

	public String getDpChange() {
		return dpChange;
	}

	public void setDpChange(String dpChange) {
		this.dpChange = dpChange;
	}

	public String getCampusChange() {
		return campusChange;
	}

	public void setCampusChange(String campusChange) {
		this.campusChange = campusChange;
	}

    public String getDrType() {
        return drType;
    }

    public void setDrType(String drType) {
        this.drType = drType == null ? null : drType.trim();
    }

    public String getDrId() {
        return drId;
    }

    public void setDrId(String drId) {
        this.drId = drId == null ? null : drId.trim();
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName == null ? null : drName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}