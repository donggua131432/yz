package com.yz.dao;

import java.util.List;
import java.util.Map;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 14:38
 * @版本号: V1.0
 */
public interface BdsFullScholarshipMapper {

    Map<String, Object>  selectEnrolmentCount(String scholarship);

    List<Map<String, Object>>  selectNewEnrolmentList(String scholarship);
}
