package com.yz.job.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.GwConstants;
import com.yz.constants.UsConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.generator.IDGenerator;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.UserMapper;
import com.yz.job.dao.UserRegisterMapper;
import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.job.model.AtsAwardRecord;
import com.yz.job.model.UsEnrollLog;
import com.yz.job.model.UsFollow;
import com.yz.model.AtsAwardRule;
import com.yz.model.SessionInfo;
import com.yz.model.UserRegisterEvent;
import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.template.YzBuzResolverEngine;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
public class UserRegisterService {

	private static Logger logger = LoggerFactory.getLogger(UserRegisterService.class);

	@Autowired
	private UserRegisterMapper userRegisterMapper;

	@Autowired
	private UserMapper userMapper;


	/**
	 * @step 1  赠送智米优惠券 通过规则引擎解析相应的规则 
	 *       2 赠送抽奖券 ？
	 * @desc 用户绑定手机事件
	 * @param event
	 */
	public void bindMobile(UserRegisterEvent event) {
		logger.info("bindMobile.envent:{}", JsonUtil.object2String(event));
		award(event); // 赠送注册智米
		sendCoupon(event.getUserId()); // 赠送注册用户优惠券 
	}

	/**
	 * @step 1 // 建立跟进关系 2 发送公众号消息 推送给招生老师，注册信息 3 赠送智米优惠券 通过规则引擎解析相应的规则 4
	 *       //如果已赠送过智米，后续赠送优惠券，不造成数据回滚，手动添加优惠券 ？ 5 赠送抽奖券 ？
	 * @desc 用户注册
	 * @param event
	 */
	public void register(UserRegisterEvent event) {
		logger.info("createFlow.envent:{}", JsonUtil.object2String(event));
		if (!event.isRegistered()) // 表示之前已经注册？
		{
			award(event); // 赠送注册智米
			sendCoupon(event.getUserId()); // 赠送注册用户优惠券
		}
		createFlow(event); // 创建跟进关系
	}

	/**
	 * 
	 * @desc 初始化系统的智米账号
	 * @param userId
	 */
	private void initUserAccount(String userId) {
		List<AtsAccount> accountList = new ArrayList<AtsAccount>();
		AtsAccount account = new AtsAccount();
		account.setCanDeposit(GlobalConstants.FALSE);
		account.setIsVisiable(GlobalConstants.TRUE);
		account.setUnit(FinanceConstants.ACC_UNIT_ZHIMI);
		account.setEmpId(null);
		account.setUserId(userId);
		account.setStdId(null);
		account.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		account.setAccStatus(FinanceConstants.ACC_STATUS_NORMAL);
		account.setAccId(IDGenerator.generatorId());
		accountList.add(account);
		logger.info("initUserAccount.account:{}", JsonUtil.object2String(accountList));
		userRegisterMapper.initAccount(accountList);
	}

	/**
	 * 
	 * @description 智米赠送、优惠券
	 * @param event
	 */
	private void award(UserRegisterEvent event) {
		int relation = event.getpRelation();
		boolean isEmp = (relation & 2) > 0;
		if (!isEmp && StringUtil.isNotBlank(event.getpId())) // 未注册 并且推荐人非员工
		{
			Map<String, Object> param = Maps.newHashMap();
			param.put("event", event);
			param.put("createDate", event.getCreateDate());
			// 降序排列
			List<AtsAwardRule> rules = YzBuzResolverEngine.getInstance()
					.resolverList(GwConstants.USER_REGISTER_BUZ_ENGINE, param, AtsAwardRule.class);
			if (rules != null && !rules.isEmpty()) {
				Set<AtsAwardRule> enableRule = Sets.newHashSet();
				AtsAwardRule rule = rules.stream().sorted().findFirst().get();
				boolean mutex = rule.getIsMutex() == 0; // 奖励规则互斥
				if (mutex) {
					enableRule.add(rule);
				} else {
					enableRule.addAll(rules);
				}
				long amount = enableRule.parallelStream().mapToLong(r -> {
					AtsAwardRecord record = new AtsAwardRecord();
					record.setAwardId(IDGenerator.generatorId());
					record.setZhimiCount(r.getZhimiCount());
					record.setExpCount(r.getExpCount());
					record.setRuleCode(r.getRuleCode());
					record.setAwardDesc(r.getRuleDesc());
					record.setUserId(event.getpId());
					record.setTriggerUserId(event.getUserId());
					record.setRuleType(r.getRuleType());
					this.userRegisterMapper.saveAwardRecord(record);
					return NumberUtils.toLong(r.getZhimiCount());
				}).sum();
				addAtsAccountSerial(event.getpId(), rule.getRuleDesc(), amount);
				YzTaskContext.getTaskContext().addEventDetail(event.getMobile(),
						"用户注册【" + event.getUserId() + "】赠送【" + event.getpId() + "】" + amount + "智米。");
			} 
		}
	}

	/**
	 * @description 更新用户账户信息 ，并记录智米帐变流水
	 * @param userId
	 * @param remark
	 * @param amount
	 */
	private void addAtsAccountSerial(String userId, String remark, Long amount) {
		AtsAccount account = userRegisterMapper.getAccountInfo(userId, FinanceConstants.ACC_TYPE_ZHIMI);
		logger.info("用户{}的账户信息{}", userId, JsonUtil.object2String(account));
		long accAmount = new BigDecimal(account.getAccAmount()).longValue();
		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccId(account.getAccId());
		serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		serial.setAction(FinanceConstants.ACC_ACTION_IN);
		serial.setAmount(String.valueOf(amount));
		serial.setUserId(userId);
		serial.setExcDesc(remark + "获得" + amount + "智米");
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
		serial.setBeforeAmount(account.getAccAmount());
		serial.setAfterAmount(String.valueOf(accAmount + amount));
		serial.setAccSerialNo(IDGenerator.generatorId());
		userRegisterMapper.saveSerial(serial);
		account.setAccAmount(String.valueOf(amount));
		userRegisterMapper.updateAccount(account);
	}

	/**
	 *
	 * @desc 创建跟进关系
	 * @param UserRegisterEvent
	 */
	private void createFlow(UserRegisterEvent event) {

		if (StringUtil.isNotBlank(event.getpId())) // 推荐人非员工，则将推荐人的跟进关系进行复制
		{
			boolean isEmp = (event.getpRelation() & 2) > 0;
			Map<String, String> recruit = userMapper.getRecruit(event.getIdCard(), event.getMobile());
			UsFollow follow = new UsFollow();
			if (recruit == null || recruit.isEmpty()) // 如果没有报读记录没有招生老师
														// 则走正常流程建立跟进关系
			{

				if (isEmp) // 推荐人是员工进入跟进关系绑定
				{
					Map<String, String> userInfo = userMapper.getEmpInfo(event.getpId()); // 根据用户Id查询员工信息
					follow.setEmpId(userInfo.get("empId"));
					follow.setDpId(userInfo.get("dpId"));
					follow.setDpManager(userInfo.get("dpManager"));
					follow.setCampusId(userInfo.get("campusId"));
					follow.setCampusManager(userInfo.get("campusManager"));
					follow.setUserId(event.getUserId());
					userMapper.saveUsFollow(follow); // 建立用户跟进关系
				} else // 非员工直接复制推荐人ID的跟进关系
				{
					userMapper.createUsFollowByPId(event.getpId(), event.getUserId());
				}
				if (!event.isRegistered()) {
					logger.info("{}开始初始化账户...", event.getUserId());
					initUserAccount(event.getUserId());
					logger.info("{}开始初始化账户结束...", event.getUserId());
					YzTaskContext.getTaskContext().addEventDetail(event.getMobile(),
							"【" + event.getUserId() + "】创建初始化智米账号！");
				}
			} else {
				String empId = recruit.get("empId");
				String dpId = recruit.get("dpId");
				String campusId = recruit.get("campusId");
				follow.setUserId(event.getUserId());
				follow.setCampusId(campusId);
				follow.setEmpId(empId);
				follow.setCampusManager(recruit.get("campusManager"));
				follow.setDpId(dpId);
				follow.setDpManager(recruit.get("dpManager"));
				userMapper.saveUsFollow(follow); // 建立用户跟进关系
				addLog(event.getUserId(), recruit);
				String stdId = recruit.get("stdId");
				if (StringUtil.isNotBlank(stdId)) {
					SessionInfo info = AppSessionHolder.getSessionInfo(event.getUserId(),
							AppSessionHolder.RPC_SESSION_OPERATOR);
					if (info != null) {
						info.setRelation(info.getRelation() | 4);
						AppSessionHolder.setSessionInfo(event.getUserId(), info, AppSessionHolder.RPC_SESSION_OPERATOR);
					}
					userMapper.bindUserStd(event.getUserId(), stdId); // 将用户系统和学员系统相互绑定
					userMapper.updateAccountUserIdByStdId(stdId, event.getUserId());
				}
			}
			// 推送微信消息
			sendMessage(follow, event.getpId(), event.getRealName(), event.getMobile());
		} else if (!event.isRegistered()) {
			initUserAccount(event.getUserId());
			YzTaskContext.getTaskContext().addEventDetail(event.getMobile(), "【" + event.getUserId() + "】创建初始化智米账号！");
		}

	}

	/**
	 * 
	 * @desc 添加报读日志
	 * @param recruit
	 */
	private void addLog(String userId, Map<String, String> recruit) {
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
		userMapper.saveUsEnrollLog(eLog);
	}

	/**
	 * 
	 * @desc 用户派券
	 * @param userId
	 */
	private void sendCoupon(String userId) {
		userRegisterMapper.sendCoupon(userId);
	}

	private void sendMessage(UsFollow follow, String pId, String stdName, String mobile) {
		try {
			if (!StringUtil.hasValue(follow.getUserId()) && StringUtil.hasValue(pId)) {
				// 非员工邀约
				String empId = userMapper.getUsFollowByPId(pId);
				if (StringUtil.hasValue(empId)) {
					follow.setEmpId(empId);
				}
			}
			logger.info("----------------------------  跟进信息发送：" + JsonUtil.object2String(follow));

			logger.info("----------------------------  跟进人信息：{}", pId);

			String recruitOpenId = "";
			String recruitName = "";

			String pOpenId = "";
			String pRealName = "";
			String pMobile = "";

			if (null != follow) {
				// 获取招生老师信息
				Map<String, String> recruitUserInfo = userMapper.getUserInfoByEmpId(follow.getEmpId());
				if (null != recruitUserInfo && recruitUserInfo.size() > 0) {
					recruitOpenId = recruitUserInfo.get("bindId");
					recruitName = recruitUserInfo.get("realName");
					logger.info("------------------------------- 发送微信消息，招生老师openId：{},姓名：{}", recruitOpenId,
							recruitName);
				}
			}

			if (StringUtil.hasValue(pId)) {
				Map<String, String> pUserInfo = userMapper.getUserInfoByUserId(pId);
				if (null != pUserInfo && pUserInfo.size() > 0) {
					pOpenId = pUserInfo.get("bindId");
					if (StringUtil.hasValue(pOpenId) && !pOpenId.equals(recruitOpenId)) {
						pRealName = pUserInfo.get("realName") == null ? "" : pUserInfo.get("realName");
						pMobile = pUserInfo.get("mobile") == null ? "" : pUserInfo.get("mobile");
					} else {
						pOpenId = null;
					}
					logger.info("------------------------------- 发送微信消息，邀约人openId：" + pOpenId + "手机号:" + pMobile);
				}
			}
			// 推送微信公众号信息
			sendStudentRegist(recruitOpenId, recruitName, stdName, mobile, pOpenId, pRealName, pMobile, null);

		} catch (Exception e) {
			logger.error("------------------------------- 用户注册微信推送失败,errorMsg:" + e.getMessage());
		}
	}

	/**
	 * 用户注册 推送微信公众号信息
	 * 
	 * @param openId
	 * @param teacherName
	 * @param stdName
	 * @param stdMobile
	 * @param pOpenId
	 * @param inviter
	 * @param inviterMobile
	 * @param url
	 */
	private void sendStudentRegist(String openId, String teacherName, String stdName, String stdMobile, String pOpenId,
			String inviter, String inviterMobile, String url) {

		if (StringUtil.hasValue(openId)) { // 给招生老师推送
			// 发送招生老师推送
			logger.info("招生老师openId:{},上级openId:{}", openId, pOpenId);
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
		if (StringUtil.hasValue(pOpenId)) { // 给上级推送信息,需要隐藏信息
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
}
