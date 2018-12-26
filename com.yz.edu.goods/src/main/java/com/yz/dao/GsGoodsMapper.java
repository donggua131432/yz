package com.yz.dao;


import com.yz.model.GsCourseGoods;
import com.yz.model.GsSalesPlan;
import org.apache.ibatis.annotations.Param;

public interface GsGoodsMapper {

	Object getGsGoodsDetailInfo(@Param("goodsId") String goodsId);
	
	GsCourseGoods getGsCourseGoods(String goodsId);
	
	GsCourseGoods getGsActivitiesGoods(String goodsId);
	
	/**
	 * 修改库存
	 * @param count
	 * @param goodsId
	 */
	void updateGoodsCount(@Param("count") int count, @Param("goodsId") String goodsId);
	
	GsSalesPlan getGsSalesPlan(String planId);
	
	void updateGsSalesPlan(GsSalesPlan plan);
}
