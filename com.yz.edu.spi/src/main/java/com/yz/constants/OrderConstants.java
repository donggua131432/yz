package com.yz.constants;

public class OrderConstants {
	
	/** 流水状态 3-待审核*/
	public static final String SERIAL_STATUS_UNCHECK = "3";
	/** 流水状态 2-已完成*/
	public static final String SERIAL_STATUS_FINISHED = "2";
	/** 流水状态 1-处理中*/
	public static final String SERIAL_STATUS_PROCESS = "1";
	/** 流水状态 4-交易失败*/
	public static final String SERIAL_STATUS_FAILED = "4";

	/** 订书状态 1-未订*/
	public static final String ORDER_BOOK_NO = "1";
	/** 订书状态 2-已订未发*/
	public static final String ORDER_BOOK_NO_SEND = "2";
	/** 订书状态 3-已发*/
	public static final String ORDER_BOOK_SEND = "3";
	
	/** 教材类型 FD-辅导教材*/
	public static final String TEXTBOOK_TYPE_FD = "FD";
	/** 教材类型 XK-学科教材*/
	public static final String TEXTBOOK_TYPE_XK = "XK";
}
