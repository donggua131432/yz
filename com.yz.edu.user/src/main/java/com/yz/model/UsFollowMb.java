package com.yz.model;

import java.io.Serializable;

/**
 * @描述: 米瓣信息
 * @作者: DuKai
 * @创建时间: 2018/3/5 17:53
 * @版本号: V1.0
 */
public class UsFollowMb implements Serializable{

    private static final long serialVersionUID = -7075356667844566771L;

    private String userId;
    private String realName;
    private String sex;
    private String mobile;
    private String idCard;
    private String headImg;
    private String authToken;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
