package com.yz.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.yz.model.GsAuctionPart;
import com.yz.model.GsAuctionPartInsertInfo;
import com.yz.model.GsExchangePartInsertInfo;
import com.yz.model.GsGoodsCommentInsertInfo;
import com.yz.model.GsGoodsOrderInfo;
import com.yz.model.GsLotteryPart;
import com.yz.model.GsSalesAuction;
import com.yz.model.GsSalesLottery;
import com.yz.model.GsSalesNotify;
import com.yz.model.GsSalesPlan;
import com.yz.model.ZmcGoodsAuctionInfo;
import com.yz.model.ZmcGoodsExChangeInfo;
import com.yz.model.ZmcGoodsLotteryInfo;
import com.yz.model.ZmcGoodsSalesInfo;


/**
 * 活动商品
 * @author lx
 * @date 2017年7月24日 下午12:16:14
 */
public interface ZmcGoodsSalesMapper {
  
	List<ZmcGoodsSalesInfo> getZmcGoodsSalesInfo(@Param("salesType") String salesType, @Param("goodsType") String goodsType);

	ZmcGoodsExChangeInfo getZmcGoodsExChangeInfo(@Param("salesId") String salesId);

	int selectExchangeCount(@Param("salesId") String salesId, @Param("userId") String userId);

	ZmcGoodsAuctionInfo getZmcGoodsAuctionInfo(@Param("salesId") String salesId);

	ZmcGoodsLotteryInfo getZmcGoodsLotteryInfo(@Param("salesId") String salesId);

	void insertGoodsComment(GsGoodsCommentInsertInfo insertInfo);

	void addNewSalesNotify(GsSalesNotify notify);

	void addGsAuctionPart(GsAuctionPartInsertInfo partInfo);

	void addGsLotteryPart(GsLotteryPart partInfo);

	GsGoodsOrderInfo getGsGoodsOrderInfoById(@Param("salesId") String salesId);

	int getGoodsCountById(@Param("goodsId") String goodsId);

	void updateGoodsCount(@Param("count") int count, @Param("goodsId") String goodsId);

	void addGsExchangePart(GsExchangePartInsertInfo partInfo);

	GsSalesNotify getSalesNotifyLog(@Param("salesId") String salesId, @Param("userId") String userId);

	GsLotteryPart getGsLotteryPart(@Param("salesId") String salesId, @Param("userId") String userId, @Param("planCount") String planCount);

	/**
	 * 最新的出价信息记录
	 * @param salesId
	 * @return
	 */
	GsAuctionPart getLastAuctionLog(@Param("salesId") String salesId, @Param("planCount") String planCount);
	/**
	 * 修改最新的出价信息
	 * @param auction
	 */
	void updateGsSalesAuction(GsSalesAuction auction);

	/**
	 * 获取某个活动的抽奖规则
	 * @param salesId
	 * @return
	 */
	GsSalesLottery getGsSalesLotteryById(@Param("salesId") String salesId);

	/**
	 * 某个活动的抽奖人数
	 * @param salesId
	 * @return
	 */
	int getGsLotteryPartCount(@Param("salesId") String salesId, @Param("planCount") String planCount);

	/**
	 * 随机获取参与抽奖的幸运用户  作为中奖用户
	 * @param salesId
	 * @param count
	 * @return
	 */
	List<GsLotteryPart> getLuckyUserInfo(@Param("salesId") String salesId, @Param("count") int count, @Param("planCount") String planCount);

	/**
	 * 修改选中的幸运用户中奖状态
	 * @param salesId
	 * @param userId
	 */
	void updateUserWinStatus(@Param("salesId") String salesId, @Param("userId") String userId, @Param("planCount") String planCount);

	/**
	 * 活动排期
	 * @param planId
	 * @return
	 */
	GsSalesPlan getGsSalesPalnById(@Param("planId") String planId);

	/**
	 * 修改活动排期的信息
	 * @param salesInfo
	 */
	void updateGsSalesPlanCount(GsGoodsOrderInfo salesInfo);

	/**
	 * 修改排期信息
	 * @param plan
	 */
	void updateGsSalesPlan(GsSalesPlan plan);

	/**
	 * 修改中拍人的中拍状态
	 * @param salesId
	 * @param userId
	 * @param planCount
	 */
	void updateUserMineStatus(@Param("salesId") String salesId, @Param("userId") String userId, @Param("planCount") String planCount);

	/**
	 * 修改状态
	 * @param salesId
	 */
	void updateGoodsSalesStatus(@Param("salesId") String salesId);

	/**
	 * 获取某个活动的可兑换数量
	 * @param salesId
	 * @return
	 */
	int getSalesCountById(@Param("salesId") String salesId);

	/**
	 * 修改某个活动的可兑换数量
	 * @param count
	 * @param salesId
	 */
	void updateSalesCount(@Param("count") int count, @Param("salesId") String salesId);
	
	/**
	 * 获取一个主键
	 * @return
	 */
	public String getSeq();
}
