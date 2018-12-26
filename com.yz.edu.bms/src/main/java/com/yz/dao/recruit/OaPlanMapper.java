package com.yz.dao.recruit;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.recruit.OaPlan;

public interface OaPlanMapper {
	int deleteByPrimaryKey(String planId);

	int insert(OaPlan record);

	int insertSelective(OaPlan record);

	OaPlan selectByPrimaryKey(String planId);

	int updateByPrimaryKeySelective(OaPlan record);

	int updateByPrimaryKey(OaPlan record);

	List<HashMap<String, String>> selectPlanByType(@Param(value = "plan") OaPlan plan,
			@Param(value = "planType") String planType);

	int deletePlans(@Param(value = "planIds") String[] planIds);

}