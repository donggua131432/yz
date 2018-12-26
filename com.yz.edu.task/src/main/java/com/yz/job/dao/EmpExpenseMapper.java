package com.yz.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface EmpExpenseMapper
{
	
	/**
	 * 当前年度没有生产报销的员工信息
	 * @param year
	 * @return
	 */
	public List<String> selectAllEmpNoContains(String year);

	/**
	 * 插入员工年度报销记录
	 * @param empIds
	 * @param year
	 */
	public void insertExpenses(@Param("empIds") List<String> empIds, @Param("year")String year);
	
}
