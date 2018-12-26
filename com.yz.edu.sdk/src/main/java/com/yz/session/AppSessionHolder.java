package com.yz.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.constants.RedisKeyConstants;
import com.yz.model.SessionInfo;
import com.yz.redis.RedisService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc session 引用
 * @author Administrator
 *
 */
public class AppSessionHolder {

	private static Logger logger = LoggerFactory.getLogger(AppSessionHolder.class);
	
	public static RpcSessionOp RPC_SESSION_OPERATOR = new RpcSessionOp();
	
	public static final int SESSION_LIVE_TIME = 3600*24*15 ; // session 存活时间 15天

	/**
	 * 
	 * @param userId
	 *            用户Id
	 * @param info
	 *            用户登录信息
	 * @param op
	 */
	public static void setSessionInfo(String userId, SessionInfo info, SessionOperator op) {
		if (op != null) {
			op.setSession(info);
		}
	}

	/**
	 * @desc 获取当前的sessionInfo信息
	 * @param userId
	 *            用户id
	 * @param sessionProvider
	 *            获取sessionInfo提供者
	 * @return
	 */
	public static SessionInfo getSessionInfo(String userId, SessionOperator sessionProvider) {
		SessionInfo info = sessionProvider.getSessionLocal();
		if (info == null) {
			info = sessionProvider.getSession(userId);
		}
		if (info != null) {
			sessionProvider.setSession(info);
		}
		return info;
	}

	/**
	 * 从session中删除
	 * @param userId
	 * @param sessionProvider
	 */
	public static void delSessionInfo(String userId,SessionOperator sessionProvider){
		sessionProvider.delSession(userId);
	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static interface SessionOperator {
		/**
		 * @desc 提供默认实现方法
		 * @param userId
		 * @return SessionInfo
		 */
		default SessionInfo getSession(String userId) {
			SessionInfo userInfo = RedisService.getRedisService().getByte(userId, SessionInfo.class,RedisKeyConstants.REDIS_DB_INDEX_1);
			logger.debug("userInfo:{}", JsonUtil.object2String(userInfo));
			return userInfo;
		}

		/**
		 * 
		 * @param userId
		 * 
		 * @return SessionInfo
		 */
		default SessionInfo getSessionLocal() {
			return null;
		}

		/**
		 * 
		 * @param userId
		 * @param info
		 */
		default void setSession(SessionInfo info)// 存储sessionInfo信息
		{

		}
		/**
		 * 删除sessionInfo
		 * @param info
		 */
		default void delSession(String userId){
			RedisService.getRedisService().del(RedisKeyConstants.REDIS_DB_INDEX_1,StringUtil.getString2Utf8(userId));
		}
	}
}
