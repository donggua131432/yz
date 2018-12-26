package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.review.BdFeeReview;
import com.yz.model.finance.review.BdFeeReviewExport;
import com.yz.model.finance.review.BdFeeReviewQuery;
import com.yz.model.finance.statistics.BdFeeStatisticsQuery;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;

public interface BdFeeReviewMapper {

	List<BdFeeReview> selectFeeReviewByPage(BdFeeReviewQuery feeReview);

	int reviewSerial(@Param("serialNos") String[] serialNos, @Param("userId") String userId,
			@Param("realName") String realName);

	int rollBackSerialReviews(@Param("serialNos") String[] serialNos, @Param("userId") String userId,
			@Param("realName") String realName);

	String selectCountAmount(BdFeeReviewQuery feeReview);

	String selectLearnIdBySerialNo(@Param("serialNo") String serialNo,@Param("serialStatus") String serialStatus);

	String[] selectItemCodesBySeriaoNo(String serialNo);

	String[] selectItemYearsByItemCodes(@Param("itemCodes") String[] itemCodes);

	List<BdFeeReviewExport> selectFeeStatisticsExport(BdFeeStatisticsQuery query);
	/**
	 * 查询实缴金额
	 * @param serialNo
	 * @return
	 */
	String selectPayable(String serialNo);
	/**
	 * 查询该流水是否进行过撤销操作
	 * @param serialNo
	 * @return
	 */
	String isRepeal(String serialNo);

	List<BdFeeReview> selectFeeReviewExport(BdFeeReviewQuery feeReview);
	
	List<BdStdPayInfoResponse> selectPaidInfo(String serialNo);

	List<BdFeeReviewExport> selectEnrollFeeStatisticsExport(String unvsId);

	List<BdFeeReviewExport> selectUnPaidStudent(String grade);

	List<Map<String, String>> selectHomeCampus();

	String selectUserIdByStdId(String stdId);

}