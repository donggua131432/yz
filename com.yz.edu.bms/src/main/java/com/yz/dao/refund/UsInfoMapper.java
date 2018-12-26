package com.yz.dao.refund;

import java.util.Map;


public interface UsInfoMapper {

	String selectUserOpenId(String userId);

	Map<String, String> selectUsBookAddress(String learnId);
}
