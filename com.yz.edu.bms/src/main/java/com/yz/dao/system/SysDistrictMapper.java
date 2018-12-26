package com.yz.dao.system;

import java.util.List;

import com.yz.model.system.SysDistrict;

public interface SysDistrictMapper {
	int deleteByPrimaryKey(String districtCode);

	int insert(SysDistrict record);

	int insertSelective(SysDistrict record);

	SysDistrict selectByPrimaryKey(String districtCode);

	int updateByPrimaryKeySelective(SysDistrict record);

	int updateByPrimaryKey(SysDistrict record);

	List<SysDistrict> getDistrict(String cityId);
}
