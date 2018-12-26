package com.yz.dao.educational;

import com.yz.model.educational.BdExamYearProfession;
import com.yz.model.educational.BdPlaceYear;
import com.yz.model.educational.ExamRoomSeatsQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface BdExamYearProfessionMapper {
    int insert(BdExamYearProfession profession);

    BdExamYearProfession getProfession(@Param("eyId") String eyId, @Param("grade") String grade, @Param("unvsId") String unvsId, @Param("pfsnLevel") String pfsnLevel, @Param("pfsnId") String pfsnId);

    int updateCode(@Param("eypId") String eypId, @Param("eypCode") String eypCode, @Param("updateUser") String updateUser, @Param("updateUserId") String updateUserId, @Param("updateTime") Date updateTime);

    String selectExamYearId(String examYear);

    Map<String, String> selectProfession(@Param("grade") String grade, @Param("unvsName") String unvsName, @Param("pfsnLevel") String pfsnLevel, @Param("pfsnName") String pfsnName);
}
