package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;

public interface BdsScheduleApi {

	public Object cutOff(Body body) throws IRpcException;

	/**
	 * 发送考试提醒
	 * @param body
	 * @throws IRpcException
	 */
	public void sendExamWarn(Body body) throws IRpcException;
}
