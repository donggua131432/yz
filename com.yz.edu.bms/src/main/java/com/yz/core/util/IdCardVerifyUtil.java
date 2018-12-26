package com.yz.core.util;

import java.util.regex.Pattern;

public class IdCardVerifyUtil
{   
	/** 大陆地区地域编码最大值 **/
	public static final int          MAX_MAINLAND_AREACODE     = 659004;
    /** 大陆地区地域编码最小值 **/
    public static final int          MIN_MAINLAND_AREACODE     = 110000;
    /** 数字正则 **/
    public static final String       regexNum                  = "^[0-9]*$";
    /** 闰年生日正则 **/
    public static final String       regexBirthdayInLeapYear   = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
    /** 平年生日正则 **/
    public static final String       regexBirthdayInCommonYear = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";
	
    /**
     * 身份证格式强校验
     * @param idNumber
     * @return
     */
	public static final boolean strongVerifyIdNumber(String idNumber)
	{
		idNumber = idNumber.trim();
		
		if (!checkIdNumberRegex(idNumber)) {
			return false;
		}
		if (!checkIdNumberArea(idNumber.substring(0, 6))) {
			return false;
		}
		//idNumber = convertFifteenToEighteen(idNumber);
		if (!checkBirthday(idNumber.substring(6, 14))) {
			return false;
		}
		if (!checkIdNumberVerifyCode(idNumber)) {
			return false;
		}
		return true;
	}
	/**
     * 身份证正则校验
     */
	private static boolean checkIdNumberRegex(String idNumber)
	{
		return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
	}
	 /**
     * 身份证地区码检查
     */
	private static boolean checkIdNumberArea(String idNumberArea)
	{
		int areaCode = Integer.parseInt(idNumberArea);
		
		if (areaCode <= MAX_MAINLAND_AREACODE && areaCode >= MIN_MAINLAND_AREACODE) {
			return true;
		}
		return false;
	}
	  /**
     * 将15位身份证转换为18位
     */
    private static String convertFifteenToEighteen(String idNumber) {
        if (15 != idNumber.length()) {
            return idNumber;
        }
        idNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
        idNumber = idNumber + getVerifyCode(idNumber);
        return idNumber;
    }
    
    /**
     * 身份证出生日期嘛检查
     */
	private static boolean checkBirthday(String idNumberBirthdayStr) {
        Integer year = null;
        try {
            year = Integer.valueOf(idNumberBirthdayStr.substring(0, 4));
        } catch (Exception e) {
        }
        if (null == year) {
            return false;
        }
        if (isLeapYear(year)) {
            return Pattern.matches(regexBirthdayInLeapYear, idNumberBirthdayStr);
        } else {
            return Pattern.matches(regexBirthdayInCommonYear, idNumberBirthdayStr);
        }
    }
	/**
     * 判断是否为闰年
     */
	private static boolean isLeapYear(int year)
	{
		return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
	}
	 /**
     * 身份证校验码检查
     */
    private static boolean checkIdNumberVerifyCode(String idNumber) {
        return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
    }
    /**
     * 根据身份证前17位计算身份证校验码
     */
    private static String getVerifyCode(String idNumber) {
        if (!Pattern.matches(regexNum, idNumber.substring(0, 17))) {
            return null;
        }
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        return ValCodeArr[sum % 11];
    }

	public static void main(String[] args)
	{
	    boolean fff = Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", "201698001010004");
	    System.out.println(strongVerifyIdNumber("201698001010004"));
		System.err.println("-------------------------------- 测试结果：" +fff);
	}
}
