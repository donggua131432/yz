package com.yz.dao.recruit;


import com.yz.model.admin.BaseUser;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentCheckGKMapper {


   List<Map<String,Object>> selectAllStudentInfoGKList(@Param(value = "queryInfo") StudentAnnexCheckQuery queryInfo,
                    @Param("user") BaseUser user, @Param("campusIdList") List<String> campusIdList);

   List<Map<String,Object>> selectAllLearnInfoGKList(@Param(value = "learnIds") String[] learnIds);

   void updateBdCheckRecordById(StudentCheckRecord studentCheckRecord);

   void updateIsDataCheckList(@Param(value = "bdLearnInfoList") List<BdLearnInfo> bdLearnInfoList);
   void updateBdCheckRecordList(@Param(value = "checkRecordList") List<BdCheckRecord> checkRecordList);
   void insertBdStudentModifyList(@Param(value = "bdStudentModifyList") List<BdStudentModify> bdStudentModifyList);

   void insertCheckRecordBatch(@Param(value = "studentCheckRecordList") List<StudentCheckRecord> studentCheckRecordList);
}