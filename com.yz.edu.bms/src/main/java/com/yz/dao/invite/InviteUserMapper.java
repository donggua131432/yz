package com.yz.dao.invite;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.core.datasource.DB;
import com.yz.core.datasource.Database;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.condition.zhimi.ZhimiProductRecordsQuery;
import com.yz.model.invite.InviteUserList;
import com.yz.model.us.UsFollowLog;

public interface InviteUserMapper {

	/**
	 * 查询已分配的被邀约人与米瓣
	 * 
	 * @param queryInfo
	 * @return
	 */
	List<InviteUserList> getList(InviteUserQuery queryInfo);

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, String> getUserInfo(String userId);

	/**
	 * 查询用户Id
	 * 
	 * @param queryInfo
	 * @return
	 */
	List<String> getUserIds(ZhimiProductRecordsQuery queryInfo);

	/**
	 * 获取人事信息
	 * @param l
	 * @return
	 */
	@DB(db=Database.BDS)
	List<Map<String, String>> getOaInfo(UsFollowLog l);

	void updateIntentionType(@Param(value = "userId") String userId, @Param(value = "intentionType") String intentionType);

}
