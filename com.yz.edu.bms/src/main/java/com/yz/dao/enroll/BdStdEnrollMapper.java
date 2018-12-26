package com.yz.dao.enroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.enroll.BdInclusionStatusImport;
import com.yz.model.enroll.stdenroll.BdStdAdmit;
import com.yz.model.enroll.stdenroll.BdStdEnroll;
import com.yz.model.enroll.stdenroll.BdStdEnrollImport;
import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;
import com.yz.model.enroll.stdenroll.BdStdEnrolled;
import com.yz.model.message.GwMsgStudentQuery;
import com.yz.model.message.GwReceiver;
import com.yz.model.message.GwStdInfo;
import com.yz.model.recruit.BdLearnInfo;

public interface BdStdEnrollMapper {

	List<BdStdEnroll> selectStdEnrollByPage(BdStdEnrollQuery enroll);
	
	List<BdStdEnroll> selectBatchAdmitResult(BdStdEnrollQuery enroll);

	BdStdEnroll selectStdEnrollInfoByLearnId(String learnId);

	List<BdStdEnrolled> selectStdEnrolledByPage(BdStdEnrollQuery enroll);

	BdLearnInfo selectLearnInfoByLearnId(String learnId);

	int insertBdAdmit(BdStdAdmit admit);

	String[] selectPaidItem(String learnId);

	int selectEnrolledInfoExist(@Param("learnId") String learnId, @Param("unvsId") String unvsId,
			@Param("pfsnId") String pfsnId, @Param("taId") String taId, @Param("scholarship") String scholarship);

	int deleteRelationSubOrderAndAdmitInfo(String learnId);

	BdStdEnrollImport selectLearnId(BdStdEnrollImport enroll);

	String selectStdScore(String learnId);

	void updateLearnInfo(BdLearnInfo learn);

	GwReceiver selectStdInfo(GwReceiver std);

	Map<String, String> selectStdLearnInfo(String learnId);

	GwReceiver selectStdInfoByLearnId(String learnId);

	ArrayList<String> selectMtpIdByLearnId(String learnId);

	List<GwStdInfo> queryMsgStudentInfo(@Param("query") GwMsgStudentQuery studentQuery, @Param("user") BaseUser user);

	String selectFeeIdByLearnId(String learnId);

	String selectOfferIdByLearnId(@Param("learnId") String learnId, @Param("tutorPaidTime") String tutorPaidTime,
			@Param("inclusionStatus") String inclusionStatus);

	int selectSubjectOrderCount(String learnId);

	int selectSubjectPaidCount(String learnId);

	String selectSubjectPaidAmount(String learnId);

	List<String> selectSUbjectPaidItemCode(String learnId);

	String selectstdIdByLearnId(String learnId);

	/**
	 * 修改学科子订单状态
	 * 
	 * @param learnId
	 * @param newOrderStatus
	 *            新的状态
	 * @param oldOrderStatus
	 *            旧的状态
	 * @return
	 */
	int updateSubjectOrderStatus(@Param("learnId") String learnId, @Param("newOrderStatus") String newOrderStatus,
			@Param("oldOrderStatus") String oldOrderStatus);

	String selectTutorPaidTime(String learnId);

	int updateInclusionStatus(@Param("learnId") String learnId, @Param("inclusionStatus") String inclusionStatus);

	List<String> selectLearnIdByInclusion(BdInclusionStatusImport in);

	String selectScholarshipByLearnId(String learnId);

	int selectResetOrderCount(String learnId);

	int insertResetOrderInfo(@Param("learnId") String learnId, @Param("stdName") String stdName,
			@Param("idCard") String idCard);

	int insertResetOrderError(@Param("id") String id,@Param("learnId") String learnId, @Param("stdName") String stdName,
			@Param("idCard") String idCard, @Param("grade") String grade, @Param("errorMsg") String errorMsg);

	String selectInclusionStatus(String learnId);

	int deleteTestHelpCoupon(String learnId);

	// 修改学业所属校区
	public void updateStuCampusId(@Param("learnId") String learnId, @Param("campusId") String campusId,
			@Param("user") BaseUser user);

	// 根据条件查询已录取学员信息
	List<Map<String, String>> selectNeedOperStdByPage(BdStdEnrollQuery enroll);

	// 获取所有的分配校区
	public List<Map<String, String>> getHomeCampusInfo(@Param("campusStatus") String campusStatus);

	// 根据选择分配所属校区
	public void selectAllocation(@Param("ids") String[] ids, @Param("campusId") String campusId,
			@Param("user") BaseUser user);

	List<BdInclusionStatusImport> selectEnrolledLearnId(@Param("unvsId") String unvsId,
			@Param("scholarships") String[] scholarships);

	List<BdInclusionStatusImport> selectEnrolledLearnIdGuangbo(String unvsId);

	String selectLearnIdByIdCard(@Param("idCard") String idCard, @Param("unvsId") String unvsId,
			@Param("grade") String grade);

	String selectLearnIdByExamNo(String examNo);

	void deleteResetOrcerInfo(String learnId);

	int selectResetOrderErrorCount(String idCard);

	String selectTaIdByTaName(String taName);

	void updateAdmitTaId(@Param("learnId") String learnId, @Param("taId") String taId);

	void updateScholarship(@Param("learnId") String learnId, @Param("scholarship") String scholarship);

	String[] queryMsgLearnIds(@Param("query") GwMsgStudentQuery studentQuery, @Param("user") BaseUser user);

}
