package com.yz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.SysDict;

public interface SysDictMapper {
    int deleteByPrimaryKey(String dictId);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(String dictId);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);

	List<SysDict> selectByPid(String groupId);

	int delete(String dictId);

	List<String> getGroupIds();

	List<SysDict> selectAll();

	List<SysDict> selectAllButParent();

	void deleteAllSysDict(@Param("ids") String[] ids);

	List<SysDict> selectAllBySysDict(SysDict sysDict);
	
	List<SysDict> getParents(@Param("sName") String sName);
}