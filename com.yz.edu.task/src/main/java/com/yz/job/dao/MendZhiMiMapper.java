package com.yz.job.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @描述: 智米补送
 * @作者: DuKai
 * @创建时间: 2018/5/29 9:55
 * @版本号: V1.0
 */
public interface MendZhiMiMapper {

    List<String> selectAwardRecordByRuleCode(@Param(value = "ruleCode") String ruleCode);
    List<Map<String,Object>> selectEvenReadMendStdList();
    List<Map<String,Object>> selectReferralStdList();
    List<Map<String,Object>> selectReferralByPidList();
}
