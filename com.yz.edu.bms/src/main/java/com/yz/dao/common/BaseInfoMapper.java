package com.yz.dao.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.common.CampusSelectInfo;
import com.yz.model.common.DpSelectInfo;
import com.yz.model.common.EmpListInfo;
import com.yz.model.common.EmpQueryInfo;
import com.yz.model.common.GroupSelectInfo;
import com.yz.model.common.ItemSelectInfo;
import com.yz.model.common.PfsnSelectInfo;
import com.yz.model.common.TaSelectInfo;
import com.yz.model.common.TbListInfo;
import com.yz.model.common.TbQueryInfo;
import com.yz.model.common.ThpListInfo;
import com.yz.model.common.ThpQueryInfo;
import com.yz.model.common.UnvsSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.oa.EmployeeSelectInfo;

public interface BaseInfoMapper {
	/**
	 * 查询院校信息
	 * 
	 * @param sqInfo
	 * @return
	 */
	List<UnvsSelectInfo> getUnvsSelectList(SelectQueryInfo sqInfo);

	/**
	 * 查询专业信息
	 * 
	 * @param sqInfo
	 * @return
	 */
	List<PfsnSelectInfo> getPfsnSelectList(SelectQueryInfo sqInfo);

	/**
	 * 查询考区信息
	 * 
	 * @param sqInfo
	 * @return
	 */
	List<TaSelectInfo> getTaSelectList(SelectQueryInfo sqInfo);

	/**
	 * 查询院校、专业、考区名称
	 * 
	 * @param unvsId
	 * @param pfsnId
	 * @param taId
	 * @return
	 */
	Map<String, String> getUPTName(@Param("unvsId") String unvsId, @Param("pfsnId") String pfsnId,
			@Param("taId") String taId);
	
	/**
	 * 查询部门信息
	 * @param sqInfo
	 * @return
	 */
	List<CampusSelectInfo> getCampusList(SelectQueryInfo sqInfo);
	
	/**
	 * 查询部门信息
	 * @param sqInfo
	 * @return
	 */
	List<DpSelectInfo> getDepartmentList(SelectQueryInfo sqInfo);
	
	/**
	 * 查询员工信息
	 * @param sqInfo
	 * @return
	 */
	List<EmployeeSelectInfo> getEmployeeList(SelectQueryInfo sqInfo);
	
	/**
	 * 查询查询招生组信息
	 * @param sqInfo
	 * @return
	 */
	List<GroupSelectInfo> getGroupList(SelectQueryInfo sqInfo);

	/**
	 * 
	 * @param sqInfo
	 * @return
	 */
	List<ItemSelectInfo> getFeeItemList(SelectQueryInfo sqInfo);
	/**
	 * 获取员工列表
	 * @param queryInfo
	 * @param user 
	 * @return
	 */
	List<EmpListInfo> getEmpList(@Param("queryInfo") EmpQueryInfo queryInfo, @Param("user") BaseUser user);
	/**
	 * 查询教学计划列表
	 * @param queryInfo
	 * @return
	 */
	List<ThpListInfo> getThpUnSelectedList(ThpQueryInfo queryInfo);
	/**
	 * 获取已关联的教学计划列表
	 * @param queryInfo
	 * @return
	 */
	List<ThpListInfo> getThpSelectedList(ThpQueryInfo queryInfo);
	/**
	 * 查询教材列表
	 * @param queryInfo
	 * @return
	 */
	List<TbListInfo> getTbUnSelectedList(TbQueryInfo queryInfo);
	/**
	 * 查询已选教材列表
	 * @param queryInfo
	 * @return
	 */
	List<TbListInfo> getTbSelectedList(TbQueryInfo queryInfo);


	/**
	 * 所有校监列表
	 * @param eName
	 * @return
	 */
	List<Map<String, String>> schoolSuperKeyValue(@Param("eName") String eName);

	/**
	 * 根据校监查部门列表
	 * @param empId
	 * @return
	 */
	List<String> getDpIdList(@Param("empId") String empId);

	/**
	 * 根据员工ID查下属信息集合
	 * @param empId
	 * @return
	 */
	List<String> getSubEmpIdList(@Param("empId") String empId);
}
