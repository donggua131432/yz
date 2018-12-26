package com.yz.model.recruit;

public class StudentModifyListInfo {
    private String modifyId;

    private String learnId;

    private String stdId;

    private String modifyType;

    private String stdName;
    
    private String modifyText;
    
    private String isComplete;
    
    private String createTime;
    
    private String updateTime;
    
    private String createUser;
    
    private String updateUser;

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId == null ? null : learnId.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType == null ? null : modifyType.trim();
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName == null ? null : stdName.trim();
    }

	public String getModifyText() {
		return modifyText;
	}

	public void setModifyText(String modifyText) {
		this.modifyText = modifyText == null ? null : modifyText.trim();
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	
	
	

}