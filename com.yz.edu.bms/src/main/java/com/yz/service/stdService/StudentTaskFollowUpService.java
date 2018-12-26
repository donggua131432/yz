package com.yz.service.stdService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.stdService.StudentTaskFollowUpMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.stdService.StudentTaskFollowUp;
import com.yz.model.stdService.StudentTaskFollowUpQuery;

@Service
@Transactional
public class StudentTaskFollowUpService
{

	@Autowired
	private StudentTaskFollowUpMapper studentTaskFollowUpMapper;
	
	public IPageInfo<StudentTaskFollowUp> getStudentTaskFollowUp(int page,int pageSize,StudentTaskFollowUpQuery query){
		PageHelper.offsetPage(page, pageSize);
		List<StudentTaskFollowUp> taskList = studentTaskFollowUpMapper.getStudentTaskFollowUp(query);
		
		return new IPageInfo<>((Page<StudentTaskFollowUp>)taskList);
	}
	public void finishTask(String taskId,String learnId){
		studentTaskFollowUpMapper.finishTask(taskId,learnId);
	}
}
