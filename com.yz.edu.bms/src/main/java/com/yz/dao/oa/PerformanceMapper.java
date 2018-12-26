package com.yz.dao.oa;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.edu.paging.bean.Page;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaMonthExpense;
import com.yz.model.oa.OaMonthExpenseQuery;

public interface PerformanceMapper {

	/**
	 * 查询我的学员列表
	 * 
	 * @param queryInfo
	 * @return
	 */
	List<String> findRecruitStudents(@Param("months") List<String> months, @Param("user") BaseUser user,
			@Param("stdStages") String[] stdStages, @Param("grades") List<String> grades);

	String selectAudit(@Param("unvsId") String unvsId, @Param("grade") String grade, @Param("scholarship") String scholarship);

	List<Map<String, String>> selectStudentByMonth(@Param("months") String[] month, @Param("user") BaseUser user,
			@Param("grades") List<String> grades);

	List<OaMonthExpense> selectMonthExpenseByPage(OaMonthExpenseQuery query);

	List<String> selectGradeByYear(String year);

	List<String> selectRecruitCount(@Param("startTime") String startTime, @Param("endTime") String endTime,
			@Param("grades") List<String> grades, @Param("user") BaseUser user, @Param("stdStages") String[] stdStages);

	String selectRenderd(@Param("empId") String empId, @Param("year") String year);

	int insertExpense(OaMonthExpense ex);

	List<Map<String, String>> selectExpenseInfo(OaMonthExpense ex);

	OaMonthExpense selectExpense(OaMonthExpense ex);

	String selectReplayed(@Param("months") List<String> months, @Param("empId") String empId,
			@Param("year") String year);

	String selectReplayedByRecruit(@Param("month") String month, @Param("empId") String empId,
			@Param("year") String year);

	String selectReplyedAmount(OaMonthExpense ex);

	List<Map<String, String>> getRecruitList(@Param("sName") String sName);

	String selectMonths(@Param("year") String year, @Param("month") String month);

	List<OaEmployeeBaseInfo> selectUnderEmpId(SelectQueryInfo queryInfo);

}