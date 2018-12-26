package com.yz.core.handler;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5784608369338698016L;
	
	public CustomObjectMapper() {
        this.setSerializationInclusion(Include.NON_NULL);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}
