package com.yz.dao.recruit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.recruit.AllStudentQuery;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdFeeOffer;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentEnroll;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.model.recruit.RecruitEmployeeInfo;
import com.yz.model.recruit.StudentAllListInfo;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.model.recruit.StudentFeeListInfo;
import com.yz.model.recruit.StudentModify;

public interface StudentAllMapper {
	/**
	 * 获取全部学员列表
	 * 
	 * @param queryInfo
	 * @return
	 */
	List<StudentAllListInfo> findAllStudent(AllStudentQuery queryInfo);

	/**
	 * 查询学员基本信息
	 * 
	 * @param stdId
	 * @return
	 */
	BdStudentBaseInfo getStudentBaseInfo(String stdId);

	/**
	 * 查询学员附属信息
	 * 
	 * @param stdId
	 * @return
	 */
	BdStudentOther getStudentOther(String stdId);

	/**
	 * 更新基础信息
	 * 
	 * @param baseInfo
	 */
	void updateBaseInfo(BdStudentBaseInfo baseInfo);

	/**
	 * 更新附属信息
	 * 
	 * @param other
	 */
	void updateOtherInfo(BdStudentOther other);

	/**
	 * 更新学历信息
	 * 
	 * @param history
	 */
	void updateHistory(BdStudentHistory history);

	/**
	 * 更新报读信息
	 * 
	 * @param recruitInfo
	 */
	void updateRecruit(BdStudentRecruit recruitInfo);

	/**
	 * 查询学历信息
	 * 
	 * @param learnId
	 * @return
	 */
	BdStudentHistory getStudentHistory(String learnId);

	/**
	 * 获取报读信息
	 * 
	 * @param learnId
	 * @return
	 */
	BdStudentEnroll getStudentEnroll(String learnId);

	/**
	 * 查询学员收费信息
	 * 
	 * @param learnId
	 * @return
	 */
	BdFeeOffer findFeeOffer(String learnId);

	/**
	 * 查询学员收费列表
	 * 
	 * @param feeInfo
	 * @return
	 */
	List<BdFeeInfo> findFeeItem(BdFeeOffer feeInfo);

	/**
	 * 获取报读招生信息
	 * 
	 * @param learnId
	 * @return
	 */
	RecruitEmployeeInfo getRecruitEmpInfo(String learnId);

	/**
	 * 查询学员流水
	 * 
	 * @param learnId
	 * @return
	 */
	List<StudentFeeListInfo> getFeeList(String learnId);

	/**
	 * 查询变更记录
	 * 
	 * @param map
	 * @return
	 */
	List<StudentModify> getModifyList(Map map);

	/**
	 * 
	 * @param learnId
	 * @return
	 */
	List<StudentCheckRecord> getCheckDataStatus(String learnId);
	/**
	 * 
	 * @param learnId
	 * @return
	 */
	List<StudentCheckRecord> getGraduateStatus(String learnId);

	/**
	 * 根据学业ID获取学员基本信息
	 * 
	 * @param learnId
	 * @return
	 */
	BdStudentBaseInfo getStudentBaseInfoByLearnId(String learnId);

	/**
	 * 更新资料完善状态
	 * 
	 * @param learnInfo
	 * @return
	 */
	int updateIsDataCompleted(BdLearnInfo learnInfo);

	/**
	 * 查询用户ID
	 * 
	 * @param stdId
	 *            学员ID
	 * @return
	 */
	String selectUserIdByStdId(String stdId);

	/**
	 * 查询用户ID
	 * 
	 * @param learnId
	 *            学业ID
	 * @return
	 */
	String selectUserIdByLearnId(String learnId);

	/**
	 * 查询该学员的招生老师是当前用户
	 * 
	 * @param stdId
	 * @param user
	 * @return
	 */
	int isMine(@Param("stdId") String stdId, @Param("user") BaseUser user);

	/**
	 * 查询学员的招生老师信息
	 * 
	 * @param learnId
	 * @return
	 */
	Map<String, String> getRecruitInfo(String learnId);

	/**
	 * 查询报读信息
	 * 
	 * @param learnId
	 * @return
	 */
	Map<String, String> getEnrollInfo(String learnId);

	/**
	 * 查询学业信息
	 * 
	 * @param oldLearnId
	 * @return
	 */
	Map<String, String> getLearnInfo(String oldLearnId);

	/**
	 * 查询目标年级是否有未完成学业
	 * 
	 * @param oldLearnId
	 * @param grade
	 * @return
	 */
	int selectLearnInfoCount(@Param("stdId") String stdId, @Param("grade") String grade);

	/**
	 * 学员关联的招生老师是否还在职
	 * @param stdId
	 * @return
	 */
	int getStuRecruitStatus(@Param("stdId") String stdId);

	/**
	 * 修改报读信息
	 * @param recruitInfo
	 * @return
	 */
	int updateBdStudentEnroll(BdStudentRecruit recruitInfo);

	/**
	 * 修改学业信息
	 * @param recruitInfo
	 * @return
	 */
	int updateBdLearnInfo(BdStudentRecruit recruitInfo);

	/**
	 * 通过learnId查找stdId
	 * @param learnId
	 * @return
	 */
	String selectStdIdByLearnId(String learnId);

	/**
	 * 查找学业中是否有意向学员
	 * @param stdId
	 * @return
	 */
	int selectIsSuppose(String stdId);
}
