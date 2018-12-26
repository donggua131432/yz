package com.yz.job.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TaskCardMapper {
    /**
     * 查询未结束的任务列表
     * @return
     */
    List<Map<String,Object>> selectBdTaskCard();

    void updateBdTaskCard(Map map);

    List<Map<String,Object>> selectUsTaskCard();

    void updateIsAward(@Param("id") String id);
}
