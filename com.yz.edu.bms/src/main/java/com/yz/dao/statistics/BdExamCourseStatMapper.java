package com.yz.dao.statistics;

import java.util.List;
import com.yz.model.statistics.BdExamCourseStatisticsInfo;
import com.yz.model.statistics.ExamCourseStatQuery;
import com.yz.model.statistics.PaperPrintStatInfo;
import com.yz.model.statistics.PaperPrintStatQuery;
/**
 * 考试课程统计
 * @author lx
 * @date 2017年11月16日 下午4:38:02
 */
public interface BdExamCourseStatMapper
{

	public List<BdExamCourseStatisticsInfo> queryExamCourseStat(ExamCourseStatQuery statQuery);
	
	
	public List<PaperPrintStatInfo> queryPaperPrintStat(PaperPrintStatQuery statQuery);
}
