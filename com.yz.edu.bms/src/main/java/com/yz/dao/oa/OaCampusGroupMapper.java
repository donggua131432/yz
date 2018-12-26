package com.yz.dao.oa;


import com.yz.model.oa.OaCampusGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OaCampusGroupMapper {

    List<OaCampusGroup> selectCampusGroupList(OaCampusGroup oaCampusGroup);

    List<String> selectCampusGroupByCampusId(@Param(value = "campusId") String campusId);

    OaCampusGroup selectCampusGroupById(String id);

    int deleteCampusGroupById(String id);

    int deleteCampusGroupByIds(@Param(value = "ids") String[] ids);

    int insertCampusGroup(OaCampusGroup oaCampusGroup);

    int updateCampusGroup(OaCampusGroup oaCampusGroup);
}