package com.yz.service.educational;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yz.dao.educational.BdClassPlanMapper;
import com.yz.dao.educational.BdCourseMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdClassPlan;
import com.yz.model.educational.CourseExcel;


@Service
public class BdCourseExcelService {

	@Autowired
	private BdClassPlanMapper classPlanMapper;
	@Autowired
	private BdCourseMapper courseMapper;

	/**
	 * Excel导入得到不存在课程名称
	 * @param classplanList
	 * @return
	 */
	public List<Map<String, Object>> getNonExistsCourse(List<BdClassPlan> classplanList){
		List<Map<String, Object>> courseList=classPlanMapper.getNonExistsCourse(classplanList);
		return courseList;
	}
	
	/**
	 * Excel导入得到不存在课程名称
	 * @param classplanList
	 * @return
	 */
	public List<Map<String, Object>> getNonExistsCampus(List<BdClassPlan> classplanList){
		List<Map<String, Object>> campusList=classPlanMapper.getNonExistsCampus(classplanList);
		return campusList;
	}
	
	/**
	 * Excel导入得到不存在授课老师
	 * @param classplanList
	 * @return
	 */
	public List<Map<String, Object>> getNonExistsEmployee(List<BdClassPlan> classplanList){
		List<Map<String, Object>> campusList=classPlanMapper.getNonExistsEmployee(classplanList);
		return campusList;
	}
	
	
	/**
	 * Excel导入得到不存在教材计划
	 * @param classplanList
	 * @return
	 */
	public List<Map<String, Object>> getNonExistsTeachPlan(List<CourseExcel> courseList){
		List<Map<String, Object>> teachplanList=courseMapper.getNonExistsTeachPlan(courseList);
		return teachplanList;
	}
	
	/**
	 * Excel导入得到不存在教材名称
	 * @param classplanList
	 * @return
	 */
	public List<Map<String, Object>> getNonExistsTextBook(List<CourseExcel> courseList){
		List<Map<String, Object>> teachplanList=courseMapper.getNonExistsTextBook(courseList);
		return teachplanList;
	}
	
	
	public void insertByExcel(List<BdClassPlan> classplanList,BaseUser user) {
		classPlanMapper.initTmpExcelTable(classplanList);
		List<Map<String, String>> list = classPlanMapper.selectTmpClassPlan(user);
		for (Map<String, String> map : list) {
			map.put("cpId", IDGenerator.generatorId());
		}
		classPlanMapper.insertByExcel(list);
	}
	public void insertCourseByExcel(List<CourseExcel> courseList,BaseUser user) {
		courseMapper.insertByExcel(courseList,user);
	}
	
}
