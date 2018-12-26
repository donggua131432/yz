package com.yz.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.constants.GlobalConstants;
import com.yz.dao.BdsLearnMapper;


@Service
@Transactional
public class BdsLearnInfoService {

	private static final Logger log = LoggerFactory.getLogger(BdsLearnInfoService.class);

	@Autowired
	private BdsLearnMapper learnMapper;
	
	public Object selectStudentInfo(String stdId) {
		Map<String, Object> rt = learnMapper.selectStudentInfoByStdId(stdId);
		List<Map<String, String>> learnInfos = (List<Map<String, String>>) rt.get("learnInfos");
			
		for (Map<String, String> map : learnInfos) {
			int paidCount = learnMapper.selectPaidCount((String) map.get("learnId"));
			String hasSerial = paidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;

			int unpaidCount = learnMapper.selectUnpaidCount((String) map.get("learnId"));
			String hasUnpaid = unpaidCount > 0 ? GlobalConstants.TRUE : GlobalConstants.FALSE;
			map.put("hasSerial", hasSerial);
			map.put("hasUnpaid", hasUnpaid);
		}
		rt.put("learnInfos", learnInfos);
		return rt;
	}

	
}