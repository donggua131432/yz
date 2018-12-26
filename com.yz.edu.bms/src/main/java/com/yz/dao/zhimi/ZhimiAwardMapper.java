package com.yz.dao.zhimi;

import java.util.List;
import java.util.Map;

import com.yz.cache.common.YzCache;
import com.yz.model.condition.zhimi.ZhimiAwardQueryInfo;
import com.yz.model.condition.zhimi.ZhimiAwardRecordsQuery;
import com.yz.model.zhimi.ZhimiAwardInfo;
import com.yz.model.zhimi.ZhimiAwardList;
import com.yz.model.zhimi.ZhimiAwardRecords;

public interface ZhimiAwardMapper {
	/**
	 * 查询赠送规则
	 * @param queryInfo
	 * @return
	 */
	List<ZhimiAwardList> getList(ZhimiAwardQueryInfo queryInfo);
	/**
	 * 新增赠送规则
	 * @param awardInfo
	 */
	//@YzCache(useCache=true,cacheHandler="zhimiAwardCacheHandler",cacheKey="${ruleGroup}")
	void addAwardInfo(ZhimiAwardInfo awardInfo);
	/**
	 * 更新赠送规则
	 * @param awardInfo
	 */
	//@YzCache(useCache=true,cacheHandler="zhimiAwardCacheHandler",cacheKey="${ruleGroup}")
	void updateAwardInfo(ZhimiAwardInfo awardInfo);
	/**
	 * 获取赠送规则信息
	 * @param ruleCode
	 * @return
	 */
	ZhimiAwardInfo getAwardInfo(String ruleCode);
	/**
	 * 判断规则编码是否存在
	 * @param ruleCode
	 * @return
	 */
	int countBy(String ruleCode);
	/**
	 * 查询奖励记录
	 * @param queryInfo
	 * @return
	 */
	List<ZhimiAwardRecords> getRecordsList(ZhimiAwardRecordsQuery queryInfo);
	
	/**
	 * 更新智米赠送规则,针对启用或者禁用
	 * @param awardInfo
	 */
	//@YzCache(useCache=true,cacheHandler="zhimiAwardCacheHandler",cacheKey="${ruleGroup}")
    void zhimiAwardToggle(ZhimiAwardInfo awardInfo);
    
    /**
     * 获取系统字典中的智米赠送额外属性
     * @return
     */
    List<Map<String, String>> getRuleGroupAttrList();
    
    /**
     * 同组的奖励规则信息
     * @param ruleGroup
     * @return
     */
    List<ZhimiAwardInfo> getAwardListInfo(String ruleGroup);

}
