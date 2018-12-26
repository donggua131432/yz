package com.yz.dao.oa;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.oa.EmpModifyListInfo;
import com.yz.model.oa.EmployeeSelectInfo;
import com.yz.model.oa.OaEmpFollowInfo;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.oa.OaEmployeeLearnInfo;
import com.yz.model.oa.OaEmployeeModifyInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;

/**
 * 员工
 * 
 * @author lx
 * @date 2017年6月30日 下午5:26:10
 */
public interface OaEmployeeMapper {

	List<EmployeeSelectInfo> getTutorList(SelectQueryInfo queryInfo);

	/**
	 * 获取所有在职员工列表
	 * 
	 * @return
	 */
	public List<Map<String, String>> findAllKeyValue(@Param("eName") String eName);

	/**
	 * 根据状态获取所有员工列表
	 * 
	 * @return
	 */
	List<Map<String, String>> findKeyValueByStatus(@Param("eName") String eName, @Param("empStatus") String empStatus);

	/**
	 * 某个员工的基本信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeBaseInfo getEmpBaseInfo(String empId);
	
	/**
	 * 获取跟进人信息
	 * 
	 * @param empId
	 * @return
	 */
	OaEmpFollowInfo getEmpFollowInfo(String empId);

	/**
	 * 某个员工的附属信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeOtherInfo getEmpOtherInfo(@Param("empId") String empId, @Param("annexType") String annexType);

	/**
	 * 插入某个员工的基本信息
	 * 
	 * @param baseInfo
	 */
	public void insertEmpBaseInfo(OaEmployeeBaseInfo baseInfo);

	/**
	 * 插入员工的附件信息
	 * 
	 * @param annexs
	 */
	public void insertEmpAnnexInfos(List<OaEmployeeAnnex> annexs);

	/**
	 * 插入员工的其他信息
	 * 
	 * @param otherInfo
	 */
	public void insertEmpOtherInfo(OaEmployeeOtherInfo otherInfo);

	/**
	 * 修改员工的附件信息
	 * 
	 * @param annexInfo
	 */
	public void updateAnnexInfo(OaEmployeeAnnex annexInfo);

	/**
	 * 修改员工的其他信息
	 * 
	 * @param otherInfo
	 */
	public void updateOtherInfo(OaEmployeeOtherInfo otherInfo);

	/**
	 * 修改员工的基本信息
	 * 
	 * @param baseInfo
	 */
	public void updateEmpBaseInfo(OaEmployeeBaseInfo baseInfo);

	/**
	 * 某个证件类型下的某个证件号是否存在员工
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	public int getCountBy(@Param("idType") String idType, @Param("idCard") String idCard);

	/**
	 * 某个员工的职位信息
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeJobInfo getEmployeeJobInfo(String empId);

	/**
	 * 初始化职位信息
	 * 
	 * @param jobInfo
	 */
	public void initEmpJob(OaEmployeeJobInfo jobInfo);

	/**
	 * 修改职位信息
	 * 
	 * @param jobInfo
	 */
	public void empJobUpdate(OaEmployeeJobInfo jobInfo);

	List<EmployeeSelectInfo> findEmployeeByName(@Param("sName") String sName, @Param("campusId") String campusId);

	/**
	 * 某个部门下的员工信息
	 * 
	 * @param dpId
	 * @return
	 */
	public List<Map<String, String>> findAllListByDpId(String dpId);

	public void deleteRecruiter(@Param("ids") String[] ids);

	/**
	 * 根据员工姓名查询员工编号
	 * 
	 * @param recruitName
	 * @return
	 */
	List<String> getEmpIds(String recruitName);

	/**
	 * 修改老师职位
	 * 
	 * @param otherInfo
	 */
	public void updateTeacherJob(OaEmployeeOtherInfo otherInfo);

	/**
	 * 单独修改员工 校区，部门，招生组(新增招生老师的时候用)
	 * 
	 * @param jobInfo
	 */
	public void aloneUpdateEmpJob(OaEmployeeJobInfo jobInfo);

	/**
	 * 新增员工职称
	 * 
	 * @param empId
	 * @param jdIds
	 */
	public void insertEmpJdIds(@Param("empId") String empId, @Param("ids") String[] jdIds);

	/**
	 * 删除员工职称
	 * 
	 * @param dpId
	 */
	public void deleteEmpTitle(String empId);

	/**
	 * 员工所有的职称
	 * 
	 * @param empId
	 * @return
	 */
	List<Map<String, String>> findEmpTitle(String empId);

	/**
	 * 远智编码是否存在
	 * 
	 * @param yzCode
	 * @return
	 */
	public int isYzCodeExist(@Param("yzCode") String yzCode);

	/**
	 * 某个员工的远智编码是否存在
	 * 
	 * @param yzCode
	 * @param empId
	 * @return
	 */
	public int isSelfYzCode(@Param("yzCode") String yzCode, @Param("empId") String empId);

	/**
	 * 招生编码是否存在
	 * 
	 * @param recruitCode
	 * @return
	 */
	public int isRecruitCodeExist(@Param("recruitCode") String recruitCode);

	/**
	 * 某个员工的招生编码是否存在
	 * 
	 * @param recruitCode
	 * @param empId
	 * @return
	 */
	public int isSelfRecruiteCode(@Param("recruitCode") String recruitCode, @Param("empId") String empId);

	/**
	 * 招生老师最新的一条变更记录id
	 * 
	 * @param empId
	 * @return
	 */
	public OaEmployeeModifyInfo getEmpModifyInfoId(@Param("empId") String empId);

	/**
	 * 获取上一条变更记录的截止时间
	 * 
	 * @param empId
	 */
	public String getEmpModifyEndTime(@Param("modifyId") String modifyId);

	/**
	 * 增加变更记录
	 * 
	 * @param modifyInfo
	 */
	public void addEmpModifyInfo(OaEmployeeModifyInfo modifyInfo);

	/**
	 * 插入招生老师学员记录
	 */
	public void insertEmpLearnInfos(List<OaEmployeeLearnInfo> learnInfos);

	/**
	 * 某个招生老师,某个部门 在某个时间段的学员信息
	 * 
	 * @param recruit
	 * @param dpId
	 * @return
	 */
	public List<OaEmployeeLearnInfo> getEmpLearnInfos(@Param("recruit") String recruit, @Param("dpId") String dpId);

	public OaEmployeeLearnInfo getEmpLearnInfosByLearnId(@Param("learnId") String learnId);

	/**
	 * 老的部门总共的学员
	 * 
	 * @param dpId
	 * @return
	 */
	public int totalLearnCount(@Param("dpId") String dpId, @Param("empId") String empId,@Param("modifyId")String modifyId);

	/**
	 * 老的部门已分配的学员
	 * 
	 * @param dpId
	 * @return
	 */
	public int finishLearnCount(@Param("dpId") String dpId, @Param("empId") String empId,@Param("modifyId")String modifyId);

	/**
	 * 员工的变更记录
	 * 
	 * @param empId
	 * @return
	 */
	public List<EmpModifyListInfo> getModifyList(@Param("empId") String empId);

	/**
	 * 员工最后一条变更记录
	 */

	public EmpModifyListInfo getLastModifyInfo(@Param("empId") String empId);

	/**
	 * 清楚招生关系
	 * 
	 * @param empId
	 * @return
	 */
	int clearLearnRules(String empId);

	/**
	 * 查询员工信息
	 * 
	 * @param sName
	 * @param empStatus
	 * @return
	 */
	List<Map<String, String>> selectEmpInfo(@Param("sName") String sName, @Param("empStatus") String empStatus);

	/**
	 * 查看员工下的学员
	 * 
	 * @param empId
	 * @return
	 */
	List<Map<String, String>> selectEmpStudents(String empId);

	/**
	 * 查询未生效记录
	 * @param empId
	 * @return
	 */
	List<OaEmployeeModifyInfo> selectUnModifyInfos(String empId);

	/**
	 * 删除员工变更记录
	 * @param modifyId
	 */
	void deleteEmpModifyInfo(String modifyId);

	/**
	 * 修改员工信息生效信息
	 * @param empId
	 * @param effectTime
	 */
	void updateEmpJObEffectTime(@Param("empId") String empId, @Param("effectTime") String effectTime);
	
	/**
	 * 通过yzCode查询user是否存在
	 * @param yzCode
	 * @return
	 */
	int checkUserIfExistsByYzCode(String yzCode);
	
	
	void deleteTeacher(@Param("ids") String[] ids);
	
	
	/**
	 * 某个证件类型下的某个证件号是否存在相应类型教师
	 * 
	 * @param idType
	 * @param idCard
	 * @return
	 */
	public int getCountByEmpType(@Param("idType") String idType, @Param("idCard") String idCard,@Param("empType") String empType);
	
	/**
	 * 置空员工的userId
	 */
	void updateEmpUserIdByEmpId(@Param("empId") String empId);
}
