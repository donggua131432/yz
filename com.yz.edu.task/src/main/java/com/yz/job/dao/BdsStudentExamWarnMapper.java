package com.yz.job.dao;

import java.util.List;
import java.util.Map;

public interface BdsStudentExamWarnMapper {

	/**
	 * 根据时间获取考试安排
	 * @param date
	 * @return
	 */
	List<Map<String, String>> selectExamPlansByDate(String date);

	/**
	 * 根据考场获取考试信息
	 * @param pyId
	 * @return
	 */
	List<Map<String, String>> selectStdExamInfo(String pyId);
	
}
