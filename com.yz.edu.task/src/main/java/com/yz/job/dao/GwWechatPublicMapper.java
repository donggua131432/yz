package com.yz.job.dao;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.constants.CommonConstants;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.job.model.GwWechatPublic;
 

public interface GwWechatPublicMapper {
 
    /**
     * 获取公众号配置信息
     * @param pubId
     * @return
     */
	@YzCache(cacheHandler=CommonConstants.MEMORY_CACHE_HANDLER,useCache=true,cacheKey="yz.wechat-${pubId}" ,expire = 3600 * 24 )
    public GwWechatPublic getPublicInfo(@Param(value="pubId")String pubId);
     
	/**
	 * 查询微信消息模板
	 * @param templateId
	 * @return
	 */
	@YzCache(cacheHandler=CommonConstants.MEMORY_CACHE_HANDLER,useCache=true,cacheKey="yz.wechat.msg-${templateId}",expire = 3600 * 24 )
	GwWechatMsgTemplate selectMsgTemplate(@Param(value="templateId")String templateId);
}