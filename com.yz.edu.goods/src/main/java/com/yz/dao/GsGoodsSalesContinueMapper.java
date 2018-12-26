package com.yz.dao;

import com.yz.model.GsAuctionPart;
import com.yz.model.GsGoodsOrderInfo;
import com.yz.model.GsLotteryPart;
import com.yz.model.GsSalesLottery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抽奖、竞拍延期
 * @author lx
 * @date 2017年8月24日 下午12:25:20
 */
public interface GsGoodsSalesContinueMapper{

	/**
	 * 获取延期活动商品的信息
	 * @param salesId
	 * @return
	 */
	GsGoodsOrderInfo getGsGoodsOrderInfoById(@Param("salesId") String salesId);
	
	/**
	 * 获取抽奖活动的信息
	 * @param salesId
	 * @return
	 */
	GsSalesLottery getGsSalesLotteryById(@Param("salesId") String salesId);
	
	/**
	 * 随机抽取某个活动的某期的指定个数幸运用户
	 * @param salesId
	 * @param count
	 * @param planCount
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
	 * 最新的出价信息记录
	 * @param salesId
	 * @return
	 */
	GsAuctionPart getLastAuctionLog(@Param("salesId") String salesId, @Param("planCount") String planCount);

	/**
	 * 未中拍的人员名单
	 * @param salesId
	 * @param planCount
	 * @param userId
	 * @return
	 */
	List<GsAuctionPart> getNoAuctionList(@Param("salesId") String salesId, @Param("planCount") String planCount, @Param("userId") String userId);

	/**
	 * 修改中拍人的中拍状态
	 * @param salesId
	 * @param userId
	 * @param planCount
	 */
	void updateUserMineStatus(@Param("salesId") String salesId, @Param("userId") String userId, @Param("planCount") String planCount);

	/**
	 * 修改活动排期的信息
	 * @param salesInfo
	 */
	void updateGsSalesPlanCount(GsGoodsOrderInfo salesInfo);
}
