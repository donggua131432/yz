package com.yz.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.model.system.SysParameter;


public interface SysParameterMapper {
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
    int deleteByPrimaryKey(String paramName);
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
    int insert(SysParameter record);
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
    int insertSelective(SysParameter record);

    SysParameter selectByPrimaryKey(String paramName);
    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
    int updateByPrimaryKeySelective(SysParameter record);
    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
    int updateByPrimaryKey(SysParameter record);

	List<SysParameter> selectAll(@Param("paramName") String paramName,@Param("paramValue") String paramValue);
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="param")
	int deleteByNames(@Param("paramNames") String[] paramNames);
	
}