package com.yz.core.constants;

public class AppConstants {

	public static final String GOODS_TYPE_COMMON="1";
	public static final String GOODS_TYPE_COURSE="2";
	public static final String GOODS_TYPE_ACTIVITY="3";
	public static final String GOODS_TYPE_TEXTBOOK="4";
	
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
	
	/** 智米比例系统参数名 */
	public static final String SYS_PARAM_ZHIMI_SCALE = "yz.zhimi.scale";
	
	/** 缓存*/
	public static final String BMS_CACHE_HANDLER="bmsCacheHandler";
	
	/** 智米赠送规则缓存*/
	public static final String ZHIMI_AWARD_CACHE_HANDLER="zhimiAwardCacheHandler";
	
	
	/**京东下单,不预占库存 0 预占(可通过接口取消),1 不预占*/
	public static final String SUBMIT_STATE = "submit.state";
	/**京东开票 纳税号*/
	public static final String REG_CODE = "reg.code";
	/**京东开票 收增票人电话*/
	public static final String INVOICE_PHONE = "invoice.phone";
	
	/**京东发票类型:1 普通发票 2 增值税发票 3 电子发票*/
	public static final String INVOICE_TYPE = "invoice.type";

	//需要导入论文题目的学校字典标识
	public static final String PAPER_TITLE_IMPORT_SCHOOL = "paperTitleImportSchool";

	//需要导入论文题目长度限制
	public static final int PAPER_TITLE_IMPORT_LENGTH = 50;

}
