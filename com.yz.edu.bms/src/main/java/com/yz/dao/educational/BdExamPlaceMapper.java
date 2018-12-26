package com.yz.dao.educational;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.educational.BdExamPlace;
import com.yz.model.educational.BdPlaceInfo;
import com.yz.model.educational.BdPlaceTime;

public interface BdExamPlaceMapper {
	int deleteByPrimaryKey(String epId);

	int insert(BdExamPlace record);

	int insertSelective(BdExamPlace record);

	BdExamPlace selectByPrimaryKey(String epId);

	int updateByPrimaryKeySelective(BdExamPlace record);

	int updateByPrimaryKey(BdExamPlace record);

	List<BdExamPlace> selectExamRoomByPage(BdExamPlace query);

	int insertExamRoom(BdExamPlace exam);

	BdExamPlace selectExamRoomById(String epId);

	int updateStatus(@Param("epId") String epId, @Param("status") String status);

	int deleteExamRoom(@Param("epIds") String[] epIds);

	int deltePlaceInfoAndTimes(String epId);

	int insertPlaceInfoAndTimes(@Param("place") BdPlaceInfo place, @Param("times") List<BdPlaceTime> times);

	int updateStatusBatch(@Param("epIds") String[] epIds, @Param("status") String status);

	BdExamPlace selectExamRoomByCode(String epCode);

	int selectPlaceYearCountByEpId(String epId);

	List<String> getEmNameList(SelectQueryInfo sqInfo);

}