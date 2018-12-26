package com.yz.dao.wechat;

import java.util.List;

import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.wechat.GwWechatPublic;
import com.yz.model.wechat.GwWechatPublicQuery;

public interface GwWechatMapper {

	List<GwWechatPublic> selectPublicByPage(GwWechatPublicQuery query);

	int insertWechatPublic(GwWechatPublic wechat);

	int updateWechatPublic(GwWechatPublic wechat);

	GwWechatPublic selectWechatPubById(String pubId);

	List<GwWechatPublic> queryWechatPublic(SelectQueryInfo sqInfo);

}
