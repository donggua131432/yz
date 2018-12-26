package com.yz.service.goods;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.dao.goods.GsGoodsMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.MsgRemindVo;
import com.yz.model.common.IPageInfo;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsSalesInsertInfo;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsGoodsStore;
import com.yz.model.goods.GsLotteryGoodsSales;
import com.yz.model.goods.GsLotteryGoodsSalesDetail;
import com.yz.model.goods.GsLotteryPart;
import com.yz.model.goods.GsSalesLottery;
import com.yz.model.goods.GsSalesPlan;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 抽奖
 * @author lx
 * @date 2017年8月4日 下午5:14:00
 */
@Service
@Transactional
public class GsGoodsLotteryService {
	
	@Autowired
	private YzSysConfig yzSysConfig ;
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private GsGoodsService gsGoodsService;
	
	public IPageInfo<GsLotteryGoodsSales> getZmcLotteryGoodsSales(int start, int length,GsGoodsSalesQuery salesQuery) {
		PageHelper.offsetPage(start, length);
		List<GsLotteryGoodsSales> resultList = gsGoodsMapper.getZmcLotteryGoodsSales(salesQuery);
		if(null != resultList && resultList.size() >0){
			for(GsLotteryGoodsSales goodsSales : resultList){
				if(StringUtil.hasValue(goodsSales.getEndTime())){
					goodsSales.setActivityStatus("1"); //已结束
				}else{
					Date startDate = DateUtil.convertDateStrToDate(goodsSales.getStartTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
					Date currentDate = new Date();
					if (currentDate.getTime() < startDate.getTime()) {
						goodsSales.setActivityStatus("2"); // 即将开始
					} else if (currentDate.getTime() > startDate.getTime()) {
						goodsSales.setActivityStatus("3"); // 进行中
					}
				}
			}
		}
		return new IPageInfo<GsLotteryGoodsSales>((Page<GsLotteryGoodsSales>) resultList);
	}
	
	public GsLotteryGoodsSalesDetail getZmcLotteryGoodsSalesDetail(String salesId){
		return gsGoodsMapper.getZmcLotteryGoodsSalesDetail(salesId);
	}
    public Object insertLotteryGoodsSales(GsLotteryGoodsSalesDetail salesDetail){
    	//商品信息
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
		GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
    	//如果添加的时候 有goodsId 说明是继承
    	if(StringUtil.hasValue(salesDetail.getGoodsId())){
    	
    		updateLotteryUseGoodsInfo(salesDetail);
    		
    		salesInfo.setGoodsId(salesDetail.getGoodsId());
			
    	}else{
    		
    		goodsInfo.setGoodsName(salesDetail.getGoodsName());
    		goodsInfo.setGoodsDesc(salesDetail.getGoodsDesc());
    		goodsInfo.setGoodsContent(salesDetail.getGoodsContent());
    		goodsInfo.setGoodsType(salesDetail.getGoodsType());
    		goodsInfo.setCostPrice(salesDetail.getCostPrice());
    		goodsInfo.setOriginalPrice(salesDetail.getOriginalPrice());
    		goodsInfo.setGoodsCount(salesDetail.getGoodsCount());
    		goodsInfo.setUpdateUser(salesDetail.getUpdateUser());
    		goodsInfo.setUpdateUserId(salesDetail.getUpdateUserId());
    		goodsInfo.setGoodsId(IDGenerator.generatorId());
    		gsGoodsMapper.addNewGoodsInfo(goodsInfo);
    		//库存信息
    		GsGoodsStore store = new GsGoodsStore();
    		store.setGoodsId(goodsInfo.getGoodsId());
    		int goodsCount = Integer.parseInt(salesDetail.getGoodsCount());
    		int totalCount = Integer.parseInt(salesDetail.getTotalCount()) * Integer.parseInt(salesDetail.getWinnerCount());
    		int stockCount = goodsCount - totalCount;
    		store.setGoodsCount(stockCount +"");
    		store.setUpdateUser(goodsInfo.getUpdateUser());
    		store.setUpdateUserId(goodsInfo.getUpdateUserId());
    		gsGoodsMapper.addNewGsGoodsStore(store);
    		
			//附件
			goodsInfo.setAnnexUrl(salesDetail.getCoverUrl());
			goodsInfo.setAnnexUrlPortrait(salesDetail.getCoverUrlPortrait());
			gsGoodsService.initGoodsAnnex(goodsInfo);
			
			salesInfo.setGoodsId(goodsInfo.getGoodsId());
    	}
		salesInfo.setUpdateUser(salesDetail.getUpdateUser());
		salesInfo.setUpdateUserId(salesDetail.getUpdateUserId());
		salesInfo.setSalesName(salesDetail.getSalesName());
		salesInfo.setSalesType("2");
		salesInfo.setStartTime(salesDetail.getStartTime());
		//根据周期算出结束的时间 根据180309版本,暂时不对结束时间赋值
		//salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesPrice(salesDetail.getSalesPrice());
		salesInfo.setSalesRules(salesDetail.getSalesRules());
		salesInfo.setInterval(salesDetail.getInterval());
		salesInfo.setSalesStatus(salesDetail.getSalesStatus());
		
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		salesInfo.setShowSeq(salesDetail.getShowSeq()==null?"0":salesDetail.getShowSeq());
		salesInfo.setSalesCount(salesDetail.getGoodsCount());
		salesInfo.setSalesId(IDGenerator.generatorId());
		gsGoodsMapper.addZmcGoodsSales(salesInfo);
		
		GsSalesLottery lottery = new GsSalesLottery();
		lottery.setSalesId(salesInfo.getSalesId());
		lottery.setRunCount(salesDetail.getRunCount());
		lottery.setWinnerCount(salesDetail.getWinnerCount());
		lottery.setRunTime(salesDetail.getStartTime());
		lottery.setUpdateUser(salesDetail.getUpdateUser());
		lottery.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.addGsSalesLottery(lottery);
		
		GsSalesPlan plan = new GsSalesPlan();
		plan.setCurCount("1");
		plan.setLessCount(String.valueOf((Integer.parseInt(salesDetail.getTotalCount())-1)));
		plan.setPlanStatus("0");
		plan.setTotalCount(salesDetail.getTotalCount());
		plan.setStartTime(salesInfo.getStartTime());
		plan.setUpdateUser(salesDetail.getUpdateUser());
		plan.setUpdateUserId(salesDetail.getUpdateUserId());
		plan.setPlanId(IDGenerator.generatorId());
		gsGoodsMapper.addGsSalesPlan(plan);
		
		//更新兑换活动商品 的排期信息
		salesInfo.setIsPlan("1");
		salesInfo.setPlanCount("1");
		salesInfo.setPlanId(plan.getPlanId());
		gsGoodsMapper.updateZmcGoodsSales(salesInfo);
    	
		
		//定时任务
		//由于添加的时候已经添加任务,只需要修改cron表达式的数值
		try {
			//根据你180309版本规划  智米中心 - 抽奖取消截止时间，只按人数开奖
			//salesStart(salesInfo,"抽奖活动开始");
			salesBeginRemind(salesInfo);
			//salesContinue(salesInfo, "抽奖活动商品延期");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
    
    public void updateLotteryGoodsSales(GsLotteryGoodsSalesDetail salesDetail){
		
    	updateLotteryUseGoodsInfo(salesDetail);
    	
		GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
		salesInfo.setSalesId(salesDetail.getSalesId());
		salesInfo.setGoodsId(salesDetail.getGoodsId());
		salesInfo.setUpdateUser(salesDetail.getUpdateUser());
		salesInfo.setUpdateUserId(salesDetail.getUpdateUserId());
		salesInfo.setSalesName(salesDetail.getSalesName());
		salesInfo.setSalesType("2");
		salesInfo.setStartTime(salesDetail.getStartTime());
		//同新增  根据180309 版本修改
		//salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesPrice(salesDetail.getSalesPrice());
		salesInfo.setSalesStatus(salesDetail.getSalesStatus());
		salesInfo.setSalesRules(salesDetail.getSalesRules());
		salesInfo.setInterval(salesDetail.getInterval());
		salesInfo.setPlanCount(salesDetail.getPlanCount());
		
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		
		
		GsSalesLottery lottery = new GsSalesLottery();
		lottery.setSalesId(salesInfo.getSalesId());
		lottery.setRunCount(salesDetail.getRunCount());
		lottery.setWinnerCount(salesDetail.getWinnerCount());
		lottery.setRunTime(salesDetail.getStartTime());
		lottery.setUpdateUser(salesDetail.getUpdateUser());
		lottery.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.updateGsSalesLottery(lottery);
		
		//查出已经存在的排期信息
		GsSalesPlan oldPlan = gsGoodsMapper.getGsSalesPlan(salesDetail.getPlanId());
		if(null != oldPlan){
			if(oldPlan.getPlanStatus().equals("3")){ //结束后再修改
				oldPlan.setPlanStatus("0"); //继续未开始
				oldPlan.setLessCount(String.valueOf(Integer.parseInt(oldPlan.getLessCount()) +Integer.parseInt(salesDetail.getTotalCount())));
				oldPlan.setTotalCount(String.valueOf(Integer.parseInt(oldPlan.getTotalCount()) +Integer.parseInt(salesDetail.getTotalCount())));
				oldPlan.setCurCount(String.valueOf(Integer.valueOf(oldPlan.getCurCount()) +1));
				oldPlan.setStartTime(salesDetail.getStartTime());
				oldPlan.setEndTime(null);
				oldPlan.setReason("排期结束后,延期");
				
				//把活动的当前排期也顺延一期
				salesInfo.setPlanCount(String.valueOf(Integer.valueOf(oldPlan.getCurCount()) +1));
			}else{
				oldPlan.setStartTime(salesDetail.getStartTime());
				oldPlan.setLessCount(String.valueOf(Integer.parseInt(salesDetail.getTotalCount())-1));
				oldPlan.setTotalCount(salesDetail.getTotalCount());
			}
			
		}
		oldPlan.setUpdateUser(salesDetail.getUpdateUser());
		oldPlan.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.updateGsSalesPlan(oldPlan);
		
		salesInfo.setShowSeq(salesDetail.getShowSeq());
		salesInfo.setSalesCount(salesDetail.getGoodsCount());
		gsGoodsMapper.updateZmcGoodsSales(salesInfo);
		
		//TODO 由于添加的时候已经添加任务,只需要修改cron表达式的数值
		try {
			//salesStart(salesInfo, "抽奖活动开始");
			salesBeginRemind(salesInfo);
			//salesContinue(salesInfo, "抽奖活动商品延期");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

    }
	 public void batchDeleteLottery(String[] idArray){
		 gsGoodsMapper.batchDeleteLottery(idArray);
	 }
    public void updateLotteryUseGoodsInfo(GsLotteryGoodsSalesDetail salesDetail){
    	//商品信息
    	GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
    	
		goodsInfo.setGoodsId(salesDetail.getGoodsId());
		goodsInfo.setGoodsName(salesDetail.getGoodsName());
		goodsInfo.setGoodsDesc(salesDetail.getGoodsDesc());
		goodsInfo.setGoodsContent(salesDetail.getGoodsContent());
		goodsInfo.setGoodsType(salesDetail.getGoodsType());
		goodsInfo.setCostPrice(salesDetail.getCostPrice());
		goodsInfo.setOriginalPrice(salesDetail.getOriginalPrice());
		goodsInfo.setGoodsCount(salesDetail.getGoodsCount());
		
		gsGoodsMapper.updateGoodsInfo(goodsInfo);
		//库存信息
		GsGoodsStore store = new GsGoodsStore();
		store.setGoodsId(goodsInfo.getGoodsId());
		//库存-减去预定的期数
		String goodsCount = goodsInfo.getGoodsCount();
		GsSalesPlan oldPlan = gsGoodsMapper.getGsSalesPlan(salesDetail.getPlanId());
		if(null != oldPlan){
			//期数变小,库存改变
			GsSalesLottery lottery = gsGoodsMapper.getGsSalesLottery(salesDetail.getSalesId());
			int oldWinCount = 0;
			if(null != lottery){
				oldWinCount  = Integer.parseInt(lottery.getWinnerCount());
			}
			int oldTotalCount = Integer.parseInt(oldPlan.getTotalCount())*oldWinCount;//之前期数
			int curTotalCount = Integer.parseInt(salesDetail.getTotalCount())*Integer.parseInt(salesDetail.getWinnerCount());//当前期数
			int differCount = curTotalCount - oldTotalCount;
		
			int stockCount = Integer.parseInt(goodsCount)-differCount;
			store.setGoodsCount(stockCount+"");
		}else{
			String totalCount = salesDetail.getTotalCount();
			int stockCount = Integer.parseInt(goodsCount) - Integer.parseInt(totalCount);
			store.setGoodsCount(stockCount+"");
		}
		store.setUpdateUser(goodsInfo.getUpdateUser());
		store.setUpdateUserId(goodsInfo.getUpdateUserId());
		gsGoodsMapper.updateGsGoodsStore(store);
		//附件
		goodsInfo.setAnnexUrl(salesDetail.getCoverUrl());
		goodsInfo.setAnnexUrlPortrait(salesDetail.getCoverUrlPortrait());
		gsGoodsService.updateGoodsAnnex(goodsInfo);
    }
    
    public List<GsLotteryPart> getGsLotteryParts(String salesId){
    	return gsGoodsMapper.getGsLotteryParts(salesId);
    }
    
	public void salesBeginRemind(GsGoodsSalesInsertInfo salesInfo)
	{
		// 推送微信公众号信息
		MsgRemindVo remindVo = new MsgRemindVo();
		remindVo.setSalesId(salesInfo.getSalesId());
		remindVo.setSalesName(salesInfo.getSalesName());
		remindVo.setStartTime(salesInfo.getStartTime());
		remindVo.setSalesType(salesInfo.getSalesType());
		remindVo.setPlanCount(salesInfo.getPlanCount());
		String remindTime = DateUtil.dateTimeAddOrReduceMinutes(salesInfo.getStartTime(),
				-Integer.valueOf(yzSysConfig.getSalesRemindTime()));
		
		RedisService.getRedisService().zrem(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, JsonUtil.object2String(remindVo));
		RedisService.getRedisService().zadd(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET,
				DateUtil.stringToLong(remindTime, DateUtil.YYYYMMDDHHMMSS_SPLIT), JsonUtil.object2String(remindVo));

	}
}
