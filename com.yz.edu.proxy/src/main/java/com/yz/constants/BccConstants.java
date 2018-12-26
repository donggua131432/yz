package com.yz.constants;

import com.yz.session.AppSessionHolder;
import com.yz.session.ProxySessionOp;

public class BccConstants {

	public static final String SYSTEM_NAME = "bcc";
	
	/** 接口状态 关闭 */
	public static final byte STATUS_CLOSED = 2;
	/** 接口状态 开启 */
	public static final byte STATUS_OPEN = 1;

	public static final byte FIELD_CHECK_MATCH = 1;

	public static final byte FIELD_CHECK_REPLACE = 2;
	
	public static final String PUB_USER_ID = "_userId";
	
	public static final String PUB_JWT_TOKEN = "_jwt_token";
	/**用户状态 账户冻结 无法交易*/
	public static final byte USER_STATUS_TRANS_LOCK = 4;
	
	public static final byte APP_TYPE_USER = 1;
	
	public static final byte APP_TYPE_SELLER = 2;
	
	public static final byte APP_TYPE_ALL = 3;

	public static final byte USER_STATUS_FROZEN = 2;

	public static final String UNKNOWN_EXCEPTION = "未知异常";

	public static final String HEADER_NAME = "auth";

	public static final String HEADER_INFO_USERID = "userId";

	public static final String HEADER_INFO_SELLERID = "sellerId";

	public static final String HEADER_INFO_APPTYPE = "appType";

	public static final String HEADER_INFO_APPVERSION = "appVersion";

	public static final String HEADER_INFO_DEVICEMODE = "deviceMode";

	public static final String INTERFACENAME_ERROR = "接口名称不能为空";

	public static final String INTERFACEVERSION_ERROR = "接口版本号不能为空";

	public static final String ZK_REGISTRY = "zkRegistry";

	public static final String CURENT_USER_SESSION_KEY = "yz-session-user";
	
	public static final AppSessionHolder.SessionOperator PROXY_LOCAL_SESSION_OPERATOR = new ProxySessionOp();
	
}
