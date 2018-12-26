package com.yz.service.baseinfo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.GlobalConstants;
import com.yz.dao.baseinfo.BdTestAreaMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.baseinfo.BdTestArea;
import com.yz.model.system.SysArea;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysProvince;

@Transactional
@Service
public class BdTestAreaServiceImpl {

	@Autowired
	private BdTestAreaMapper testAreaMapper;

	public List<BdTestArea> findBdTestArea(String testArea) {
		String[] arr = testArea.split("#");

		String provice = "";
		String city = "";
		String district = "";

		switch (arr.length) {
		case 1:
			provice = arr[0];
			break;
		case 2:
			provice = arr[0];
			city = arr[1];
			break;
		case 3:
			provice = arr[0];
			city = arr[1];
			district = arr[2];
			break;
		default:
			break;
		}

		return testAreaMapper.findBdTestArea(provice, city, district);
	}

	public List<BdTestArea> selectAll(BdTestArea testArea) {
		return testAreaMapper.selectAll(testArea);
	}

	public List<BdTestArea> findAllKeyValue(String eName){
		return testAreaMapper.findAllKeyValue(eName);
	}

	public void deleteAllTestArea(String[] idArray) {
		// TODO Auto-generated method stub
		testAreaMapper.deleteAllTestArea(idArray);
	}

	public void deleteTestArea(String id) {
		// TODO Auto-generated method stub
		testAreaMapper.deleteByPrimaryKey(id);
	}

	public BdTestArea getTestArea(String taId) {
		return testAreaMapper.selectByPrimaryKey(taId);
	}

	public void updateTestArea(BdTestArea testArea) {
		testAreaMapper.updateByPrimaryKeySelective(testArea);
	}

	public void addTestArea(BdTestArea testArea) {
		testArea.setTaId(IDGenerator.generatorId());
		testAreaMapper.insertSelective(testArea);
	}

	public List<BdTestArea> findBdTestAreaByPfsnId(String pfsnId) {
		// TODO Auto-generated method stub
		return testAreaMapper.findBdTestAreaByPfsnId(pfsnId);
	}

	public boolean isExitTaCode(String exType, String taCode, String oldTaCode) {
		if ("UPDATE".equals(exType)) {
			if (taCode.equals(oldTaCode))
				return false;
		}
		return testAreaMapper.isExitTaCode(exType, taCode, oldTaCode);
	}

	public List<SysProvince> getTestAreaProvice() {
		return testAreaMapper.selectTestAreaProvince();
	}

	public List<SysArea> getCityList(List<SysProvince> provinceList, String mappingId, String type) {
		SysArea sysArea = null;
		List<SysArea> resultList = new ArrayList<SysArea>();

		List<String> provinceIds = null;
		List<String> cityIds = null;

		if (GlobalConstants.UNVS.equals(type)) {
			provinceIds = testAreaMapper.selectUnvsProvinceCode(mappingId);
			cityIds = testAreaMapper.selectUnvsCityCode(mappingId);
		} else if (GlobalConstants.PROFESSION.equals(type)) {
			provinceIds = testAreaMapper.selectProfessionProvinceCode(mappingId);
			cityIds = testAreaMapper.selectProfessionCityCode(mappingId);
		}

		// 第一级
		for (SysProvince sysProvince : provinceList) {
			String provinceCode = sysProvince.getProvinceCode();
			sysArea = new SysArea();
			sysArea.setId(provinceCode);
			sysArea.setpId("0");
			sysArea.setName(sysProvince.getProvinceName());
			sysArea.setFlag("P");
			sysArea.setIsShowCheck(false);
			
			if (provinceIds != null && provinceIds.contains(provinceCode)) {
				sysArea.setChecked(true);
			}

			resultList.add(sysArea);
			
			List<SysCity> cityList = testAreaMapper.selectTestAreaCity(provinceCode);
			for (SysCity sysCity : cityList) {
				sysArea = new SysArea();
				sysArea.setId(sysCity.getCityCode());
				sysArea.setpId(provinceCode);
				sysArea.setName(sysCity.getCityName());
				sysArea.setIsShowCheck(false);
				sysArea.setFlag("C");
				
				if (cityIds.contains(sysCity.getCityCode())) {
					sysArea.setChecked(true);
				}

				resultList.add(sysArea);
			}
		}

		// 根據省級id獲取对应市级
		

		return resultList;
	}

}
