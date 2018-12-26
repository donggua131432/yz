package com.yz.dao.system;

import java.util.List;

import com.yz.model.system.SysCity;

public interface SysCityMapper {

	List<SysCity> getCity(String proviceId);

	int deleteByPrimaryKey(String cityCode);

	int insert(SysCity record);

	int insertSelective(SysCity record);

	SysCity selectByPrimaryKey(String cityCode);

	int updateByPrimaryKeySelective(SysCity record);

	int updateByPrimaryKey(SysCity record);
}