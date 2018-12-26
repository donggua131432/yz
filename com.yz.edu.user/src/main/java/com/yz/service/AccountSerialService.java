package com.yz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.AtsAccountSerialMapper;
import com.yz.model.AtsAccountSerial;

@Service
@Transactional
public class AccountSerialService {

	@Autowired
	private AtsAccountSerialMapper serialMapper;

	public int initSerial(AtsAccountSerial serial) {
		return serialMapper.initSerial(serial);
	}

	public int updateSerial(AtsAccountSerial serial) {
		return serialMapper.updateSerial(serial);
	}

	public Object getAccountSerials(int pageNum, int pageSize, String userId, String action) {
		PageHelper.startPage(pageNum, pageSize, false);
		List<Map<String, String>> serials = serialMapper.getAccountSerials(userId, action, "");
		return serials;
	}

	public Object getAccountSerials(int pageNum, int pageSize, String userId, String action, String accType) {
		PageHelper.startPage(pageNum, pageSize, false);
		List<Map<String, String>> serials = serialMapper.getAccountSerials(userId, action, accType);
		return serials;
	}

}
