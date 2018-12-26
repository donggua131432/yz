package com.yz.dao.educational;

import com.yz.model.educational.BdPlaceInfo;
import com.yz.model.educational.BdPlaceYear;
import com.yz.model.educational.ExamRoomAssign;
import com.yz.model.educational.ExamRoomAssignQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface BdExamRoomAssignMapper {
    List<ExamRoomAssign> findAllExamRoomAssign(ExamRoomAssignQuery query);

    int updateStatus(@Param("pyIds") String[] pyIds, @Param("status") String status);

    int deleteExamRoomAssign(@Param("pyIds") String[] pyIds);

    BdPlaceYear getById(String pyId);

    List<Map<String, String>> findAllKeyValue(@Param("sName")String sName);

    int update(BdPlaceYear bdPlaceYear);

    int insert(BdPlaceYear bdPlaceYear);

    int getMaxExamPlaceNum(@Param("placeId")String placeId);

    int countExamAffirm(@Param("pyIds") String[] pyIds);

    int countExamAssign(@Param("eyId") String eyId,@Param("placeId") String placeId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

	int deleteExamClass(String pyId);

	int selectClassCountByPyId(String pyId);

	String selectPyCodeByPyId(String pyId);

    BdPlaceYear getBdPlaceYear(@Param("eyId") String eyId, @Param("placeId") String placeId, @Param("startTime")String startTime, @Param("endTime")String endTime);

    List<Map<String, String>> findAllKeyValueByEyId(@Param("sName")String sName, @Param("eyId")String eyId, @Param("provinceCode") String provinceCode, @Param("cityCode") String cityCode, @Param("districtCode") String districtCode);

    List<Map<String, String>> findExamTime(@Param("eyId")String eyId,@Param("epId")String epId);

    List<Map<String, String>> findExamRoomAssignPyId(ExamRoomAssignQuery query);
}
