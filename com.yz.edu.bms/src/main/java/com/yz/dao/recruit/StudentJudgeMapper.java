package com.yz.dao.recruit;

import java.util.List;

import com.yz.model.condition.recruit.JudgeQueryInfo;
import com.yz.model.recruit.StduentJudgeList;
import com.yz.model.recruit.StudentJudgeInfo;

public interface StudentJudgeMapper {
	/**
	 * 查询圆梦学员
	 * @param queryInfo
	 * @return
	 */
	List<StduentJudgeList> getStudentList(JudgeQueryInfo queryInfo);
	/**
	 * 执行审核
	 * @param judgeInfo
	 * @return
	 */
	int doJudge(StudentJudgeInfo judgeInfo);

}
