package com.yz.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.constants.UsConstants;
import com.yz.core.util.UsRelationUtil;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsWechatFansMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.SessionInfo;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsWechatFans;
import com.yz.model.UserRegisterEvent;
import com.yz.model.communi.Body;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.CodeUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.MobileOwnershipUtil;
import com.yz.util.StringUtil;
import com.yz.util.TokenUtil;
import com.yz.util.TokenUtil.Secure;

@Service
public class UserRegisterService {

	private Logger logger = LoggerFactory.getLogger(UserRegisterService.class);

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private UsBaseInfoMapper baseInfoMapper;

	@Autowired
	private BdsStudentService studentService;

	@Autowired
	private UsWechatFansMapper fansMapper;

	/**
	 * 
	 * @param mobile
	 * @param valicode
	 */
	private void preRegister(String mobile, String valicode) {
		// 是否开启短信验证
		String smsIsOn = yzSysConfig.getSmsSwitch();
		String saveValicode = null;
		if (GlobalConstants.TRUE.equals(smsIsOn)) {
			saveValicode = RedisService.getRedisService().getAndRem(mobile);
		} else {
			saveValicode = "888888";
		}
		if (saveValicode == null) {
			throw new BusinessException("E30009");// 短信验证码失效
		}
		if (!StringUtil.equalsIgnoreCase(saveValicode.toString(), valicode)) {
			throw new BusinessException("E30010");// 短信验证码错误
		}
	}

	/**
	 * 
	 * @param body
	 * @return
	 */
	private UsBaseInfo getBaseInfo(Body body) {
		UsBaseInfo baseInfo = new UsBaseInfo();
		String realName = body.getString("realName");
		String sg = body.getString("sg");
		String scholarship = body.getString("scholarship");
		baseInfo.setScholarship(scholarship);
		baseInfo.setSg(sg);
		baseInfo.setUserStatus(UsConstants.USER_STATUS_NORMAL);
		baseInfo.setRealName(realName);
		//手机归属地的获取
		Map<String, String> locationMap = MobileOwnershipUtil.getLocation(
				body.getString("mobile"),HttpTraceInterceptor.TRACE_INTERCEPTOR);
		baseInfo.setMobileLocation(locationMap.get("area"));
		baseInfo.setMobileZip(locationMap.get("zipcode"));
		return baseInfo;
	}

	/**
	 * @desc
	 * @param inviteToken
	 * @param mobile
	 * @param baseInfo
	 * @return
	 */
	private int setParentBaseInfo(String inviteToken, String mobile, UsBaseInfo baseInfo, UserRegisterInvoker invoker) {
		if (StringUtil.isNotBlank(inviteToken)) {
			Secure s = TokenUtil.convert(inviteToken);
			Map<String, String> studentMap = studentService.getStudentByMobile(mobile);
			if (studentMap == null || studentMap.isEmpty()) {
				UsBaseInfo pBaseInfo = baseInfoMapper.selectByPrimaryKey(s.getUserId());
				if (pBaseInfo != null) { // 非员工 才建立上下级关系
					if (!UsRelationUtil.isEmp(pBaseInfo.getRelation())) {
						baseInfo.setpId(s.getUserId());
						baseInfo.setpIsMb(pBaseInfo.getIsMb());
						baseInfo.setpType(pBaseInfo.getUserType());
					}
					invoker.setpInfo(pBaseInfo);
				}
				return pBaseInfo.getRelation();
			}
		}
		return 0;
	}

	/**
	 * @desc UsBaseInfo user = baseInfoMapper.selectByMobile(mobile);
	 * @desc 通过手机号码注册
	 *
	 */
	public Object register(Body body) {
		String mobile = body.getString("mobile");
		String valicode = body.getString("valicode");
		String idCard = body.getString("idCard");
		String channelId = body.getString("channelId");
		preRegister(mobile, valicode);

		UsBaseInfo user = this.getBaseInfo(body);
		user.setMobile(mobile);
		if (!TextUtils.isEmpty(channelId))
			user.setChannelId(channelId);
		UserRegisterInvoker invoker = null;
		String inviteToken = body.getString("inviteToken");

		switch (body.getString("bindType")) {
		case UsConstants.BIND_TYPE_NONE: // 分享链接h5注册渠道 无openId信息
			invoker = new UserRegisterInvoker(user, UsConstants.BIND_TYPE_NONE) {
				@Override
				public void wrapUser() {
					int count = baseInfoMapper.countByMobile(this.getInfo().getMobile());
					if (count > 0) {
						throw new BusinessException("E30011");// 手机号已被注册
					}
					String nickname = CodeUtil.base64Encode2String("zmc_" + StringUtil.shortUUID().toLowerCase());
					this.getInfo().setNickname(nickname);
					this.getInfo().setYzCode(IDGenerator.generatorYzCode());
					this.getInfo().setUserId(IDGenerator.generatorId());
					this.setpRelation(setParentBaseInfo(inviteToken, mobile, this.getInfo(), this));
					baseInfoMapper.insertSelective(this.getInfo());
				}
			};
			break;
		case UsConstants.BIND_TYPE_WECHAT: // 微信公众号注册 渠道
			invoker = new UserRegisterInvoker(user, UsConstants.BIND_TYPE_WECHAT) {
				@Override
				public void wrapUser() {
					String token = body.getString("token");
					String openId = TokenUtil.parse(token);
					UsWechatFans fansInfo = fansMapper.selectByPrimaryKey(openId);
					final UsBaseInfo exsitInfo = baseInfoMapper.selectByMobile(mobile);
					if (exsitInfo != null) // 表示该电话已被绑定 ，校验openid
					{
						String oldOpenId = exsitInfo.getWechatOpenId();
						if (StringUtil.hasValue(oldOpenId) && !oldOpenId.equals(openId)) {
							throw new BusinessException("E30016");
						}
						try {
							BeanUtils.copyProperties(this.getInfo(), exsitInfo);
						} catch (Exception e) {
							logger.error("copyProperties.error:{}", ExceptionUtil.getStackTrace(e));
						}
						this.setRegistered(true);
					}
					this.getInfo().setWechatOpenId(openId);
					this.getInfo().setHeadImg(fansInfo.getHeadImgUrl());
					this.getInfo().setNickname(fansInfo.getNickname());
					this.getInfo().setSex(fansInfo.getSex());
					if (this.isRegistered()) {
						baseInfoMapper.updateByPrimaryKeySelective(this.getInfo());
					} else {
						this.getInfo().setYzCode(IDGenerator.generatorYzCode());
						this.getInfo().setUserId(IDGenerator.generatorId());
						this.setpRelation(setParentBaseInfo(inviteToken, mobile, this.getInfo(), this));
						baseInfoMapper.insertSelective(this.getInfo());
					}
				}
			};
			break;
		default:
			logger.error("not support!");
			throw new BusinessException("E30014"); // 注册通道尚未开放
		}
		invoker.setRealName(user.getRealName());
		invoker.setIdCard(idCard);
		invoker.setMobile(mobile);
		return invoker.execute();
	}

	/**
	 * 
	 * 
	 * @author Administrator
	 *
	 */
	private abstract class UserRegisterInvoker {
		private UsBaseInfo baseInfo;

		private UsBaseInfo pInfo = new UsBaseInfo();

		private String token;

		private String bindType;

		private boolean isRegistered = false;

		private String idCard; // 身份证号码

		private int pRelation; // 推荐人类型

		private String realName; // 名称

		private String mobile; // 注册手机号码

		public UserRegisterInvoker(UsBaseInfo info, String bindType) {
			this.baseInfo = info;
			this.token = StringUtil.UUID();
			this.bindType = bindType;
		}

		public void setpInfo(UsBaseInfo pInfo) {
			this.pInfo = pInfo;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}

		public void setpRelation(int pRelation) {
			this.pRelation = pRelation;
		}

		public UsBaseInfo getInfo() {
			return baseInfo;
		}

		public void setRegistered(boolean isRegistered) {
			this.isRegistered = isRegistered;
		}

		public boolean isRegistered() {
			return isRegistered;
		}

		/**
		 * 
		 * @desc 设置存入session数据
		 * 
		 */
		private void setSession() {
			SessionInfo session = new SessionInfo();
			session.setUserId(baseInfo.getUserId());
			session.setNickName(CodeUtil.base64Decode2String(baseInfo.getNickname()));
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
			AppSessionHolder.setSessionInfo(baseInfo.getUserId(), session, AppSessionHolder.RPC_SESSION_OPERATOR);
		}

		/**
		 * @desc 返回给调用端结果
		 * @return
		 */
		private Object createReturn() {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("auth_token", TokenUtil.createToken(baseInfo.getUserId(), token));
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("nickname", CodeUtil.base64Decode2String(baseInfo.getNickname()));
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
		 * @desc 根据不同的业务规则进行用户的操作
		 * 
		 */
		public abstract void wrapUser();

		/**
		 * 
		 * @description 具体的处理方法
		 * @return
		 */
		public Object execute() {
			wrapUser();
			setSession();
			UserRegisterEvent event = new UserRegisterEvent();
			event.setUserId(baseInfo.getUserId());
			event.setpId(pInfo.getUserId());
			event.setpUserType(pInfo.getUserType());
			event.setBindType(bindType);
			event.setRegistered(isRegistered);
			event.setIdCard(idCard);
			event.setpRelation(pRelation);
			event.setRealName(realName);
			event.setMobile(mobile);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_REGISTER_EVENT, JsonUtil.object2String(event));
			logger.info("YZ_USER_REGISTER_EVENT:{}",JsonUtil.object2String(event));
			return this.createReturn();
		}
	}
}
