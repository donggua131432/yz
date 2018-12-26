package com.yz.model;

public class UsBaseInfo {
	private String userId;

    private String headImg;

    private String nickname;

    private String realName;

    private String sex;

    private String birthday;

    private String education;

    private String profession;

    private String graduateTime;

    private String userType;

    private String yzCode;

    private String mobile;

    private String mySign;

    private String province;

    private String city;

    private String district;

    private String mobileLocation;

    private String mobileZip;

    private String pId;
    
    private String pType;
    
    private String pIsMb;

    private String regTime;

    private String userStatus;
    
    private String unblockedTime;
    
    private String wechatOpenId;
    
    private String countryName;
    
    private String provinceName;
    
    private String cityName;
    
    private String sg;
    
    private String scholarship;
    
    private String isMb;
    
    private String stdId; // 学员Id
    
    private String empId; // 员工Id
    
    private int relation  ; //   2-员工  4-学员
    
    private String channelId  ;//渠道ID
    
    public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession == null ? null : profession.trim();
    }

    public String getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(String graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getYzCode() {
        return yzCode;
    }

    public void setYzCode(String yzCode) {
        this.yzCode = yzCode == null ? null : yzCode.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getMySign() {
        return mySign;
    }

    public void setMySign(String mySign) {
        this.mySign = mySign == null ? null : mySign.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getMobileLocation() {
        return mobileLocation;
    }

    public void setMobileLocation(String mobileLocation) {
        this.mobileLocation = mobileLocation == null ? null : mobileLocation.trim();
    }

    public String getMobileZip() {
        return mobileZip;
    }

    public void setMobileZip(String mobileZip) {
        this.mobileZip = mobileZip == null ? null : mobileZip.trim();
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId == null ? null : pId.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
    }

	public String getUnblockedTime() {
		return unblockedTime;
	}

	public void setUnblockedTime(String unblockedTime) {
		this.unblockedTime = unblockedTime;
	}

	public String getWechatOpenId() {
		return wechatOpenId;
	}

	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String getIsMb() {
		return isMb;
	}

	public void setIsMb(String isMb) {
		this.isMb = isMb;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	public String getpIsMb() {
		return pIsMb;
	}

	public void setpIsMb(String pIsMb) {
		this.pIsMb = pIsMb;
	}


	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

}