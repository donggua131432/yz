package com.yz.dao.graduate;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.graduate.BdGraduateRecordsInfo;
import com.yz.model.graduate.GraduateApplyInfo;
import com.yz.model.graduate.GraduateApplyQuery;
import com.yz.model.graduate.GraduateStudentInfo;

/**
 * 毕业发起 & 学员毕业
 * @author lx
 * @date 2017年7月12日 下午12:32:11
 */
public interface GraduateApplyMapper {
	
	/**
	 * 申请列表
	 * @param query
	 * @return
	 */
	public List<GraduateApplyInfo> queryGraduateApplyInfo(GraduateApplyQuery query);
	/**
	 * 学员信息
	 * @param condition
	 * @param userId
	 * @return
	 */
	public GraduateStudentInfo queryStudentByCondition(@Param("condition") String condition,@Param("userId") String userId);
	
	/**
	 * 新增学员申请
	 * @param studentInfo
	 */
	public void insertGraduateApply(GraduateStudentInfo studentInfo);
	
	/**
	 * 初始化审核的基础记录信息
	 * @param list
	 */
	public void initGCheckTypes(List<BdGraduateRecordsInfo> list);
	
	/**
	 * 删除毕业申请记录
	 * @param ids
	 */
	public void deleteGraduate(@Param("ids") String[] ids);
	
	/**
	 * 查询毕业学员列表
	 * @param query
	 * @return
	 */
	public List<GraduateApplyInfo> queryGraduateStudentInfosByPage(GraduateApplyQuery query);
	
	/**
	 * 确认毕业
	 * @param learnId
	 */
	public void confirmGraduate(@Param("learnId") String learnId);
}
