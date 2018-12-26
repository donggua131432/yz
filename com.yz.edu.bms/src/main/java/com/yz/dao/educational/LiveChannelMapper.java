package com.yz.dao.educational;


import com.yz.model.educational.LiveChannel;
import com.yz.model.educational.LiveChannelExport;
import com.yz.model.educational.LiveChannelQuery;
import com.yz.model.educational.LivePolyvInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface LiveChannelMapper {
    List<Map<String, Object>> findAllLiveChannel(LiveChannelQuery query);

    List<Map<String, Object>> findCourseByYear(@Param("sName") String sName, @Param("year") String year);

    int insert(LiveChannel liveChannel);

    int update(LiveChannel liveChannel);

    LiveChannel getById(@Param("lcId") String lcId);

    int countCourseId(@Param("courseId") String courseId);

    int deletes(@Param("lcIds") String[] lcIds);

    List<Map<String, Object>> findChannel(@Param("sName") String sName);

    List<Map<String, Object>> findLiveDate(@Param("channelId") String channelId);

    List<LiveChannelExport> getLiveChannelViewLog(@Param("livePolyvInfos") List<LivePolyvInfo> livePolyvInfos,@Param("courseId")String courseId);
}
