package com.yz.service.recruit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.recruit.StudentJudgeMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.JudgeQueryInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentEnroll;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.StduentJudgeList;
import com.yz.model.recruit.StudentJudgeInfo;

@Service
@Transactional
public class StudentJudgeService {
	
	@Autowired
	private StudentJudgeMapper judgeMapper;

	public IPageInfo<StduentJudgeList> getList(JudgeQueryInfo queryInfo) {
		
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<StduentJudgeList> jList = judgeMapper.getStudentList(queryInfo);
		if(jList == null)
			return null;
		return new IPageInfo<StduentJudgeList>((Page<StduentJudgeList>) jList);
	}

	public void check(StudentJudgeInfo judgeInfo) {
		judgeMapper.doJudge(judgeInfo);
	}
	
	@Autowired
	private StudentAllMapper allMapper;

	public StudentJudgeInfo getJudgeInfo(String learnId) {
		
		StudentJudgeInfo judgeInfo = new StudentJudgeInfo();
		judgeInfo.setLearnId(learnId);
		
		BdStudentBaseInfo baseInfo = allMapper.getStudentBaseInfoByLearnId(learnId);
		BdStudentHistory history = allMapper.getStudentHistory(learnId);
		BdStudentEnroll enroll = allMapper.getStudentEnroll(learnId);
		
		judgeInfo.setBaseInfo(baseInfo);
		judgeInfo.setHistory(history);
		judgeInfo.setEnroll(enroll);
		
		return judgeInfo;
	}
	
	

}
