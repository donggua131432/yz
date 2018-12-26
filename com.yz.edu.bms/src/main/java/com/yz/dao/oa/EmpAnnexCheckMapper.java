package com.yz.dao.oa;

import java.util.List;

import com.yz.model.oa.OaEmployeeAnnex;

public interface EmpAnnexCheckMapper {

	/**
	 * 查询招生老师附件列表
	 * @param learnId
	 * @return
	 */
	List<OaEmployeeAnnex> getAnnexList(String empId);
	/**
	 * 更新附件信息
	 * @param annexInfo
	 */
	int updateAnnexInfo(OaEmployeeAnnex annexInfo);
	
	/**
	 * 针对删除的更新
	 * @param annexInfo
	 * @return
	 */
	int updateAnnexInfoForDel(OaEmployeeAnnex annexInfo);
	
}
