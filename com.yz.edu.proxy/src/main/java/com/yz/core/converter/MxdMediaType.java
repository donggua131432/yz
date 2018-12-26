package com.yz.core.converter;

import org.springframework.http.MediaType;
/**
 * 请求媒体类型
 * @author cyf
 *
 */
public class MxdMediaType {
	/** 加密 */
	public final static MediaType TEXT_ITYPE;

	public final static String TEXT_ITYPE_VALUE = "text/mxd+";
	/** 不加密 */
	public final static MediaType TEXT_ITYPE_UNENCRYPT;
	
	public final static String TEXT_ITYPE_UNENCRYPT_VALUE = "text/mxd-";
	
	static {
		TEXT_ITYPE = MediaType.valueOf(TEXT_ITYPE_VALUE);
		TEXT_ITYPE_UNENCRYPT = MediaType.valueOf(TEXT_ITYPE_UNENCRYPT_VALUE);
	}
}
