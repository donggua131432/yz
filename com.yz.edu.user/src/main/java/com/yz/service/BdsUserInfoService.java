package com.yz.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.GlobalConstants;
import com.yz.constants.UsConstants;
import com.yz.dao.BdsApplyMapper;
import com.yz.dao.BdsUserMapper;
import com.yz.exception.BusinessException;
import com.yz.model.identity.BdRelation;
import com.yz.util.StringUtil;

@Service
public class BdsUserInfoService {

	@Autowired
	private BdsApplyMapper applyMapper;

	@Autowired
	private BdsUserMapper userMapper;
	
	@Autowired
	private AccountService accountService;

	public Map<String, String> getUserInfo(String userId) {
		Map<String, String> result = new HashMap<String, String>();

		BdRelation relation = applyMapper.selectRelationByUserId(userId);

		if (relation == null) {
			return null;
		}

		String iUserType = null;
		
		if(StringUtil.hasValue(relation.getEmpId())) {
			iUserType = UsConstants.I_USER_TYPE_EMPLOYEE;
		} else {
			iUserType = UsConstants.I_USER_TYPE_STUDENT;
		}

		result.put("iUserType", iUserType);

		if (UsConstants.I_USER_TYPE_EMPLOYEE.equals(iUserType)) {
			String empId = relation.getEmpId();
			Map<String, String> empInfo = userMapper.getEmpInfo(empId);

			result.putAll(empInfo);
		} else if (UsConstants.I_USER_TYPE_STUDENT.equals(iUserType)) {
			String stdId = relation.getStdId();
			result.put("stdId", stdId);
		} else {
			return null;
		}

		return result;
	}
	
	/**
	 * 根据手机号查询招生老师信息
	 * @param mobile
	 * @param idCard 
	 * @return
	 */
	public Map<String, String> getRecruit(String mobile, String idCard) {
		int count = userMapper.countByMobile(mobile);
		//如果找到多个学员
		if(count > 1) {
			if(StringUtil.hasValue(idCard)) {
				Map<String, String> recruit = userMapper.getCloseAndOnJobByIdCard(idCard);
				
				String stdId = userMapper.getStdIdByIdCard(idCard);
				if(StringUtil.isEmpty(stdId)) {
					throw new BusinessException("E60027");//身份证对应学员不存在
				}
				
				return recruit;
			} else {
				Map<String, String> result = new HashMap<String, String>();
				result.put("isMore", GlobalConstants.TRUE);
				return result;
			}
		} else if(count == 1){
			Map<String, String> recruit = userMapper.getCloseAndOnJob(mobile);
			return recruit;
		} else {
			return null;
		}
	}

	/**
	 * 建立学员与用户绑定关系
	 * @param recruit
	 * @param pUserId
	 * @param pIsMb
	 * @param userId
	 */
	public void createRelation2(String pUserId, String pIsMb, String userId, String stdId, String userType, String pType) {
		if(StringUtil.hasValue(stdId)) {
			accountService.copyZhimi(userId, stdId);
		}
	}

}
