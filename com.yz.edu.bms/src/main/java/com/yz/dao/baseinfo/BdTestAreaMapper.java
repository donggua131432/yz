package com.yz.dao.baseinfo;

import java.util.List;

import com.yz.model.oa.OaCampusInfo;
import org.apache.ibatis.annotations.Param;

import com.yz.model.baseinfo.BdTestArea;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysProvince;

public interface BdTestAreaMapper {
	int deleteByPrimaryKey(String taId);

	int insert(BdTestArea record);

	int insertSelective(BdTestArea record);

	BdTestArea selectByPrimaryKey(String taId);

	int updateByPrimaryKeySelective(BdTestArea record);

	int updateByPrimaryKey(BdTestArea record);

	List<BdTestArea> findBdTestArea(@Param("provice") String provice, @Param("city") String city,
			@Param("district") String district);

	List<BdTestArea> selectAll(BdTestArea testArea);

	List<BdTestArea> findAllKeyValue(@Param("eName") String eName);

	void deleteAllTestArea(@Param("ids") String[] ids);

	List<BdTestArea> findBdTestAreaByPfsnId(String pfsnId);

	boolean isExitTaCode(@Param("exType") String exType, @Param("taCode") String taCode,
			@Param("oldTaCode") String oldTaCode);

	List<SysProvince> selectTestAreaProvince();

	List<SysCity> selectTestAreaCity(String provinceCode);

	List<String> selectUnvsProvinceCode(String mappingId);

	List<String> selectProfessionProvinceCode(String mappingId);

	List<String> selectUnvsCityCode(String mappingId);

	List<String> selectProfessionCityCode(String mappingId);

	/**
	 * 查询专业与考区关联关系
	 * 
	 * @param pfsnId
	 * @return
	 */
	List<String> selectTestAreaIds(String pfsnId);
}