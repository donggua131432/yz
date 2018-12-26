package com.yz.dao.salarykpi;

import com.yz.model.salarykpi.StudentDetailQuery;
import com.yz.model.salarykpi.YearKPIQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface YearKPIMapper {
        List<Map<String,Object>> findAllYearKPI(YearKPIQuery query);

        int getKPIValue(@Param("ckSum") double ckSum);

        List<Map<String,Object>> findAllStudentDetail(StudentDetailQuery query);

        List<Map<String,Object>> gkStudentDetail(@Param("recruit")String recruit);

        List<Map<String,Object>> qtqeCkStudentDetail(@Param("recruit")String recruit);
}
