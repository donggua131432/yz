package com.yz.dao.recruit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.recruit.StudentRecruitQuery;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdFeeOffer;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.recruit.BdLearnRemarkLog;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.recruit.BdRecruitStudentListInfo;
import com.yz.model.recruit.BdShoppingAddress;
import com.yz.model.recruit.BdStudentAnnex;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.model.recruit.StudentCheckRecord;

public interface StudentRecruitMapper {

	/**
	 * 查询学员数量
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	int getCountBy(@Param("idType") String idType, @Param("idCard") String idCard,
			@Param("oldIdCard") String oldIdCard);

	/**
	 * 插入学员基础信息
	 * 
	 * @param baseInfo
	 * @return
	 */
	int insertStudentBaseInfo(BdStudentBaseInfo baseInfo);

	/**
	 * 初始化学业信息
	 * 
	 * @param learnInfo
	 */
	int initLearnInfo(BdLearnInfo learnInfo);

	/**
	 * 插入学员加分信息
	 * 
	 * @param recruitInfo
	 * @return
	 */
	int updateStudentBpRecords(BdStudentRecruit recruitInfo);

	/**
	 * 插入学员招生信息
	 * 
	 * @param recruitInfo
	 * @return
	 */
	int updateStudentEnrolInfo(BdStudentRecruit recruitInfo);

	/**
	 * 插入学员附件信息
	 * 
	 * @param annexInfos
	 */
	void insertStudentAnnexInfos(List<BdStudentAnnex> annexInfos);

	/**
	 * 插入学员附属信息
	 * 
	 * @param other
	 */
	void insertStudentOtherInfo(BdStudentOther other);

	/**
	 * 获取已结业学员信息
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	BdStudentBaseInfo getStudentBaseInfo(@Param("idType") String idType, @Param("idCard") String idCard);

	/**
	 * 更新学员基础信息
	 * 
	 * @param baseInfo
	 */
	int updateStudentBaseInfo(BdStudentBaseInfo baseInfo);

	/**
	 * 更新学员学历信息
	 * 
	 * @param history
	 * @return
	 */
	int updateStudentHistory(BdStudentHistory history);
	/**
	 * 插入学员学历信息
	 * @param history
	 * @return
	 */
	int insertStudentHistory(BdStudentHistory history);

	/**
	 * 查询缴费科目
	 * 
	 * @param scholarship
	 * @return
	 */
	List<BdFeeInfo> findFeeItem(String scholarship);

	/**
	 * 查询缴费项目
	 *
	 * @param scholarship
	 * @return
	 */
	BdFeeInfo findFeeInfo(@Param("feeId") String feeId, @Param("offerId") String scholarship,
			@Param("itemCode") String itemCode);

	/**
	 * 查询收费标准与优惠政策信息
	 * 
	 * @param pfsnId
	 * @param scholarship
	 * @param taId
	 * @return
	 */
	BdFeeOffer findFeeOffer(@Param("pfsnId") String pfsnId, @Param("scholarship") String scholarship,
			@Param("taId") String taId);

	/**
	 * 根据学员姓名 身份证 年级 查询学员数量
	 * 
	 * @param param
	 * @return
	 */
	int countLearnBy(Map<String, String> param);
	/**
	 * 获取学员附属信息
	 * @param stdId
	 * @return
	 */
	BdStudentOther getStudentOtherInfo(String stdId);
	/**
	 * 获取招生老师部门信息
	 * @param empId
	 * @return
	 */
	Map<String, String> getDepartmentInfo(String empId);
	/**
	 * 插入学业数据权限信息
	 * @param rulesInfo
	 * @return
	 */
	int insertLearnRules(BdLearnRules rulesInfo);
	/**
	 * 查询我的学员列表
	 * @param queryInfo
	 * @return
	 */
	List<BdRecruitStudentListInfo> findRecruitStudents(@Param("queryInfo") StudentRecruitQuery queryInfo,@Param("user") BaseUser user);
	/**
	 * 插入学员备注信息
	 * @param remarkList
	 */
	void insertLearnRemarks(@Param("remarkList")List<BdLearnRemark> remarkList);
	/**
	 * 查询业务备注状态
	 * @param param
	 * @return
	 */
	List<BdLearnRemark> findLearnRemark(Map<String, Object> param);
	/**
	 * 更新学业备注
	 * @param remarkInfo
	 */
	int updateLearnRemark(BdLearnRemark remarkInfo);

	/**
	 * 查询业务备注状态
	 * @param learnId
	 * @return
	 */
	List<BdLearnRemarkLog> findLearnRemarkLog(@Param("learnId") String learnId);
	
	/**
	 * 插入学员业务备注信息
	 */
	int insertLearnRemarkLogs(BdLearnRemarkLog remarkInfo);
	
	/**
	 * 插入地址信息
	 * @param saInfo
	 * @return
	 */
	int insertShoppingAddress(BdShoppingAddress saInfo);
	/**
	 * 初始化报读信息
	 * @param recruit
	 */
	int insertStudentEnrolInfo(BdStudentRecruit recruit);
	
	/**
	 * 插入加分信息
	 * @param recruit
	 * @return
	 */
	int insertStudentBpRecords(BdStudentRecruit recruit);
	/**
	 * 插入审核记录
	 * @param record
	 * @return
	 */
	int insertCheckRecord(StudentCheckRecord record);

	int insertCheckRecordBatch(@Param(value = "recordList") List<StudentCheckRecord> recordList);
	/**
	 * 更新附件信息
	 * @param annexInfo
	 * @return
	 */
	int updateAnnexInfo(BdStudentAnnex annexInfo);
	/**
	 * 更新学生老师信息
	 * @param learnRules
	 * @return
	 */
	int updateLearnRules(BdLearnRules learnRules);
	/**
	 * 获取学员学业信息
	 * @param stdId
	 * @return
	 */
	List<BdLearnInfo> getLearnList(String stdId);
	/**
	 * 判断学员权限是否存在
	 * @param learnId
	 * @return
	 */
	int countLearnRules(String learnId);
	/**
	 * 根据专业ID、考区ID获取收费标准类型(scholarship)
	 * @param pfsnId
	 * @param taId
	 * @return
	 */
	List<String> getScholarships(@Param("pfsnId") String pfsnId, @Param("taId") String taId);
	/**
	 * 查询学员子订单信息
	 * @param learnId
	 * @return
	 */
	List<Map<String, String>> getSubOrder(String learnId);
	
	/**
	 * 201709 国开学员信息
	 * @return
	 */
	List<Map<String, String>> getStudentInfoForSendSMS();
	
	/**
	 * 通过招生老师和学员身份证信息判断是否存在 招生关系
	 * @param idType
	 * @param idCard
	 * @param empId
	 * @return
	 */
	int getStuCountByEmpId(@Param("idType") String idType, @Param("idCard") String idCard,@Param("empId") String empId);
	
	/**
	 * 通过学员的身份证查招生老师是否在职
	 * @param idType
	 * @param idCard
	 * @return
	 */
	int getStuRecruitStatus(@Param("idType") String idType, @Param("idCard") String idCard);
	
	/**
	 * 初始化录取信息
	 * @param recruit
	 */
	int insertStudentAdmitInfo(BdStudentRecruit recruit);

	HashMap<String, String> getLearnIdByUserId(@Param("userId") String userId);
	HashMap<String, String> getFeeByLearnId(@Param("learnId") String learnId);
	String getUserIdByLearnId(@Param("learnId") String learnId);

	HashMap<String, String> getUserRelationByLearnId(@Param("learnId") String learnId);
	
	/**
	 * 某个年级未建立绑定关系的学员
	 * @return
	 */
	List<Map<String, String>> getStuUnboundUserIdInfo(@Param("grade") String grade);
	
	/**
	 * 根据手机号查询学员数量
	 * @param grade
	 * @return
	 */
	int getStudentCountByMobile(@Param("mobile") String mobile);
	
}