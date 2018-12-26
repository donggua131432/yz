package com.yz.dao.markting;

import java.util.List;
import java.util.Map;

import com.yz.model.markting.BdMarktingJar;

public interface BdMarktingJarMapper {
    int deleteByPrimaryKey(String jarName);

    int insertSelective(BdMarktingJar record);

    BdMarktingJar selectByPrimaryKey(String jarName);

    int updateByPrimaryKeySelective(BdMarktingJar record);

	List<Map<String, String>> getList();

	void clearStatus();

	int countBy(String jarName);

	BdMarktingJar selectAllowJar();

}