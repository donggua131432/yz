package com.yz.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.exception.BusinessException;
import com.yz.redis.RedisService;
import com.yz.util.DateUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @desc ID 生成器
 * @author Administrator
 *
 */
public class IDGenerator {

	private static Logger logger = LoggerFactory.getLogger(IDGenerator.class);

	public static final int GENERATOR_ORDERNO = 1;// 订单号

	public static final int GENERATOR_PAYNO = 2;// 支付单号

	public static final String YZ_CODE_PREFIX = "YZ_";

	public static final String YZ_CODE_SEED_KEY = "com.yzcode.seed";
	
	public static final String INCR_ORDER_NO_PREFIX = "yz.order.incr-";

	/**
	 * 
	 * @return
	 */
	public static String generatorYzCode() {
		return generatorCode(YZ_CODE_PREFIX, YZ_CODE_SEED_KEY, 7, "0");
	}
	
	/**
	 * 
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generatororderNo(String prefix)
	{
		String sb = generatorNoWithDateFormat(prefix);
		if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX + sb.toString()) == 1) {
			return sb.toString();
		}
		int i = 0;
		while (i++ < 3) {
			sb = generatorNo();
			if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX +sb.toString()) == 1) {
				return sb.toString();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("InterruptedException e:{}", ExceptionUtil.getStackTrace(e));
			}
		}
		logger.error("generatorId.error;please try again!");
		throw new BusinessException("generatorId.error;please try again!");
	}

	/**
	 * 
	 * @param prefix
	 * @param length
	 * @param padStr
	 * @return
	 */
	public static String generatorCode(String prefix, String seedKey, int length, String padStr) {
		StringBuilder sb = new StringBuilder(prefix);
		String seed = String.valueOf(RedisService.getRedisService().incrBy(seedKey, 1));
		sb.append(StringUtil.leftPad(seed, length, padStr));
		logger.info("seedKey:{},seed:{},result:{}", seedKey,seed, sb);
		return sb.toString();
	}

	/**
	 * 类型 + userId 后两位 sellerId 后两位 + 10位时间戳 + 2位随机数
	 * 
	 * @return
	 */
	public static String generatorOrderNo(int type, String userId) {
		switch (type) {
		case GENERATOR_ORDERNO:
		case GENERATOR_PAYNO:
			StringBuilder sb = new StringBuilder();
			sb.append(type).append(StringUtil.substring(userId, userId.length() - 6));
			return generatorInnerOrderNo(sb.toString());
		default:
			logger.error("not support the type:{}!", type);
			throw new BusinessException("not support the type!");
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static String generatorId() {
		String sb = generatorNo();
		if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX +sb.toString()) == 1) {
			return sb.toString();
		}
		int i = 0;
		while (i++ < 3) {
			sb = generatorNo();
			if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX +sb.toString()) == 1) {
				return sb.toString();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.error("InterruptedException e:{}", ExceptionUtil.getStackTrace(e));
			}
		}
		logger.error("generatorId.error;please try again!");
		throw new BusinessException("generatorId.error;please try again!");
	}

	
	/**
	 * 带日期格式化
	 * 
	 * @return
	 */
	private static String generatorNoWithDateFormat(String prefix) {
		if(!StringUtil.hasValue(prefix)){
			prefix = "";
		}
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(DateUtil.getNow());
		sb.append(StringUtil.leftPad(StringUtil.randomNumber(6), 6, "0"));
		return sb.toString();
	}
	
	/**
	 * 毫秒级别
	 * 
	 * @return
	 */
	private static String generatorNo() {
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis());
		sb.append(StringUtil.leftPad(StringUtil.randomNumber(5), 5, "0"));
		return sb.toString();
	}

	/**
	 * 
	 * @param prefix
	 * @return
	 */
	private static String generatorInnerOrderNo(String prefix) {
		String sb = generatorOrderNo(prefix);
		if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX +sb.toString()) == 1) {
			return sb.toString();
		}
		int i = 0;
		while (i++ < 3) {
			sb = generatorOrderNo(prefix);
			if (RedisService.getRedisService().incr(INCR_ORDER_NO_PREFIX +sb.toString()) == 1) {
				return sb.toString();
			}
		}
		logger.error("generatorInnerOrderNo.error;please try again!");
		throw new BusinessException("generatorInnerOrderNo.error;please try again!");
	}

	/**
	 * 秒级别
	 * 
	 * @return
	 */
	private static String generatorOrderNo(String prefix) {
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(System.currentTimeMillis() / 1000);
		sb.append(StringUtil.leftPad(StringUtil.randomNumber(2), 2, "0"));
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(String.valueOf(System.currentTimeMillis()).length());
		System.out.println(generatorNo());
	}
}
