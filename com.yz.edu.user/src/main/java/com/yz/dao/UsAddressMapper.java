package com.yz.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.UsAddress;

public interface UsAddressMapper {

    int insertAddress(UsAddress record);

    UsAddress selectByPrimaryKey(String saId);

    int updateAddress(UsAddress record);

	int countBy(String userId);

	int deleteAddress(String saId);

	List<Map<String, String>> getAddressList(@Param("userId") String userId,@Param("saType") String saType);
	
	void clearDefault(@Param("userId") String userId,@Param("saType") String saType);

	Map<String, String> getAddress(String saId);

}