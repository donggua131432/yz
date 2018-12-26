package com.yz.dao.gk;

import com.yz.model.admin.BaseUser;
import com.yz.model.gk.StudentGraduateExamGKInfo;
import com.yz.model.gk.StudentGraduateExamGKInfoSub;
import com.yz.model.gk.StudentGraduateExamGkQuery;

import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StudentGraduateExamGKMapper {
	/**
	 * 国开本科统考跟进列表
	 * @param query
	 * @param user
	 * @return
	 */
	 List<StudentGraduateExamGKInfo> findStudentGraduateExamGKList(@Param("queryInfo") StudentGraduateExamGkQuery query, @Param("user") BaseUser user);
	 
	 
	 int updateRemark(@Param("followId") String followId, @Param("remark") String remark);
	 
	 /**
	  * 获取详细
	  * @param learnId
	  * @return
	  */
	 StudentGraduateExamGKInfo getStudentGraduateExamGKById(@Param("learnId") String learnId);
	 
	 /**
	  * 批量初始化
	  * @param list
	  */
	 void addStuGkUnifiedExamInfo(@Param("list") List<StudentGraduateExamGKInfo> list);
	 
	 /**
	  * 修改
	  * @param list
	  */
	 void updateGkUnifiedExamList(@Param("list") List<StudentGraduateExamGKInfo> list);
	 
	 /**
	  * 批量初始化科目
	  * @param list
	  */
	 void addStuGkUnifiedExamSubInfo(@Param("list") List<StudentGraduateExamGKInfoSub> list);
	 
	 /**
	  * 删除
	  * @param ids
	  * @param taskId
	  */
	 void delStuGkUnifiedExamSubInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	 /**
	  * 单独添加
	  * @param examInfo
	  */
	 void singleAddStuUnifiedExamInfo(StudentGraduateExamGKInfo examInfo);
	 
	 /**
	  * 单独修改
	  * @param examInfo
	  */
	 void singleUpdateStuUnifiedExamInfo(StudentGraduateExamGKInfo examInfo);
	 
	 /**
	  * 单个删除
	  * @param learnId
	  * @param taskId
	  */
	 void aloneDelStuGkUnifiedExamInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);
	 
	 int getStudentGraduateExamGkCount(@Param("queryInfo") StudentGraduateExamGkQuery query, @Param("user") BaseUser user);
}
