package com.yz.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.GsLotteryApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.UsConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.constants.AppConstants;
import com.yz.core.util.UsRelationUtil;
import com.yz.dao.BdsStudentMapper;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsCertificateMapper;
import com.yz.dao.UsEnrollLogMapper;
import com.yz.dao.UsFollowLogMapper;
import com.yz.dao.UsFollowMapper;
import com.yz.dao.UsLoginLogMapper;
import com.yz.dao.UsSignMapper;
import com.yz.dao.UsWechatFansMapper;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.AtsAccountSerial;
import com.yz.model.AtsAwardRecord;
import com.yz.model.AtsAwardRule;
import com.yz.model.CommunicationMap;
import com.yz.model.SendSmsVo;
import com.yz.model.SessionInfo;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsCertificate;
import com.yz.model.UsEnrollLog;
import com.yz.model.UsFollow;
import com.yz.model.UsFollowLog;
import com.yz.model.UsLoginLog;
import com.yz.model.UsSign;
import com.yz.model.UsWechatFans;
import com.yz.model.UserRegisterEvent;
import com.yz.model.WechatMsgVo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.Assert;
import com.yz.util.CodeUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import com.yz.util.TokenUtil;
import com.yz.util.TokenUtil.Secure;

@Service
@Transactional
public class UsLoginService {

	@Autowired
	private UsSignMapper signMapper;

	@Autowired
	private AccountService accountService;
	
	@Autowired
    private AwardRuleService awardRuleService;

	@Autowired
	private GwWechatService wechatService;

	@Autowired
	private BdsStudentService studentService;

	@Reference(version = "1.0")
	private GsLotteryApi lotteryApi;

	@Autowired
	private UsBaseInfoMapper baseInfoMapper;

	@Autowired
	private UsWechatFansMapper fansMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Autowired
	private UsFollowMapper followMapper;

	@Autowired
	private UsFollowLogMapper followLogMapper;

	@Autowired
	private UsEnrollLogMapper enrollLogMapper;

	@Autowired
	private UsCertificateMapper certMapper;
	
	@Autowired
	private BdsStudentMapper bdsStudentMapper;
	
	@Autowired
	private BdsUserInfoService userInfoService;

	private static final Logger log = LoggerFactory.getLogger(UsLoginService.class);

	/**
	 * 签到
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Object sign(String userId) {
		int count = signMapper.countTodayBy(userId);

		if (count > 0) {
			throw new BusinessException("E30002");// 当天已签到
		}

		UsSign sign = new UsSign();
		sign.setUserId(userId);
		signMapper.insertSelective(sign);

		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		body.put("ruleCode", "sign");
		body.put("userId", userId);
		body.put("excDesc", "用户签到");
		body.put("triggerUserId", sign.getSignId());
		body.put("mappingId", sign.getSignId());

		//Map<String, String> amountInfo = accountApi.award(body);
		
		String ruleCode = body.getString("ruleCode");
		userId = body.getString("userId");
		String stdId = body.getString("stdId");
		String empId = body.getString("empId");
		String triggerUserId = body.getString("triggerUserId");
		String excDesc = body.getString("excDesc");
		String mappingId = body.getString("mappingId");
		String ruleType = body.getString("ruleType", FinanceConstants.AWARD_RT_NORMAL);
		AtsAwardRule ruleInfo = awardRuleService.getRule(ruleCode);

		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(excDesc, "动账描述不能为空");
		Assert.hasText(ruleCode, "奖励规则编码不能为空");

		Map<String, String> amountInfo = new HashMap<String, String>();
		if (GlobalConstants.TRUE.equals(ruleInfo.getIsAllow())) {
			String zhimiCount = ruleInfo.getZhimiCount();
			String expCount = ruleInfo.getExpCount();

			AtsAwardRecord record = new AtsAwardRecord();
			record.setZhimiCount(zhimiCount);
			record.setExpCount(expCount);
			record.setRuleCode(ruleCode);
			record.setMappingId(mappingId);
			record.setAwardDesc(excDesc);
			record.setUserId(userId);
			record.setStdId(stdId);
			record.setEmpId(empId);
			record.setTriggerUserId(triggerUserId);
			record.setRuleType(ruleType);

			AtsAccountSerial serial = accountService.award(record);
			if (serial != null) {
				amountInfo.put("amount", serial.getAmount());
				amountInfo.put("afterAmount", serial.getAfterAmount());
			}
		}

		SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);

		int total = Integer.valueOf(yzSysConfig.getSignNumber());

		total += signMapper.countToday();

		String amount = amountInfo.get("amount");

		String afterAmount = amountInfo.get("afterAmount");
		
		//推送微信公众号消息
		sendSignInMsg(amount,afterAmount,total,session);
		
		return amount;
	}



	/**
	 * 判断时候有抽奖活动，并赠送抽奖机会
	 */
	public void sendLotterTicket(String mobile) {

		try {
			// 判断是否有抽奖活动进行
			String lotteryCode = yzSysConfig.getLotteryCode();
			// 无抽奖活动，结束
			if (!StringUtil.hasValue(lotteryCode)) {
				return;
			}

			// bds接口判断是否符合抽奖活动送票条件
			//boolean flag = studentApi.checkLotteryCondition(mobile);
			boolean flag = studentService.checkLotteryCondition(mobile);
			if (flag) {
				UsBaseInfo usInfo = baseInfoMapper.selectByMobile(mobile);
				// 调用增加抽奖券接口
				lotteryApi.gainLoterryTicket(usInfo.getUserId(), usInfo.getpId(), usInfo.getRealName());
			}
		} catch (IRpcException e) {
			log.error("--------------------------------- 注册获取抽奖机会报错：" + e.getMessage());
		}

	}

	private void sendMessage(UsFollow follow, UsBaseInfo pBaseInfo, String stdName, String mobile) {
		try {
			log.debug("----------------------------  跟进信息发送：" + JsonUtil.object2String(follow));

			log.debug("----------------------------  跟进人信息：" + JsonUtil.object2String(pBaseInfo));

			String recruitOpenId = "";
			String recruitName = "";

			String pOpenId = "";
			String pRealName = "";
			String pMobile = "";

			if (null != follow) {
				// 发送公众号消息
				recruitOpenId = baseInfoMapper.getOpenIdByEmpId(follow.getEmpId());

				log.debug("------------------------------- 发送微信消息，招生老师openId：" + recruitOpenId);

				UsBaseInfo recruitInfo = baseInfoMapper.selectByBind(recruitOpenId, UsConstants.BIND_TYPE_WECHAT);

				if (null != recruitInfo) {
					recruitName = recruitInfo.getRealName();
				}

			}

			if (null != pBaseInfo) {
				pOpenId = baseInfoMapper.getOpenIdByUserId(pBaseInfo.getUserId());
				if (StringUtil.hasValue(pOpenId) && !pOpenId.equals(recruitOpenId)) {
					pRealName = pBaseInfo.getRealName() == null ? "" : pBaseInfo.getRealName();
					pMobile = pBaseInfo.getMobile() == null ? "" : pBaseInfo.getMobile();
					log.debug("------------------------------- 发送微信消息，跟进人openId：" + pOpenId + "手机号:" + pMobile);
				} else {
					pOpenId = null;
				}
			}
			//推送微信公众号信息
			sendStudentRegist(recruitOpenId, recruitName, stdName, mobile, pOpenId, pRealName, pMobile, null);

		} catch (Exception e) {
			log.error("------------------------------- 用户注册微信推送失败,errorMsg:" + e.getMessage());
		}
	}

	/**
	 * 微信授权登录
	 * 
	 * @param openId
	 * @param ip
	 * @param mac
	 * @param coordinate
	 *            坐标
	 * @return
	 */
	private Map<String, Object> loginByWechat(String openId, String ip, String mac, String coordinate) {
		UsBaseInfo baseInfo = baseInfoMapper.selectByBind(openId, UsConstants.BIND_TYPE_WECHAT);
		baseInfo.setWechatOpenId(openId);

		return loginByBaseInfoWithLog(baseInfo, UsConstants.LOGIN_TYPE_WECHAT, ip, mac, coordinate);
	}

	/**
	 * 登录参数组装
	 * 
	 * @param baseInfo
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Map<String, Object> loginByBaseInfo(UsBaseInfo baseInfo) {
		String userId = baseInfo.getUserId();
		String token = StringUtil.UUID();
		String authToken = TokenUtil.createToken(userId, token);
  

		log.debug("---------------------------- 解码前昵称 : " + baseInfo.getNickname());

		String nickName = CodeUtil.base64Decode2String(baseInfo.getNickname());

		log.debug("---------------------------- 解码后昵称 : " + nickName);

		SessionInfo session = new SessionInfo();

		session.setUserId(userId);
		session.setNickName(nickName);
		session.setHeadImg(baseInfo.getHeadImg());
		session.setRealName(baseInfo.getRealName());
		session.setStdId(baseInfo.getStdId());
		session.setEmpId(baseInfo.getEmpId());
		session.setiUserTypes(UsRelationUtil.makeUserTypes(baseInfo.getRelation()));
		session.setJwtToken(token);
		session.setMobile(baseInfo.getMobile());
		session.setPid(baseInfo.getpId());
		session.setpType(baseInfo.getpType());
		session.setpIsMb(baseInfo.getpIsMb());
		session.setSex(baseInfo.getSex());
		session.setOpenId(baseInfo.getWechatOpenId());
		session.setUserType(baseInfo.getUserType());
		session.setRelation(baseInfo.getRelation());

		//MapCacheUtil map = RemoteCacheUtilFactory.map(userId).setLiveTime(7200);
		//map.putObject(session);
		
		//RedisService.getRedisService().setByte(userId, FstSerializer.getInstance().serialize(session), 7200);

		AppSessionHolder.setSessionInfo(userId, session, AppSessionHolder.RPC_SESSION_OPERATOR);
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("auth_token", authToken);
		result.put("iUserTypes", session.getiUserTypes());

		Map<String, String> userMap = new HashMap<String, String>();

		userMap.put("nickname", nickName);
		userMap.put("realName", baseInfo.getRealName());
		userMap.put("mobile", baseInfo.getMobile());
		userMap.put("sex", baseInfo.getSex());
		userMap.put("headImg", baseInfo.getHeadImg());
		userMap.put("province", baseInfo.getProvince());
		userMap.put("city", baseInfo.getCity());
		userMap.put("district", baseInfo.getDistrict());
		userMap.put("yzCode", baseInfo.getYzCode());

		result.put("userInfo", userMap);
		result.put("needBind", GlobalConstants.FALSE);
		result.put("relation", baseInfo.getRelation());
		return result;
	}

	/**
	 * 微信用户授权 生成平台放行票据
	 * 
	 * @param code
	 * @param scope
	 * @param ip
	 * @param mac
	 * @param coordinate
	 * @return
	 */
	@Transactional
	public Object auth(String code, String scope, String ip, String mac, String coordinate) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (AppConstants.SCOPE_BASE.equals(scope)) {

			String openId = wechatService.getOpenId(code);

			int count = fansMapper.countBy(openId);

			if (count > 0) {
				int bindCount = baseInfoMapper.countByBind(openId, UsConstants.BIND_TYPE_WECHAT);

				if (bindCount > 0) {
					result = loginByWechat(openId, ip, mac, coordinate);
					int u_count = baseInfoMapper.countByOpenId(openId);
					if (u_count > 0) {
						result.put("needBind", GlobalConstants.FALSE);
					} else {
						result.put("needBind", GlobalConstants.TRUE);
					}

				} else {
					result.put("needBind", GlobalConstants.TRUE);
					result.put("token", TokenUtil.createToken(openId));// 绑定手机号票据
				}

			} else {
				throw new BusinessException("E30003");// 尚未授权
			}
		} else if (AppConstants.SCOPE_USER_INFO.equals(scope)) {
			CommunicationMap userInfo = wechatService.getUserInfo(code);

			if (userInfo == null) {
				throw new BusinessException("E30006");// 用户授权失败
			}

			log.debug("----------------------------- 编码前昵称：" + userInfo.getString("nickname"));
			// 昵称编码
			String nickName = CodeUtil.base64Encode2String(userInfo.getString("nickname"));

			log.debug("----------------------------- 解码后昵称：" + nickName);

			// 用户基本信息
			UsWechatFans fans = new UsWechatFans();
			fans.setNickname(nickName);
			fans.setHeadImgUrl(userInfo.getString("headimgurl"));
			fans.setOpenId(userInfo.getString("openid"));
			fans.setProvince(StringUtil.removeFourChar(userInfo.getString("province")));
			fans.setCountry(userInfo.getString("country"));
			fans.setCity(userInfo.getString("city"));
			fans.setUnionId(userInfo.getString("unionid"));
			fans.setIsSubscribe(
					userInfo.getString("subscribe") == null ? GlobalConstants.FALSE : userInfo.getString("subscribe"));
			long subscribeTime = userInfo.getLong("subscribe_time");
			fans.setSubscribeTime(subscribeTime == 0 ? null : new Date(subscribeTime));

			int count = fansMapper.countBy(userInfo.getString("openid"));
			if (count > 0) {
				fansMapper.updateByPrimaryKeySelective(fans);
				int u_count = baseInfoMapper.countByOpenId(fans.getOpenId());

				if (u_count > 0) {
					result.put("needBind", GlobalConstants.FALSE);
				} else {
					result.put("needBind", GlobalConstants.TRUE);
				}
			} else {
				fansMapper.insertSelective(fans);
				result.put("needBind", GlobalConstants.TRUE);
			}

			result.put("token", TokenUtil.createToken(fans.getOpenId()));// 绑定手机号票据
		}
		return result;
	} 

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param authCode
	 * @return
	 */
	public Object valicode(String mobile, String authCode) {

		int valicodeLength = 6;// 短信验证码长度

		//SimpleCacheUtil simple = RemoteCacheUtilFactory.simple().setLiveTime(300);// 5分钟有效时间
		/*
		 * Object saveAuthCode = simple.get(mobile);
		 * 
		 * if (saveAuthCode == null) { throw new BusinessException("E30008");//
		 * 图形验证码失效 }
		 * 
		 * if (!authCode.toLowerCase().equals(saveAuthCode)) { throw new
		 * BusinessException("E30007");// 图形验证码错误 }
		 */

		String valicode = StringUtil.randomNumber(valicodeLength);

		Map<String, String> content = new HashMap<String, String>();
		content.put("code", valicode);
		log.debug("手机号-------"+mobile +"注册获取的验证码为:"+ valicode);
		String smsIsOn = yzSysConfig.getSmsSwitch();

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			/*boolean isSuccess = smsApi.sendMessage(content, GlobalConstants.TEMPLATE_VALI_CODE, mobile);
			if (isSuccess) {
				//simple.put(mobile, valicode);
				RedisService.getRedisService().setex(mobile, valicode, 300);
			} else {
				throw new BusinessException("E30013");// 短信验证码发送失败
			}*/
			SendSmsVo vo = new SendSmsVo();
			vo.setContent(content);
			vo.setMobiles(mobile);
			vo.setTemplateId(GlobalConstants.TEMPLATE_VALI_CODE);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
			RedisService.getRedisService().setex(mobile, valicode, 300);
		} else {
			//simple.put(mobile, "888888");
			RedisService.getRedisService().setex(mobile, "888888", 300);
		}

		return null;
	}


	/**
	 * 手机号登录
	 * 
	 * @param mobile
	 * @param valicode
	 * @param ip
	 * @param mac
	 * @param coordinate
	 * @return
	 */
	public Object login(String mobile, String valicode, String ip, String mac, String coordinate) {

		String smsIsOn = yzSysConfig.getSmsSwitch();

		Object saveValicode = null;

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			saveValicode =RedisService.getRedisService().get(mobile);
		} else {
			saveValicode = "888888";
		}

		if (saveValicode == null) {
			throw new BusinessException("E30009");// 短信验证码失效
		}

		if (!saveValicode.equals(valicode)) {
			throw new BusinessException("E30010");// 短信验证码错误
		}

		int count = baseInfoMapper.countByMobile(mobile);

		if (count > 0) {
			UsBaseInfo baseInfo = baseInfoMapper.selectByMobile(mobile);

			return loginByBaseInfoWithLog(baseInfo, UsConstants.LOGIN_TYPE_MBILE, ip, mac, coordinate);

		} else {
			throw new BusinessException("E30015");// 手机号尚未注册
		}
	}

	public boolean isSign(String userId) {
		int count = signMapper.countTodayBy(userId);
		return count > 0;
	}

	@Autowired
	private UsLoginLogMapper logMapper;

	/**
	 * 登录记录日志
	 * 
	 * @param baseInfo
	 * @param loginType
	 *            登录方式
	 * @param ip
	 * @param mac
	 * @param coordinate
	 *            坐标
	 * @return
	 */
	private Map<String, Object> loginByBaseInfoWithLog(UsBaseInfo baseInfo, String loginType, String ip, String mac,
			String coordinate) {
		final UsLoginLog usLog = new UsLoginLog();

		usLog.setUserId(baseInfo.getUserId());
		usLog.setIsSuccess(GlobalConstants.TRUE);
		usLog.setLoginType(loginType);
		usLog.setIp(ip);
		usLog.setMac(mac);
		usLog.setCoordinate(coordinate);
		usLog.setLoginTime(new Date());
		try {
			return loginByBaseInfo(baseInfo);
		} catch (Exception e) {
			usLog.setIsSuccess(GlobalConstants.FALSE);
			usLog.setReason(e.getMessage());
			throw e;
		} finally {
			logMapper.insertSelective(usLog);
		}
	}

	


	/**
	 * 建立跟进关系
	 * 
	 * @param pBaseInfo
	 * @param baseInfo
	 * @param sIdCard
	 */
	private UsFollow createFlow(UsBaseInfo pBaseInfo, UsBaseInfo baseInfo, String sIdCard) {

		String userId = baseInfo.getUserId();
		String mobile = baseInfo.getMobile();
		String userType = baseInfo.getUserType();

		String pUserId = baseInfo.getpId();
		String pIsMb = baseInfo.getpIsMb();
		String pType = baseInfo.getpType();

		// 根据手机号码查询 学员所属招生老师
		Map<String, String> recruit = userInfoService.getRecruit(mobile, sIdCard);

		if (recruit == null || recruit.isEmpty()) {
			// 如果没有报读记录没有招生老师 则走正常流程建立跟进关系
			if (pBaseInfo != null) {
				UsFollow follow = queryFollow(userId, pBaseInfo.getUserId());
				checkFollow(follow);
				return follow;
			}
		} else {
			String isMore = recruit.get("isMore");

			if (GlobalConstants.TRUE.equals(isMore)) {
				throw new BusinessException("E30017");// 特殊错误码 指引学员绑定身份证
			} else {

				String empId = recruit.get("empId");
				String dpId = recruit.get("dpId");
				String campusId = recruit.get("campusId");

				boolean noRecruit = StringUtil.isEmpty(empId) && StringUtil.isEmpty(dpId)
						&& StringUtil.isEmpty(campusId);
				UsFollow follow = null;
				if (noRecruit) {
					if (pBaseInfo != null)
						follow = queryFollow(userId, pBaseInfo.getUserId());
				} else {
					follow = new UsFollow();
					follow.setUserId(userId);
					follow.setCampusId(campusId);
					follow.setEmpId(empId);
					follow.setCampusManager(recruit.get("campusManager"));
					follow.setDpId(dpId);
					follow.setDpManager(recruit.get("dpManager"));
				}

				checkFollow(follow);
				recruit.put("userId", userId);
				addLog(recruit);

				String idCard = recruit.get("idCard");
				String name = recruit.get("name");

				UsCertificate cert = certMapper.getCertBy(userId, UsConstants.CERT_TYPE_SFZ);

				if (cert == null) {
					cert = new UsCertificate();
					cert.setUserId(userId);
					cert.setCertNo(idCard);
					cert.setCertType(UsConstants.CERT_TYPE_SFZ);
					cert.setName(name);
					certMapper.insertSelective(cert);
				} else {
					cert.setCertNo(idCard);
					cert.setName(name);
					certMapper.updateByPrimaryKeySelective(cert);
				}
				String stdId = recruit.get("stdId");
				
				
				baseInfo.setRealName(name);
                baseInfo.setStdId(stdId);
                baseInfo.setpId(pUserId);
                baseInfo.setpIsMb(pIsMb);
                baseInfo.setUserType(userType);
                baseInfo.setpType(pType);
				
                log.info("学员["+stdId+"]对应的用户["+userId+"]的邀约人信息pid:["+pUserId+"]pType["+pType+"]pIsMb["+pIsMb+"]");
				baseInfoMapper.updateByPrimaryKeySelective(baseInfo);
				
				bdsStudentMapper.updateUserIdByStdId(userId, stdId);
				
				//userInfoApi.createRelation(pUserId, pIsMb, userId, stdId, userType, pType);
				
				accountService.copyZhimi(userId, stdId);

				return follow;
			}
		}

		return null;
	}

	private UsFollow queryFollow(String userId, String pid) {
		UsFollow follow = null;
		// 获取上线的身份
		Map<String, String> userInfo = userInfoService.getUserInfo(pid);
		// 判断上线身份是否为员工
		if (userInfo != null && UsConstants.I_USER_TYPE_EMPLOYEE.equals(userInfo.get("iUserType"))) {
			follow = new UsFollow();
			follow.setEmpId(userInfo.get("empId"));
			follow.setDpId(userInfo.get("dpId"));
			follow.setDpManager(userInfo.get("dpManager"));
			follow.setCampusId(userInfo.get("campusId"));
			follow.setCampusManager(userInfo.get("campusManager"));
			follow.setUserId(userId);
		} else {
			follow = followMapper.selectByPrimaryKey(pid);
			if (follow != null) {
				follow.setUserId(userId);
			}
		}

		return follow;
	}

	/**
	 * 更新/创建跟进关系
	 * 
	 * @param follow
	 */
	private void checkFollow(UsFollow follow) {
		if (follow != null) {
			String userId = follow.getUserId();
			log.debug("----------------------------  用户[" + userId + "]跟进关系创建开始：" + JsonUtil.object2String(follow));
			UsFollow oldFollow = followMapper.selectByPrimaryKey(userId);
			// 判断用户是否存在跟进关系
			if (oldFollow == null) {
				followMapper.insertSelective(follow);
			} else {
				UsFollowLog log = new UsFollowLog();
				log.setOldCampusId(oldFollow.getCampusId());
				log.setOldDpId(oldFollow.getDpId());
				log.setOldEmpId(oldFollow.getEmpId());
				log.setCampusId(follow.getCampusId());
				log.setDpId(follow.getDpId());
				log.setEmpId(follow.getEmpId());
				log.setUserId(userId);
				log.setDrType(UsConstants.DR_TYPE_REGISTER);
				log.setDrId(follow.getEmpId());
				log.setRemark("注册或绑定手机 更新跟进人");
				// 插入跟进人变更记录
				followLogMapper.insertSelective(log);

				followMapper.updateByPrimaryKeySelective(follow);
			}
		}
	}

	private void addLog(Map<String, String> recruit) {
		String grade = recruit.get("grade");
		String pfsnId = recruit.get("pfsnId");
		String pfsnLevel = recruit.get("pfsnLevel");
		String pfsnName = recruit.get("pfsnName");
		String unvsId = recruit.get("unvsId");
		String unvsName = recruit.get("unvsName");
		String scholarship = recruit.get("scholarship");
		String sg = recruit.get("sg");

		String idCard = recruit.get("idCard");
		String name = recruit.get("name");
		String enrollTime = recruit.get("enrollTime");
		String userId = recruit.get("userId");

		UsEnrollLog eLog = new UsEnrollLog();

		eLog.setGrade(grade);
		eLog.setEnrollChannel(UsConstants.ENROLL_CHANNEL_SYSTEM);
		eLog.setIdCard(idCard);
		eLog.setName(name);
		eLog.setPfsnId(pfsnId);
		eLog.setPfsnLevel(pfsnLevel);
		eLog.setPfsnName(pfsnName);
		eLog.setUnvsId(unvsId);
		eLog.setUnvsName(unvsName);
		eLog.setScholarship(scholarship);
		eLog.setUserId(userId);
		eLog.setSg(sg);

		if (StringUtil.hasValue(enrollTime)) {
			eLog.setEnrollTime(DateUtil.convertDateStrToDate(enrollTime, DateUtil.YYYYMMDDHHMMSS_SPLIT));
		} else {
			eLog.setEnrollTime(new Date());
		}

		enrollLogMapper.insertSelective(eLog);
	}

	/**
	 * 绑定手机号码
	 * 
	 * @param userId
	 * @param mobile
	 * @param valicode
	 * @param inviteToken
	 * @param realName
	 * @return
	 */
	public Object bindMobile(String userId, String mobile, String valicode, String inviteToken, String realName,
			String scholarship, String ip, String mac, String coordinate, String idCard) {
		log.info("-------------------------- 开始绑定手机号[" + mobile + "] -- userId[" + userId + "]");

		// 是否开启短信验证
		String smsIsOn = yzSysConfig.getSmsSwitch();

		Object saveValicode = null;

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			 
			saveValicode = RedisService.getRedisService().get(mobile);
		} else {
			saveValicode = "888888";
		}

		if (saveValicode == null) {
			throw new BusinessException("E30009");// 短信验证码失效
		}

		if (!saveValicode.equals(valicode)) {
			throw new BusinessException("E30010");// 短信验证码错误
		}

		UsBaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(userId);

		if (baseInfo == null) {
			throw new BusinessException("E30016");// 用户不存在
		}

		String pId = null;
		UsBaseInfo pBaseInfo = null;

		boolean pIsEmp = false;

		if (StringUtil.hasValue(inviteToken)) {
			Secure s = TokenUtil.convert(inviteToken);
			pId = s.getUserId();
			if (userId.equals(pId)) {
				pId = null;
				log.error("------------------------ 自己不能邀约自己");
			}

			if (StringUtil.hasValue(pId)) {
				pBaseInfo = baseInfoMapper.selectByPrimaryKey(pId);
			    pIsEmp =UsRelationUtil.isEmp(pBaseInfo.getRelation());
			}

			log.info("-------------------------- pId[" + pId + "] -- 上线用户[" + JsonUtil.object2String(pBaseInfo) + "]");
		}

		UsBaseInfo exsitBaseInfo = baseInfoMapper.selectByMobile(mobile);

		if (exsitBaseInfo != null) {
			throw new BusinessException("E30011");
		} else {
			baseInfo.setMobile(mobile);
			String n = baseInfo.getRealName();
			if (StringUtil.isEmpty(n)) {
				baseInfo.setRealName(realName);
			}
			baseInfo.setScholarship(scholarship);
			if (!pIsEmp && pBaseInfo != null) {
				baseInfo.setpId(pId);
				baseInfo.setpIsMb(pBaseInfo.getIsMb());
				baseInfo.setpType(pBaseInfo.getUserType());
			}
			baseInfo.setRegTime(DateUtil.getCurrentDate(DateUtil.YYYYMMDDHHMMSS_SPLIT));
		}
		log.info("bindBaseInfo{}:"+JsonUtil.object2String(baseInfo));
		
		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);

		// 建立跟进关系
		UsFollow follow = createFlow(pBaseInfo, baseInfo, idCard);

		//推送给招生老师，注册信息
		sendMessage(follow, pBaseInfo, realName, mobile);

		Map<String, Object> result = loginByBaseInfoWithLog(baseInfo, UsConstants.LOGIN_TYPE_MBILE, ip, mac,coordinate);

		// 赠送智米优惠券
		
		UserRegisterEvent event = new UserRegisterEvent();
		event.setUserId(baseInfo.getUserId());
		if(null != pBaseInfo){
			event.setpId(pBaseInfo.getUserId());
			event.setpUserType(pBaseInfo.getUserType()); 
			event.setpRelation(pBaseInfo.getRelation());
		}
		event.setRegistered(true);
		event.setIdCard(idCard);
		event.setRealName(realName);
		event.setMobile(mobile);
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_BINDMOBILE_EVENT, JsonUtil.object2String(event));

		return result;
	}

	/**
	 * 是否绑定手机号
	 * 
	 * @param userId
	 * @return
	 */
	public Object isBindMobile(String userId) {
		UsBaseInfo baseInfo = baseInfoMapper.selectByPrimaryKey(userId);

		if (baseInfo == null) {
			throw new BusinessException("E30016");// 用户不存在
		}

		if (StringUtil.hasValue(baseInfo.getMobile())) {
			SessionInfo userInfo = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
			Object _mobile = userInfo.getMobile();

			if (_mobile == null) {
				//map.put(userId, "mobile", baseInfo.getMobile());
				userInfo.setMobile(baseInfo.getMobile());
			} else if (!_mobile.equals(baseInfo.getMobile())) {
				//map.put(userId, "mobile", baseInfo.getMobile());
				userInfo.setMobile(baseInfo.getMobile());
			}

			return GlobalConstants.TRUE;
		} else {
			return GlobalConstants.FALSE;
		}
	}

	/**
	 * 获取用户类型信息
	 * 
	 * @param userId
	 * @return
	 */
	public Object userTypes(String userId) {
		 
		UsBaseInfo userInfo = baseInfoMapper.selectByPrimaryKey(userId);
		//SessionInfo userInfo = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
		if (userInfo == null) {
			throw new BusinessException("E30016");// 用户不存在
		}
		return userInfo.getRelation();
	}
	/**
	 * 签到赠送智米 推送微信公众号信息
	 * @param amount
	 * @param afterAmount
	 * @param total
	 * @param session
	 */
	public void sendSignInMsg(String amount,String afterAmount,int total,SessionInfo session){
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(session.getOpenId());
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_SIGN);
		msgVo.addData("amount", amount);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		msgVo.addData("afterAmount", afterAmount);
		msgVo.addData("total", total+"");
		msgVo.setIfUseTemplateUlr(true);
		
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	}
	/**
	 * 用户注册 推送微信公众号信息
	 * @param openId
	 * @param teacherName
	 * @param stdName
	 * @param stdMobile
	 * @param pOpenId
	 * @param inviter
	 * @param inviterMobile
	 * @param url
	 */
	public void sendStudentRegist(String openId, String teacherName, String stdName, String stdMobile,
			String pOpenId, String inviter, String inviterMobile, String url){
	
		if (StringUtil.hasValue(openId)) {  //给招生老师推送
			// 发送招生老师推送
			log.info("招生老师openId:{},上级openId:{}",openId,pOpenId);
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_REGISTER);
			msgVo.addData("now", DateUtil.getNowDateAndTime());
			StringBuffer sb = new StringBuffer();
			sb.append("指定老师：").append(teacherName).append("\n被邀约人：").append(stdName).append(";\n");
			sb.append("电话：").append(stdMobile);

			if (StringUtil.hasValue(pOpenId)) {
				sb.append("\n邀约人：").append(inviter).append("\n邀约人电话：").append(inviterMobile);
			}
			msgVo.addData("remark", sb.toString());
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
		if(StringUtil.hasValue(pOpenId)){ //给上级推送信息,需要隐藏信息
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(pOpenId);
			msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_REGISTER);
			msgVo.addData("now", DateUtil.getNowDateAndTime());
			StringBuffer sbs = new StringBuffer();
			
			sbs.append("指定老师：").append(teacherName);

			stdName = stdName.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + "**");
			sbs.append("\n被邀约人：").append(stdName).append(";\n");
			sbs.append("电话：").append(stdMobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

			sbs.append("\n邀约人：").append(inviter);
			sbs.append("\n邀约人电话：").append(inviterMobile);
			
			msgVo.addData("remark", sbs.toString());
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
	}
	/**
	 * 修改手机号码获取验证码
	 * @param header
	 * @param body
	 * @return
	 */
	public Object getValicodeForUpdateMobile(Header header, Body body) {
		
		String mobile = body.getString("mobile");
		
		int valicodeLength = 6;// 短信验证码长度
		//根据新手机号验证
		Map<String, String> stuMap = bdsStudentMapper.getStudentByMobile(mobile);
		if(null != stuMap){
			throw new BusinessException("E30018"); //已经被其他学员信息占用了手机号
		}
		
		String stdId = header.getStdId();
		if(StringUtil.hasValue(stdId)){
			//根据当前用户对应的学员信息
			String userId = bdsStudentMapper.getUserIdByStdId(stdId);
			if(StringUtil.hasValue(userId)){
				//存在用户和学员的对应关系
				int existCount = baseInfoMapper.getCountByStdIdOrMobile(stdId, mobile);
				if(existCount >1){
					throw new BusinessException("E30018"); //已经被其他学员信息占用了手机号
				}
			}
		}
		
		String valicode = StringUtil.randomNumber(valicodeLength);

		Map<String, String> content = new HashMap<String, String>();
		content.put("code", valicode);
		log.info("手机号-------"+mobile +"修改手机号的验证码为:"+ valicode);
		String smsIsOn = yzSysConfig.getSmsSwitch();

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			SendSmsVo vo = new SendSmsVo();
			vo.setContent(content);
			vo.setMobiles(mobile);
			vo.setTemplateId(GlobalConstants.TEMPLATE_VALI_CODE);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
			RedisService.getRedisService().setex(mobile, valicode, 300);
		} else {
			RedisService.getRedisService().setex(mobile, "888888", 300);
		}

		return null;
	}
	/**
	 * 修改手机号码
	 * @param header
	 * @param body
	 * @return
	 */
	public Object updateMobile(Header header, Body body){
		String mobile = body.getString("mobile");
		String valicode = body.getString("valicode");
		
		String smsIsOn =  yzSysConfig.getSmsSwitch();

		Object saveValicode = null;

		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			saveValicode =RedisService.getRedisService().get(mobile);
		} else {
			saveValicode = "888888";
		}

		if (saveValicode == null) {
			throw new BusinessException("E30009");// 短信验证码失效
		}

		if (!saveValicode.equals(valicode)) {
			throw new BusinessException("E30010");// 短信验证码错误
		}
		//根据新手机号验证
		Map<String, String> stuMap = bdsStudentMapper.getStudentByMobile(mobile);
		if(null != stuMap){
			throw new BusinessException("E30018"); //已经被其他学员信息占用了手机号
		}
		
		String stdId = header.getStdId();
		if(StringUtil.hasValue(stdId)){  //是学员身份
			//根据当前用户对应的学员信息
			String userId = bdsStudentMapper.getUserIdByStdId(stdId);
			if(StringUtil.hasValue(userId)){
				//存在用户和学员的对应关系
				int existCount = baseInfoMapper.getCountByStdIdOrMobile(stdId, mobile);
				if(existCount >1){
					throw new BusinessException("E30018"); //已经被其他学员信息占用了手机号
				}
			}
			log.info("学员{}对应的用户{}修改手机号:{}"+mobile,stdId,userId);
			//开始修改(学员系统)
			bdsStudentMapper.updateStdMobileByStdId(mobile, stdId);
			//根据当前用户对应的学员信息
			if (StringUtil.hasValue(userId)) {
				//修改用户信息
				UsBaseInfo record = new UsBaseInfo();
				record.setUserId(userId);
				record.setMobile(mobile);
				baseInfoMapper.updateByPrimaryKeySelective(record);
			}
		}else{
			String userId = header.getUserId();
			UsBaseInfo record = new UsBaseInfo();
			record.setUserId(userId);
			record.setMobile(mobile);
			baseInfoMapper.updateByPrimaryKeySelective(record);
		}
		
		return null;
	}
}
