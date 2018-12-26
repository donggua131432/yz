package com.yz.dao.statistics;

import com.yz.model.admin.BaseUser;
import com.yz.model.statistics.StudentStatQuery;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @描述: 学员统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 12:32
 * @版本号: V1.0
 */
public interface StudentStatMapper {

    /**
     * 学员统计细信息集合
     */
	
    List<Map<String,Object>> selectStudentStat(@Param("queryInfo") StudentStatQuery queryInfo,@Param("user") BaseUser user);

    Map<String,Object> selectStudentStatTotal(@Param("queryInfo") StudentStatQuery queryInfo,@Param("user") BaseUser user);

}
