package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.dao.GsGoodsSalesContinueMapper;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.GsGoodsOrderInfo;
import com.yz.model.GsSalesNotify;
import com.yz.model.communi.Body;
import com.yz.service.GsGoodsSalesContinueService;
import com.yz.service.GsSalesNotifyService;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class GsSalesRemindApiImpl implements GsSalesRemindApi{

	@Autowired
	private GsSalesNotifyService gsSalesNotifyService;
	
	@Autowired
	private GsGoodsSalesContinueService continueService;
	
	@Autowired
	private GsGoodsSalesContinueMapper continueMapper;
	
	@Override
	public boolean salesBeginRemind(Body body) throws BusinessException
	{
		if(body == null){
			return false;
		}
		String salesId = body.getString("salesId");
		if(StringUtil.isEmpty(salesId)){
			return false;
		}
		GsGoodsOrderInfo salesInfo = continueMapper.getGsGoodsOrderInfoById(salesId);
  		if(salesInfo == null){
  			throw new BusinessException("E200011");
  		}
		String planCount = body.getString("planCount");
		//查被提醒的用户信息
		List<GsSalesNotify> notifyList = gsSalesNotifyService.getAllNotifyBySalesId(salesId,planCount);
		if(null != notifyList && notifyList.size() >0){
			for(GsSalesNotify notify:notifyList){
			
				
				//修改通知状态
				gsSalesNotifyService.updateNotifyStatus(notify.getNotifyId());
				// 微信推送消息
				//统一到task中处理
				if(notify.getSalesType().equals("1")){ //兑换
					//wechatApi.sendExchangeWarn(notify.getOpenId(), salesInfo.getSalesName(), salesInfo.getStartTime());
				}else if(notify.getSalesType().equals("2")){ //抽奖
					//wechatApi.sendWinningWarn(notify.getOpenId(), salesInfo.getSalesName(), salesInfo.getStartTime());
				}else if(notify.getSalesType().equals("3")){ //竞拍
					//wechatApi.sendAuctionWarn(notify.getOpenId(), salesInfo.getSalesName(), salesInfo.getStartTime());
				}
				
			}
		}
		//删除定时任务
		gsSalesNotifyService.deleteTimer(salesId);
		return true;
	}

	@Override
	public boolean salesContinue(Body body) throws IRpcException
	{
		if(body == null){
			return false;
		}
		return continueService.salesContinue(body);
	}

	@Override
	public boolean salesStart(Body body) throws IRpcException
	{
		if(body == null){
			return false;
		}
		return continueService.salesStart(body);
		
	}

	
}
