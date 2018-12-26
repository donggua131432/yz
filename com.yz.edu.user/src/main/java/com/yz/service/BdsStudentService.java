package com.yz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.core.constants.UsRelationConstants;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.BdsStudentMapper;
import com.yz.dao.StudentAllMapper;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.exception.BusinessException;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.SendSmsVo;
import com.yz.model.SessionInfo;
import com.yz.model.StudentHistory;
import com.yz.model.UsBaseInfo;
import com.yz.model.student.BdCheckRecord;
import com.yz.model.student.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
public class BdsStudentService {

	private static final Logger log = LoggerFactory.getLogger(BdsStudentService.class);

	@Autowired
	private StudentAllMapper studentMapper;

	@Autowired
	private UsCertService certService;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Autowired
	private BdsStudentMapper bdsStudentMapper;
	
	@Autowired
	private UsBaseInfoMapper usBaseInfoMapper;
	
	public void sendCoupon(String userId) {
		List<String> couponIds = studentMapper.selectAvailableRegistCouponIds();
		if (null != couponIds && couponIds.size() > 0) {
			studentMapper.insertStdCoupon(userId, couponIds);
		}
	}

	public Object getAuthCode(String idCard, String userId) {
		int valicodeLength = 6;
		BdStudentBaseInfo baseInfo = studentMapper.getStudentInfoByIdCard(idCard);
		log.info("-------------------------- 根据身份证号[" + idCard + "] -- userId[" + userId + "]");

		if (baseInfo == null) {
			throw new BusinessException("E60007");// 身份证填写错误，或者学员不存在
		}

		String mobile = baseInfo.getMobile();

		if (StringUtil.isEmpty(mobile)) {
			throw new BusinessException("E60006");/// 学员手机号为空，无法进行身份验证
		}

		if (StringUtil.hasValue(baseInfo.getUserId())) {
			log.info("身份证号为{}的学员已和用户{}建立了关联关系",idCard,baseInfo.getUserId());
			throw new BusinessException("E60004");// 该学员身份已被关联，无法重复关联
		}

		//SimpleCacheUtil simple = RemoteCacheUtilFactory.simple().setLiveTime(300);// 5分钟有效时间
		
		String valicode = StringUtil.randomNumber(valicodeLength);
		Map<String, String> content = new HashMap<String, String>();
		content.put("code", valicode);
		//boolean isSuccess = smsApi.sendMessage(content, GlobalConstants.TEMPLATE_VALI_CODE, mobile);

		SendSmsVo vo = new SendSmsVo();
		vo.setContent(content);
		vo.setMobiles(Collections.singletonList(mobile));
		vo.setTemplateId(GlobalConstants.TEMPLATE_VALI_CODE);
	    RedisService.getRedisService().set(idCard, valicode); 
	    RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
		return null;
	}

	

	public Object authStudent(String idCard, String authCode, String userId) {
		String smsIsOn = yzSysConfig.getSmsSwitch();

		//SimpleCacheUtil simple = RemoteCacheUtilFactory.simple().setLiveTime(300);// 5分钟有效时间

		//Object saveAuth = simple.get(idCard);
		//simple.del(idCard);
		Object saveAuth = RedisService.getRedisService().get(idCard);
		RedisService.getRedisService().del(idCard);
		

		if (GlobalConstants.FALSE.equals(smsIsOn)) {
			saveAuth = "888888";
		}

		if (saveAuth == null) {
			throw new BusinessException("E60008");// 验证码超时，请重新获取
		}

		if (!saveAuth.equals(authCode)) {
			throw new BusinessException("E60009");// 验证码不正确
		}

		BdStudentBaseInfo baseInfo = studentMapper.getStudentInfoByIdCard(idCard);

		if (baseInfo == null) {
			throw new BusinessException("E60019");// 学员信息不存在
		}

		//MapCacheUtil map = RemoteCacheUtilFactory.map();

		//SessionInfo sessionInfo = map.getAll(userId, SessionInfo.class);
		
		SessionInfo sessionInfo = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);

		UsBaseInfo usBaseInfo = new UsBaseInfo();
		usBaseInfo.setUserId(userId);
		usBaseInfo.setStdId(baseInfo.getStdId());
		
		String existUserId = bdsStudentMapper.getUserIdByStdId(baseInfo.getStdId());
		
		if (StringUtil.hasValue(existUserId)) {
			log.info("学员编号{}已和用户{}存在了关联关系",baseInfo.getStdId(),existUserId);
			throw new BusinessException("E60011");
		}
		
		//更新绑定关系
		bdsStudentMapper.updateUserIdByStdId(userId,baseInfo.getStdId());
		
		usBaseInfoMapper.updateByPrimaryKeySelective(usBaseInfo);
		
		
		Map<String, String> recruitMap = studentMapper.getRecruitMap(baseInfo.getStdId());
		String stdId = baseInfo.getStdId();
		Assert.hasText(stdId, "学员ID不能为空");
		Assert.hasText(idCard, "身份证不能为空");
		Assert.hasText(userId, "用户ID不能为空");
		certService.createStdRelation(stdId, idCard, userId, recruitMap);
		 
		//map.put(userId, "stdId", baseInfo.getStdId());
		//RedisService.getRedisService().setByte(userId, FstSerializer.getInstance().serialize(sessionInfo),7200);
		sessionInfo.setStdId(stdId);
		sessionInfo.setRelation(UsRelationConstants.N_USER_TYPE_STUDENT);
		AppSessionHolder.setSessionInfo(userId, sessionInfo, AppSessionHolder.RPC_SESSION_OPERATOR);
		return null;
	}

	@Autowired
	private BdsStudentMapper stdMapper;

	public List<Map<String, String>> getEnrollInfos(String stdId) {
		return stdMapper.getEnrollList(stdId);
	}

	public Map<String, String> getHistoryInfo(String learnId) {
		return stdMapper.getHistoryInfo(learnId);
	}

	public Object completeEnroll(String learnId, String stdId, String sfzFront, String sfzBack, String education) {
		String newSfzFront = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, sfzFront);
		String newSfzBack = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, sfzBack);
		String newEducation = FileSrcUtil.createFileSrc(Type.STUDENT, stdId, education);

		FileUploadUtil.copyToDisplay(sfzFront, newSfzFront);
		FileUploadUtil.copyToDisplay(sfzBack, newSfzBack);
		FileUploadUtil.copyToDisplay(education, newEducation);

		stdMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_IDCARD_FRONT, newSfzFront);
		stdMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_IDCARD_BEHIND, newSfzBack);
		stdMapper.updateAnnex(stdId, StudentConstants.ANNEX_TYPE_EDUCATION, newEducation);

		stdMapper.updateAnnexStatus(stdId);

		stdMapper.testCompleted(learnId);

		return null;
	}

	public void updateHistory(StudentHistory history) {
		stdMapper.updateHistory(history);
	}

	public Map<String, String> getStudentByMobile(String mobile) {
		return stdMapper.getStudentByMobile(mobile);
	}

	public Map<String, String> getStudentModify(String learnId) {
		Map<String, String> map = stdMapper.getStudentModify(learnId);
		if (map == null) {
			return stdMapper.getStudentInfo(learnId);
		}
		return map;
	}

	public void addStudentModify(BdStudentModify studentModify) {
		// 圆梦计划入围
		if (stdMapper.countYMStudent(studentModify.getLearnId()) > 0) {
			throw new BusinessException("E60037");
		}
		stdMapper.addStudentModify(studentModify);
		List<Map<String, String>> checkWeights = null;
		checkWeights = stdMapper.getCheckWeight(TransferConstants.CHECK_TYPE_SCHOOLROLL_MODIFY_NEW);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			stdMapper.addBdCheckRecord(checkRecord);
		}
	}

	public void sendLotteryCoupon(String userId, String couponId) {
		List<String> couponIds = new ArrayList<String>();
		couponIds.add(couponId);
		studentMapper.insertStdCoupon(userId, couponIds);
	}

	public boolean checkLotteryCondition(String mobile) {

		List<Map<String, String>> learnInfos = studentMapper.selectLearnIdsByMobile(mobile);

		if (learnInfos.isEmpty()) {
			return false;
		}

		for (Map<String, String> map : learnInfos) {
			
			String recruitType = map.get("recruitType");
			String learnId = map.get("learnId");
			String itemCode = null;
			
			if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
				itemCode = "Y0";
			} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				itemCode = "Y1";
			}
			
			int count = studentMapper.selectTutionPaidCount(learnId, itemCode);
			
			if (count > 0) {
				return true;
			}
			
		}

		return false;
	}
}
