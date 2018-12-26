package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentLectureNoticeInfo;

import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface StudentLectureNoticeMapper {

	/**
	 * 学员开课通知跟进列表
	 * @param query
	 * @param user
	 * @return
	 */
    List<StudentLectureNoticeInfo> findAllLectureNoticeList(@Param("queryInfo") StudentLectureNoticeInfo query, @Param("user") BaseUser user);
    
    /**
     * 编辑备注
     * @param lectureId
     * @param remark
     * @return
     */
    int updateRemark(@Param("lectureId") String lectureId, @Param("remark") String remark);
    /**
     * 重置任务
     * @param lectureId
     * @param taskId
     * @param learnId
     * @return
     */
    int resetTask(@Param("lectureId") String lectureId, @Param("taskId") String taskId, @Param("learnId") String learnId);

	/**
	 * 批量初始化学籍资料跟进的数据
	 * @param list
	 */
	public void addStuLectureNotcieInfo(@Param("list") List<StudentLectureNoticeInfo> list);
	
	
	/**
	 * 单个生成学籍资料跟进的数据
	 */
	public void singleAddStuLectureNotcieInfo(StudentLectureNoticeInfo noticeInfo);
	
	/**
	 * 批量删除学籍资料跟进的数据
	 * @param ids
	 * @param taskId
	 */
	public void delStuLectureNotcieInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	/**
	 * 单个删除
	 * @param learnId
	 * @param taskId
	 */
	public void aloneDelStuLectureNotcieInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);

}
