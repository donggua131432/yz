package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import com.yz.model.educational.BdStudentEScore;
import com.yz.model.educational.BdStudentEScoreReceive;
import com.yz.model.educational.BdStudentScoreMap;

public interface BdStudentEScoreMapper {
    int deleteByPrimaryKey(String escoreId);

    int insert(BdStudentEScore record);

    int insertSelective(BdStudentEScore record);

    BdStudentEScore selectByPrimaryKey(String escoreId);

    int updateByPrimaryKeySelective(BdStudentEScore record);

    int updateByPrimaryKey(BdStudentEScore record);

	List<BdStudentScoreMap> selectAll(BdStudentScoreMap studentScoreMap);

	List<BdStudentEScore> findStudentScore(String learnId);

	void updateStudentScore(BdStudentEScore bdStudentEScore);

	List<Map<String, String>> selectStudentExamCourse(String learnId);

	int insertStudentScore(BdStudentEScoreReceive scores);

	String selectStdIdByLearnId(String learnId);

	String getScholarshipSelectedStatus(String learnId);
}