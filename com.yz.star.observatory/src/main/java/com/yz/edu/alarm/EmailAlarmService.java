package com.yz.edu.alarm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit; 

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yz.edu.conf.EmailAlarmConfig;
import com.yz.edu.constant.ObservatoryStarConstant.ObserStarEnum;
import com.yz.edu.model.AnnotationAlarm;
import com.yz.edu.model.EsTraceInfo;
import com.yz.template.YzBuzResolverEngine;
import com.yz.util.StringUtil;

@Service(value = "emailAlarmService")
public class EmailAlarmService {

	private Logger logger = LoggerFactory.getLogger(EmailAlarmService.class);

	private YzBuzResolverEngine templateEngine = YzBuzResolverEngine.getInstance();

	private ThreadPoolExecutor executors = new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(64));

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmailAlarmConfig config;

	/**
	 * @desc 发送电子邮件
	 * @param info
	 */
	public void sendCostTimeEmail(EsTraceInfo info) {
		executors.execute(new Runnable() {
			@Override
			public void run() {
				sendEmail2CostTime(info);
			}
		});
	}

	/**
	 * @desc 发送电子邮件
	 * @param info
	 */
	public void sendSlowResEmail(List<AnnotationAlarm> annotations) {
		if (annotations != null) {
			AnnotationAlarm alarm = annotations.parallelStream()
					.max((x, y) -> (x.getAvgDestination() >= y.getAvgDestination()) ? 1 : -1).get();
			if (alarm.getAvgDestination() >= 100) {
				executors.execute(new Runnable() {
					@Override
					public void run() {
						sendEmail2SlowRes(annotations);
					}
				});
			}
		}
	}

	/**
	 * @desc 调用链耗时预警发送邮件
	 * @param info
	 */
	private void sendEmail2SlowRes(List<AnnotationAlarm> annotations) {
		try {
			ObserStarEnum alarm = ObserStarEnum.SLOWRES_ALARM;
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setSubject(alarm.getRemark());
			helper.setFrom(config.getUsername());
			helper.setTo(StringUtil.split(config.getReceiver(),","));
			Map<String, Object> data = Maps.newHashMap();
			data.put("annotations", annotations);
			helper.setText(templateEngine.resolveTemplate(alarm.getTemplate(), data), true);
			mailSender.send(mail);
		} catch (Exception e) {
			logger.error("sendEmail2SlowRes.error:{}", e);
		}
	}

	/**
	 * @desc 调用链耗时预警发送邮件
	 * @param info
	 */
	private void sendEmail2CostTime(EsTraceInfo info) {
		try {
			ObserStarEnum alarm = info.getAlarm();
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setSubject(alarm.getRemark());
			helper.setFrom(config.getUsername());
			helper.setTo(StringUtil.split(config.getReceiver(),","));
			Map<String, Object> data = Maps.newHashMap();
			data.put("info", info);
			helper.setText(templateEngine.resolveTemplate(alarm.getTemplate(), data), true);
			mailSender.send(mail);
		} catch (Exception e) {
			logger.error("sendEmail2CostTime.error:{}", e);
		}
	}
}
