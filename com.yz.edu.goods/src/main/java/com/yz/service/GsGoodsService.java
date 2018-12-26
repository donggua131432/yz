package com.yz.service;


import com.yz.dao.GsGoodsMapper;
import com.yz.model.GsCourseGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品service
 * @author lx
 * @date 2017年7月27日 上午10:56:59
 */
@Service
@Transactional
public class GsGoodsService {

	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	

	public Object getGsGoodsDetailInfo(String goodsId){
		return gsGoodsMapper.getGsGoodsDetailInfo(goodsId);
	}
	
	public Map<String, String> getGsCourseGoods(String goodsId){
		Map<String, String> map = new HashMap<>();
		GsCourseGoods goods = gsGoodsMapper.getGsCourseGoods(goodsId);
		if(null != goods){
			map.put("address", goods.getAddress());
			map.put("courseType", goods.getCourseType());
			map.put("startTime", goods.getStartTime());
			map.put("location", goods.getLocation());
			map.put("endTime", goods.getEndTime());
		}
		return map;
	}
	public GsCourseGoods getGsActivitiesGoods(String goodsId){
		return gsGoodsMapper.getGsActivitiesGoods(goodsId);
	}
}
