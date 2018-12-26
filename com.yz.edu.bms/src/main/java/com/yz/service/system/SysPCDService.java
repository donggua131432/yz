package com.yz.service.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.model.system.SysArea;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.util.FileUtil;
import com.yz.util.JsonUtil;

@Service
@Transactional
public class SysPCDService {
	private static final Logger log = LoggerFactory.getLogger(SysPCDService.class);

	@Value("${cacheFile.pcd}")
	public String filePath;

	// 省
	@Autowired
	private SysProvinceMapper sysProvinceMapper;
	// 市
	@Autowired
	private SysCityMapper sysCityMapper;
	// 区
	@Autowired
	private SysDistrictMapper sysDistrictMapper;

	public List<SysProvince> getProvice() {
		return sysProvinceMapper.selectAll();
	}

	public List<SysCity> getCity(String proviceId) {
		return sysCityMapper.getCity(proviceId);
	}

	public List<SysDistrict> getDistrict(String cityId) {
		return sysDistrictMapper.getDistrict(cityId);
	}

	public List<SysArea> getList(List<SysProvince> provinceList) {
		SysArea sysArea = null;
		List<SysArea> resultList = new ArrayList<SysArea>();

		// 第一级
		for (SysProvince sysProvince : provinceList) {
			sysArea = new SysArea();
			sysArea.setId(sysProvince.getProvinceCode());
			sysArea.setpId("0");
			sysArea.setName(sysProvince.getProvinceName());
			sysArea.setIsShowCheck(false);
			resultList.add(sysArea);

			// 根據省級id獲取对应市级
			List<SysCity> cityList = getCity(sysProvince.getProvinceCode());

			// 第二级
			for (SysCity sysCity : cityList) {
				sysArea = new SysArea();
				sysArea.setId(sysCity.getCityCode());
				sysArea.setpId(sysProvince.getProvinceCode());
				sysArea.setName(sysCity.getCityName());
				sysArea.setIsShowCheck(false);
				resultList.add(sysArea);

				// 根据市级获取区级
				/*
				 * List<SysDistrict> districtList =
				 * getDistrict(sysCity.getCityCode());
				 */

				// 第三级
				/*
				 * for (SysDistrict sysDistrict : districtList) { sysArea = new
				 * SysArea();
				 * sysArea.setId(Integer.parseInt(sysDistrict.getDistrictCode())
				 * ); sysArea.setpId(Integer.parseInt(sysCity.getCityCode()));
				 * sysArea.setName(sysDistrict.getDistrictName());
				 * sysArea.setIsShowCheck(true); resultList.add(sysArea); }
				 */
			}
		}

		return resultList;
	}

	public boolean setPCDMap(List<Map<String, Object>> resultList) {
		// TODO Auto-generated method stub
		// 1、map转json
		String json = JsonUtil.object2String(resultList);
		// 2、将json对象转换为字符串
		String jsonStr = "var pcdJson = " + json;
		// 写入文件
		try {
			FileUtil.writeToJson("d://"+filePath, jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.debug("----------------" + e.getMessage() + "-------------------");
			return false;
		}
		return true;
	}

	public List<SysProvince> getProviceTwo() {
		return sysProvinceMapper.selectAllTwo();
	}

	public List<Map<String, Object>> queryArea() {
		
		// 1、查询所有省级
		List<SysProvince> provinceList = getProviceTwo();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		for (SysProvince sysProvince : provinceList) {
			

			Map<String, Object> resultMap = new HashMap<String, Object>();

			resultMap.put("provinceCode", sysProvince.getProvinceCode());
			resultMap.put("provinceName", sysProvince.getProvinceName());

			List<Map<String, Object>> cityResultList = new ArrayList<Map<String, Object>>();
			// 查省对应的市
			List<SysCity> cityList = getCity(sysProvince.getProvinceCode());

			for (SysCity sysCity : cityList) {
				

				Map<String, Object> cityMap = new HashMap<String, Object>();
				cityMap.put("cityCode", sysCity.getCityCode());
				cityMap.put("cityName", sysCity.getCityName());

				List<Map<String, String>> diResultList = new ArrayList<Map<String, String>>();

				// 查省对应的市
				List<SysDistrict> districtList = getDistrict(sysCity.getCityCode());

				for (SysDistrict sysDistrict : districtList) {
					
					Map<String, String> districtMap = new HashMap<String, String>();
					districtMap.put("districtCode", sysDistrict.getDistrictCode());
					districtMap.put("districtName", sysDistrict.getDistrictName());
					districtMap.put("zipCode", sysDistrict.getZipCode());
					diResultList.add(districtMap);
				}

				cityMap.put("district", diResultList);
				cityResultList.add(cityMap);
			}
			resultMap.put("city", cityResultList);
			resultList.add(resultMap);
		}

		// 3、将json写入缓存
		
		//localCacheUtil.put("area", resultList);
		
		/*log.debug("-------------------------- 地区信息 start ----------------------");
		log.debug(JsonUtil.object2String(resultList));
		log.debug("-------------------------- 地区信息 end ----------------------");*/
		log.debug("-------------------------- 地区信息缓存成功 ----------------------");
		return resultList;
	}
}
