package com.yz.service;

import com.alibaba.dubbo.config.annotation.Reference;

import com.yz.constants.WechatMsgConstants;
import com.yz.dao.GsGoodsMapper;
import com.yz.dao.GsGoodsSalesContinueMapper;
import com.yz.exception.BusinessException;
import com.yz.model.*;
import com.yz.model.communi.Body;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 抽奖、竞拍延期
 * @author lx
 * @date 2017年8月24日 下午12:25:20
 */
@Service
@Transactional
public class GsGoodsSalesContinueService{

	@Autowired
	private GsGoodsSalesContinueMapper continueMapper;
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private BsOrderService bsOrderService;
	
	@SuppressWarnings("unchecked")
	public boolean salesContinue(Body body){
		
		String salesId = body.getString("salesId");
		GsGoodsOrderInfo salesInfo = continueMapper.getGsGoodsOrderInfoById(salesId);
  		if(salesInfo == null){
  			throw new BusinessException("E200011");
  		}
  		if(salesInfo.getSalesType().equals("2")){ //抽奖
  		    //开奖  随机取出 开奖人员
  			GsSalesLottery lottery =continueMapper.getGsSalesLotteryById(salesId);
			if (null != lottery) {
				List<GsLotteryPart> partList = continueMapper.getLuckyUserInfo(salesId,
						Integer.parseInt(lottery.getWinnerCount()), salesInfo.getPlanCount());
				if (null != partList && partList.size() > 0) {
					for (GsLotteryPart part : partList) {
						//修改中奖状态同时给当前用户推送微信消息
						continueMapper.updateUserWinStatus(salesId, part.getUserId(), salesInfo.getPlanCount());
						//下订单
						Body lotteryPartBody = new Body();
						lotteryPartBody.put("userId", part.getUserId());
						lotteryPartBody.put("userName", part.getUserName());
						lotteryPartBody.put("mobile", part.getMobile());
						
						String orderNo = lotteryOrAuctionAddOrder(salesId,lotteryPartBody);
						
						
						//推送中奖结果 微信公众号提醒信息
						sendWinningMsg(true,salesInfo.getSalesName(),orderNo,part.getOpenId());
						
					}
				}else{
					//没人参与，库存回滚1
					gsGoodsMapper.updateGoodsCount(-Integer.parseInt(lottery.getWinnerCount()),salesInfo.getGoodsId());
				}
			}
  		}else if(salesInfo.getSalesType().equals("3")){ //竞拍
  			GsAuctionPart lastAuctionPart = continueMapper.getLastAuctionLog(salesId,salesInfo.getPlanCount());
			if (null != lastAuctionPart) {
				//修改竞拍状态同时给当前用户推送微信消息
				continueMapper.updateUserMineStatus(salesId, lastAuctionPart.getUserId(), salesInfo.getPlanCount());
				//下订单
				Body auctionPartBody = new Body();
				auctionPartBody.put("userId", lastAuctionPart.getUserId());
				auctionPartBody.put("userName", lastAuctionPart.getUserName());
				auctionPartBody.put("mobile", lastAuctionPart.getMobile());
				String orderNo = lotteryOrAuctionAddOrder(salesId,auctionPartBody);
				
				sendAuctionMsg(true,salesInfo.getSalesName(),salesInfo.getStartTime(),orderNo,lastAuctionPart.getOpenId());
				
				//查询出未中拍的
				List<GsAuctionPart> noAuctionPart = continueMapper.getNoAuctionList(salesId,salesInfo.getPlanCount(),lastAuctionPart.getUserId());
				if(null != noAuctionPart && noAuctionPart.size() >0){
					for(GsAuctionPart part : noAuctionPart){
						sendAuctionMsg(false,salesInfo.getSalesName(),salesInfo.getStartTime(),null,part.getOpenId());
					}
				}
			}else{
				//没人参与，库存回滚1
				gsGoodsMapper.updateGoodsCount(-1,salesInfo.getGoodsId());
			}
  		}
		
		//结束本期，开启下一期
		//修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan =gsGoodsMapper.getGsSalesPlan(salesInfo.getPlanId());
		if(null != salesPlan){
			// 修改排期数,以及当前排期的结束时间 增加定时任务
			GsGoodsOrderInfo updateSalesInfo = new GsGoodsOrderInfo();
			GsSalesPlan updatePlan = new GsSalesPlan();
			updatePlan.setPlanId(salesPlan.getPlanId());
			
			if (Integer.parseInt(salesInfo.getPlanCount()) < Integer.parseInt(salesPlan.getTotalCount())){
				//期数没有结束,正常接期
				updateSalesInfo.setSalesId(salesInfo.getSalesId());
				updateSalesInfo.setPlanCount(String.valueOf(Integer.parseInt(salesInfo.getPlanCount())+1));
				updateSalesInfo.setStartTime(DateUtil.stampToDate(new Date()));
				updateSalesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(DateUtil.stampToDate(new Date()), Integer.parseInt(salesInfo.getInterval())));
				continueMapper.updateGsSalesPlanCount(updateSalesInfo);
				// 定时任务
				GsGoodsSalesInsertInfo salesContinue =new GsGoodsSalesInsertInfo();
				salesContinue.setSalesId(salesInfo.getSalesId());
				salesContinue.setEndTime(updateSalesInfo.getEndTime());
				salesContinue.setSalesName(salesInfo.getSalesName());
				salesContinue.setPlanCount(salesInfo.getPlanCount());
				salesContinue.setSalesType(salesInfo.getSalesType());
				
				salesContinue(salesContinue, "定时延期任务");
				
				updatePlan.setCurCount(String.valueOf(Integer.parseInt(salesInfo.getPlanCount())+1));
				updatePlan.setReason("上期结束,正常延期");
				updatePlan.setPlanStatus("1");
			}else{
				//本期结束
				updatePlan.setCurCount(salesInfo.getPlanCount());
				updatePlan.setReason("本期结束,延期终止");
				updatePlan.setPlanStatus("3");
				updatePlan.setEndTime(DateUtil.stampToDate(new Date()));
			/*	//删除定时任务
				BmsTimer timer = scheduleApi.selectByJobName(salesInfo.getSalesId() + "continuePlan");
				if(null != timer){
					scheduleApi.removeTimer(timer.getId());
					scheduleApi.deleteTimer(timer.getId());
				}*/
			}
			updatePlan.setLessCount(String.valueOf(Integer.parseInt(salesPlan.getTotalCount()) - Integer.parseInt(updatePlan.getCurCount())));
			gsGoodsMapper.updateGsSalesPlan(updatePlan);
		}
		
		return true;
	}

	/**
	 * 中奖或者竞拍中奖后下订单(不进行智米扣除)
	 * @param salesId
	 * @param body
	 * @return
	 */
	public String lotteryOrAuctionAddOrder(String salesId,Body body){
		//查询商品信息
		GsGoodsOrderInfo goodsInfo = continueMapper.getGsGoodsOrderInfoById(salesId);
		if(goodsInfo == null){
			throw new BusinessException("E200011");
		}
		//由于已经提前预置了库存
		//进行下单
		BsOrderParamInfo paramInfo = new BsOrderParamInfo();
		paramInfo.setCostPrice(goodsInfo.getCostPrice());
		paramInfo.setGoodsId(goodsInfo.getGoodsId());
		paramInfo.setGoodsName(goodsInfo.getGoodsName());
		paramInfo.setOriginalPrice(goodsInfo.getOriginalPrice());
		paramInfo.setSalesId(goodsInfo.getSalesId());
		paramInfo.setSalesName(goodsInfo.getSalesName());
		paramInfo.setSalesPrice(goodsInfo.getSalesPrice());
		paramInfo.setSalesType(goodsInfo.getSalesType());
		paramInfo.setCount("1");
		paramInfo.setGoodsType(goodsInfo.getGoodsType());
		paramInfo.setUnit(goodsInfo.getUnit());
		paramInfo.setUserId(body.getString("userId"));
		paramInfo.setGoodsImg(goodsInfo.getGoodsImg());
		paramInfo.setMobile(body.getString("mobile"));
		paramInfo.setUserName(body.getString("userName"));

		return bsOrderService.addNewBsOrderForActivity(paramInfo).toString();
	}
	public boolean salesStart(Body body){
		String salesId = body.getString("salesId");
		GsGoodsOrderInfo salesInfo = continueMapper.getGsGoodsOrderInfoById(salesId);
  		if(salesInfo == null){
  			throw new BusinessException("E200011");
  		}
  		//修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan =new GsSalesPlan();
		salesPlan.setPlanId(salesInfo.getPlanId());
		salesPlan.setPlanStatus("1");
		gsGoodsMapper.updateGsSalesPlan(salesPlan);
		
		//删除定时任务
		/*BmsTimer timer = scheduleApi.selectByJobName(salesInfo.getSalesId() + "start");
		if(null != timer){
			scheduleApi.removeTimer(timer.getId());
			scheduleApi.deleteTimer(timer.getId());
		}*/
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void salesContinue(GsGoodsSalesInsertInfo salesInfo, String desc){
		BmsTimer timer =null;// scheduleApi.selectByJobName(salesInfo.getSalesId() + "continuePlan");
		if (null != timer) {
			// 直接更新cron表达式
			String executeTime = salesInfo.getEndTime();
			//scheduleApi.editCronTimer(timer.getId(), getCronByTimeStr(executeTime));
		} else {
			// 获得提醒时间
			timer = new BmsTimer();
			String executeTime = salesInfo.getEndTime();
			timer.setJobName(salesInfo.getSalesId() + "continuePlan");
			timer.setJobGroup("continue");
			timer.setJobStatus("1");
			Body body = new Body();
			body.put("salesId", salesInfo.getSalesId());
			body.put("salesType", salesInfo.getSalesType());
			body.put("planCount", salesInfo.getPlanCount());
			timer.setBody(body);
			//转换
			timer.setParam(JsonUtil.object2String(body));
			timer.setBeanType("1");
			timer.setCronExpression(getCronByTimeStr(executeTime));
			timer.setDescription(salesInfo.getSalesName() + desc);
			timer.setIsConcurrent("1");
			timer.setUpdateTime(new Date());
			timer.setCronTime(DateUtil.convertDateStrToDate(executeTime, "yyyy-MM-dd HH:mm:ss"));
			timer.setBeanClass("com.yz.api.GsSalesRemindApi");
			timer.setMethodName("salesContinue");
			//Object timerId = scheduleApi.addTimer(timer);
			//timer.setId(String.valueOf(timerId));
			//加入任务栈
			//scheduleApi.loadingTimer(timer.getId());
		}
	}
	
	public static String getCronByTimeStr(String timeStr){
		if(StringUtil.isEmpty(timeStr)){
			return null;
		}
		// cron 
		// 秒  分 时  日 月 周  年
		String[] timeStrArray = timeStr.split(" ");
		String[] ymdArray = timeStrArray[0].split("-");
		String[] hsmArray = timeStrArray[1].split(":");
		String d = ymdArray[2];
		String m = ymdArray[1];
		if(Integer.parseInt(d)<10){
			d = Integer.parseInt(d)+"";
		}
		if(Integer.parseInt(m)<10){
			m = Integer.parseInt(m)+"";
		}
		return hsmArray[2]+" " +hsmArray[1]+" "+hsmArray[0] +" " +d +" " + m +" " +"? " + ymdArray[0];
	}
	
	/**
	 * 中奖通知 推送微信公众号信息
	 * @param isWinning
	 * @param goodName
	 * @param orderNo
	 * @param openId
	 */
	public void sendWinningMsg(boolean isWinning, String goodName, String orderNo, String openId){

		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(openId);
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_WINNING);
		msgVo.addData("goodName", goodName);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		if(isWinning){
			msgVo.setExt1(orderNo);
			msgVo.addData("firstWord", "恭喜您，中奖啦！！！\n");
			msgVo.addData("remark", "\n请点击查看详情，完善收货地址");
		}else{
			msgVo.addData("firstWord", "很遗憾，此次活动未中奖\n");
			msgVo.addData("remark", "");
		}
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	}
	/**
	 * 竞拍结果 微信推送公众号信息
	 * @param isWinning
	 * @param goodName
	 * @param startTime
	 * @param orderNo
	 * @param openId
	 */
	public void sendAuctionMsg(boolean isWinning, String goodName, String startTime, String orderNo, String openId){
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(openId);
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_AUCTION);
		msgVo.addData("goodName", goodName);
		msgVo.addData("startTime", startTime);
		if (isWinning) {
			msgVo.setExt1(orderNo);
			msgVo.addData("firstWord", "恭喜您竞拍成功！\n");
			msgVo.addData("keyWord3", "竞拍成功");
			msgVo.addData("remark", "\n请点击查看详情，完善收货地址");
		} else {
			msgVo.addData("firstWord", "很遗憾，此次竞拍失败\n");
			msgVo.addData("keyWord3", "竞拍失败");
			msgVo.addData("remark","");
		}
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	}
}
