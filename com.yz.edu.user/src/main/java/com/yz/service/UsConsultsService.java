package com.yz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.UsConsultsMapper;
import com.yz.model.UsConsults;

@Service
public class UsConsultsService {

	@Autowired
	private UsConsultsMapper consultsMapper;

	public List<Map<String, String>> myConsults(String userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize,false);
		List<Map<String, String>> list = consultsMapper.selectMyConsults(userId);
		return list;
	}

	@Transactional
	public void addConsults(UsConsults consults) {
		consultsMapper.insertSelective(consults);
	}

}
