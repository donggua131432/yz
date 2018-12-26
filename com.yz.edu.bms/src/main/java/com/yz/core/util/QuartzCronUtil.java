package com.yz.core.util;

import com.yz.util.StringUtil;

/**
 * cron 表达式工具类
 * @author lx
 * @date 2017年8月4日 上午9:43:39
 */
public class QuartzCronUtil {

	public static String getCronByTimeStr(String timeStr){
		if(StringUtil.isEmpty(timeStr)){
			return null;
		}
		// cron 
		// 秒  分 时  日 月 周  年
		String[] timeStrArray = timeStr.split(" ");
		String[] ymdArray = timeStrArray[0].split("-");
		String[] hsmArray = timeStrArray[1].split(":");
		String d = ymdArray[2];
		String m = ymdArray[1];
		if(Integer.parseInt(d)<10){
			d = Integer.parseInt(d)+"";
		}
		if(Integer.parseInt(m)<10){
			m = Integer.parseInt(m)+"";
		}
		return hsmArray[2]+" " +hsmArray[1]+" "+hsmArray[0] +" " +d +" " + m +" " +"? " + ymdArray[0];
	}
}
