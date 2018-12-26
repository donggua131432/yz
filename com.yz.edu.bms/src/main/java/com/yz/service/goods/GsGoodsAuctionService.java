package com.yz.service.goods;

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
import com.yz.model.goods.GsAuctionGoodsSales;
import com.yz.model.goods.GsAuctionGoodsSalesDetail;
import com.yz.model.goods.GsAuctionPart;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsSalesInsertInfo;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsGoodsStore;
import com.yz.model.goods.GsSalesAuction;
import com.yz.model.goods.GsSalesPlan;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;
/**
 * 竞拍
 * @author lx
 * @date 2017年8月4日 下午5:17:06
 */
@Service
@Transactional
public class GsGoodsAuctionService {
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private GsGoodsService gsGoodsService;
	
	
	public IPageInfo<GsAuctionGoodsSales> getGsAuctionGoodsSales(int start, int length,GsGoodsSalesQuery salesQuery) {
		PageHelper.offsetPage(start, length);
		List<GsAuctionGoodsSales> resultList = gsGoodsMapper.getGsAuctionGoodsSales(salesQuery);
		return new IPageInfo<GsAuctionGoodsSales>((Page<GsAuctionGoodsSales>) resultList);
	}	
    
    public GsAuctionGoodsSalesDetail getGsAuctionGoodsSalesDetail(String salesId){
    	return gsGoodsMapper.getGsAuctionGoodsSalesDetail(salesId);
    }
    public Object insertAuctionGoodsSales(GsAuctionGoodsSalesDetail salesDetail){
    	//商品信息
		GsGoodsInsertInfo goodsInfo = new GsGoodsInsertInfo();
    	GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
		
    	//如果添加的时候 有goodsId 说明是继承
    	if(StringUtil.hasValue(salesDetail.getGoodsId())){
    		
    		updateAuctionUseGoosdInfo(salesDetail);
    		
    		salesInfo.setGoodsId(salesDetail.getGoodsId());
    	}else{
    		//商品信息
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
    		//减掉期数
    		String goodsCount = goodsInfo.getGoodsCount();
    		String totalCount = salesDetail.getTotalCount();
    		int stockCount = Integer.parseInt(goodsCount) - Integer.parseInt(totalCount);
    		store.setGoodsCount(stockCount+"");
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
		salesInfo.setSalesType("3");
		salesInfo.setStartTime(salesDetail.getStartTime());
		salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesRules(salesDetail.getSalesRules());
		salesInfo.setInterval(salesDetail.getInterval());
		salesInfo.setSalesStatus(salesDetail.getSalesStatus());
		
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		salesInfo.setShowSeq(salesInfo.getShowSeq()==null?"0":salesInfo.getShowSeq());
		salesInfo.setSalesId(IDGenerator.generatorId());
		gsGoodsMapper.addZmcGoodsSales(salesInfo);
		
		GsSalesAuction auction = new GsSalesAuction();
		auction.setSalesId(salesInfo.getSalesId());
		auction.setUpsetPrice(salesDetail.getUpsetPrice());
		auction.setAuctionCount("0");
		auction.setCurPrice(salesDetail.getUpsetPrice());
		auction.setRaisePrice(salesDetail.getRaisePrice());
		auction.setUpdateUser(salesDetail.getUpdateUser());
		auction.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.addGsSalesAuction(auction);
		
		GsSalesPlan plan = new GsSalesPlan();
		plan.setCurCount("1");
		plan.setLessCount(String.valueOf((Integer.parseInt(salesDetail.getTotalCount())-1)));
		plan.setPlanStatus("0");
		plan.setStartTime(salesInfo.getStartTime());
		plan.setTotalCount(salesDetail.getTotalCount());
		plan.setUpdateUser(salesDetail.getUpdateUser());
		plan.setUpdateUserId(salesDetail.getUpdateUserId());
		plan.setPlanId(IDGenerator.generatorId());
		gsGoodsMapper.addGsSalesPlan(plan);
		
		//更新兑换活动商品 的排期信息
		salesInfo.setIsPlan("1");
		salesInfo.setPlanCount("1");
		salesInfo.setPlanId(plan.getPlanId());
		gsGoodsMapper.updateZmcGoodsSales(salesInfo);
    	try {
    		//开始
    		//salesStart(salesInfo, "竞拍活动开始");
    		//定时提醒任务
    		salesBeginRemind(salesInfo);//新增
    		//定时延期任务
    		//salesContinue(salesInfo,"竞拍活动商品<"+salesInfo.getSalesName()+">的延期");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
    
    public void updateAuctionGoodsSales(GsAuctionGoodsSalesDetail salesDetail){
    	//商品信息
		updateAuctionUseGoosdInfo(salesDetail);
		
		GsGoodsSalesInsertInfo salesInfo = new GsGoodsSalesInsertInfo();
		salesInfo.setSalesId(salesDetail.getSalesId());
		salesInfo.setGoodsId(salesDetail.getGoodsId());
		salesInfo.setUpdateUser(salesDetail.getUpdateUser());
		salesInfo.setUpdateUserId(salesDetail.getUpdateUserId());
		salesInfo.setSalesName(salesDetail.getSalesName());
		salesInfo.setStartTime(salesDetail.getStartTime());
		salesInfo.setEndTime(DateUtil.dateTimeAddOrReduceDays(salesDetail.getStartTime(), Integer.parseInt(salesDetail.getInterval())));
		salesInfo.setShowAfterOver(salesDetail.getShowAfterOver());
		salesInfo.setSalesRules(salesDetail.getSalesRules());
		if(StringUtil.isEmpty(salesInfo.getStartTime())){
			salesInfo.setStartTime(null);
		}
		if(StringUtil.isEmpty(salesInfo.getEndTime())){
			salesInfo.setEndTime(null);
		}
		
		GsSalesAuction auction = new GsSalesAuction();
		auction.setSalesId(salesInfo.getSalesId());
		auction.setUpsetPrice(salesDetail.getUpsetPrice());
		auction.setRaisePrice(salesDetail.getRaisePrice());
		auction.setUpdateUser(salesDetail.getUpdateUser());
		auction.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.updateGsSalesAuction(auction);
		
		//TODO 修改的时候需要加入逻辑判断
		
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
				salesInfo.setPlanCount("1");
			}
			
		}
		salesInfo.setSalesType("3");
		oldPlan.setUpdateUser(salesDetail.getUpdateUser());
		oldPlan.setUpdateUserId(salesDetail.getUpdateUserId());
		gsGoodsMapper.updateGsSalesPlan(oldPlan);
		
		gsGoodsMapper.updateZmcGoodsSales(salesInfo);
		try { //定时有异常时不影响别的操作
			//开始
			//salesStart(salesInfo, "竞拍活动开始");
			//定时提醒任务
			salesBeginRemind(salesInfo);//新增
			//定时延期任务
			//salesContinue(salesInfo,"竞拍活动商品<"+salesInfo.getSalesName()+">的延期");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
    }
    public void batchDeleteAuction(String[] idArray){
    	gsGoodsMapper.batchDeleteAuction(idArray);
    }

    public void updateAuctionUseGoosdInfo(GsAuctionGoodsSalesDetail salesDetail){
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
			int oldTotalCount = Integer.parseInt(oldPlan.getTotalCount());//之前期数
			int curTotalCount = Integer.parseInt(salesDetail.getTotalCount());//当前期数
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
    
    public List<GsAuctionPart> getGsAuctionParts(String salesId){
    	return gsGoodsMapper.getGsAuctionParts(salesId);
    }
    public void salesBeginRemind(GsGoodsSalesInsertInfo salesInfo)
	{
    	//推送微信公众号信息
		MsgRemindVo remindVo = new MsgRemindVo();
		remindVo.setSalesId(salesInfo.getSalesId());
		remindVo.setSalesName(salesInfo.getSalesName());
		remindVo.setStartTime(salesInfo.getStartTime());
		remindVo.setSalesType(salesInfo.getSalesType());
		remindVo.setPlanCount(salesInfo.getPlanCount());
		String remindTime = DateUtil.dateTimeAddOrReduceMinutes(salesInfo.getStartTime(),-Integer.valueOf(yzSysConfig.getSalesRemindTime()));
		
		RedisService.getRedisService().zadd(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, DateUtil.stringToLong(remindTime, DateUtil.YYYYMMDDHHMMSS_SPLIT),JsonUtil.object2String(remindVo));
		
	}
    //TODO 竞拍的开始和结束暂时先不处理
	/*public void salesStart(GsGoodsSalesInsertInfo salesInfo, String desc)
	{
    	//推送微信公众号信息
		MsgRemindVo remindVo = new MsgRemindVo();
		remindVo.setSalesId(salesInfo.getSalesId());
		remindVo.setSalesName(salesInfo.getSalesName());
		remindVo.setStartTime(salesInfo.getStartTime());
		remindVo.setSalesType(salesInfo.getSalesType());
		remindVo.setPlanCount(salesInfo.getPlanCount());
		String remindTime = salesInfo.getStartTime();

		RedisService.getRedisService().zadd(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET,
				DateUtil.stringToLong(remindTime, DateUtil.YYYYMMDDHHMMSS_SPLIT), JsonUtil.object2String(remindVo));

	}
	public void salesContinue(GsGoodsSalesInsertInfo salesInfo, String desc){
    	MsgRemindVo remindVo = new MsgRemindVo();
		remindVo.setSalesId(salesInfo.getSalesId());
		remindVo.setSalesName(salesInfo.getSalesName());
		remindVo.setStartTime(salesInfo.getStartTime());
		remindVo.setSalesType(salesInfo.getSalesType());
		remindVo.setPlanCount(salesInfo.getPlanCount());
		String remindTime = salesInfo.getEndTime();

		RedisService.getRedisService().zadd(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET,
				DateUtil.stringToLong(remindTime, DateUtil.YYYYMMDDHHMMSS_SPLIT), JsonUtil.object2String(remindVo));
	}*/
}
