package com.yz.dao.goods;

import java.util.List;

import com.yz.model.goods.GsGoodsPurchasingInfo;
import com.yz.model.goods.GsGoodsPurchasingQuery;

public interface GsGoodsPurchasingMapper {

	public List<GsGoodsPurchasingInfo> getGsGoodsPurchasing(GsGoodsPurchasingQuery purchasing);
	
	public void initGoodsPurchasing(GsGoodsPurchasingInfo goodsInfo);
	
	public GsGoodsPurchasingInfo getPurchasingDetail(String id);
	
}
