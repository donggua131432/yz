package com.yz.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yz.model.educational.BdStudentTScore;
import com.yz.model.educational.BdStudentTScoreYZ;

public interface BdStudentTScoreMapper {
	
	/**
	 * 根据学业编号，课程编号，学期查询唯一记录
	 * @param learnId
	 * @param courseId
	 * @param semester
	 * @return
	 */
	BdStudentTScore findStudentScoreByUnionKey(@Param("learnId") String learnId, @Param("courseId") String courseId, @Param("semester") String semester);
	
	List<BdStudentTScoreYZ> GetPreScoringResaulByPage(@Param("queryWhere") String queryWhere);
	
	
	int deleteByPrimaryKey(String tscoreId);

    int insert(BdStudentTScore record);

    int insertSelective(BdStudentTScore record);

    BdStudentTScore selectByPrimaryKey(String tscoreId);

    int updateByPrimaryKeySelective(BdStudentTScore record);

    int updateByPrimaryKey(BdStudentTScore record);
    
    Map<String, String> selectLearnInfoByLearnId(String learnId);
    

}
