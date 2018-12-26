package com.yz.constants;

/**
 * 
 * @author Administrator
 *
 */
public interface RedisKeyConstants {

	String SYS_DICT_VERSION = "sys.dict.version";

	String SYS_DICT_LIST = "sys.dict.list.1.0";

	String BCC_GATE_KEY_COUNT_LIST = "yz.bcc.gate.key.list.count";

	String DUBBO_EXPORT_CHANNEL = "yz.dubbbo.export.channel";

	String GET_JD_TOKEN_CHANNEL = "yz.get.jdToken.channel";

	String GET_WECHAT_TOKEN_CHANNEL = "yz.get.wechat.channel";
	
	String GET_JD_OR_WECHAT_TOKEN_CHANNEL = "yz.get.*.channel";
	
	String YZ_PAY_FAILD_ORDER = "yz.pay.faild.order.list";
	
	int REDIS_DB_INDEX_0 = 0; //redis db 索引
	int REDIS_DB_INDEX_1 = 1; //redis db 索引
	int REDIS_DB_INDEX_2 = 2; //redis db 索引
	int REDIS_DB_INDEX_3 = 3; //redis db 索引
	
	int REDIS_DB_INDEX_TRACE =4 ; // trace数据源
}
