package com.yz.model.oa;

import java.io.Serializable;

/**
 * @描述: 粉丝跟进人信息
 * @作者: DuKai
 * @创建时间: 2017/12/7 18:20
 * @版本号: V1.0
 */
public class OaEmpFollowInfo implements Serializable {

    private static final long serialVersionUID = -6860956559729352418L;

    //跟进人基本信息
    private String empId;              //员工ID
    private String empName;            //员工名称
    private String mobile;             //手机
    private String empStatus;          //员工状态
    //跟进人所属部门信息
    private String dpId;               //部门id
    private String dpName;             //部门名称
    private String dpManager;          //部门负责人ID
    private String dpManagerName;      //部门负责人名称
    //跟进人所属校区信息
    private String campusId;            //校区ID
    private String campusName;          //校区名称
    private String campusManager;       //校区负责人ID
    private String campusManagerName;   //校区负责人名称

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getDpManager() {
        return dpManager;
    }

    public void setDpManager(String dpManager) {
        this.dpManager = dpManager;
    }

    public String getDpManagerName() {
        return dpManagerName;
    }

    public void setDpManagerName(String dpManagerName) {
        this.dpManagerName = dpManagerName;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusManager() {
        return campusManager;
    }

    public void setCampusManager(String campusManager) {
        this.campusManager = campusManager;
    }

    public String getCampusManagerName() {
        return campusManagerName;
    }

    public void setCampusManagerName(String campusManagerName) {
        this.campusManagerName = campusManagerName;
    }
}
