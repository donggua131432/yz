package com.yz.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.model.common.PubInfo;
import com.yz.model.system.SysDictPlus;


public interface SysDictPlusMapper {
	/**
	 * 查询年级列表
	 * @param queryInfo
	 * @return
	 */
	List<SysDictPlus> getList(SysDictPlus queryInfo);
	/**
	 * 查询年级信息
	 * @param dictId
	 * @return
	 */
	SysDictPlus getOne(String dictId);
	/**
	 * 判断年级信息是否存在
	 * @param dictId
	 * @return
	 */
	int countBy(String dictId);
	/**
	 * 添加年级信息
	 * @param gradeInfo
	 * @param pubInfo 
	 */
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
	int add(SysDictPlus dict);
	/**
	 * 更新年级信息
	 * @param gradeInfo
	 * @param pubInfo 
	 */
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
	int update(SysDictPlus dict);
	/**
	 * 批量禁用启用
	 * @param gradeList
	 * @param pubInfo
	 * @return
	 */
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
	int batch(@Param("dictIds") String[] dictIds, @Param("isEnable") String isEnable, @Param("pubInfo") PubInfo pubInfo);
	/**
	 * 获取序列
	 * @param getpId
	 * @return
	 */
	 int seq(String pId);
	/**
	 * 
	 * @param dictName
	 * @param pId
	 * @return
	 */
	int countByName(@Param("dictName") String dictName, @Param("pId") String pId);
	/**
	 * 更新字典，不刷新缓存与json文件
	 * @param dict
	 */
	void updateUnCache(SysDictPlus dict);
}