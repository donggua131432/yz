package com.yz.dao.wechat;

import java.util.List;

import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.wechat.GwWechatButton;
import com.yz.model.wechat.GwWechatMenu;
import com.yz.model.wechat.GwWechatMenuQuery;

public interface GwWechatMenuMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(GwWechatMenu record);

    GwWechatMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GwWechatMenu record);

	List<GwWechatMenu> selectWechatMenuByPage(GwWechatMenuQuery query);

	int deleteMenus(String[] ids);

	List<GwWechatMenu> sMenu(SelectQueryInfo sqInfo);
	
	GwWechatButton[] selectMenuByPubId(String pubId);
}