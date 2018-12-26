package com.yz.service;


import com.yz.dao.StudentGraduatePaperMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.paper.StudentAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class StudentGraduatePaperService {

	private static final Logger log = LoggerFactory.getLogger(StudentGraduatePaperService.class);

	@Autowired
	private StudentGraduatePaperMapper graduatePaperMapper;

	@Transactional
	public void insertAttachment(StudentAttachment attachment){
		graduatePaperMapper.insertAttachment(attachment);
		graduatePaperMapper.updatePaperTitle(attachment.getLearnId(),attachment.getPaperTitle());
	}

	public Object selectStudentAttachment(String learnId) {
		List<StudentAttachment> list = graduatePaperMapper.selectUserStudentAttachment(learnId);
		if (list != null && !list.isEmpty()) {
			return new IPageInfo<StudentAttachment>(list, list.size());
		} else {
			list = new ArrayList<>();
			return new IPageInfo<StudentAttachment>(list, 0);
		}
	}

	public Map<String,String> getLearnInfo(String learnId){
		return graduatePaperMapper.getLearnInfo(learnId);
	}

	public Map<String,String> getPaperInfoByLearnId(String learnId) {
		return graduatePaperMapper.getPaperInfoByLearnId(learnId);
	}
}
