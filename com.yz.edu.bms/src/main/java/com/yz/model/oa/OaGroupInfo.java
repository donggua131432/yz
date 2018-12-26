package com.yz.model.oa;

import java.io.Serializable;

/**
 * 部门招生组信息
 * @author lx
 * @date 2017年6月30日 下午5:02:15
 */
public class OaGroupInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1992441845179200020L;

	private String groupId;         //组id
	private String groupName;       //组名称
	private String dpId;            //部门id
	private String empId;           //负责人id
	private String isStop;          //是否停用
 
	private String createTime;      //添加时间
	private String createUser;      //添加人
	
	private String createUserId;    //添加人id
	private String updateUser;      //修改人
	private String updateUserId;    //修改人id
	
	private String dpName;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getIsStop() {
		return isStop;
	}
	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}
	public String getDpName()
	{
		return dpName;
	}
	public void setDpName(String dpName)
	{
		this.dpName = dpName;
	}
}
