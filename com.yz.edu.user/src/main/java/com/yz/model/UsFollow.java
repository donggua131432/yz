package com.yz.model;

public class UsFollow {
    private String userId;

    private String empId;
    
    private String dpId;
    private String dpManager;
    
    private String campusId;
    private String campusManager;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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

	public String getDpManager() {
		return dpManager;
	}

	public void setDpManager(String dpManager) {
		this.dpManager = dpManager == null ? null : dpManager.trim();
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId == null ? null : campusId.trim();
	}

	public String getCampusManager() {
		return campusManager;
	}

	public void setCampusManager(String campusManager) {
		this.campusManager = campusManager == null ? null : campusManager.trim();
	}
    
}