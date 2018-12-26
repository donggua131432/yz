package com.yz.dao.us;

import java.util.Map;

import com.yz.model.us.UsFollow;
import org.apache.ibatis.annotations.Param;

public interface UsFollowMapper {

    int insertSelective(UsFollow record);

    UsFollow selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UsFollow record);

	UsFollow selectByMobile(String mobile);

	String selectUserIdByMobile(String mobile);

	int inertEnrollLog(Map<String, String> enrollLog);


    /**
     * 根据员工的empId获取OpenId
     * @param empId
     * @return
     */
	String selectOpenIdByEmpId(@Param("empId") String empId);
	
	/**
	 * 获取openId
	 * @param userId
	 * @return
	 */
	String selectOpenIdByStdId(String stdId);

}