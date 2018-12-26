package com.yz.dao;

import java.util.List;
import java.util.Map;

import com.yz.model.UsFollow;
import com.yz.model.UsFollowMb;
import org.apache.ibatis.annotations.Param;

public interface UsFollowMapper {

    int insertSelective(UsFollow record);

    UsFollow selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UsFollow record);

	List<Map<String, String>> selectList(String empId);
	
	int clearFollow(String empId);

	int deleteFollow(String userId);

    List<UsFollowMb> selectFollowMbByNameOrMobile(@Param("empId") String empId, @Param("nameOrMobile") String nameOrMobile);

}