package com.yz.dao;

import com.yz.model.UsWechatFans;

public interface UsWechatFansMapper {
    int deleteByPrimaryKey(String openId);

    int insert(UsWechatFans record);

    int insertSelective(UsWechatFans record);

    UsWechatFans selectByPrimaryKey(String openId);

    int updateByPrimaryKeySelective(UsWechatFans record);

    int updateByPrimaryKey(UsWechatFans record);
    /**
     * 查询微信信息是否存在
     * @param openId
     * @return
     */
	int countBy(String openId);
	
}