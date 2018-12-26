package com.yz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.model.SysParameter;




public interface SysParameterMapper {
	
    int deleteByPrimaryKey(String paramName);

    int insert(SysParameter record);

    int insertSelective(SysParameter record);

    @YzCache(useCache=true,cachePrefix="proxy.sysparameter.",cacheKey="${paramName}",expire=3600*24)
    SysParameter selectByPrimaryKey(@Param("paramName") String paramName);

    int updateByPrimaryKeySelective(SysParameter record);

    int updateByPrimaryKey(SysParameter record);

	List<SysParameter> selectAll(@Param("paramName") String paramName,@Param("paramValue") String paramValue);

	int deleteByNames(@Param("paramNames") String[] paramNames);
	
}