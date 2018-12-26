package com.yz.dao.oa;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.oa.DepartmentInfo;
import com.yz.model.oa.OaDepQueryInfo;
/**
 * 部门
 * @author lx
 * @date 2017年6月30日 下午5:25:50
 */
public interface DepartmentMapper {

	/**
	 * 所有的部门
	 */
	public List<DepartmentInfo> selectAllDepartmentInfo(OaDepQueryInfo depInfo);
	
	/**
	 * 根据部门名字和所在校区查询部门数量
	 */
	public int selectCountByDepName(@Param("dpName") String dpName,@Param("campusId") String campusId);
	
	/**
	 * 新增部门
	 * @param dpInfo
	 * @return
	 */
	public int insertDpInfo(DepartmentInfo dpInfo);
	
	/**
	 * 获取某个部门的详细
	 * @param dpId
	 * @return
	 */
	public DepartmentInfo getDepartmentInfo(String dpId);
	
	/**
	 * 修改部门信息
	 * @param dpInfo
	 */
	public void updateDepartmentInfo(DepartmentInfo dpInfo);
	
	/**
	 * 启用或者停用部门
	 * @param dpInfo
	 */
	public void depBlock(DepartmentInfo dpInfo);
	
	/**
	 * 下拉部门
	 * @param eName
	 * @return
	 */
	public List<Map<String, String>> findAllKeyValue(@Param("eName") String eName);
	
	/**
	 * 某个校区小的所有部门
	 * @param campusId
	 * @return
	 */
	public List<Map<String, String>> findAllListByCampusId(String campusId);
	
	/**
	 * 插入部门职称
	 * @param dpId
	 * @param jdIds
	 */
	public void insertDepJdIds(@Param("dpId") String dpId,@Param("ids") String[] jdIds);
	
	/**
	 * 删除部门职称
	 * @param dpId
	 */
	public void deleteDepTitle(String dpId);
	
	/**
	 * 部门下的所有职称
	 * @param depId
	 * @return
	 */
	public List<Map<String, String>> findDepTitle(String depId);
}
