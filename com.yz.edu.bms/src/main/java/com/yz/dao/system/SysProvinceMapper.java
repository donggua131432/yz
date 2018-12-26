package com.yz.dao.system;

import java.util.List;

import com.yz.model.system.SysProvince;

public interface SysProvinceMapper {
	int deleteByPrimaryKey(String provinceCode);

	int insert(SysProvince record);

	int insertSelective(SysProvince record);

	SysProvince selectByPrimaryKey(String provinceCode);

	int updateByPrimaryKeySelective(SysProvince record);

	int updateByPrimaryKey(SysProvince record);

	List<SysProvince> selectAll();

	List<SysProvince> selectAllTwo();

}
