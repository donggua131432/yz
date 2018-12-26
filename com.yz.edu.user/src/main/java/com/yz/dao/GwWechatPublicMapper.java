package com.yz.dao;

import com.yz.model.GwWechatPublic;

public interface GwWechatPublicMapper {

    /**
     * 获取公众号配置信息
     * @param pubId
     * @return
     */
    public GwWechatPublic getPublicInfo(String pubId);
    
}