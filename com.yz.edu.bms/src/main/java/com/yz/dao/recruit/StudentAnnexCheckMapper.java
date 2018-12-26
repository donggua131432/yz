package com.yz.dao.recruit;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.condition.recruit.StudentRecruitQuery;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdStudentAnnex;
import com.yz.model.recruit.StudentAnnexCheck;
import com.yz.model.recruit.StudentCheckRecord;

public interface StudentAnnexCheckMapper {
	/**
	 * 查询考前资料核查列表
	 * @param queryInfo
	 * @return
	 */
	List<StudentAnnexCheck> getStudentAnnexCheckList(@Param("queryInfo") StudentAnnexCheckQuery queryInfo,@Param("user") BaseUser user);
	/**
	 * 查询学员附件列表
	 * @param learnId
	 * @return
	 */
	List<BdStudentAnnex> getAnnexList(String learnId);
	/**
	 * 更新附件信息
	 * @param annexInfo
	 */
	int updateAnnexInfo(BdStudentAnnex annexInfo);
	/**
	 * 更新审核记录
	 * @param check
	 * @return
	 */
	int updateCheckRecord(StudentCheckRecord check);
	/**
	 * 更新是否已审核考前资料状态
	 * @return
	 */
	int updateIsDataCheck(BdLearnInfo learnInfo);
	/**
	 * 获取学业信息
	 * @param getle
	 * @return
	 */
	BdLearnInfo getLearnInfo(Object getle);
	/**
	 * 查询必传附件
	 * @param stdId
	 * @return
	 */
	List<BdStudentAnnex> selectRequiredAnnex(String stdId);
	/**
	 * 
	 * @param stdId
	 */
	int updateStudentAnnexStatus(@Param("stdId") String stdId, @Param("annexStatus") String annexStatus);
	/**
	 * 查询学员对应附件状态的文件数量
	 * @param stdId
	 * @param annexStatus
	 */
	int countBy(@Param("stdId") String stdId, @Param("annexStatus") String annexStatus);
	/**
	 * 更新学员计算提成时间
	 * @param learnId
	 */
	void updateExpenseTime(String learnId);

}
