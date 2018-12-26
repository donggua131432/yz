package com.yz.service.oa;

import com.yz.dao.oa.OaCampusGroupMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaCampusGroup;
import com.yz.model.oa.OaCampusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @描述: 校区组管理
 * @作者: DuKai
 * @创建时间: 2018/5/23 11:44
 * @版本号: V1.0
 */
@Service
public class OaCampusGroupService {


    @Autowired
    private OaCampusGroupMapper oaCampusGroupMapper;


    /**
     * 分页查询
     * @param start
     * @param length
     * @param oaCampusGroup
     * @return
     */
    public IPageInfo getCampusGroupList(int start, int length, OaCampusGroup oaCampusGroup) {
        PageHelper.offsetPage(start, length);
        List<OaCampusGroup> campus = oaCampusGroupMapper.selectCampusGroupList(oaCampusGroup);
        return new IPageInfo((Page) campus);
    }


    public OaCampusGroup getOaCampusGroup(String id){
        return oaCampusGroupMapper.selectCampusGroupById(id);
    }
    /**
     * 添加校区分组
     * @param oaCampusGroup
     */
    public void addCampusGroup(OaCampusGroup oaCampusGroup){
        oaCampusGroupMapper.insertCampusGroup(oaCampusGroup);
    }

    public void updateCampusGroup(OaCampusGroup oaCampusGroup){
        oaCampusGroupMapper.updateCampusGroup(oaCampusGroup);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteCampusGroup(String id){
        oaCampusGroupMapper.deleteCampusGroupById(id);
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteCampusGroupBatch(String[] ids){
        oaCampusGroupMapper.deleteCampusGroupByIds(ids);
    }

}
