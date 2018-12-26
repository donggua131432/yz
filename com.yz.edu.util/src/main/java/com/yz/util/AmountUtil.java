package com.yz.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
/**
 * 金额格式化工具
 * @author cyf
 *
 */
public class AmountUtil {
	
	/** 科学计数法格式 */
	public static final String AMOUNT_PATTERN_SN = "###,###.00";
	/** 小数点后两位 */
	public static final String AMOUNT_PATTERN_2 = "0.00";
	/** 整数 */
	public static final String AMOUNT_PATTERN_ZERO = "0";
	
	public static final BigDecimal HUNDRED = new BigDecimal("100");

	public static String format(String amount, String pattern) {
		if (StringUtil.isEmpty(amount))
			return "0.00";
		return new DecimalFormat(pattern).format(amount);
	}
	
	public static String format(BigDecimal bd, int scale, int roundingMode) {
		return bd.setScale(scale, roundingMode).toString();
	}
	
	public static String formatNoRound(BigDecimal bd, int scale) {
		return format(bd, scale, BigDecimal.ROUND_DOWN);
	}
	
	public static String format(BigDecimal bd, int scale) {
		return bd.setScale(scale).toString();
	}

	/**
	 * 将字符串转换为金额
	 * 
	 * @param amountStr
	 * @return
	 */
	public static BigDecimal str2Amount(String amountStr) {
		if (StringUtil.hasValue(amountStr))
			return new BigDecimal(amountStr).setScale(2, BigDecimal.ROUND_DOWN);
		return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);
	}

}
