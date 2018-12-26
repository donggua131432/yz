package com.yz.dao.goods;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.goods.GsActivitiesGoods;
import com.yz.model.goods.GsAuctionGoodsSales;
import com.yz.model.goods.GsAuctionGoodsSalesDetail;
import com.yz.model.goods.GsAuctionPart;
import com.yz.model.goods.GsCourseGoods;
import com.yz.model.goods.GsGoodsAnnex;
import com.yz.model.goods.GsGoodsInsertInfo;
import com.yz.model.goods.GsGoodsQueryInfo;
import com.yz.model.goods.GsGoodsShowInfo;
import com.yz.model.goods.GsGoodsStore;
import com.yz.model.goods.GsSalesExchange;
import com.yz.model.goods.GsSalesLottery;
import com.yz.model.goods.GsSalesPlan;
import com.yz.model.goods.GsExchangeGoodsSales;
import com.yz.model.goods.GsExchangeGoodsSalesDetail;
import com.yz.model.goods.GsGoodsSalesInsertInfo;
import com.yz.model.goods.GsGoodsSalesQuery;
import com.yz.model.goods.GsLotteryGoodsSales;
import com.yz.model.goods.GsLotteryGoodsSalesDetail;
import com.yz.model.goods.GsLotteryPart;
import com.yz.model.goods.GsSalesAuction;


public interface GsGoodsMapper {

	public List<GsGoodsShowInfo> getGsGoodsShowInfo(GsGoodsQueryInfo queryInfo);
	
	public void addNewGoodsInfo(GsGoodsInsertInfo goodsInfo);
	
	public void addNewGsGoodsStore(GsGoodsStore store);
	
	public void addZmcGoodsSales(GsGoodsSalesInsertInfo salesInfo);
	
	public void addGsSalesExchange(GsSalesExchange exchange);
	
	public void addGsGoodsAnnex(GsGoodsAnnex annex);
	
	public GsGoodsInsertInfo getGoodsDetailInfo(@Param("goodsId") String goodsId);
	
	public GsGoodsSalesInsertInfo getZmcGoodsSalesInsertInfo(@Param("goodsId") String goodsId);
	
	public void updateGoodsInfo(GsGoodsInsertInfo goodsInfo);
	
	public void updateGsGoodsStore(GsGoodsStore store);
	
	public void updateZmcGoodsSales(GsGoodsSalesInsertInfo salesInfo);
	
	public void updateGsSalesExchange(GsSalesExchange exchange);
	
	public void updateGsGoodsAnnex(GsGoodsAnnex annex);
	
	public void addGsActivitiesGoods(GsActivitiesGoods goods);
	
	public void addGsCourseGoods(GsCourseGoods goods);
	
	public void updateGsActivitiesGoods(GsActivitiesGoods goods);
	
	public void updateGsCourseGoods(GsCourseGoods goods);
	
	public GsGoodsInsertInfo getActivityGoodsDetailInfo(@Param("goodsId") String goodsId);
	
	public GsGoodsInsertInfo getCourseGoodsDetailInfo(@Param("goodsId") String goodsId);
	
	public List<GsExchangeGoodsSales> getZmcExchangeGoodsSales(GsGoodsSalesQuery salesQuery);
	
	public List<Map<String, String>> findAllGoodsByType(String goodsType);
	
	public GsExchangeGoodsSalesDetail getZmcExchangeGoodsSalesDetail(String salesId);
	
	public List<GsLotteryGoodsSales> getZmcLotteryGoodsSales(GsGoodsSalesQuery salesQuery);
	
	public GsLotteryGoodsSalesDetail getZmcLotteryGoodsSalesDetail(String salesId);
	
	public void addGsSalesLottery(GsSalesLottery lottery);
	
	public void addGsSalesPlan(GsSalesPlan plan);
	
	public void updateGsSalesLottery(GsSalesLottery lottery);
	
	public void updateGsSalesPlan(GsSalesPlan plan);
	
	public void addGsSalesAuction(GsSalesAuction auction);
	
	public void updateGsSalesAuction(GsSalesAuction auction);
	
	public GsAuctionGoodsSalesDetail getGsAuctionGoodsSalesDetail(String salesId);
	
	public List<GsAuctionGoodsSales> getGsAuctionGoodsSales(GsGoodsSalesQuery salesQuery);
	
	public void batchStopGoods(@Param("ids") String[] idArray);
	
	public void batchStartGoods(@Param("ids") String[] idArray);
	
	public void batchDeleteExchange(@Param("ids") String[] idArray,@Param("goodsIds") String[] goodsIds);
	
	public void batchDeleteLottery(@Param("ids") String[] idArray);
	
	public void batchDeleteAuction(@Param("ids") String[] idArray);
	
	public void initGoodsAnnexImgs(List<GsGoodsAnnex> list);
	
	public void deleteGoodsAnnex(String goodsId);
	
	public GsCourseGoods getGsCourseGoods(String goodsId);
	
	public GsActivitiesGoods getGsActivitiesGoods(String goodsId);

	public List<GsLotteryPart> getGsLotteryParts(String salesId);
	
	public GsSalesPlan getGsSalesPlan(String planId);
	
	public List<GsAuctionPart> getGsAuctionParts(String salesId);
	
	
	/**
	 * 库存
	 * @param goodsId
	 * @return
	 */
	public int getGoodsCountById(@Param("goodsId") String goodsId);
	
	/**
	 * 修改库存
	 * @param count
	 * @param goodsId
	 */
	public void updateGoodsCount(@Param("count") int count,@Param("goodsId") String goodsId);
	
	/**
	 * 抽奖
	 * @param salesId
	 * @return
	 */
	public GsSalesLottery getGsSalesLottery(@Param("salesId") String salesId);
	
}
