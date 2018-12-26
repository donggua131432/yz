package com.yz.model.educational;

import java.io.Serializable;

/**
 * 教务任务统计
 * @author lx
 * @date 2017年7月21日 下午2:18:21
 */
public class OaTaskStatisticsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 590930582104886068L;

	private String empName;
	private String totalCount;
	private String finishCount;
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(String finishCount) {
		this.finishCount = finishCount;
	}
}
