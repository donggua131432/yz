package com.yz.dao.zhimi;

import java.util.List;

import com.yz.model.condition.zhimi.ZhimiProductQuery;
import com.yz.model.condition.zhimi.ZhimiProductRecordsQuery;
import com.yz.model.zhimi.ZhimiProductInfo;
import com.yz.model.zhimi.ZhimiProductList;
import com.yz.model.zhimi.ZhimiProductRecords;

public interface ZhimiProductMapper {
	/**
	 * 查询充值产品
	 * @param queryInfo
	 * @return
	 */
	List<ZhimiProductList> getProductList(ZhimiProductQuery queryInfo);
	/**
	 * 查询智米充值产品信息
	 * @param productId
	 * @return
	 */
	ZhimiProductInfo getProductInfo(String productId);
	/**
	 * 添加智米充值产品
	 * @param productInfo
	 */
	int addProduct(ZhimiProductInfo productInfo);
	/**
	 * 更新智米充值产品
	 * @param productInfo
	 */
	int updateProduct(ZhimiProductInfo productInfo);
	/**
	 * 根据名称判断是否存在
	 * @param productInfo
	 * @return
	 */
	int countBy(ZhimiProductInfo productInfo);
	/**
	 * 新增排序
	 * @param sort
	 */
	int sortByAdd(ZhimiProductInfo productInfo);
	/**
	 * 更新排序
	 * @param productInfo
	 */
	int sortByUpdate(ZhimiProductInfo productInfo);
	/**
	 * 查询列表数量
	 * @return
	 */
	int getListCount();
	/**
	 * 查询充值记录
	 * @param queryInfo
	 * @return
	 */
	List<ZhimiProductRecords> getProductRecords(ZhimiProductRecordsQuery queryInfo);

}
