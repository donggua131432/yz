package com.yz.dao.statistics;

import com.yz.model.admin.BaseUser;
import com.yz.model.statistics.DataExportQuery;
import com.yz.model.statistics.StudentDataGKExcel;
import com.yz.model.statistics.StudentDataXBExcel;
import com.yz.model.statistics.StudentStatQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @描述: 数据导出
 */
public interface DataExportMapper {

    List<StudentDataXBExcel> exportStudentDataXB(DataExportQuery query);

    List<StudentDataGKExcel> exportStudentDataGK(DataExportQuery query);

    List<Map<String,String>> getDictName(@Param("pId") String pId);

}
