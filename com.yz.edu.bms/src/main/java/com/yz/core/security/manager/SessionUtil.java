package com.yz.core.security.manager;

import com.yz.core.security.constants.SecurityConstant;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.redis.RedisService;
import com.yz.serializ.FstSerializer;
import com.yz.util.StringUtil;

public class SessionUtil {

	private static final int SESSION_TIMEOUT = 3 * 3600;
	private static final int VALID_CODE_TIMEOUT = 120;

	/**
	 * 获取session用户
	 * 
	 * @return
	 */
	public static BaseUser getUser() {
		return RedisService.getRedisService().getByte(RequestUtil.getSessionId(), BaseUser.class);
	}

	/**
	 * 删除用户session
	 * 
	 * @param user
	 * @param timeout
	 */
	public static void delUser() {
		RedisService.getRedisService().del(StringUtil.getString2Utf8(RequestUtil.getSessionId()));
	}

	/**
	 * 获取验证码
	 * 
	 * @return
	 */
	public static String getValidCode() {
		return RedisService.getRedisService().get(SecurityConstant.VALID_CODE_PREFIX + RequestUtil.getSessionId());
	}

	/**
	 * 设置用户
	 * 
	 * @param user
	 * @param timeout
	 */
	public static void setUser(BaseUser user) {
		RedisService.getRedisService().setex(RequestUtil.getSessionId(), FstSerializer.getInstance().serialize(user),
				SESSION_TIMEOUT);
	}

	/**
	 * 存入验证码
	 * 
	 * @param validCode
	 * @param timeout
	 */
	public static void setValidCode(String validCode) {
		RedisService.getRedisService().set(SecurityConstant.VALID_CODE_PREFIX + RequestUtil.getSessionId(), validCode);
	}

	/**
	 * 存入Token
	 * 
	 * @param groupId
	 * @param token
	 */
	public static void pushToken(String groupId, String token) {
		// MapCacheUtil map =
		// RemoteCacheUtilFactory.map(RequestUtil.getSessionId()).setLiveTime(24
		// * 3600);
		// map.put(groupId, token);
		RedisService.getRedisService().setex(RequestUtil.getSessionId() + "-" + groupId, token, 24 * 3600);
	}

	/**
	 * 提取Token并移除原有token
	 * 
	 * @param groupId
	 * @return
	 */
	public static String pullToken(String groupId) {
		return RedisService.getRedisService().getAndRem(RequestUtil.getSessionId() + "-" + groupId);
	}

	/**
	 * 设置用户登录的唯一标识
	 * 
	 * @param userId
	 * @param timeout
	 */
	public static void setKey(String userId) {
		// SimpleCacheUtil simple =
		// RemoteCacheUtilFactory.simple().setLiveTime(SESSION_TIMEOUT);
		// simple.put(SecurityConstant.SESSION_KEY_PREFIX + userId,
		// RequestUtil.getSessionId());
		RedisService.getRedisService().setex(SecurityConstant.SESSION_KEY_PREFIX + userId, RequestUtil.getSessionId(),
				SESSION_TIMEOUT);
	}

	/**
	 * 获取用户登录唯一标识
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKey(String userId) {
		// SimpleCacheUtil simple = RemoteCacheUtilFactory.simple();
		// return (String) simple.get(SecurityConstant.SESSION_KEY_PREFIX +
		// userId);
		return RedisService.getRedisService().get(SecurityConstant.SESSION_KEY_PREFIX + userId);
	}

	/**
	 * 删除用户登录唯一标识
	 * 
	 * @param userId
	 */
	public static void delKey(String userId) {
		// SimpleCacheUtil simple = RemoteCacheUtilFactory.simple();
		// simple.del(SecurityConstant.SESSION_KEY_PREFIX + userId);
		RedisService.getRedisService().del(SecurityConstant.SESSION_KEY_PREFIX + userId);
	}

	/**
	 * 更新session生存时间
	 * 
	 * @param sessionId
	 * @param userId
	 */
	public static void freshTime(String sessionId, String userId) {
		/*
		 * SimpleCacheUtil simple =
		 * RemoteCacheUtilFactory.simple().liveTime(SESSION_TIMEOUT);
		 * simple.freshTime(SecurityConstant.SESSION_KEY_PREFIX + userId);
		 * simple.freshTime(sessionId);
		 */
		RedisService.getRedisService().expire(SecurityConstant.SESSION_KEY_PREFIX + userId, SESSION_TIMEOUT);
	}

	/**
	 * 清除用户缓存
	 * 
	 * @param userId
	 */
	public static void clearUser(String userId) {
		String sessionId = getKey(userId);
		if (!StringUtil.hasValue(sessionId)) {
			return;
		}
		delKey(userId);
		/*
		 * MapCacheUtil map = RemoteCacheUtilFactory.map(sessionId);
		 * map.remove();
		 */
	}

}
