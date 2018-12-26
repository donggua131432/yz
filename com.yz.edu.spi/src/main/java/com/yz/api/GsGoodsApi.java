package com.yz.api;

import java.util.Map;

import com.yz.exception.IRpcException;

public interface GsGoodsApi
{

	public Map<String, String> getGsCourseGoods(String goodsId) throws IRpcException;
	
	public Map<String, String> getGsActivitiesGoods(String goodsId) throws IRpcException;
}
