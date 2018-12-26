package com.yz.dao;

import com.yz.model.UsTaskCard;
import com.yz.model.UsTaskCardDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UsTaskCardMapper {


    int insertUsTaskCard(UsTaskCard usTaskCard);

    int updateUsTaskCard(Map map);

    /**
     * 查询任务
     * @return
     */
    List<Map<String,Object>> selectReleaseTaskCard(@Param(value = "userId") String userId);
    /**
     * 查询已领取发布的任务
     * @return
     */
    List<Map<String,Object>> selectReceiveTaskCard(@Param(value = "userId") String userId);

    int insertUsTaskCardDetail(UsTaskCardDetail usTaskCardDetail);


    /**
     * 查询完成任务详情
     * @return
     */
    List<Map<String, Object>> selectUsTaskCardDetail(Map map);

    int selectUsTaskCardDetailCount(@Param(value = "userId") String userId,
                                                   @Param(value = "taskId") String taskId,
                                                   @Param(value = "triggerUserId") String triggerUserId);
}