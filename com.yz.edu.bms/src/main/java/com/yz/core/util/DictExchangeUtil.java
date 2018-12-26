package com.yz.core.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

 
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;

@Component
public class DictExchangeUtil {
	
	@Autowired
	private SysDictService sysDictService;

	/**
	 * 获取对应字段值
	 * 
	 * @param dict
	 * @param key
	 * @return
	 */
	public  String getParamValue(String dict, String key) {
		// 取字典进行转换
		List<SysDict> list = sysDictService.getDicts(dict);
		for (SysDict sysDict : list) {
			if (sysDict.getDictName().equals(key)) {
				return sysDict.getDictValue();
			}
		}

		return null;
	}

	/**
	 * 获取对应字段值
	 * 
	 * @param dict
	 * @param key
	 * @return
	 */
	public String getParamKey(String dict, String value) {

		// 取字典进行转换
		List<SysDict> list = sysDictService.getDicts(dict);

		for (SysDict sysDict : list) {
			if (sysDict.getDictValue().equals(value)) {
				return sysDict.getDictName();
			}
		}

		return null;
	}

	/**
	 * 获取对应省市区值
	 * 
	 * @param dict
	 * @param key
	 * @return
	 */
	public  String getAreaValue(String value) {
		// 缓存获取省市区

		List<Map<String, Object>> resultList =  null ;

		if (null != resultList) {
			for (Map<String, Object> map : resultList) {

				if (value.equals(map.get("provinceName"))) {
					return (String) map.get("provinceCode");
				} else {

					List<Map<String, Object>> mapCity = (List<Map<String, Object>>) map.get("city");

					if (null != mapCity) {
						for (Map<String, Object> map2 : mapCity) {

							if (value.equals(map2.get("cityName"))) {
								return (String) map2.get("cityCode");
							} else {

								List<Map<String, Object>> mapDistrict = (List<Map<String, Object>>) map2
										.get("district");
								if (null != mapDistrict) {
									for (Map<String, Object> map3 : mapDistrict) {

										if (value.equals(map3.get("districtName"))) {
											return (String) map3.get("districtCode");
										}
									}
								}
							}
						}
					}

				}
			}
		}
		return "";
	}

}
