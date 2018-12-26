package com.yz.edu.task;

import com.yz.constants.RedisKeyConstants;
import com.yz.util.RegUtils;

/**
 * 
 * @author Administrator
 *
 */
public class RegUtilTest {

	public static void main(String[] args) {
		System.out.println(RegUtils.isMatch(RedisKeyConstants.GET_JD_OR_WECHAT_TOKEN_CHANNEL, RedisKeyConstants.GET_JD_TOKEN_CHANNEL));
	}
}
