package com.yz.dao.invite;

import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.invite.InviteUserList;

import java.util.List;

/**
 * @描述: 校监分配粉丝
 * @作者: DuKai
 * @创建时间: 2017/12/4 17:32
 * @版本号: V1.0
 */
public interface InviteStayFansMapper {

    /**
     * 查询校监待分配的粉丝
     * @param queryInfo
     * @return
     */
    List<InviteUserList> getList(InviteUserQuery queryInfo);
}
