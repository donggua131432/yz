package com.yz.dao;

import com.yz.model.TestConfirm;
import com.yz.model.TestProveInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @作者: DuKai
 * @创建时间: 2017/11/23 19:29
 * @版本号: V1.0
 */
public interface BdsQueryScoreMapper {

    Map<String, Object> selectScoreInfo(Map map);

    List<Map<String, Object>> selectFinalScoreInfo(@Param("learnId") String learnId, @Param("semester") String semester);
}
