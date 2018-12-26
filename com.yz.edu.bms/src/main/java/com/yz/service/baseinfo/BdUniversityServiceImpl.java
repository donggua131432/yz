package com.yz.service.baseinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.baseinfo.BdTestAreaMapper;
import com.yz.dao.baseinfo.BdUniversityMapper;
import com.yz.dao.baseinfo.BdUniversityTestMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.baseinfo.BdUniversity;
import com.yz.model.baseinfo.BdUniversityTest;

@Service
@Transactional
public class BdUniversityServiceImpl {

	@Autowired
	private BdUniversityMapper bdUniversityMapper;

	@Autowired
	private BdTestAreaMapper testAreaMapper;

	@Autowired
	private BdUniversityTestMapper universityTestMapper;

	public List<BdUniversity> selectAll(BdUniversity bdUniversity) {
		return bdUniversityMapper.selectAll(bdUniversity);
	}

	public BdUniversity getBdUniversity(String unvsId) {
		// TODO Auto-generated method stub
		return bdUniversityMapper.selectByPrimaryKey(unvsId);
	}

	public void updateBdUniversity(BdUniversity bdUniversity) {
		// TODO Auto-generated method stub
		bdUniversityMapper.updateByPrimaryKeySelective(bdUniversity);
	}

	public void addBdUniversity(BdUniversity bdUniversity) {
		bdUniversity.setUnvsId(IDGenerator.generatorId());
		bdUniversityMapper.insertSelective(bdUniversity);
	}

	public void deleteAllBdUniversity(String[] idArray) {
		// TODO Auto-generated method stub
		bdUniversityMapper.deleteAllBdUniversity(idArray);
	}

	public void deleteBdUniversity(String id) {
		// TODO Auto-generated method stub
		bdUniversityMapper.deleteByPrimaryKey(id);
	}

	public String getTestArea(String unvsId) {
		List<BdUniversityTest> resultList = universityTestMapper.getTestArea(unvsId);
		String testAreaId = "";
		for (BdUniversityTest bdUniversityTest : resultList) {
			testAreaId += bdUniversityTest.getTaId() + "#";
		}
		return testAreaId;
	}

	public void updateBdUniversityTest(String unvsId, String testAreaId) {
		// TODO Auto-generated method stub
		universityTestMapper.updateBdUniversityTest(unvsId, testAreaId);
	}

	public void addBdUniversityTest(String unvsId, String testAreaId) {
		BdUniversityTest universityTest = new BdUniversityTest();
		universityTest.setTaId(testAreaId);
		universityTest.setUnvsId(unvsId);
		universityTestMapper.insertSelective(universityTest);
	}

	public void batchAddBdUniversityTest(String unvsId, String[] taIds) {
		deleteBdUniversityTest(unvsId);
		universityTestMapper.insertBatch(unvsId, taIds);
	}

	public void deleteBdUniversityTest(String unvsId) {
		universityTestMapper.deleteBdUniversityTest(unvsId);
	}

	public List<Map<String, String>> findAllKeyValue(String sName) {
		return bdUniversityMapper.findAllKeyValue(sName);
	}

	public String getBdUniversityByName(String unvsId) {
		return bdUniversityMapper.getBdUniversityByName(unvsId);
	}

	public List<Map<String, String>> searchUniversity(String unvsName) {
		return bdUniversityMapper.searchUniversity(unvsName);
	}

	public Map<String, Map<String, String>> getTaMap(String unvsId) {
		Map<String, Map<String, String>> result = new HashMap<>();
		// 获取考试区县id
		List<Map<String, String>> resultList = universityTestMapper.getTaMap(unvsId);
		for (Map<String, String> taMap : resultList) {
			String taId = taMap.get("taId");
			result.put(taId, taMap);
		}

		return result;
	}

}
