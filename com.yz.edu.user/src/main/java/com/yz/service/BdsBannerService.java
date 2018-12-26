package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.BdsBannerMapper;

import net.sf.json.JSONArray;

@Service
@Transactional
public class BdsBannerService {

	@Autowired
	private BdsBannerMapper bannerMapper;

	public Object selectBanner() {
		return JSONArray.fromObject(bannerMapper.selectBanner());
	}

}
