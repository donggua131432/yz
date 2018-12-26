package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.TeacherInfoQuery;
import com.yz.model.educational.BdTeacherRecommendQuery;
import com.yz.model.educational.JStudentStudyingImportExcel;
import com.yz.model.educational.TeacherShowInfo;
import com.yz.model.oa.OaEmployeeImportInfo;

import org.apache.ibatis.annotations.Param;

/**
 * 教师管理
 * @author lx
 * @date 2017年7月10日 下午7:48:45
 */
public interface TeacherManageMapper {
	
	
	/**
	 * 所有的教师
	 */
	public List<TeacherShowInfo> selectAllTeacherInfo(TeacherInfoQuery queryInfo);

	public List<Map<String,Object>> getTeacherRecommend(BdTeacherRecommendQuery recommendQuery);
	
	/**
	 * 查找不存在所属校区
	 * @param teacherlist
	 * @return
	 */
	List<Map<String, Object>> getNonExistsCampus(@Param("teacherlist") List<OaEmployeeImportInfo> teacherlist);
	/**
	 * 初始化临时表
	 * @param teacherlist
	 */
	void initTmpTeacherInfoTable(@Param("teacherlist") List<OaEmployeeImportInfo> teacherlist);
	
	List<Map<String, String>> selectTeacherInfoInsert(@Param("user") BaseUser user);
	
	void insertTeacherInfoExcel(@Param("list") List<Map<String, String>> list);
	
	void insertTmpinitTeacherAnnex(@Param("list")List<Map<String, String>> annexList);
	
	List<Map<String, String>> selectTmpTeacherAnnex();
	
	/**
	 * 教师管理excel导出结果查询
	 * @param queryInfo
	 * @return
	 */
	public List<OaEmployeeImportInfo> selectExprotTeacherInfo(TeacherInfoQuery queryInfo);
	
	


	
	
}