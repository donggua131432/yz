package com.yz.dao;

import java.util.List;
import java.util.Map;
import com.yz.model.communi.Body;

public interface BaseInfoMapper {

	List<Map<String, String>> selectUnvs(Body queryInfo);

	List<Map<String, String>> selectProfession(Body queryInfo);

	List<Map<String, String>> selectTestArea(Body queryInfo);

}
