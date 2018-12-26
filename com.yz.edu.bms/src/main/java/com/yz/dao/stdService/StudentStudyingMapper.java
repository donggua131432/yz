package com.yz.dao.stdService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.stdService.StudentStudyingQuery;
import com.yz.model.stdService.StudentStudyingListInfo;
import com.yz.model.stdService.StudyingContacts;
import com.yz.model.stdService.StudyingContactsShow;
import com.yz.model.stdService.StudyingPaymentInfo;

public interface StudentStudyingMapper {
	/**
	 * 获取在读学员列表
	 * @param queryInfo
	 * @param user 
	 * @return
	 */
	List<StudentStudyingListInfo> getStudyingList(@Param("queryInfo") StudentStudyingQuery queryInfo, @Param("user") BaseUser user);
	
	/**
	 * 废弃方法，使用下面的getStudyingListJoinAccNew方法代替
	 * @param queryInfo
	 * @param user
	 * @return
	 */
	List<StudentStudyingListInfo> getStudyingListJoinAcc(@Param("queryInfo") StudentStudyingQuery queryInfo, @Param("user") BaseUser user);
	
	List<StudentStudyingListInfo> getStudyingListJoinAccNew(@Param("queryInfo") StudentStudyingQuery queryInfo, @Param("user") BaseUser user);
	/**
	 * 根据学员learnId得到学员信息
	 * @param learnId
	 * @return
	 */
	StudentStudyingListInfo getStudyingInfoByLearnId(String learnId);
	
	/**
	 * 更新学员编号或学号
	 * @param learnId
	 * @param stdNo
	 * @param schoolRoll
	 * @return
	 */
	int updateStudyIngNo (@Param("learnId")String learnId,@Param("stdNo")String stdNo,@Param("schoolRoll") String schoolRoll);
	
	/**
	 * 查询在读学员明细
	 * @param learnId
	 * @return
	 */
	List<StudyingPaymentInfo> getPaymentInfos(String learnId);
	/**
	 * 获取学员联系信息
	 * @param learnId
	 * @return
	 */
	StudyingContactsShow getStudyingContacts(String learnId);
	/**
	 * 更新学员联系方式
	 * @param contacts
	 * @return
	 */
	int updateContacts(StudyingContacts contacts);
	
	String selectGradeByLearnId(String learnId);
	
	String getStudyingFeeAmountByLearnId(String learnId);
	
	/**
	 * 根据学业和学年查询当前学年要交费
	 * @param learnId
	 * @param itemYear
	 * @return
	 */
	String getStuFeeAmountByLearnIdAndYear(@Param("learnId") String learnId,@Param("itemYear") String itemYear);
	
	/**
	 * 在读学员要交费的数量
	 * @param queryInfo
	 * @param user
	 * @return
	 */
	int getStudyingCount(@Param("queryInfo") StudentStudyingQuery queryInfo, @Param("user") BaseUser user);

}
