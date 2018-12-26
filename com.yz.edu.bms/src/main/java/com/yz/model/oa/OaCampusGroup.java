package com.yz.model.oa;

import com.yz.model.common.PubInfo;

import java.util.Date;

public class OaCampusGroup extends PubInfo{
    private String id;
    private String campusGroupId;
    private String campusId;
    private String ext1;
    private String createTime;
    private String campusName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCampusGroupId() {
        return campusGroupId;
    }

    public void setCampusGroupId(String campusGroupId) {
        this.campusGroupId = campusGroupId == null ? null : campusGroupId.trim();
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId == null ? null : campusId.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }
}