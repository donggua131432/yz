package com.yz.dao.statistics;

import com.yz.model.condition.stdService.StudentStudyingQuery;

import java.util.List;
import java.util.Map;

/**
 * @描述: 在读学员应缴费用统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 12:32
 * @版本号: V1.0
 */
public interface StdStudyFeeStatMapper {

    /**
     * 在读学员应缴费用集合
     */
    List<Map<String,Object>> selectStdStudyFeeStat(StudentStudyingQuery studentStudyingQuery);

}
