package com.yz.service.wechat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.wechat.GwWechatMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.wechat.GwWechatPublic;
import com.yz.model.wechat.GwWechatPublicQuery;

@Service
@Transactional
public class GwWechatPublicService {

	@Autowired
	private GwWechatMapper wechatMapper;

	/**
	 * 分页查询
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public IPageInfo<GwWechatPublic> selectWechatPublicByPage(int start, int length, GwWechatPublicQuery query) {
		PageHelper.offsetPage(start, length);
		List<GwWechatPublic> list = wechatMapper.selectPublicByPage(query);
		return new IPageInfo<GwWechatPublic>((Page<GwWechatPublic>) list);
	}
	
	public void addWechatPublic(GwWechatPublic wechat){
		wechatMapper.insertWechatPublic(wechat);
	}
	
	public void updateWechatPublic(GwWechatPublic wechat){
		wechatMapper.updateWechatPublic(wechat);
	}

	public GwWechatPublic selectWechatPubById(String pubId) {
		return wechatMapper.selectWechatPubById(pubId);
	}

}
