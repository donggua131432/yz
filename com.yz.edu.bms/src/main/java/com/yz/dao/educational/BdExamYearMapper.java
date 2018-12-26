package com.yz.dao.educational;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdExamReason;
import com.yz.model.educational.BdExamYear;

public interface BdExamYearMapper {

	List<BdExamYear> selectExamYearByPage();

	int insertExamYear(BdExamYear exam);

	int insertExamReasons(BdExamYear exam);

	BdExamYear selectExamYearById(String eyId);

	int updateExam(BdExamYear exam);

	int deleteExamReason(String eyId);

	int updateExamYearStatus(@Param("eyId") String eyId, @Param("status") String status);

	int deleteExamYear(String eyId);

	int deleteExamYears(@Param("eyIds") String[] eyIds);

	int deleteExamReasons(@Param("eyIds") String[] eyIds);

	int selectExamYearCount(String examYear);

	int deleteExamSubject(String eyId);

	int insertExamSubject(BdExamYear exam);

	int deleteExamSubjects(@Param("eyIds") String[] eyIds);

	int selectPlaceYearCount(String eyId);

	void updateExamReason(@Param("erId") String erId, @Param("reason") String reason);

	void deleteExamReasonByErId(@Param("erId") String erId);

	void insertExamReason(BdExamReason re);

}