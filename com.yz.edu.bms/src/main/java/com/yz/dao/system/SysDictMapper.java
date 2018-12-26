package com.yz.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.cache.common.YzCache;
import com.yz.model.system.SysDict;


public interface SysDictMapper {
	@YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
    int deleteByPrimaryKey(String dictId);

    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
    int insert(SysDict record);

    /**
     * 增加数据字典
     * @param record
     * @return
     */
    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(String dictId);
    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
    int updateByPrimaryKeySelective(SysDict record);
    @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
    int updateByPrimaryKey(SysDict record);

	List<SysDict> selectByPid(String groupId);
	 @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
	int delete(String dictId);

	List<String> getGroupIds();

	List<SysDict> selectAll();

	List<SysDict> selectAllButParent();
	 @YzCache(useCache=true,cacheHandler="bmsCacheHandler",cacheRelation="dict")
	void deleteAllSysDict(@Param("ids") String[] ids);

	List<SysDict> selectAllBySysDict(SysDict sysDict);
	
	List<SysDict> getParents(@Param("sName") String sName);
}