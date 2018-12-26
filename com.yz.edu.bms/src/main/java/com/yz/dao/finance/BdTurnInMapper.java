package com.yz.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.BdTurnIn;
import com.yz.model.finance.BdTurnInQuery;

public interface BdTurnInMapper {
	int deleteByPrimaryKey(String turnId);

	int insert(BdTurnIn record);

	int insertSelective(BdTurnIn record);

	BdTurnIn selectByPrimaryKey(String turnId);

	int updateByPrimaryKeySelective(BdTurnIn record);

	int updateByPrimaryKey(BdTurnIn record);

	List<BdTurnIn> selectTurnInfo(BdTurnInQuery query);

	List<String> selectLearnId(@Param("idCard") String idCard, @Param("grade") String grade);

	int selectTurnInCount(@Param("learnId") String learnId, @Param("schoolYear") String schoolYear);

	BdTurnIn selectStdInfo(@Param("learnId") String learnId, @Param("schoolYear") String schoolYear);

	String selectTurnId(@Param("learnId") String learnId, @Param("schoolYear") String schoolYear);
}