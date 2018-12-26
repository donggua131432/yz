package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentDegreeEnglishInfo;
import com.yz.model.stdService.StudentDegreeEnglishQuery;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentDegreeEnglishMapper {
    List<StudentDegreeEnglishInfo> findAllDegreeEnglishList(@Param("queryInfo") StudentDegreeEnglishQuery query, @Param("user") BaseUser user);

    int updateRemark(@Param("degreeId") String degreeId, @Param("remark") String remark,@Param("isSceneConfirm") String isSceneConfirm);

    int resetTask(@Param("degreeId") String degreeId, @Param("taskId") String taskId, @Param("learnId") String learnId);

    List<Map<String, Object>> getNonExistsStudent(@Param("studentDegreeEnglishInfoList")List<StudentDegreeEnglishInfo> studentDegreeEnglishInfoList);

    void insert(@Param("studentDegreeEnglishInfoList")List<StudentDegreeEnglishInfo> studentDegreeEnglishInfoList,@Param("user") BaseUser user);
    
    /**
     * 批量初始化 英语学位报名学员任务数据
     * @param list
     */
	public void addStuDegreeEnglishInfo(@Param("list") List<StudentDegreeEnglishInfo> list);
	/**
	 * 单个添加 英语学位报名学员任务数据
	 * @param info
	 */
	public void singleAddStuEnglishInfo(StudentDegreeEnglishInfo info);
	
	/**
	 * 批量删除 英语学位报名
	 * @param ids
	 * @param taskId
	 */
	public void delStuDegreeEnglishInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	/**
	 * 单个删除 英语学位报名
	 * @param learnId
	 * @param taskId
	 */
	public void aloneDelStuDegreeEnglishInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);
}
