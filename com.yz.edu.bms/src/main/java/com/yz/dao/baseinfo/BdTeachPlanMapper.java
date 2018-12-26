package com.yz.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdTeachPlan;
import com.yz.model.baseinfo.BdTeachPlanMap;
import com.yz.model.baseinfo.TUPDemo;

public interface BdTeachPlanMapper {
    int deleteByPrimaryKey(String thpId);

    int insert(BdTeachPlan record);

    int insertSelective(BdTeachPlan record);

    BdTeachPlan selectByPrimaryKey(String thpId);

    int updateByPrimaryKeySelective(BdTeachPlan record);

    int updateByPrimaryKey(BdTeachPlan record);

	List<BdTeachPlanMap> selectAll(TUPDemo postValue);

	Map<String, Object> selectOne(String teachPlanId);

	int getCount(@Param("pfsnId") String pfsnId,@Param("semester") String semester);

	void deleteTeachPlan(@Param("ids")String[] ids);

	List<BdTeachPlan> findTeachPlan(String sName);

	void selectTextBook(String thpId);

	boolean isTeachPlanExit(BdTeachPlan teachPlan);

	BdTeachPlan selectTeachPlan(BdTeachPlan teachPlan);
	
	public String selectPfsnIdByCon(@Param("grade") String grade,@Param("pfsnName") String pfsnName,@Param("unvsName") String unvsName,
			@Param("pfsnLevel") String pfsnLevel,@Param("pfsnCode") String pfsnCode);

	String selectUnvsCodeByPfsnId(String pfsnId);

	int selectTeachPlanCount(BdTeachPlan teachPlan);
	
	void initTeachPlanTable(@Param("teachPlanList")List<BdTeachPlanMap> teachPlanList);
	/**
	 * Excel导入查找不存在教材计划
	 * @param classplanList
	 * @return
	 */
	List<Map<String, Object>>  getNonExistsTeachPlan(@Param("teachPlanList")List<BdTeachPlanMap> teachPlanList);
	
	/**
	 * 批量更新考试科目
	 * @param teachPlanList
	 */
	void batchUpdateTestSubjectByExcel();
	
}