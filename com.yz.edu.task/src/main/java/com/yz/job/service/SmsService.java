package com.yz.job.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.constant.Code;
import com.yunpian.sdk.constant.YunpianConstant;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsBatchSend;
import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;
import com.yz.job.constants.JobConstants;
import com.yz.model.SendSmsVo;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

@Service
public class SmsService {

	private static Logger logger = LoggerFactory.getLogger(SmsService.class);

	@Value("${yp.host.user}")
	private String userHost;
	@Value("${yp.host.sign}")
	private String signHost;
	@Value("${yp.host.tpl}")
	private String tplHost;
	@Value("${yp.host.sms}")
	private String smsHost;
	@Value("${yp.host.voice}")
	private String voiceHost;
	@Value("${yp.host.flow}")
	private String flowHost;
	@Value("${yp.host.call}")
	private String callHost;
	@Value("${yp.apikey}")
	private String apiKey;

	private Properties properties = null;

	YunpianClient client = null;

	@PostConstruct
	public void initProperties() {
		properties = new Properties();
		properties.setProperty(YunpianConstant.YP_USER_HOST, userHost);
		properties.setProperty(YunpianConstant.YP_SIGN_HOST, signHost);
		properties.setProperty(YunpianConstant.YP_TPL_HOST, tplHost);
		properties.setProperty(YunpianConstant.YP_SMS_HOST, smsHost);
		properties.setProperty(YunpianConstant.YP_VOICE_HOST, voiceHost);
		properties.setProperty(YunpianConstant.YP_FLOW_HOST, flowHost);
		properties.setProperty(YunpianConstant.YP_CALL_HOST, callHost);
		client = new YunpianClient(apiKey, properties).init();
	}

	/**
	 * 
	 * @param sms
	 */
	public void sendSms(SendSmsVo sms) {
		Map<String, String> content = sms.getContent();
		String mobile = StringUtil.concatEntries(sms.getMobiles(), ",", null);

		Map<String, String> param = new HashMap<String, String>();
		param.put(YunpianConstant.TPL_ID, sms.getTemplateId());
		param.put(YunpianConstant.MOBILE, mobile);
		param.put(YunpianConstant.TPL_VALUE, StringUtil.encode2String(content));
		boolean voice = sms.isVoice();
		if (voice) {
			client.voice().send(param);
		} else {
			Result<SmsBatchSend> result = client.sms().tpl_batch_send(param);
			logger.debug("sms.sendMsg.data:{}", JsonUtil.object2String(result.getData()));
			if (Code.OK == result.getCode()) {
				YzJobInfo info = YzTaskContext.getTaskContext().getContext(YzJobInfo.class);
				result.getData().getData().stream().forEach(v -> {
					YzTaskContext.getTaskContext().addEventDetail(v.getMobile(),
							String.format(info.getLogFormat(), sms.getTemplateId(), v.getMsg()));
				});
			}
		}

	}
}
