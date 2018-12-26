package com.yz.dao.enroll;

import com.yz.model.admin.BaseUser;
import com.yz.model.enroll.BdSceneConfirmQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @描述: 学员现场确认
 * @作者: DuKai
 * @创建时间: 2017/10/19 15:58
 * @版本号: V1.0
 */
public interface BdSceneConfirmMapper {

    List<Map<String, Object>> findSceneConfirmStdPage(@Param("queryInfo") BdSceneConfirmQuery queryInfo, @Param("user") BaseUser user);

    Map<String, Object> findSceneConfirmStd(@Param("stdId") String stdId, @Param("learnId") String learnId);

    Map<String, Object> findSceneConfirm(@Param("stdId") String stdId, @Param("learnId") String learnId);

    void addSceneConfirm(@Param("map") Map map);

    void updateSceneConfirm(@Param("map") Map map);

    List<Map<String, Object>> selectSceneConfirmAllList();

    void updateTestProveUrl(Map map);

    List<Map<String, Object>> selectDownloadProveList();

    void insertTestProveInfo(Map map);

}
