package com.yz.dao.stdService;

import java.util.List;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentAttachment;
import com.yz.model.stdService.StudentGraduatePaperInfo;
import com.yz.model.stdService.StudentGraduatePaperQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface StudentGraduatePaperMapper {
    List<StudentGraduatePaperInfo> findAllPaperList(@Param("queryInfo") StudentGraduatePaperQuery query, @Param("user") BaseUser user);

	/**
	 * 批量初始化 毕业论文及报告任务数据
	 * @param list
	 */
	public void addStuGraduatePaperInfo(@Param("list") List<StudentGraduatePaperInfo> list);
	
	/**
	 * 单个生成毕业论文及报告任务数据
	 */
	public void singleAddStuGraduatePaperInfo(StudentGraduatePaperInfo paperInfo);
	
	/**
	 * 批量删除 毕业论文及报告任务数据
	 * @param ids
	 * @param taskId
	 */
	public void delStuGraduatePaperInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	/**
	 * 单个删除
	 * @param learnId
	 * @param taskId
	 */
	public void aloneDelStuGraduatePaperInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);

	/**
	 * 添加备注
	 * @param gpId
	 * @param remark
	 * @return
	 */
    int updateRemark(@Param("gpId") String gpId, @Param("remark") String remark);

    List<Map<String, Object>> getNonExistsStudent(@Param("studentGraduatePaperInfoList")List<StudentGraduatePaperInfo> studentGraduatePaperInfoList);

    void insert(@Param("studentGraduatePaperInfoList")List<StudentGraduatePaperInfo> studentGraduatePaperInfoList,@Param("user") BaseUser user);

	/**
	 * 附件插入
	 * @param attachment
	 */
    void insertAttachment(StudentAttachment attachment);

	List<StudentAttachment> selectStudentAttachment(String learnId);

	List<StudentAttachment> selectUserStudentAttachment(String learnId);

	List<StudentAttachment> selectCheckStudentAttachment(String learnId);

	StudentAttachment getAttachmentById(String attachmentId);

	void updateAttachment(StudentAttachment attachment);

	int countCheckSuccessAttachment(String learnId);

	int updatePaperCheckStatus(@Param("learnId") String learnId,@Param("checkStatus") String checkStatus);

	Map<String,String> getLearnInfo(String learnId);
	
	//判断当前专业是否已经存在了数据
	int checkIfExistByLearnId(String learnId);

	void updatePaperStatus(StudentGraduatePaperInfo paperInfo);
}
