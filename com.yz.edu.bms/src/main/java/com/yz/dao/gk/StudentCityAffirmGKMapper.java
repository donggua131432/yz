package com.yz.dao.gk;

import com.yz.model.admin.BaseUser;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentCityAffrimGkQuery;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface StudentCityAffirmGKMapper {
	/**
	 * 国开考场城市确认列表
	 * @param query
	 * @param user
	 * @return
	 */
	 List<StudentCityAffirmGKInfo> findStudentCityAffirmGKList(@Param("queryInfo") StudentCityAffrimGkQuery query, @Param("user") BaseUser user);
	 
	 /**
	  * 根据年度统计各考试城市的人数
	  * @param eyId
	  * @return
	  */
	 List<StudentCityAffirmGKInfo> getGkCityStatisticsInfo(@Param("eyId") String eyId,@Param("statGroup")String statGroup);
	 
	 /**
	  * 国开考场城市列表
	  * @return
	  */
	 List<Map<String, String>> getExamCityForGK(@Param("status")String status);
	 /**
	  * 更新未确认原因
	  * @param affirmId
	  * @param reason
	  * @param isExam
	  * @return
	  */
    int updateReason(@Param("affirmId") String affirmId, @Param("reason") String reason,@Param("isExam") String isExam);
    /**
     * 重置任务
     * @param affirmId
     * @param taskId
     * @param learnId
     * @return
     */
    int resetTask(@Param("affirmId") String affirmId, @Param("taskId") String taskId, @Param("learnId") String learnId);

	
	public int checkIfExistByLearnId(@Param("eyId")String eyId,@Param("learnId")String learnId);
	
	public void addStuCityAffirmGK(@Param("list") List<StudentCityAffirmGKInfo> list);
	
	public void delStuCityAffirm(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	public void singleAddStuCityAffirmInfo(StudentCityAffirmGKInfo cityAffirmInfo);
	
	public void aloneDelStuCityAffirmInfo(@Param("learnId")String learnId,@Param("taskId")String taskId);
	
}
