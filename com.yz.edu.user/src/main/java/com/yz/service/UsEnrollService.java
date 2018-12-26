package com.yz.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.constants.StudentConstants;
import com.yz.constants.UsConstants;
import com.yz.core.constants.UsRelationConstants;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsEnrollLogMapper;
import com.yz.dao.UsFollowMapper;
import com.yz.model.SessionInfo;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsEnrollLog;
import com.yz.model.UsFollow; 
import com.yz.model.communi.Body;
import com.yz.redis.RedisService;
import com.yz.serializ.FstSerializer;
import com.yz.session.AppSessionHolder;
import com.yz.util.StringUtil;

@Service
public class UsEnrollService {

	private static final Logger log = LoggerFactory.getLogger(UsEnrollService.class);

	@Autowired
	private UsEnrollLogMapper logMapper;

	@Autowired
	private BdsLearnInfoService learnService;
	
	@Autowired
	private UsBaseInfoMapper baseMapper;
	
	@Autowired
	private UsCertService certService;
	
	@Autowired
	private UsFollowMapper followMapper;

	@Transactional
	public String enroll(Body body) {
		//设置录入渠道
		body.put("enrollChannel", StudentConstants.ENROLL_CHANNEL_INVITE);

		String userId = body.getString("userId");
		String idCard = body.getString("idCard");
		String name = body.getString("name");

		String scholarship = body.getString("scholarship");
		String sg = body.getString("sg");
		String unvsId = body.getString("unvsId");
		String unvsName = body.getString("unvsName");

		String grade = body.getString("grade");
		String pfsnId = body.getString("pfsnId");
		String pfsnName = body.getString("pfsnName");
		String pfsnLevel = body.getString("pfsnLevel");
		String taId = body.getString("taId");
		String taName = body.getString("taName");
 

        SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);

		body.put("mobile", session.getMobile());

		UsEnrollLog eLog = new UsEnrollLog();
		eLog.setUserId(userId);
		eLog.setIdCard(idCard);
		eLog.setName(name);

		eLog.setScholarship(scholarship);
		eLog.setUnvsId(unvsId);
		eLog.setUnvsName(unvsName);

		eLog.setGrade(grade);
		eLog.setPfsnId(pfsnId);
		eLog.setPfsnName(pfsnName);
		eLog.setPfsnLevel(pfsnLevel);
		eLog.setTaId(taId);
		eLog.setTaName(taName);
		eLog.setEnrollTime(new Date());
		eLog.setEnrollChannel(StudentConstants.ENROLL_CHANNEL_INVITE);
		eLog.setSg(sg);

		logMapper.insertSelective(eLog);
		
		String stdId = body.getString("stdId");
		
		Map<String, String> result = null;
		
		UsFollow follow = followMapper.selectByPrimaryKey(userId);
		if(follow != null) {
			body.put("recruitId", follow.getEmpId());
		}
		
		if(StringUtil.isEmpty(stdId)) {
			certService.bindCert(userId, name, idCard, UsConstants.CERT_TYPE_SFZ, true);
			log.debug("------------------ 学员[" + stdId + "] 开始初始报名");
			result = learnService.addLearnInfo(body);
			if(result != null)
				stdId = result.get("stdId"); 
			
			UsBaseInfo info = new UsBaseInfo();
			info.setUserId(userId);
			info.setStdId(stdId);
			baseMapper.updateByPrimaryKeySelective(info);
			
			session.setStdId(stdId);
			//session.getiUserTypes().add(UsConstants.I_USER_TYPE_STUDENT);
			session.setRelation(UsRelationConstants.N_USER_TYPE_STUDENT);
			 
			//RedisService.getRedisService().setByte(userId, FstSerializer.getInstance().serialize(session),7200);
			AppSessionHolder.setSessionInfo(userId, session, AppSessionHolder.RPC_SESSION_OPERATOR);
		} else {
			log.debug("------------------ 学员[" + stdId + "] 开始报名其他专业");
			result = learnService.addLearnInfo(body);
		}
		
		return result == null ? null : result.get("learnId");
	}

}
