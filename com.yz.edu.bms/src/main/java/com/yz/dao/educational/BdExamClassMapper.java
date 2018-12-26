package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import com.yz.model.educational.BdExamClassExcel;
import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdExamClass;
import com.yz.model.educational.BdExamClassQuery;

public interface BdExamClassMapper {

	List<Map<String, String>> selectAllExamClass(BdExamClassQuery query);

	BdExamClass selectExamClassByPyId(String pyId);

	int updateDivideRemark(@Param("pyId") String pyId, @Param("divideRemark") String divideRemark);

	int insertExamClass(BdExamClass examClass);

	int deleteExamClassByPyId(String pyId);

	List<Map<String, String>> selectEpName(@Param("eyId") String eyId, @Param("cityCode") String cityCode, @Param("districtCode") String districtCode);

	List<BdExamClassExcel> getExamClass(@Param("eyId") String eyId, @Param("cityCode") String cityCode, @Param("districtCode") String districtCode, @Param("epId") String epId);
}