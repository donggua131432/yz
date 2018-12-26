package com.yz.service.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.transfer.BdCheckRecordMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.transfer.BdCheckRecord;

@Service
@Transactional
public class BdCheckRecordService {
	private static final Logger log = LoggerFactory.getLogger(BdCheckRecordService.class);
	@Autowired
	private BdCheckRecordMapper checkRecordMapper;

	public void addBdCheckRecord(BdCheckRecord checkRecord) {
		checkRecord.setCrId(IDGenerator.generatorId());
		// TODO Auto-generated method stub
		checkRecordMapper.insertSelective(checkRecord);
	}

	public void updateBdCheckRecord(BdCheckRecord bcr) {
		// TODO Auto-generated method stub
		checkRecordMapper.updateBdCheckRecord(bcr);
	}
}
