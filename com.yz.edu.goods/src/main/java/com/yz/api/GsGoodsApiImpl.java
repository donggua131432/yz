package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.GsCourseGoods;
import com.yz.model.YzService;
import com.yz.service.GsGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service(version="1.0",timeout=30000, retries=0)
public class GsGoodsApiImpl implements GsGoodsApi
{
	@Autowired
	private GsGoodsService gsGoodsService;
	
	@Override
	public Map<String, String> getGsCourseGoods(String goodsId){
		return gsGoodsService.getGsCourseGoods(goodsId);
	}

	@Override
	public Map<String, String> getGsActivitiesGoods(String goodsId){
		GsCourseGoods goodsInfo = gsGoodsService.getGsActivitiesGoods(goodsId);
		Map<String, String> map = new HashMap<>();
		if(null != goodsInfo){
			map.put("startTime", goodsInfo.getStartTime());
			map.put("endTime", goodsInfo.getEndTime());
			map.put("takeIn", goodsInfo.getTakein());
			map.put("address", goodsInfo.getAddress());
		}
		return map;
	}
}
