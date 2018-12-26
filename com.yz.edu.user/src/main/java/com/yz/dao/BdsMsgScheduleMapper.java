package com.yz.dao;

import java.util.List;
import java.util.Map;

public interface BdsMsgScheduleMapper {

	List<Map<String, String>> selectExamPlansByDate(String date);

	List<Map<String, String>> selectStdExamInfo(String pyId);

}
