package com.yz.dao;

import java.util.List;
import java.util.Map;

import com.yz.model.UsConsults;

public interface UsConsultsMapper {

    int insertSelective(UsConsults record);
    
    List<Map<String, String>> selectMyConsults(String userId);

}