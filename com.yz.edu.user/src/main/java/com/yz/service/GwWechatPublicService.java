package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.dao.GwWechatPublicMapper;
import com.yz.model.GwWechatPublic;

@Service
public class GwWechatPublicService {

	@Value("${wehchat_pub.pub_id}")
	private String pubId;

	@Autowired
	private GwWechatPublicMapper pubMapper;


	public GwWechatPublic getPubInfo(String pubId) {
		return pubMapper.getPublicInfo(pubId);
	}

}
