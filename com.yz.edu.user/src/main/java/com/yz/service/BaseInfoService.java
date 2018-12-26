package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.BaseInfoMapper;
import com.yz.model.communi.Body;

@Service
public class BaseInfoService {

	@Autowired
	private BaseInfoMapper baseInfoMapper;
	
	public Object getEnrollInfo(Body queryInfo) {
		
		String type = queryInfo.getString("type");
		
		int pageSize = queryInfo.getInt("pageSize");
		int pageNum = queryInfo.getInt("pageNum");
		
		PageHelper.startPage(pageNum, pageSize);
		
		switch(type) {
		case "U" : 
			return baseInfoMapper.selectUnvs(queryInfo);
		case "P" : 
			return baseInfoMapper.selectProfession(queryInfo);
		case "T" :
			return baseInfoMapper.selectTestArea(queryInfo);
		}
		return null;
	}

}
