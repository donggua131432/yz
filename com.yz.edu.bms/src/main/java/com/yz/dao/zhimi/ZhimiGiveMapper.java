package com.yz.dao.zhimi;

import com.yz.model.admin.BaseUser;
import com.yz.model.zhimi.ZhimiGive;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZhimiGiveMapper {
    int insertSelective(ZhimiGive zhimiGive);

    List<ZhimiGive> selectGiveZhimiByPage(@Param("zhimiGive") ZhimiGive zhimiGive,@Param("user") BaseUser user);

    ZhimiGive selectZhimiGiveInfo(@Param("id") String userId);

    int deleteGiveRecords(@Param("ids") String[] ids);

    /**
     * 获取用户信息
     * @return
     */
    List<Map<String, String>> findKeyValueUser(@Param("eName") String eName, @Param("user") BaseUser user);

    int updateZhimiGive(ZhimiGive zhimiGive);
}