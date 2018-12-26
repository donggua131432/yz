package com.yz.dao.transfer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdStudentChange;
import com.yz.model.transfer.BdStudentChangeMap;
import com.yz.model.transfer.BdStudentChangeQuery;

public interface BdStudentChangeMapper {
	int deleteByPrimaryKey(String changeId);

	int insert(BdStudentChange record);

	int insertSelective(BdStudentChange record);

	BdStudentChange selectByPrimaryKey(String changeId);

	int updateByPrimaryKeySelective(BdStudentChange record);

	int updateByPrimaryKey(BdStudentChange record);

	Map<String, String> findBdStudentChange(@Param("learnId") String learnId, @Param("stdName") String stdName,
			@Param("phone") String phone, @Param("idCard") String idCard);

	List<BdStudentChange> findAllBdStudentChange(@Param("change") BdStudentChangeQuery query,
			@Param("user") BaseUser user);

	Map<String, String> findTransferByStdId(@Param("learnId") String learnId);

	List<Map<String, String>> findStudentInfo(@Param("sName") String sName, @Param("user") BaseUser user);

	void updateBSEByLearbId(BdStudentChangeMap studentChangeMap);

	void updateBLIByLearbId(BdStudentChangeMap studentChangeMap);

	void updateBSCByLearbId(BdStudentChangeMap studentChangeMap);

	int addBLI(BdStudentChangeMap studentChangeMap);

	void addBSE(BdStudentChangeMap studentChangeMap);

	void addBSC(BdStudentChangeMap studentChangeMap);

	void exitStudent(String oldLearnId);

	String selectRecruitTypeByUnvsId(String unvsId);

	List<String> findStudentGrade(String grade);

	String selectTutorSerialAmount(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	String selectTutorOrderAmount(@Param("learnId") String learnId, @Param("itemCode") String itemCode);

	Map<String, String> selectLearnId(@Param("idCard") String idCard, @Param("grade") String grade);

	String selectUnvsId(String unvsName);

	String selectPfsnId(@Param("unvsId") String unvsId, @Param("pfsnName") String pfsnName,
			@Param("pfsnLevel") String pfsnLevel, @Param("grade") String grade);

	int selectLearnInfoCount(@Param("stdId")String stdId, @Param("grade")String grade);
}