package com.yz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.model.SysErrorMessage;



public interface SysErrorMessageMapper {
    int deleteByPrimaryKey(String errorCode);

    int insert(SysErrorMessage record);

    int insertSelective(SysErrorMessage record);

    @YzCache(useCache=true,cachePrefix="proxy.syserrormessage.",cacheKey="${errorCode}",expire=3600*24)
    SysErrorMessage selectByPrimaryKey(@Param("errorCode") String errorCode);

    int updateByPrimaryKeySelective(SysErrorMessage record);

    int updateByPrimaryKey(SysErrorMessage record);

	List<SysErrorMessage> selectAll();

	void deleteAllSysErrorMessage(@Param("ids") String[] ids);

	List<SysErrorMessage> selectAll(SysErrorMessage sysErrorMessage);

	void deleteErrorMsg(String errorCode);
}