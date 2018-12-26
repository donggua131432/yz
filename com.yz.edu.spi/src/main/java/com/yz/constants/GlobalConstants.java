package com.yz.constants;

public class GlobalConstants {
	/** 分页起始下标 */
	public static final String PAGE_START_DEFAULT = "page.start.defualt";
	/** 分页每页数量 */
	public static final String PAGE_LENGTH_DEFAULT = "page.length.default";
	/** 请求成功编码 */
	public static final String SUCCESS_CODE = "00";
	
	
	/** 是 */
	public static final String TRUE = "1";
	/** 否 */
	public static final String FALSE = "0";
	
	/** 院校 */
	public static final String UNVS = "1";
	/** 否专业*/
	public static final String PROFESSION = "2";
	
	/** 是否允许 1-允许*/
	public static final String STATUS_ALLOW = "1";
	/** 是否允许 2-不允许*/
	public static final String STATUS_NOT_ALLOW = "0";
	
	/** 上传文件类型  - 单个文件上传*/
	public static final String UPLOAD_RESOURCE_TYPE_SINGLE = "single";
	/** 上传文件类型 - 多文件上传 */
	public static final String UPLOAD_RESOURCE_TYPE_MULTI = "multi";
	
	/** 文件临时路径 */
	public static final String FILE_TEMP_SRC = "temp.file.src";
	
	/** 文件真实路径 */
	public static final String FILE_PRO_SRC = "pro.file.src";
	
	/** 微信回复信息类型  text - 文本信息*/
	public static final String WECHAT_REPLY_TYPE_TEXT = "text";
	/** 微信回复信息类型  news - 图文信息*/
	public static final String WECHAT_REPLY_TYPE_NEWS = "news";
	
	public static final String PAGE_NUM = "pageNum";
	public static final String PAGE_SIZE = "pageSize";
	
	
	/** 启用状态 */
	public static final String STATUS_START = "1";
	/** 禁用状态 */
	public static final String STATUS_BLOCK = "2";
	
	/** 成功状态 */
	public static final String STATUS_SUCCESS = "1";
	/** 失败状态 */
	public static final String STATUS_FAILED = "2";
	
	/** 阿里云显示目录BUCKET 参数名*/
	public static final String FILE_BUCKET = "aliyun.oss.bucket";
	/** 阿里云临时目录BUCKET 参数名 */
	public static final String TEMP_BUCKET = "aliyun.oss.tempBucket";
	
	/** 权限级别 - 超级管理员 */
	public static final String USER_LEVEL_SUPER = "1";
	/** 权限级别 - 校长 */
	public static final String USER_LEVEL_SCHOOL = "2";
	/** 权限级别 - 校监 */
	public static final String USER_LEVEL_DEPARTMENT = "3";
	/** 权限级别 - 招生主管 */
	public static final String USER_LEVEL_GROUP = "4";
	/** 招生老师 */
	public static final String USER_LEVEL_NORMARL = "5";
	/** 班主任*/
	public static final String USER_LEVEL_TEACHER = "6";
	/** 权限级别 - 校区助理*/
	public static final String USER_LEVEL_XQZL = "7";
	/** 权限级别 - 校监助理*/
	public static final String USER_LEVEL_XJZL = "8";
	/** 权限级别 - 400专员*/
	public static final String USER_LEVEL_400 = "9";
	
	/** 兑换/抽奖/竞拍 开始前置提醒时间*/
	public static final String SALRS_BEGIN_REMIND_TIME ="goods.sales.remind.time";
	/** 获取手机验证码 - 图形验证码存储名称 */
	public static final String VALICODE_KEY = "_valicode";
	
	/** 短信验证码模板 ==> 您的验证码是#code#*/
	public static final String TEMPLATE_VALI_CODE = "1981604";
	/** 短信通知 */
	public static final String TEMPLATE_MSG_NOTIFY = "1989570";
	/** 离职通知 */
	public static final String TEMPLATE_MSG_DIMISSION = "2215190";
	/**SQL慢查询的模板*/
	public static final String TEMPLATE_MSG_SLOWSQL = "2253544";
	
	public static final String TEMPLATE_MSG_USER_LIST = "13530625715";
	
	/** 通讯成功*/
	public static final String SUCCESS = "SUCCESS";
	/** 通讯失败*/
	public static final String FAILED = "FAILED";

	/** 文件浏览路径 */
	public static final String FILE_BROWSER_URL = "file.browser.url";
	
	/** 营销工具配置路径 */
	public static final String MP_JAR_JSON = "markting.jar.json";
	
	/** 用户绑定类型 */
	public static final String USER_BIND_TYPE_WECHAT = "wechat";
	
	/** bcc 本地缓存刷新渠道名称 */
	public static final String BCC_CRF_CHANNEL = "bcc.cache.refresh";
	/** bds 本地缓存刷新渠道名称 */
	public static final String BDS_CRF_CHANNEL = "bds.cache.refresh";
	/** bms 本地缓存刷新渠道名称 */
	public static final String BMS_CRF_CHANNEL = "bms.cache.refresh";
	/** ats 本地缓存刷新渠道名称 */
	public static final String ATS_CRF_CHANNEL = "ats.cache.refresh";
	/** bs 本地缓存刷新渠道名称 */
	public static final String BS_CRF_CHANNEL = "bs.cache.refresh";
	/** gs 本地缓存刷新渠道名称 */
	public static final String GS_CRF_CHANNEL = "gs.cache.refresh";
	/** gate-way 本地缓存刷新渠道名称 */
	public static final String GW_CRF_CHANNEL = "gw.cache.refresh";
	/** us 本地缓存刷新渠道名称 */
	public static final String US_CRF_CHANNEL = "us.cache.refresh";
	
	
	/** 缓存刷新类型 - 刷新系统参数*/
	public static final String REFRESH_TYPE_PARAM = "PARAM";
	/** 缓存刷新类型 - 刷新业务字典*/
	public static final String REFRESH_TYPE_DICT = "DICT";
	/** 缓存刷新类型 - 刷新错误信息*/
	public static final String REFRESH_TYPE_ERRORMSG = "EMSG";
	/** 缓存刷新类型 - 刷新调用实例*/
	public static final String REFRESH_TYPE_RPC = "RPC";
	/** 缓存刷新类型 - 刷新微信支付证书信息*/
	public static final String REFRESH_TYPE_CERT = "CERT";
	/** 缓存刷新类型 - 刷新微信公众号信息*/
	public static final String REFRESH_TYPE_PUBLIC = "WPUB";
	/** 缓存刷新类型 - 刷新奖励规则信息*/
	public static final String REFRESH_TYPE_AWARD = "AWARD";
	/** 缓存刷新类型 - 刷新所有缓存信息*/
	public static final String REFRESH_TYPE_ALL = "ALL";
	/** 缓存刷新类型 - 刷新地区缓存*/
	public static final String REFRESH_TYPE_AREA = "AREA";
	/** 缓存刷新类型 - 营销工具*/
	public static final String REFRESH_TYPE_MPJAR = "MPJAR";
	/** 缓存刷新类型 - 刷新远智教育公众号菜单*/
	public static final String REFRESH_TYPE_WECHAT_MENU = "YZMENU";
	
}
