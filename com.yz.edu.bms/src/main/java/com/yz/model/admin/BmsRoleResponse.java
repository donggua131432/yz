package com.yz.model.admin;

/**
 * 页面返回角色实体 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月11日.
 *
 */
public class BmsRoleResponse extends BmsRole {

	/* 角色对应资源ID数组 */
	private String[] funcArray;

	public String[] getFuncArray() {
		return funcArray;
	}

	public void setFuncArray(String[] funcArray) {
		this.funcArray = funcArray;
	}

}
