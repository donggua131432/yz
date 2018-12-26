package com.yz.model.baseinfo;

import java.util.Date;

public class BdUniversity {
    private String unvsId;

    private String unvsName;

    private String unvsCode;

    private String recruitType;

    private String unvsAddress;

    private String remark;

    private String isStop;

    private String provinceCode;
    private String cityCode;
    private String districtCode;

    private String sort;

    private Date updateTime;

    private String updateUser;

    private String updateUserId;

    private String createUserId;

    private Date createTime;

    private String createUser;

    private String ext1;

    private String ext2;

    private String ext3;

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId == null ? null : unvsId.trim();
    }

    public String getUnvsName() {
        return unvsName;
    }

    public void setUnvsName(String unvsName) {
        this.unvsName = unvsName == null ? null : unvsName.trim();
    }

    public String getUnvsCode() {
        return unvsCode;
    }

    public void setUnvsCode(String unvsCode) {
        this.unvsCode = unvsCode == null ? null : unvsCode.trim();
    }

    public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getUnvsAddress() {
        return unvsAddress;
    }

    public void setUnvsAddress(String unvsAddress) {
        this.unvsAddress = unvsAddress == null ? null : unvsAddress.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

	@Override
	public String toString() {
		return "BdUniversity [unvsId=" + unvsId + ", unvsName=" + unvsName + ", unvsCode=" + unvsCode + ", recruit_type="
				+ recruitType + ", unvsAddress=" + unvsAddress + ", remark=" + remark + ", isStop=" + isStop
				+ ", provinceCode=" + provinceCode + ", cityCode=" + cityCode + ", districtCode=" + districtCode + ", sort=" + sort
				 + ", updateTime=" + updateTime + ", updateUser=" + updateUser + ", updateUserId="
				+ updateUserId + ", createUserId=" + createUserId + ", createTime=" + createTime + ", createUser="
				+ createUser + ", ext1=" + ext1 + ", ext2=" + ext2 + ", ext3=" + ext3 + "]";
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
    
    
}