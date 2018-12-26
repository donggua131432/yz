package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

public class BdShoppingAddress extends PubInfo {
    private String saId;

    private String stdId;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String address;

    private String saType;

    private String isDefault;

    private String saStatus;

    private String reason;

    public String getSaId() {
        return saId;
    }

    public void setSaId(String saId) {
        this.saId = saId == null ? null : saId.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode == null ? null : districtCode.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getSaType() {
        return saType;
    }

    public void setSaType(String saType) {
        this.saType = saType == null ? null : saType.trim();
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
    }

    public String getSaStatus() {
        return saStatus;
    }

    public void setSaStatus(String saStatus) {
        this.saStatus = saStatus == null ? null : saStatus.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

}