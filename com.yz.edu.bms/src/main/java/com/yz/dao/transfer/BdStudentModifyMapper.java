package com.yz.dao.transfer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.BdStudentRollModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.model.transfer.StudentModifyQuery;
import com.yz.model.transfer.StudentRollModifyMap;

public interface BdStudentModifyMapper {
	int deleteByPrimaryKey(String modifyId);

	int insert(BdStudentModify record);

	int insertSelective(BdStudentModify record);

	BdStudentModify selectByPrimaryKey(String modifyId);

	int updateByPrimaryKeySelective(BdStudentModify record);

	int updateByPrimaryKey(BdStudentModify record);

	List<Map<String, String>> findStudentModify(@Param("modi") StudentModifyMap studentModifyMap,
			@Param("user") BaseUser user);

	BdStudentModify findStudentModifyById(String modifyId);

	List<Map<String, String>> findStudentInfo(@Param("sName") String sName, @Param("stauts") String[] stauts,
			@Param("user") BaseUser user);

	Map<String, String> findStudentEnrollInfo(String learnId);

	void deleteStudentModify(@Param("ids") String[] ids);

	List<Map<String, String>> findStudentAuditModify(StudentModifyMap studentModifyMap);

	void updateBdStudentEnroll(BdStudentModify bdStudentModify);

	void updateBdLearnInfo(BdStudentModify bdStudentModify);

	void updateBdStudentInfo(BdStudentModify bdStudentModify);

	List<StudentRollModifyMap> findStudentRollModify(StudentModifyMap studentModifyMap);

	BdStudentRollModify findStudentRollModifyById(String modifyId);

	List<StudentRollModifyMap> findStudentAuditRollModify(StudentModifyMap studentModifyMap);

	String selectFeeStandard(@Param("pfsnId") String pfsnId, @Param("taId") String taId,
			@Param("scholarship") String scholarship);

	String selectNowFeeId(String learnId);

	String selectOfferId(@Param("pfsnId") String pfsnId, @Param("taId") String taId,
			@Param("scholarship") String scholarship);

	String selectOfferTutorAmount(String offerId);

	void updateStdStageByLearnId(String learnId);

	int updateStdStageBack(@Param("learnId") String learnId, @Param("stdStage") String stdStage);

	String selectRecruitEmpId(String oldLearnId);

	int updateBdOrderInfo(String learnId);

	String selectPaidAmountByLearnId(String learnId);

	public void updateStdOrderStatusByLearnId(String learnId);

	List<StudentRollModifyMap> findStudentRollModifyNew(@Param("query") StudentModifyQuery query,
			@Param("user") BaseUser user);

	BdStudentRollModify findStudentRollModifyNewById(String modifyId);

	List<Map<String, String>> findStudentInfoNew(@Param("sName") String sName,@Param("user") BaseUser user);

	int countYMStudent(@Param("learnId") String learnId);

	/**
	 * 查询退款金额/智米
	 * 
	 * @param learnId
	 * @param accType
	 * @return
	 */
	Map<String, Object> getAccAmount(@Param("learnId") String learnId, @Param("accType") String accType);

	/**
	 * 获取已缴费的优惠券ID
	 * 
	 * @param learnId
	 * @return
	 */
	List<String> getCouponIds(@Param("learnId") String learnId);

	/**
	 * 修改优惠券状态
	 * 
	 * @param stdId
	 * @param couponIds
	 * @return
	 */
	int updateCouponUse(@Param("stdId") String stdId, @Param("couponIds") List<String> couponIds);

	/**
	 * 修改老订单状态为已退款或已废弃
	 * 
	 * @param learnId
	 * @return
	 */
	int updateOrderStatus(@Param("learnId") String learnId);

	int countFinish(@Param("learnId") String learnId);

	int countNewPfsnFinish(@Param("learnId") String learnId);

	/**
	 * 查询是否已完成
	 * 
	 * @param modifyId
	 * @return
	 */
	int selectModifyFinishCount(String modifyId);

	/**
	 * 废弃订单
	 * 
	 * @param learnId
	 */
	void destroyOrderByLearnId(String learnId);

	/**
	 * 查询考试科目
	 * 
	 * @param pfsnId
	 * @return
	 */
	List<String> selectTestSubjectByPfsnId(String pfsnId);

	/**
	 * 
	 * @param oldPfsnId
	 * @param newPfsnId
	 * @return
	 */
	List<String> selectUnSameTestSubjectByPfsnId(@Param("oldPfsnId") String oldPfsnId,
			@Param("newPfsnId") String newPfsnId);
}