package com.yz.core.constants;

public class AppConstants {

	public static final String SYSTEM_NAME = "bcc";
	
	/** 接口状态 关闭 */
	public static final String STATUS_CLOSED = "2";
	/** 接口状态 开启 */
	public static final String STATUS_OPEN = "1";

	/** 字段校验状态 - 匹配 */
	public static final String FIELD_CHECK_MATCH = "1";
	/** 字段校验状态  - 匹配替换*/
	public static final String FIELD_CHECK_REPLACE = "2";
	
	public static final String PUB_USER_ID = "_userId";
	
	public static final String PUB_JWT_TOKEN = "_jwt_token";
	/**用户状态 账户冻结 无法交易*/
	public static final String USER_STATUS_TRANS_LOCK = "4";
	
	/** 接口所属产品类型 - 通用 */
	public static final String APP_TYPE_ALL = "1";
	/** 接口所属产品类型 - 微信公众号 */
	public static final String APP_TYPE_WECHAT = "2";
	/** 接口所属产品类型  - 客户端（学员）*/
	public static final String APP_TYPE_STUDENT = "3";
	/** 接口所属产品类型  - 客户端（员工）*/
	public static final String APP_TYPE_EMPLOYEE = "4";
	
	
	/** 用户锁定 - 临时锁定*/
	public static final String USER_STATUS_FROZEN = "2";
	/** 用户锁定 - 系统锁定*/
	public static final String USER_STATUS_LOCK = "3";
	/** 用户锁定 - 交易锁定*/
	public static final String USER_STATUS_UNTRANS = "4";
	
	public static final String GLOBAL_EXCEPTION_CODE = "E00000";
	
	public static final String TRUE = "1";
	public static final String FALSE = "0";
	
	public static final String AUTH_TOKEN = "auth_token";
	
	public static final String AUTH_TOKEN_NEW = "authtoken";
	
	/** HTTP请求参数*/
	public static final String REQUEST_METHOD_ALL = "ALL";
	
	
	
	/** 路由类型 - 匹配报文头*/
	public static final String ROUTE_TYPE_HEAD = "1";
	/** 路由类型 - 匹配报文体*/
	public static final String ROUTE_TYPE_BODY = "2";
	/** 路由类型 - 匹配报文头、体*/
	public static final String ROUTE_TYPE_ALL = "3";
	/** 路由类型 - 不路由， 直接校验*/
	public static final String ROUTE_TYPE_NONE = "4";
	/** 防重复提交TOKEN */
	public static final String COMMIT_TOKEN_PREFIX = "commitToken_";
	/** 防重复提交TOKEN字段名称 */
	public static final String COMMIT_TOKEN = "zmtoken";

	
	
}
