package com.yz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.FinanceConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.constants.AppConstants;
import com.yz.dao.BsOrderMapper;
import com.yz.dao.GsGoodsMapper;
import com.yz.dao.ZmcGoodsSalesMapper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.BsOrderParamInfo;
import com.yz.model.GsAuctionPart;
import com.yz.model.GsAuctionPartInsertInfo;
import com.yz.model.GsCourseGoods;
import com.yz.model.GsExchangePartInsertInfo;
import com.yz.model.GsGoodsCommentInsertInfo;
import com.yz.model.GsGoodsOrderInfo;
import com.yz.model.GsLotteryPart;
import com.yz.model.GsSalesAuction;
import com.yz.model.GsSalesLottery;
import com.yz.model.GsSalesNotify;
import com.yz.model.GsSalesPlan;
import com.yz.model.SessionInfo;
import com.yz.model.WechatMsgVo;
import com.yz.model.ZmcGoodsAuctionInfo;
import com.yz.model.ZmcGoodsExChangeInfo;
import com.yz.model.ZmcGoodsLotteryInfo;
import com.yz.model.ZmcGoodsSalesInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *  活动商品servic
 * @author lx
 * @date 2017年7月24日 下午12:11:22
 */
@Service
@Transactional
public class ZmcGoodsSalesService {

	private static final Logger log = LoggerFactory.getLogger(ZmcGoodsSalesService.class);
	@Autowired
	private ZmcGoodsSalesMapper zmcGoodsSalesMapper;
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;

	@Autowired
	private BsOrderService bsOrderService;
	
	@Autowired
	private BsOrderMapper bsOrderMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atsAccountApi;
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	
	public Object queryGoodsSalesByPage(int pageNum, int pageSize, Body body) {
		
		String salesType  = body.getString("salesType");
		String goodsType = body.getString("goodsType");
		PageHelper.startPage(pageNum, pageSize);
		List<ZmcGoodsSalesInfo> list = zmcGoodsSalesMapper.getZmcGoodsSalesInfo(salesType,goodsType);

		if(null != list && list.size()>0){
			for(ZmcGoodsSalesInfo salesInfo :list){
				if(salesInfo.getSalesType().equals("2")){
					String salesStatus ="2";
					Date startDate = DateUtil.convertDateStrToDate(salesInfo.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
					Date currentDate = new Date();
					if(StringUtil.hasValue(salesInfo.getEndTime())){
						salesStatus = "1"; //已经结束
					}else{
						if (currentDate.getTime() < startDate.getTime()) {
							salesStatus = "2"; // 即将开始
						} else if (currentDate.getTime() > startDate.getTime()) {
							salesStatus = "3"; // 进行中
						}
					}
					salesInfo.setSalesStatus(salesStatus);
				}
			}
		}
		PageInfo<ZmcGoodsSalesInfo> pageInfo = new PageInfo<>(list);
		return JSONObject.fromObject(pageInfo);
	}
	public Object getGsGoodsSalesDetailInfo(Header header, Body body){
		String salesId = body.getString("salesId");
		String salesType = body.getString("salesType");
		if(null == salesId || null == salesType){
			throw new BusinessException("E200018");
		}
		String userId = header.getUserId();
		if(salesType.equals(AppConstants.SALES_TYPE_EXCHNAGE)){//兑换
			ZmcGoodsExChangeInfo info = zmcGoodsSalesMapper.getZmcGoodsExChangeInfo(salesId);
			if(null != info){
				info.setSalesStatus(getSalesStatus(info.getStartTime(),info.getEndTime()));
				int exchangeCount = zmcGoodsSalesMapper.selectExchangeCount(salesId,userId);
				if(exchangeCount > Integer.parseInt(info.getOnceCount())){
					info.setIfExchange("Y");
				}else{
					info.setIfExchange("N");
				}
				GsSalesNotify notify= zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if(null != notify){
					info.setIfAddNotify("Y");
				}
				if(StringUtil.isEmpty(info.getGoodsId())){
					throw new BusinessException("E200019");
				}
				if(info.getGoodsType().equals("2")){ //课程
					GsCourseGoods courseGoods = gsGoodsMapper.getGsCourseGoods(info.getGoodsId());
					info.setAddress(courseGoods.getAddress());
					info.setActivityStartTime(courseGoods.getStartTime());
					info.setActivityEndTime(courseGoods.getEndTime());
					info.setLocation(courseGoods.getLocation());
					info.setCourseType(courseGoods.getCourseType());
					
				}else if(info.getGoodsType().equals("3")){ //活动
					GsCourseGoods activityGoods = gsGoodsMapper.getGsActivitiesGoods(info.getGoodsId());
					if(null !=activityGoods ){
						info.setAddress(activityGoods.getAddress());
						info.setLocation(activityGoods.getLocation());
						info.setTakein(activityGoods.getTakein());
						info.setActivityEndTime(activityGoods.getEndTime());
						info.setActivityStartTime(activityGoods.getStartTime());
					}
				}
			}
			
			return JSONObject.fromObject(info);
		}else if(salesType.equals(AppConstants.SALES_TYPE_LOTTERY)){//抽奖
			ZmcGoodsLotteryInfo info = zmcGoodsSalesMapper.getZmcGoodsLotteryInfo(salesId);
			if(null != info){
				
				String salesStatus ="2";
				Date startDate = DateUtil.convertDateStrToDate(info.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
				Date currentDate = new Date();
				if(StringUtil.hasValue(info.getEndTime())){
					salesStatus = "1"; //已经结束
				}else{
					if (currentDate.getTime() < startDate.getTime()) {
						salesStatus = "2"; // 即将开始
					} else if (currentDate.getTime() > startDate.getTime()) {
						salesStatus = "3"; // 进行中
					}
				}
				info.setSalesStatus(salesStatus);
				GsSalesNotify notify= zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if(null != notify){
					info.setIfAddNotify("Y");
				}
				GsLotteryPart part = zmcGoodsSalesMapper.getGsLotteryPart(salesId, userId,info.getPlanCount());
				if(null != part){
					info.setIfJoin("Y");
				}
			}
			return JSONObject.fromObject(info);
		}else if(salesType.equals(AppConstants.SALES_TYPE_AUCTION)){ //竞拍
			ZmcGoodsAuctionInfo info = zmcGoodsSalesMapper.getZmcGoodsAuctionInfo(salesId);
			if(null != info){
				//info.setSalesStatus(getSalesStatus(info.getStartTime(),info.getEndTime()));
				GsSalesNotify notify= zmcGoodsSalesMapper.getSalesNotifyLog(salesId, userId);
				if(null != notify){
					info.setIfAddNotify("Y");
				}
			}
			return JSONObject.fromObject(info);
		}
		return null;
	}
	
	public void insertGoodsComment(Header header, Body body){
		GsGoodsCommentInsertInfo insertInfo = new GsGoodsCommentInsertInfo();
		insertInfo.setCommentContent(body.getString("commentContent"));
		insertInfo.setSalesId(body.getString("salesId"));
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
        if(null == session){
        	throw new BusinessException("E200013");
        }
        insertInfo.setHeadImgUrl(session.getHeadImg());
        insertInfo.setMobile(session.getMobile());
        insertInfo.setUserId(session.getUserId());
        insertInfo.setUserName(session.getNickName());
        
		zmcGoodsSalesMapper.insertGoodsComment(insertInfo); 
	}
	
	/**
	 * 添加活动提醒
	 * @param header
	 * @param body
	 */
	public void addNewSalesNotify(Header header, Body body){
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
        if(null == session){
        	throw new BusinessException("E200020");
        }
       
		GsSalesNotify notify = new GsSalesNotify();
		String salesType = body.getString("salesType");
		if(salesType.equals("2") || salesType.equals("3")){
			notify.setNotifyContent(body.getString("salesName")+"活动第"+body.getString("planCount")+"即将开始,请做好准备");
			notify.setPlanCount(body.getString("planCount"));
		}else{
			notify.setNotifyContent(body.getString("salesName")+"活动即将开始,请做好准备");
		}
		notify.setSalesType(salesType);
		notify.setNotifyType("2");
		notify.setSalesId(body.getString("salesId"));
		notify.setUserId(header.getUserId());
		notify.setOpenId(session.getOpenId());
		notify.setNickName(session.getNickName());
		notify.setMobile(session.getMobile());
		
		 //需要验证是否已经存在提醒,防止出现多条记录
		GsSalesNotify alreadyNotify= zmcGoodsSalesMapper.getSalesNotifyLog(body.getString("salesId"), session.getUserId());
		if(alreadyNotify == null){
			zmcGoodsSalesMapper.addNewSalesNotify(notify);	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void addGsAuctionPart(Header header, Body body){
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
	    if(null == session){
	        throw new BusinessException("E200014");
	    }
		String salesId = body.getString("salesId");
		String auctionPrice = body.getString("auctionPrice");
		String addAuctionPrice = "0";
		//查询商品信息
  		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
  		if(goodsInfo == null){
  			throw new BusinessException("E200011");
  		}
		
	    //查找最后出价纪录
	    GsAuctionPart lastAuctionPart = zmcGoodsSalesMapper.getLastAuctionLog(salesId,goodsInfo.getPlanCount());
	    if(null !=lastAuctionPart){
	    	//判断出价是否高于最后一条
	    	BigDecimal lastAuctionPrice = AmountUtil.str2Amount(lastAuctionPart.getAuctionPrice());
	    	BigDecimal thisAuctionPrice = AmountUtil.str2Amount(auctionPrice);
	    	if(lastAuctionPrice.compareTo(thisAuctionPrice)>0){
	    		throw new BusinessException("E200023");
	    	}
	    	BigDecimal diffPrice = BigDecimalUtil.substract(thisAuctionPrice, lastAuctionPrice);
	    	//判断当前出价人是否和最后一条 相符
	    	if(lastAuctionPart.getUserId().equals(header.getUserId())){
	    		//继续扣除差价
	    		addAuctionPrice = String.valueOf(diffPrice);
	    		body.put("amount",diffPrice.intValue());	
		    	body.put("excDesc","参与<"+goodsInfo.getSalesName()+">竞拍活动追加价,扣除");
	    	}else{
	    		//把最后一个出价的人 智米退还
	    		Map<String, String> otherAccountMap =atsAccountApi.getAccount(lastAuctionPart.getUserId(), null, null, FinanceConstants.ACC_TYPE_ZHIMI);
	  	  		body.put("accId", otherAccountMap.get("accId"));
	  	  		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
	  			body.put("stdId",null);
	  			body.put("userId",lastAuctionPart.getUserId());
	  			body.put("empId", null);
	  			body.put("amount",lastAuctionPart.getAuctionPrice());
	  			body.put("action",FinanceConstants.ACC_ACTION_IN);
	  			body.put("excDesc","参与<"+goodsInfo.getSalesName()+">竞拍活动失败,返还");
	  			body.put("mappingId",goodsInfo.getSalesId());
	  			atsAccountApi.trans(body);
	    	}
	    }else{
	    	//直接完成出价操作
	    	body.put("amount",auctionPrice);	
	    	body.put("excDesc","参与<"+goodsInfo.getSalesName()+">竞拍活动出价,扣除");
	    }
	    //判断当前账户智米是否够抵扣
	    Map<String, String> accountMap =atsAccountApi.getAccount(header.getUserId(), header.getStdId(), header.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
		 if(null != accountMap && accountMap.size() >0){
			String accStatus = accountMap.get("accStatus");  //账户状态
			BigDecimal accAmount = AmountUtil.str2Amount(accountMap.get("accAmount"));  //账户余额
			if(null != accStatus && !accStatus.equals(FinanceConstants.ACC_STATUS_NORMAL)){
				throw new BusinessException("E200021");
			}
			BigDecimal thisAuctionPrice = AmountUtil.str2Amount(addAuctionPrice);
			if(null != accAmount && thisAuctionPrice.compareTo(accAmount) >0){
				throw new BusinessException("E200022");
			}
		}
  		body.put("accId", accountMap.get("accId"));
  		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		body.put("stdId",header.getStdId());
		body.put("userId",header.getUserId());
		body.put("empId", header.getEmpId());
		body.put("action",FinanceConstants.ACC_ACTION_OUT);
		body.put("mappingId",goodsInfo.getSalesId());
		atsAccountApi.trans(body);
	    
	    //修改最新的竞拍价
	    GsSalesAuction auction = new GsSalesAuction();
	    auction.setCurPrice(auctionPrice);
	    auction.setUserId(session.getUserId());
	    auction.setSalesId(salesId);
	  
	    zmcGoodsSalesMapper.updateGsSalesAuction(auction);
	    
		GsAuctionPartInsertInfo partInfo = new GsAuctionPartInsertInfo();
		partInfo.setAuctionPrice(auctionPrice);
		partInfo.setSalesId(salesId);
		partInfo.setPlanCount(goodsInfo.getPlanCount());
        partInfo.setHeadImgUrl(session.getHeadImg());
        partInfo.setMobile(session.getMobile());
        partInfo.setUserId(session.getUserId());
        partInfo.setUserName(session.getNickName());
        partInfo.setOpenId(session.getOpenId());
        
		zmcGoodsSalesMapper.addGsAuctionPart(partInfo);
	}
	/**
	 * 参与抽奖
	 * @param header
	 * @param body
	 */
	@SuppressWarnings("unchecked")
	public void addGsLotteryPart(Header header, Body body){
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
        if(null == session){
        	new BusinessException("E200015");
        }
		
        //查询商品信息
        String salesId = body.getString("salesId");
  		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
  		if(goodsInfo == null){
  			throw new BusinessException("E200011");
  		}
  	    //判断当前账户智米
  		Map<String, String> accountMap =atsAccountApi.getAccount(header.getUserId(), header.getStdId(), header.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
  			
	    //账户余额变动
  		body.put("accId", accountMap.get("accId"));
  		body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		body.put("stdId",header.getStdId());
		body.put("userId",header.getUserId());
		body.put("empId", header.getEmpId());
		body.put("amount",goodsInfo.getSalesPrice());
		body.put("action",FinanceConstants.ACC_ACTION_OUT);
		body.put("excDesc","参与<"+goodsInfo.getSalesName()+">活动的抽奖扣除");
		body.put("mappingId",goodsInfo.getSalesId());
		atsAccountApi.trans(body);
	
  		
		GsLotteryPart partInfo = new GsLotteryPart();
		partInfo.setSalesId(salesId);
		partInfo.setPlanCount(goodsInfo.getPlanCount());
        partInfo.setHeadImgUrl(session.getHeadImg());
        partInfo.setMobile(session.getMobile());
        partInfo.setUserId(session.getUserId());
        partInfo.setUserName(session.getNickName());
        partInfo.setOpenId(session.getOpenId());
        
		zmcGoodsSalesMapper.addGsLotteryPart(partInfo);
		
		//TODO 如果此次抽奖刚好达到人数则开奖进入下一期
		GsSalesLottery lottery =zmcGoodsSalesMapper.getGsSalesLotteryById(salesId);
		if(null != lottery){
			
			if(zmcGoodsSalesMapper.getGsLotteryPartCount(salesId,goodsInfo.getPlanCount()) >= Integer.parseInt(lottery.getRunCount())){
				//开奖  随机取出 开奖人员
				List<GsLotteryPart> partList = zmcGoodsSalesMapper.getLuckyUserInfo(salesId,Integer.parseInt(lottery.getWinnerCount()),goodsInfo.getPlanCount());
				if(null != partList && partList.size() >0){
					for(GsLotteryPart part : partList){
						//TODO 修改中奖状态同时给当前用户推送微信消息
						zmcGoodsSalesMapper.updateUserWinStatus(salesId, part.getUserId(),goodsInfo.getPlanCount());
						
						//TODO 下订单
						String orderNo = addNewBsOrder(salesId,1,null,session,false).toString();
						//推送微信公众号信息
						sendWinningMsg(true,goodsInfo.getSalesName(),orderNo,part.getOpenId());
					}
				}
				
				updateSalesLotteryPlan(goodsInfo);
			}
		}
	}
	//处理抽奖活动排期
	public void updateSalesLotteryPlan(GsGoodsOrderInfo goodsInfo){
		//结束本期，开启下一期
		//修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan =zmcGoodsSalesMapper.getGsSalesPalnById(goodsInfo.getPlanId());
		if(null != salesPlan){
			//TODO 修改排期数,以及当前排期的结束时间 增加定时任务
			GsGoodsOrderInfo salesInfo = new GsGoodsOrderInfo();
			GsSalesPlan updatePlan = new GsSalesPlan();
			updatePlan.setPlanId(salesPlan.getPlanId());
			
			if (Integer.parseInt(goodsInfo.getPlanCount()) < Integer.parseInt(salesPlan.getTotalCount())){
				//期数没有结束,正常接期
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setPlanCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount())+1));
				//salesInfo.setStartTime(DateUtil.stampToDate(new Date()));
				//180308 版本规划 修改
				//salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(DateUtil.stampToDate(new Date()), Integer.parseInt(goodsInfo.getInterval())));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
				// 定时任务
				//把已经加入任务的删除
				// 删除定时任务
				//180308 版本规划 修改
//				BmsTimer timer = scheduleApi.selectByJobName(salesInfo.getSalesId() + "continuePlan");
//				if(null != timer){
//					scheduleApi.removeTimer(timer.getId());
//					scheduleApi.deleteTimer(timer.getId());
//				}
				//goodsInfo.setEndTime(salesInfo.getEndTime());
				//salesContinue(goodsInfo,"定时延期");
				
				updatePlan.setCurCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount())+1));
				updatePlan.setReason("上期结束,正常延期");
				updatePlan.setPlanStatus("1");
			}else{
				//本期结束
				updatePlan.setCurCount(goodsInfo.getPlanCount());
				updatePlan.setReason("本期结束,延期终止");
				updatePlan.setPlanStatus("3");
				updatePlan.setEndTime(DateUtil.stampToDate(new Date()));
				//TODO 删除定时任务
				//180308 版本规划 修改
//				BmsTimer timer = scheduleApi.selectByJobName(salesInfo.getSalesId() + "continuePlan");
//				if(null != timer){
//					scheduleApi.removeTimer(timer.getId());
//					scheduleApi.deleteTimer(timer.getId());
//				}
				//180308 版本规划 修改
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setEndTime(DateUtil.stampToDate(new Date()));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
			}
			updatePlan.setLessCount(String.valueOf(Integer.parseInt(salesPlan.getTotalCount()) - Integer.parseInt(updatePlan.getCurCount())));
			zmcGoodsSalesMapper.updateGsSalesPlan(updatePlan);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object addNewBsOrder(String salesId, int count, String saId, SessionInfo session, boolean ifNeedZM){
		if(null == session){
	        throw new BusinessException("E200016");
	    }
		log.debug("学员:"+session.getStdId()+"对应的用户:"+session.getUserId()+"正在进行下单操作....."+salesId);
		//查询商品信息
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
		if(goodsInfo == null){
			throw new BusinessException("E200011");
		}
		//TODO 针对兑换商品活动做特殊处理,其余两个等下次优化处理 2018-01-30
		if(goodsInfo.getSalesType().equals("1") || goodsInfo.getSalesType().equals("2")){
			//查询兑换活动的可兑换数量
			int salesCount = zmcGoodsSalesMapper.getSalesCountById(salesId);
			if(salesCount < count){
				throw new BusinessException("E200012");
			}
			//扣除库存 
			zmcGoodsSalesMapper.updateSalesCount(count,salesId);
			if(salesCount==count){ //刚好扣完库存,此次兑换结束,如果是结束后不可见状态改为下架 2018-01-30
				if(StringUtil.hasValue(goodsInfo.getShowAfterOver()) && goodsInfo.getShowAfterOver().equals("0")){
					zmcGoodsSalesMapper.updateGoodsSalesStatus(salesId);
				}
			}
		}else{
			int goodsCount = zmcGoodsSalesMapper.getGoodsCountById(goodsInfo.getGoodsId());
			if(goodsCount < count){
				throw new BusinessException("E200012");
			}
			//扣除库存 
			zmcGoodsSalesMapper.updateGoodsCount(goodsCount, goodsInfo.getGoodsId());
			if(goodsCount==count){ //刚好扣完库存,此次兑换结束,如果是结束后不可见状态改为下架 2018-01-30
				if(StringUtil.hasValue(goodsInfo.getShowAfterOver()) && goodsInfo.getShowAfterOver().equals("0")){
					zmcGoodsSalesMapper.updateGoodsSalesStatus(salesId);
				}
			}
		}
		
		if (ifNeedZM) {  //如果是抽奖下单则不需要扣除
			// 扣积分
			Map<String, String> accountMap = atsAccountApi.getAccount(session.getUserId(), session.getStdId(),
				session.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
			// 账户余额变动
			Body body = new Body();
			body.put("accId", accountMap.get("accId"));
			body.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			body.put("stdId", session.getStdId());
			body.put("userId", session.getUserId());
			body.put("empId", session.getEmpId());
			body.put("amount", BigDecimalUtil.multiply(AmountUtil.str2Amount(goodsInfo.getSalesPrice()),AmountUtil.str2Amount(count + "")));
			body.put("action", FinanceConstants.ACC_ACTION_OUT);
			body.put("excDesc", "参与<" + goodsInfo.getSalesName() + ">活动的兑换扣除");
			body.put("mappingId", goodsInfo.getSalesId());
			atsAccountApi.trans(body);
		}
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
		paramInfo.setCount(count+"");
		paramInfo.setGoodsType(goodsInfo.getGoodsType());
		paramInfo.setUnit(goodsInfo.getUnit());
		paramInfo.setUserId(session.getUserId());
		paramInfo.setGoodsImg(goodsInfo.getGoodsImg());
		paramInfo.setMobile(session.getMobile());
		paramInfo.setUserName(session.getNickName());
		paramInfo.setSaId(saId);
		return bsOrderService.addNewBsOrderForActivity(paramInfo);
	}
	
	public Object addGsExchangePart(Header header, Body body){
		GsExchangePartInsertInfo  partInfo = new GsExchangePartInsertInfo();
		partInfo.setExchangeCount(body.getString("exchangeCount"));
		partInfo.setSalesId(body.getString("salesId"));
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
        if(null == session){
        	throw new BusinessException("E200017");
        }
        ZmcGoodsExChangeInfo info = zmcGoodsSalesMapper.getZmcGoodsExChangeInfo(body.getString("salesId"));
  		if(null != info){
  			if(body.getInt("exchangeCount")>Integer.parseInt(info.getOnceCount())){
  				throw new BusinessException("E200025");
  			}
  	  		int exchangeCount = zmcGoodsSalesMapper.selectExchangeCount(body.getString("salesId"),session.getUserId());
  	  		log.debug("用户="+session.getUserId()+"已经兑换="+exchangeCount);
  	  		if(exchangeCount >= Integer.parseInt(info.getOnceCount())){
  	  			throw new BusinessException("E200025");
  	  		}
  		}else{
  			throw new BusinessException("E200025");
  		}
       
        partInfo.setHeadImgUrl(session.getHeadImg());
        partInfo.setMobile(session.getMobile());
        partInfo.setUserId(session.getUserId());
        partInfo.setUserName(session.getNickName());
        
    	//收货地址信息
		String saId =  body.getString("saId");
		if(StringUtil.isEmpty(saId)){
			throw new BusinessException("E200024");
		}
		
		zmcGoodsSalesMapper.addGsExchangePart(partInfo);
	
		return addNewBsOrder(partInfo.getSalesId(),Integer.parseInt(partInfo.getExchangeCount()),saId,session,true);
	}
	//获取活动商品状态
	public static String getSalesStatus(String startTime,String endTime){
		String salesStatus = "2";
		Date startDate = null;
		Date endDate = null;
		startDate = DateUtil.convertDateStrToDate(startTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		endDate = DateUtil.convertDateStrToDate(endTime, DateUtil.YYYYMMDDHHMMSS_SPLIT);
		Date currentDate = new Date();
		if(currentDate.getTime()<startDate.getTime()){
			salesStatus = "2"; //即将开始
		}else if(currentDate.getTime() > startDate.getTime() && currentDate.getTime() < endDate.getTime()){
			salesStatus = "3"; //进行中
		}else if(currentDate.getTime() > endDate.getTime()){
			salesStatus = "1"; //已经结束
		}
		return salesStatus;
	}
	
	public void updateSalesAuctionPlan(String salesId){
		GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
  		if(goodsInfo == null){
  			throw new BusinessException("E200011");
  		}
		GsAuctionPart lastAuctionPart = zmcGoodsSalesMapper.getLastAuctionLog(salesId,goodsInfo.getPlanCount());
		//TODO 修改竞拍状态同时给当前用户推送微信消息
		zmcGoodsSalesMapper.updateUserMineStatus(salesId, lastAuctionPart.getUserId(),goodsInfo.getPlanCount());
		//TODO 下订单
		//addNewBsOrder(salesId,"1",null,session);
		//结束本期，开启下一期
		//修改抽奖活动的期数以及期数信息
		GsSalesPlan salesPlan =zmcGoodsSalesMapper.getGsSalesPalnById(goodsInfo.getPlanId());
		if(null != salesPlan){
			//修改排期数,以及当前排期的结束时间 增加定时任务
			GsGoodsOrderInfo salesInfo = new GsGoodsOrderInfo();
			GsSalesPlan updatePlan = new GsSalesPlan();
			updatePlan.setPlanId(salesPlan.getPlanId());
			
			if (Integer.parseInt(goodsInfo.getPlanCount()) < Integer.parseInt(salesPlan.getTotalCount())){
				//期数没有结束,正常接期
				salesInfo.setSalesId(goodsInfo.getSalesId());
				salesInfo.setPlanCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount())+1));
				salesInfo.setStartTime(DateUtil.stampToDate(new Date()));
				salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(DateUtil.stampToDate(new Date()), Integer.parseInt(goodsInfo.getInterval())));
				zmcGoodsSalesMapper.updateGsSalesPlanCount(salesInfo);
				//定时任务
				GsGoodsOrderInfo salesContinue =new GsGoodsOrderInfo();
				salesContinue.setSalesId(salesInfo.getSalesId());
				salesContinue.setEndTime(salesInfo.getEndTime());
				salesContinue.setSalesName(salesInfo.getSalesName());
				salesContinue.setPlanCount(salesInfo.getPlanCount());
				salesContinue.setSalesType(salesInfo.getSalesType());
				//TODO 待处理
				//salesContinue(salesContinue, "定时延期任务");
				
				updatePlan.setCurCount(String.valueOf(Integer.parseInt(goodsInfo.getPlanCount())+1));
				updatePlan.setReason("上期结束,正常延期");
				updatePlan.setPlanStatus("1");
			}else{
				//本期结束
				updatePlan.setCurCount(goodsInfo.getPlanCount());
				updatePlan.setReason("本期结束,延期终止");
				updatePlan.setPlanStatus("3");
				updatePlan.setEndTime(DateUtil.stampToDate(new Date()));
			}
			updatePlan.setLessCount(String.valueOf(Integer.parseInt(salesPlan.getTotalCount()) - Integer.parseInt(updatePlan.getCurCount())));
			zmcGoodsSalesMapper.updateGsSalesPlan(updatePlan);
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
	 * 确定兑换商品(新增的接口针对兑换京东对接的商品)
	 * @param header
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object confirmExchangeGoods(Header header, Body body){
		GsExchangePartInsertInfo  partInfo = new GsExchangePartInsertInfo();
		String exchangeCount = body.getString("exchangeCount");
		String salesId = body.getString("salesId");
		
		partInfo.setExchangeCount(exchangeCount);
		partInfo.setSalesId(salesId);
		//SessionInfo session = RedisService.getRedisService().getByte(header.getUserId(),SessionInfo.class);
		SessionInfo session = AppSessionHolder.getSessionInfo(header.getUserId(), AppSessionHolder.RPC_SESSION_OPERATOR);
        if(null == session){
        	throw new BusinessException("E200017");
        }
        log.info("学员:"+session.getStdId()+"对应的用户:"+session.getUserId()+"正在进行下单操作....."+salesId);
        //验证本地库存是否充足
        int salesCount = zmcGoodsSalesMapper.getSalesCountById(salesId);
        log.info("兑换商品:"+salesId+"还剩库存:"+salesCount);
		if(salesCount < Integer.parseInt(partInfo.getExchangeCount()) || salesCount <=0){
			throw new BusinessException("E200012");
		}
        ZmcGoodsExChangeInfo info = zmcGoodsSalesMapper.getZmcGoodsExChangeInfo(salesId);
  		if(null != info){
  		    //扣除库存 
  			zmcGoodsSalesMapper.updateSalesCount(Integer.parseInt(exchangeCount),salesId);
  			if(salesCount==Integer.parseInt(exchangeCount)){ //刚好扣完库存,此次兑换结束,如果是结束后不可见状态改为下架 2018-01-30
  				if(StringUtil.hasValue(info.getShowAfterOver()) && info.getShowAfterOver().equals("0")){
  					zmcGoodsSalesMapper.updateGoodsSalesStatus(salesId);
  				}
  			}
  			if(body.getInt("exchangeCount")>Integer.parseInt(info.getOnceCount())){
  				throw new BusinessException("E200025");
  			}
  	  		int alreadyExchangeCount = zmcGoodsSalesMapper.selectExchangeCount(salesId,session.getUserId());
  	  		log.info("用户="+session.getUserId()+"已经兑换="+alreadyExchangeCount);
  	  		if(alreadyExchangeCount >= Integer.parseInt(info.getOnceCount())){
  	  			throw new BusinessException("E200025");
  	  		}
  		}else{
  			throw new BusinessException("E200025");
  		}
		// 收货地址信息
		String saId = body.getString("saId");
		if (StringUtil.isEmpty(saId)) {
			throw new BusinessException("E200024");
		}
		Map<String, String> addressMap = bsOrderMapper.getAddress(saId);
  		//验证
  		if(StringUtil.hasValue(info.getSkuId())){ //京东商品
  			String pId = addressMap.get("provinceCode"); //一级地址
  			String cId = addressMap.get("cityCode"); //二级地址
  			String dId = addressMap.get("districtCode"); //三级地址
  			//去京东验证库存
  			String stockUrl = "https://bizapi.jd.com/api/stock/getNewStockById";
  			StringBuilder sb = new StringBuilder();
  			sb.append("token="+RedisService.getRedisService().get("jdAccessToken"));
  			sb.append("&skuNums=[{skuId:"+info.getSkuId()+",num:"+exchangeCount+"}]");
  			sb.append("&area="+pId+"_"+cId+"_"+dId);
  			log.debug("验证库存请求参数:========="+sb.toString());
  			String stockResult = HttpUtil.sendPost(stockUrl, sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
  			
  			if(null != stockResult){
  				JSONObject obj = JSONObject.fromObject(stockResult);
  				if(obj.getString("resultCode").equals("0000")){
  					JSONArray stockArray = JSONArray.fromObject(obj.getString("result"));
					/* 暂时只处理无货
					 * JD库存状态描述 
					 * 33 有货 现货-下单立即发货 
					 * 39 有货 在途-正在内部配货，预计2~6天到达本仓库 
					 * 40有货 可配货-下单后从有货仓库配货 
					 * 36,99 预订 
					 * 34 无货
					 */
  					if(stockArray.size()>0){
  						JSONObject stockObj = JSONObject.fromObject(stockArray.get(0));
  						log.debug("库存状态:========="+stockObj.getString("stockStateId"));
  	  					if(stockObj.getString("stockStateId").equals("34")){
  	  						//京东无货
  	  						throw new BusinessException("E200029");
  	  					}	
  					}
  					
  				}
  			}
  		}else{
  			//不可兑换
  			throw new BusinessException("E200028");
  		}
  		//开始兑换操作
		//为了保持数据的一致性,尽量避免京东下单
		//先扣积分,由于跨服务,有可能导致事物无法回滚,如果出现需要特殊处理
		try {
			//验证智米是否足够
			
			Map<String, String> accountMap = atsAccountApi.getAccount(session.getUserId(), session.getStdId(),
					session.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
			if(null != accountMap && accountMap.size() >0){
				String accStatus = accountMap.get("accStatus");  //账户状态
				BigDecimal accAmount = AmountUtil.str2Amount(accountMap.get("accAmount"));  //账户余额
				if(null != accStatus && !accStatus.equals(FinanceConstants.ACC_STATUS_NORMAL)){
					throw new BusinessException("E200021");
				}
				BigDecimal thisAuctionPrice = BigDecimalUtil.multiply(AmountUtil.str2Amount(info.getSalesPrice()),AmountUtil.str2Amount(exchangeCount + ""));
				if(null != accAmount && thisAuctionPrice.compareTo(accAmount) >0){
					throw new BusinessException("E200022");
				}
			}
			// 账户余额变动
			Body accBody = new Body();
			accBody.put("accId", accountMap.get("accId"));
			accBody.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			accBody.put("stdId", session.getStdId());
			accBody.put("userId", session.getUserId());
			accBody.put("empId", session.getEmpId());
			accBody.put("amount", BigDecimalUtil.multiply(AmountUtil.str2Amount(info.getSalesPrice()),AmountUtil.str2Amount(exchangeCount + "")));
			accBody.put("action", FinanceConstants.ACC_ACTION_OUT);
			accBody.put("excDesc", "参与<" + info.getSalesName() + ">活动的兑换扣除");
			accBody.put("mappingId", info.getSalesId());
			atsAccountApi.trans(accBody);
		} catch (Exception e) {
			log.error("兑换商品时,扣除智米失败:-----"+e.getMessage());
			throw new BusinessException("E200030");
		}
		//京东下单
		String orderResult = jdSubmitOrder(info.getSkuId(), exchangeCount, addressMap,info.getJdGoodsType());
		if(null != orderResult){
			JSONObject obj = JSONObject.fromObject(orderResult);
			if(obj.containsKey("resultCode")){
				if(obj.getString("resultCode").equals("0001")){
					log.debug("京东下单成功-----"+obj.getString("resultMessage"));
					//成功下单 解析京东返回的参数
					JSONObject resultOjb = JSONObject.fromObject(obj.getString("result"));
					String jdOrderId  = resultOjb.getString("jdOrderId");            //京东订单号
					String freight = resultOjb.getString("freight");                 //运费
					String orderPrice = resultOjb.getString("orderPrice");           //订单总价格		
					String orderNakedPrice = resultOjb.getString("orderNakedPrice"); //订单裸价		
					String orderTaxPrice = resultOjb.getString("orderTaxPrice");     //订单税额
					//解析里面的商品
					JSONArray skus = resultOjb.getJSONArray("sku");
					List<Map<String, String>> skuVos = new ArrayList<>();
					for(int i =0;i<skus.size();i++){
						JSONObject skuObj = JSONObject.fromObject(skus.get(i));
						Map<String, String> skuMap = new HashMap<>();
						skuMap.put("skuId", skuObj.getString("skuId"));
						skuMap.put("category", skuObj.getString("category"));
						skuMap.put("nakedPrice", skuObj.getString("nakedPrice"));
						skuMap.put("name", skuObj.getString("name"));
						skuMap.put("num", skuObj.getString("num"));
						skuMap.put("oid", skuObj.getString("oid"));
						skuMap.put("price", skuObj.getString("price"));
						skuMap.put("tax", skuObj.getString("tax"));
						skuMap.put("taxPrice", skuObj.getString("taxPrice"));
						skuMap.put("type", skuObj.getString("type"));
					
						skuVos.add(skuMap);
					}
					//查询商品信息
					GsGoodsOrderInfo goodsInfo = zmcGoodsSalesMapper.getGsGoodsOrderInfoById(salesId);
					if(goodsInfo == null){
						throw new BusinessException("E200011");
					}
					Body orderBody = new Body();
					//京东参数 start====
					orderBody.put("jdOrderId", jdOrderId);
					orderBody.put("freight", freight);
					orderBody.put("orderPrice", orderPrice);
					orderBody.put("orderNakedPrice", orderNakedPrice);
					orderBody.put("orderTaxPrice", orderTaxPrice);
					orderBody.put("skus", skus);
					//京东参数end =====
					orderBody.put("costPrice", goodsInfo.getCostPrice());
					orderBody.put("goodsId", goodsInfo.getGoodsId());
					orderBody.put("goodsName", goodsInfo.getGoodsName());
					orderBody.put("originalPrice", goodsInfo.getOriginalPrice());
					orderBody.put("salesId", goodsInfo.getSalesId());
					orderBody.put("salesName", goodsInfo.getSalesName());
					orderBody.put("salesPrice", goodsInfo.getSalesPrice());
					orderBody.put("salesType", goodsInfo.getSalesType());
					orderBody.put("count", exchangeCount);
					orderBody.put("goodsType", goodsInfo.getGoodsType());
					orderBody.put("unit", goodsInfo.getUnit());
					orderBody.put("userId", session.getUserId());
					orderBody.put("goodsImg", goodsInfo.getGoodsImg());
					orderBody.put("mobile", session.getMobile());
					orderBody.put("userName", session.getNickName());
					orderBody.put("saId", saId);
			
				
					bsOrderService.addJDNewBsOrder(orderBody);
					
					//添加兑换记录
				    partInfo.setHeadImgUrl(session.getHeadImg());
			        partInfo.setMobile(session.getMobile());
			        partInfo.setUserId(session.getUserId());
			        partInfo.setUserName(session.getNickName());
			        zmcGoodsSalesMapper.addGsExchangePart(partInfo);
			   
				}else if(obj.getString("resultCode").equals("0008")){
					log.info("重复下单-----"+obj.getString("resultMessage"));
				}else{

					// 补发
					Map<String, String> accountMap = atsAccountApi.getAccount(session.getUserId(), session.getStdId(),
							session.getEmpId(), FinanceConstants.ACC_TYPE_ZHIMI);
					// 账户余额变动
					Body accBody = new Body();
					accBody.put("accId", accountMap.get("accId"));
					accBody.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
					accBody.put("stdId", session.getStdId());
					accBody.put("userId", session.getUserId());
					accBody.put("empId", session.getEmpId());
					accBody.put("amount", BigDecimalUtil.multiply(AmountUtil.str2Amount(info.getSalesPrice()),
							AmountUtil.str2Amount(exchangeCount + "")));
					accBody.put("action", FinanceConstants.ACC_ACTION_IN);
					accBody.put("excDesc","参与<" + info.getSalesName() + ">活动的兑换,由于京东下单原因，导致兑换失败，返还智米!");
					accBody.put("mappingId", info.getSalesId());
					atsAccountApi.trans(accBody);

					log.error("京东下单失败--------" + obj.getString("resultMessage"));
					throw new BusinessException("E200031");
				}
			}
		}
		return null;
	}
	
	/**
	 * 京东下单
	 * @param skuId  京东skuId
	 * @param exchangeCount  兑换个数
	 * @param saId  地址id
	 * @param jdGoodsType 京东商品类型 0-实物, 1- 实体卡
	 * @return
	 */
	public String jdSubmitOrder(String skuId,String exchangeCount,Map<String, String> addressMap,String jdGoodsType){
		String submitState =yzSysConfig.getSubmitState();
		String regCode = yzSysConfig.getRegCode();
		String invoicePhone = yzSysConfig.getInvoicePhone();
		String invoiceType = yzSysConfig.getInvoiceType();
		//京东下单
		String thirdOrderNo = zmcGoodsSalesMapper.getSeq();  //自己的订单号,预制
		//组合下单请求参数
		//默认实物的token下单
		String token = RedisService.getRedisService().get("jdAccessToken");
		if(jdGoodsType.equals("1")){ //如为实体卡,则切换下单的token
			token = RedisService.getRedisService().get("jdEntityCardToken");
			log.debug("京东实体卡下单............."+skuId);
		}
		String orderUrl = "https://bizapi.jd.com/api/order/submitOrder";
		String companyName = "惠州市远智文化教育培训学校";
		StringBuilder sb = new StringBuilder();
		sb.append("token="+token);
		sb.append("&thirdOrder="+thirdOrderNo);
		sb.append("&sku=[{\"skuId\":"+skuId+",\"num\":"+exchangeCount+",\"bNeedGift\":true}]");
		sb.append("&name="+addressMap.get("saName"));
		sb.append("&province="+addressMap.get("provinceCode"));
		sb.append("&city="+addressMap.get("cityCode"));
		sb.append("&county="+addressMap.get("districtCode"));
		sb.append("&town="+(addressMap.get("streetCode")==null?"0":addressMap.get("streetCode")));
		sb.append("&address="+addressMap.get("address"));
		sb.append("&mobile="+addressMap.get("mobile"));
		sb.append("&email="+addressMap.get("email"));
		sb.append("&invoiceState=2"); 		             //集中开票,运营确认
		sb.append("&invoiceType="+invoiceType);  		 //电子发票 1 普通发票 2 增值税发票 3 电子发票
		sb.append("&selectedInvoiceTitle=5"); 			 //发票类型 单位
		sb.append("&companyName="+companyName);          //发票抬头
		sb.append("&invoiceContent=1"); 				//明细
		sb.append("&paymentType=4");                    //在线支付
		sb.append("&isUseBalance=1");                   //余额
		sb.append("&submitState="+submitState);        //不预占库存 0 预占(可通过接口取消),1 不预占
		sb.append("&regCode="+regCode);                //纳税号 
		sb.append("&invoicePhone="+invoicePhone);      //收增票人电话
		log.debug("京东下单请求参数:--------"+sb.toString());
		String orderResult = HttpUtil.sendPost(orderUrl,sb.toString(),HttpTraceInterceptor.TRACE_INTERCEPTOR);
		log.info("京东下单结果:-------"+orderResult);
		return orderResult;
	}
}
