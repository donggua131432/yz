package com.yz.service.baseinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.baseinfo.BdProferssionTaMapper;
import com.yz.dao.baseinfo.BdTestAreaMapper;
import com.yz.dao.baseinfo.BdUnvsProfessionMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.baseinfo.BdProferssionTaKey;
import com.yz.model.baseinfo.BdTestArea;
import com.yz.model.baseinfo.BdUnvsProfession;
import com.yz.model.system.SysArea;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysProvince;

@Service
@Transactional
public class BdUnvsProfessionServiceImpl {

	@Autowired
	private BdUnvsProfessionMapper unvsProfessionMapper;

	@Autowired
	private BdProferssionTaMapper proferssionTaMapper;

	public BdUnvsProfession getUnvsProfession(String pfsnId) {
		return unvsProfessionMapper.selectByPrimaryKey(pfsnId);
	}

	public void deleteAllUnvsProfession(String[] idArray) {
		unvsProfessionMapper.deleteAllUnvsProfession(idArray);
	}

	public List<BdUnvsProfession> selectAll(BdUnvsProfession unvsProfession) {
		return unvsProfessionMapper.selectAll(unvsProfession);
	}

	public List<Map<String, String>> findAllKeyValue(String sName) {
		return unvsProfessionMapper.findAllKeyValue(sName);
	}

	public void opendownAllUnvsProfession(String[] idArray, String exType) {
		unvsProfessionMapper.opendownAllUnvsProfession(idArray, exType);
	}

	public void deleteUnvsProfession(String id) {
		unvsProfessionMapper.deleteByPrimaryKey(id);
	}

	public Map<String, Map<String, String>> getTestArea(String pfsnId) {
		// 获取专业考试区县信息
		List<Map<String, String>> mapList = proferssionTaMapper.getTestArea(pfsnId);

		Map<String, Map<String, String>> result = new HashMap<>();

		if (mapList != null && mapList.size() > 0) {
			for (Map<String, String> map : mapList) {
				result.put(map.get("taId"), map);
			}
		}

		return result;
	}

	public void updateBdUnvsProfession(BdUnvsProfession unvsProfession, String[] testAreaId) {
		unvsProfessionMapper.updateByPrimaryKeySelective(unvsProfession);

		deleteBdProfessionTa(unvsProfession.getPfsnId());
		if (testAreaId != null && testAreaId.length > 0)
			proferssionTaMapper.insertBatch(unvsProfession.getPfsnId(), testAreaId);
	}

	public void deleteBdProfessionTa(String pfsnId) {
		proferssionTaMapper.deleteBdProfessionTa(pfsnId);
	}

	public void addBdProfessionTa(String pfsnId, String id) {
		BdProferssionTaKey proferssionTaKey = new BdProferssionTaKey();
		proferssionTaKey.setPfsnId(pfsnId);
		proferssionTaKey.setTaId(id);
		proferssionTaMapper.insertSelective(proferssionTaKey);
	}

	public void addBdProfession(BdUnvsProfession unvsProfession, String[] testAreaId) {
		unvsProfession.setPfsnId(IDGenerator.generatorId());
		unvsProfessionMapper.insertSelective(unvsProfession);

		if (testAreaId != null && testAreaId.length > 0)
			proferssionTaMapper.insertBatch(unvsProfession.getPfsnId(), testAreaId);
	}

	public List<Map<String, String>> searchProfessionJson(String pfsnName, String unvsId, String pfsnLevel,
			String grade) {
		return unvsProfessionMapper.searchProfessionJson(pfsnName, unvsId, pfsnLevel, grade);
	}

	public BdUnvsProfession getParamByPfsnCode(String paramName) {
		return unvsProfessionMapper.getParamByPfsnCode(paramName);
	}

	public List<Map<String, String>> searchGradeJson(String sName, String pfsnName, String unvsId, String pfsnLevel) {
		return unvsProfessionMapper.searchGradeJson(sName, pfsnName, unvsId, pfsnLevel);
	}

	public List<Map<String, String>> findTestGroupByPfsnLevel(String pfsnLevel) {
		return unvsProfessionMapper.findTestGroupByPfsnLevel(pfsnLevel);
	}

	public String findTestSubjectByGroupId(String groupId) {
		return unvsProfessionMapper.findTestSubjectByGroupId(groupId);
	}

	@Autowired
	private BdTestAreaMapper testAreaMapper;

	public List<SysArea> getTestAreaProvice(String pfsnId, String unvsId) {
		SysArea sysArea = null;
		List<SysArea> resultList = new ArrayList<SysArea>();

		List<SysProvince> provinceList = unvsProfessionMapper.selectTestAreaProvince(unvsId);

		if (provinceList == null || provinceList.isEmpty()) {
			return resultList;
		}

		List<String> provinceIds = testAreaMapper.selectProfessionProvinceCode(pfsnId);

		List<String> cityIds = testAreaMapper.selectProfessionCityCode(pfsnId);

		List<String> taIds = testAreaMapper.selectTestAreaIds(pfsnId);

		// 第一级
		for (SysProvince sysProvince : provinceList) {

			String provinceCode = sysProvince.getProvinceCode();
			sysArea = new SysArea();
			sysArea.setId(provinceCode);
			sysArea.setpId("0");
			sysArea.setName(sysProvince.getProvinceName());
			sysArea.setIsShowCheck(false);
			sysArea.setFlag("P");

			if (provinceIds.contains(sysProvince.getProvinceCode())) {
				sysArea.setChecked(true);
			}
			resultList.add(sysArea);
		}

		List<SysCity> cityList = unvsProfessionMapper.selectTestAreaCity(unvsId);

		if (cityList == null || cityList.isEmpty()) {
			return resultList;
		}

		for (SysCity sysCity : cityList) {
			sysArea = new SysArea();
			sysArea.setId(sysCity.getCityCode());
			sysArea.setpId(sysCity.getProvinceCode());
			sysArea.setName(sysCity.getCityName());
			sysArea.setIsShowCheck(false);
			sysArea.setFlag("C");

			if (cityIds.contains(sysCity.getCityCode())) {
				sysArea.setChecked(true);
			}

			resultList.add(sysArea);

		}

		List<BdTestArea> taList = unvsProfessionMapper.selectTestArea(unvsId);

		if (taList == null || taList.isEmpty()) {
			return resultList;
		}

		for (BdTestArea taInfo : taList) {
			sysArea = new SysArea();
			sysArea.setId(taInfo.getTaId());
			sysArea.setpId(taInfo.getCityId());
			sysArea.setName(taInfo.getTaName());
			sysArea.setIsShowCheck(false);
			sysArea.setFlag("T");

			if (taIds.contains(taInfo.getTaId())) {
				sysArea.setChecked(true);
			}
			resultList.add(sysArea);
		}

		return resultList;
	}

	public List<Map<String, String>> searchAllowProfessionJson(String sName, String unvsId, String pfsnLevel,
														  String grade) {
		return unvsProfessionMapper.searchAllowProfessionJson(sName, unvsId, pfsnLevel, grade);
	}

}
