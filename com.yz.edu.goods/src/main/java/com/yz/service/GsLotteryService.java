package com.yz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.BdsLearnInfoApi;
import com.yz.api.BdsStudentApi;
import com.yz.api.UsInfoApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.constants.AppConstants;
import com.yz.core.util.LotteryUtil;
import com.yz.dao.GsLotteryMapper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.GsLottery;
import com.yz.model.GsLotteryWinning;
import com.yz.model.GsPrize;
import com.yz.model.GsUserPrize;
import com.yz.model.WechatMsgVo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class GsLotteryService {

	private static final Logger log = LoggerFactory.getLogger(GsLotteryService.class);

	private final static String iphoneX500 = "25569176257036469";
	private final static String coupon600 = "25569224424423538";

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	@Reference(version = "1.0")
	private BdsLearnInfoApi learnApi;

	@Reference(version = "1.0")
	private BdsStudentApi stdApi;

	@Autowired
	private GsLotteryMapper lotteryMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	public Object getAllWinningInfo(Header head, Body body) {
		int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
		int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 10);
		String lotteryCode = body.getString("lotteryCode");
		PageHelper.startPage(page, pageSize);
		List<Map<String, String>> list = lotteryMapper.selectAllWinningInfo(lotteryCode);
		for (Map<String, String> map : list) {
			String mobile = map.get("mobile");
			if (StringUtil.hasValue(mobile)) {
				mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
			}
			map.put("mobile", mobile);

		}
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	public void gainLotteryTicketRegist(String userId, String pId, String realName) {
		try {
			if (!StringUtil.hasValue(userId)) {
				return;
			}

			String lotteryCode = yzSysConfig.getLotteryCode();

			// 查询活动
			GsLottery lottery = lotteryMapper.selectLotteryByLotteryCode(lotteryCode);

			// 无活动 退出
			if (null == lottery) {
				return;
			}

			// 插入用户抽奖券
			lotteryMapper.insertLotteryTicket(userId, lottery.getLotteryId());

			if (StringUtil.hasValue(pId)) {
				// 插入用户抽奖券
				lotteryMapper.insertLotteryTicket(pId, lottery.getLotteryId());
				sendTicketInform(pId, "邀请" + realName + "报读获得抽奖1次");
			}

			sendTicketInform(userId, "恭喜您获得了一次抽奖机会，点击该消息进行抽奖！");
		} catch (IRpcException e) {
			log.error("-------------------------------------- 赠送抽奖券报错：" + e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void gainLoterryTicket(String userId) {

		try {
			if (!StringUtil.hasValue(userId)) {
				return;
			}

			String lotteryCode = yzSysConfig.getLotteryCode();

			// 查询活动
			GsLottery lottery = lotteryMapper.selectLotteryByLotteryCode(lotteryCode);
			// 无活动 退出
			if (null == lottery) {
				return;
			}
			log.info("抽奖信息...=" + lottery.getLotteryId());
			log.info("start.....give....");
			// 插入用户抽奖券
			lotteryMapper.insertLotteryTicket(userId, lottery.getLotteryId());
			log.info("end.....give....");

			Header head = new Header();
			head.setUserId(userId);
			Map<String, String> userInfo = (Map<String, String>) usApi.getOtherInfo(head, null);
			String pId = userInfo.get("pId");

			if (StringUtil.hasValue(pId)) {
				// 插入用户抽奖券
				lotteryMapper.insertLotteryTicket(pId, lottery.getLotteryId());
				String realName = userInfo.get("realName");
				sendTicketInform(pId, "邀请" + realName + "报读获得抽奖1次");
			}

			sendTicketInform(userId, "恭喜您获得了一次抽奖机会，点击该消息进行抽奖！");
		} catch (IRpcException e) {
			log.error("-------------------------------------- 赠送抽奖券报错：" + e.getMessage());
		}

	}

	private void sendTicketInform(String userId, String content) {
		// 获取openId
		Body body = new Body();
		body.put("userId", userId);

		Object openIdObj = usApi.getOpenIdByUserId(body);

		// 发送微信通知
		if (null != openIdObj) {
			String openId = openIdObj.toString();
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			Map<String, String> contentMap = new HashMap<>();
			contentMap.put("content", content);
			msgVo.setContentData(contentMap);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
	}

	public Object getLotteryInfo(String lotteryCode, String userId) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 查询活动
		GsLottery lottery = lotteryMapper.selectLotteryByLotteryCode(lotteryCode);

		if (null == lottery) {
			// throw new BusinessException("E60041");
			return null;
		}

		Integer ticketCount = lotteryMapper.selectTicketCount(userId, lottery.getLotteryId());
		result.put("lotteryId", lottery.getLotteryId());
		result.put("ticketCount", ticketCount);

		String ticketStatus = null;
		String learnId = null;

		if (ticketCount <= 0) {
			Map<String, String> statusInfo = learnApi.getLearnStatus(userId);
			ticketStatus = statusInfo.get("ticketStatus");
			learnId = statusInfo.get("learnId");
		}

		// 抽奖状态 1-去报读 2-去缴费 3-去邀约
		result.put("ticketStatus", ticketStatus);
		result.put("learnId", learnId);

		// 中奖信息轮循
		// List<Map<String, String>> winners =
		// lotteryMapper.selectWinnerInfoList(lottery.getLotteryId());
		List<Map<String, String>> winners = lotteryMapper.selectWinningInfo(userId, lottery.getLotteryId());

		result.put("winners", winners);

		return result;
	}

	/**
	 * 抽奖
	 * 
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	public Object lottery(String userId, String lotteryId) {

		// 判断活动是否结束
		int count = lotteryMapper.selectLotteryCount(lotteryId);

		if (count <= 0) {
			throw new BusinessException("E200026"); // 活动已结束
		}

		int ticketCount = lotteryMapper.selectTicketCount(userId, lotteryId);
		if (ticketCount <= 0) {
			throw new BusinessException("E200027"); // 抽奖券不足
		}
		// 使用抽奖券
		lotteryMapper.cutLotterTicket(userId, lotteryId);

		List<GsPrize> prizes = lotteryMapper.selectPrizes(lotteryId);

		if (prizes.isEmpty()) {
			throw new BusinessException("E200026"); // 活动已结束
		}

		/**
		 * iPhoneX 前200-500人时中一个，1000元优惠券300-600人时中一个
		 */
		int winCount = lotteryMapper.selectWinCount(lotteryId);
		if (winCount >= 200 && winCount <= 499) { // 300之前放一个
			int iphoneCount = lotteryMapper.selectPrizeWinCount(lotteryId, iphoneX500);
			if (iphoneCount <= 0) {
				GsPrize iphone = new GsPrize();
				iphone.setPrizeId(iphoneX500);
				iphone.setPrizeName("iPhoneX");
				iphone.setPrizeType(AppConstants.LOTTERY_PRIZE_TYPE_KIND);
				iphone.setProbability(0.03d);
				prizes.add(iphone);
			}
		} else if (winCount >= 300 && winCount <= 599) {
			int iphoneCount = lotteryMapper.selectPrizeWinCount(lotteryId, coupon600);
			if (iphoneCount <= 0) {
				GsPrize iphone = new GsPrize();
				iphone.setPrizeId(coupon600);
				iphone.setPrizeName("学费抵用券1000元");
				iphone.setPrizeType(AppConstants.LOTTERY_PRIZE_TYPE_VIRTUAL);
				iphone.setProbability(0.02d);
				prizes.add(iphone);
			}
		}

		// 生产随机数
		int index = LotteryUtil.drawGift(prizes);

		if (index < 0) {
			throw new BusinessException("E200026"); // 活动已结束
		}

		// 中奖奖品
		GsPrize prize = prizes.get(index);

		if (winCount == 499) { // 三百名还未中奖，则第300名中奖
			int iphoneCount = lotteryMapper.selectPrizeWinCount(lotteryId, iphoneX500);
			if (iphoneCount <= 0) {
				GsPrize iphone = new GsPrize();
				iphone.setPrizeId(iphoneX500);
				iphone.setPrizeName("iPhoneX");
				iphone.setPrizeType(AppConstants.LOTTERY_PRIZE_TYPE_KIND);
				iphone.setProbability(0.03d);

				// 中奖iphone
				prize = iphone;
			}
		} else if (winCount == 599) {
			int iphoneCount = lotteryMapper.selectPrizeWinCount(lotteryId, coupon600);
			if (iphoneCount <= 0) {
				GsPrize iphone = new GsPrize();
				iphone.setPrizeId(coupon600);
				iphone.setPrizeName("学费抵用券1000元");
				iphone.setPrizeType(AppConstants.LOTTERY_PRIZE_TYPE_VIRTUAL);
				iphone.setProbability(0.02d);

				// 中奖iphone
				prize = iphone;
			}
		}

		// 减少库存
		lotteryMapper.cutPrizeStock(prize.getPrizeId());

		// 插入中奖信息
		Header head = new Header();
		head.setUserId(userId);
		Map<String, String> userInfo = (Map<String, String>) usApi.getOtherInfo(head, null);

		GsLotteryWinning win = lotteryMapper.selectUserLotteryCount(userId, lotteryId);

		if (null == win) { // 未有中奖纪录，插入新的记录
			win = new GsLotteryWinning();

			win.setLotteryId(lotteryId);
			win.setUserId(userId);
			win.setMobile(userInfo.get("mobile"));
			win.setRealName(userInfo.get("realName"));

			lotteryMapper.insertLotteryWinning(win);
		}

		GsUserPrize userPrize = new GsUserPrize();
		userPrize.setWinningId(win.getWinningId());
		userPrize.setPrizeId(prize.getPrizeId());

		// 插入中奖商品
		lotteryMapper.insertUserPrize(userPrize);

		if (AppConstants.LOTTERY_PRIZE_TYPE_VIRTUAL.equals(prize.getPrizeType())) { // 优惠券

			// 赠送优惠券
			stdApi.sendLotteryCoupon(userId, prize.getCouponId());

			// 修改商品赠送状态
			lotteryMapper.updateUserPrizeSend(userPrize.getUpId(), AppConstants.LOTTERY_USER_PRIZE_STATUS_SENDED);

		}
		prize.setUpId(userPrize.getUpId());

		return prize;
	}

	public Object getWinningInfo(String lotteryCode) {
		return lotteryMapper.selectWinnerInfoList(lotteryCode);
	}

	public void setPrizeAddress(Header header, Body body) {
		GsUserPrize prize = new GsUserPrize();
		prize.setCity(body.getString("city"));
		prize.setCityCode(body.getString("cityCode"));
		prize.setDistrict(body.getString("district"));
		prize.setDistrictCode(body.getString("districtCode"));
		prize.setAddress(body.getString("address"));
		prize.setMobile(body.getString("mobile"));
		prize.setProvince(body.getString("province"));
		prize.setProvinceCode(body.getString("provinceCode"));
		prize.setUserName(body.getString("userName"));
		String upId = body.getString("upId");
		prize.setUpId(upId);
		lotteryMapper.updateUserPrizeAddress(prize);

	}

}
