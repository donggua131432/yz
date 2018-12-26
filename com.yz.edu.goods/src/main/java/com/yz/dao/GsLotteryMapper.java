package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.GsLottery;
import com.yz.model.GsLotteryWinning;
import com.yz.model.GsPrize;
import com.yz.model.GsUserPrize;

public interface GsLotteryMapper {

	/**
	 * 查询抽奖活动
	 * 
	 * @param lotteryCode
	 *            活动编码
	 * @return
	 */
	GsLottery selectLotteryByLotteryCode(String lotteryCode);

	/**
	 * 插入用户抽奖券
	 * 
	 * @param userId
	 *            用户ID
	 * @param lotteryId
	 *            抽奖活动ID
	 */
	void insertLotteryTicket(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	/**
	 * 查询对应活动的用户抽奖次数
	 * 
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	int selectTicketCount(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	/**
	 * 中奖轮循信息
	 * 
	 * @param lotteryICode
	 * @return
	 */
	List<Map<String, String>> selectWinnerInfoList(String lotteryICode);

	/**
	 * 查询活动中的数量
	 * 
	 * @param lotteryId
	 * @return
	 */
	int selectLotteryCount(String lotteryId);

	List<GsPrize> selectPrizes(String lotteryId);

	GsLotteryWinning selectUserLotteryCount(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	void insertLotteryWinning(GsLotteryWinning winInfo);

	void cutPrizeStock(String prizeId);

	void insertUserPrize(GsUserPrize userPrize);

	void updateUserPrizeSend(@Param("upId") String upId, @Param("sendStatus") String sendStatus);

	/**
	 * 使用一张抽奖券
	 * 
	 * @param userId
	 * @param lotteryId
	 */
	void cutLotterTicket(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	int selectWinCount(String lotteryId);

	/**
	 * 查找iphone中奖次数
	 * 
	 * @param lotteryId
	 * @param prizeId
	 * @return
	 */
	int selectPrizeWinCount(@Param("lotteryId") String lotteryId, @Param("prizeId") String prizeId);

	/**
	 * 获取我的中奖信息
	 * 
	 * @param userId
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, String>> selectWinningInfo(@Param("userId") String userId, @Param("lotteryId") String lotteryId);

	void updateUserPrizeAddress(GsUserPrize prize);

	List<Map<String, String>> selectAllWinningInfo(String lotteryCode);

	List<String> selectUnGainTicketUserId();

}
