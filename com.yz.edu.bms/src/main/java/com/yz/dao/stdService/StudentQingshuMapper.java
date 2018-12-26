package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentQingshuExcel;
import com.yz.model.stdService.StudentQingshuInfo;
import com.yz.model.stdService.StudentQingshuQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentQingshuMapper {

    List<StudentQingshuInfo> findAllQingshuList(@Param("queryInfo") StudentQingshuQuery query, @Param("user") BaseUser user);

	/**
	 * 添加备注
	 * @param qingshuId
	 * @param remark
	 * @return
	 */
	int updateRemark(@Param("qingshuId") String qingshuId, @Param("remark") String remark);

	int resetScore(@Param("qingshuId") String qingshuId, @Param("semester") String semester);

	
	/**
	 * 检查是否存在
	 * @param learnId
	 * @return
	 */
	public int checkIfExistByLearnId(String learnId);
	
	
	/**
	 * 批量初始化青书学堂跟进的数据
	 * @param list
	 */
	public void addStuQingShuInfo(@Param("list") List<StudentQingshuInfo> list);
	
	
	/**
	 * 单个生成青书学堂跟进的数据
	 */
	public void singleAddStuQingShuInfo(StudentQingshuInfo qingShuInfo);
	
	/**
	 * 批量删除青书学堂跟进的数据
	 * @param ids
	 * @param taskId
	 */
	public void delStuQingShuInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	/**
	 * 单个删除
	 * @param learnId
	 * @param taskId
	 */
	public void aloneDelStuQingShuInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);

	List<Map<String, Object>> getNonExistsStudent(@Param("studentQingshuExcelList")List<StudentQingshuExcel> studentQingshuExcelList);

	void insert(@Param("studentQingshuList")List<Map<String, String>> studentQingshuExcelList);

	void initTmpQingshu(@Param("studentQingshuExcelList")List<StudentQingshuExcel> list);

	List<Map<String, String>> selectQingshuTmpCourse();
}
