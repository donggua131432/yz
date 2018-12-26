package com.yz.dao.baseinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdTestArea;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysProvince;

public interface BdUnvsProfessionMapper {
	int deleteByPrimaryKey(String pfsnId);

	int insert(BdUnvsProfession record);

	int insertSelective(BdUnvsProfession record);

	BdUnvsProfession selectByPrimaryKey(String pfsnId);

	int updateByPrimaryKeySelective(BdUnvsProfession record);

	int updateByPrimaryKey(BdUnvsProfession record);

	void deleteAllUnvsProfession(@Param("ids") String[] ids);

	List<BdUnvsProfession> selectAll(BdUnvsProfession unvsProfession);

	List<Map<String, String>> findAllKeyValue(@Param("sName")String sName);

	void opendownAllUnvsProfession(@Param("ids") String[] ids, @Param("exType") String exType);

	List<Map<String, String>> searchProfessionJson(@Param("pfsnName") String pfsnName, @Param("unvsId") String unvsId,
			@Param("pfsnLevel") String pfsnLevel, @Param("grade") String grade);

	BdUnvsProfession getParamByPfsnCode(String paramName);

	List<Map<String, String>> searchGradeJson(@Param("sName") String sName, @Param("pfsnName") String pfsnName,
			@Param("unvsId") String unvsId, @Param("pfsnLevel") String pfsnLevel);

	public List<Map<String, String>> findTestGroupByPfsnLevel(String pfsnLevel);

	public String findTestSubjectByGroupId(String groupId);

	List<Map<String, String>> selectAllSubject();

	/**
	 * 根据院校ID 查询院校考区所在省份
	 * 
	 * @param pfsnId
	 * @return
	 */
	List<SysProvince> selectTestAreaProvince(String pfsnId);

	/**
	 * 根据院校ID 查询专业考区所在市
	 * 
	 * @param provinceCode
	 * @return
	 */
	List<SysCity> selectTestAreaCity(String unvsId);

	/**
	 * 根据院校ID 查询可用考区
	 * 
	 * @param pfsnId
	 * @return
	 */
	List<BdTestArea> selectTestArea(String unvsId);

	List<Map<String, String>> searchAllowProfessionJson(@Param("sName") String sName, @Param("unvsId") String unvsId,
												   @Param("pfsnLevel") String pfsnLevel, @Param("grade") String grade);
}