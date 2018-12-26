package com.yz.dao;

import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;

/**
 * 省市区
 * @author lx
 * @date 2017年12月8日 下午5:49:58
 */
public interface SysProvinceCityDistrictMapper
{
	public SysProvince selectProvinceByPrimaryKey(String provinceCode);
	
	public SysCity selectCityByPrimaryKey(String cityCode);
	
	public SysDistrict selectDistrictByPrimaryKey(String districtCode);
}
