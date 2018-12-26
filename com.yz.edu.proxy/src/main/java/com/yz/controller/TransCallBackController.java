package com.yz.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.rpc.RpcContext;
import com.yz.constants.GwConstants;
import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.exception.BusinessException;
import com.yz.service.TransCallBackService;
import com.yz.trace.sender.TraceSender;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;

@RestController
public class TransCallBackController {

	private static final Logger log = LoggerFactory.getLogger(TransCallBackController.class);

	@Autowired
	private TransCallBackService callService;

	@RequestMapping(value = "/wechat/{excuteType}")
	public void wechatCallback(HttpServletRequest request, @PathVariable("excuteType") String excuteType,
			@RequestBody Map<String, String> param, HttpServletResponse response)
			throws BusinessException, InterruptedException, IOException {
		TraceTransfer transfer = TraceTransfer.getTrace("bcc");
		StopWatch watch = new StopWatch();
		watch.start();
		transfer.setServiceName("/wechat/"+excuteType);
		transfer.setRemark("微信支付回调" );
		transfer.getTraceSpan("TransCallBackController", "wechatCallback", OSUtil.getIp());
		
		PrintWriter writer = response.getWriter();
		log.debug("----------------------- 微信支付回调参数 : " + param);
		try {
			callService.process(GwConstants.PAY_TYPE_WECHAT, excuteType, param);
			writer.write("success");
			writer.flush();
			log.debug("------------------------ 微信回调成功");
		} catch (Exception e) {
			transfer.setIsError(1);
			transfer.setErrorCode(e.getMessage());
			writer.write("FAILED");
			writer.flush();
			log.error("------------------------ 微信回调失败");
		} finally {
			long duration = watch.getTime();
			transfer.setDestination(duration);
			TraceSender.sendTrace(transfer);
			TraceTransfer.getTrace().remove();
			IOUtils.closeQuietly(writer);
		}
	}

	@RequestMapping(value = "/allinpay/{excuteType}")
	public void allinpayCallback(HttpServletRequest request, @PathVariable("excuteType") String excuteType,
			HttpServletResponse response) throws BusinessException, InterruptedException, IOException {
		TraceTransfer transfer = TraceTransfer.getTrace("bcc");
		StopWatch watch = new StopWatch();
		watch.start();
		transfer.setServiceName("/allinpay/"+excuteType);
		transfer.setRemark("通联支付回调" );
		transfer.getTraceSpan("TransCallBackController", "allinpayCallback", OSUtil.getIp());
		
		
		PrintWriter writer = response.getWriter();
		log.debug("----------------------- 通联支付回调参数 : " + JsonUtil.object2String(request.getParameterMap()));
		Map<String, String[]> requestParam = request.getParameterMap();
		Map<String, String> param = new HashMap<String,String>();
		for (String key : requestParam.keySet()) {
			String[] strings = requestParam.get(key);
			String temp = strings[0];
			param.put(key, temp);
		}
		try {
			callService.process(GwConstants.PAY_TYPE_ALLINPAY, excuteType, param);
			writer.write("success");
			writer.flush();
			log.debug("------------------------ 通联回调成功");
		} catch (Exception e) {
			transfer.setIsError(1);
			transfer.setErrorCode(e.getMessage());
			
			writer.write("FAILED");
			writer.flush();
			log.error("------------------------ 通联回调失败");
			
		} finally {
			long duration = watch.getTime();
			transfer.setDestination(duration);
			TraceSender.sendTrace(transfer);
			TraceTransfer.getTrace().remove();
			IOUtils.closeQuietly(writer);
		}
	}

}
