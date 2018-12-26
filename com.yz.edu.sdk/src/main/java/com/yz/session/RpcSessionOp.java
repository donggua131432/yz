package com.yz.session;

import com.alibaba.dubbo.rpc.RpcContext;
import com.yz.constants.RedisKeyConstants;
import com.yz.model.SessionInfo;
import com.yz.redis.RedisService;
import com.yz.serializ.FstSerializer; 

/**
 * 
 * @author Administrator
 *
 */
public class RpcSessionOp implements AppSessionHolder.SessionOperator {

	public static final String CURENT_USER_SESSION_KEY = "yz-rpc-session-user";


	
	public void  setSession(SessionInfo info)
	{
		String userId = info.getUserId();
		RpcContext.getContext().set(CURENT_USER_SESSION_KEY, info);
		RedisService.getRedisService().setByte(userId, FstSerializer.getInstance().serialize(info), AppSessionHolder.SESSION_LIVE_TIME,RedisKeyConstants.REDIS_DB_INDEX_1);

	}
	
	public SessionInfo getSessionLocal(String userId) {
		return (SessionInfo) RpcContext.getContext().get(CURENT_USER_SESSION_KEY);
	}
}
