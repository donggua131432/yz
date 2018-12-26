package com.yz.job.service;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.job.dao.InviteDataAcquisitionMapper;
import com.yz.model.InviteDataAcquisitionEvent;
import com.yz.util.StringUtil;
import com.yz.util.TokenUtil;

@Service
public class InviteDataAcquisitionService {

	@Autowired
	private InviteDataAcquisitionMapper inviteDataAcquisitionMapper;

	/**
	 *数据保存操作
	 * 
	 * @param event
	 */
	public void saveDataAcquisition(List<InviteDataAcquisitionEvent> event) {
		if (event != null) {
			// 转换成JSON数据存储
			event.forEach(v -> {
				v.setChannelType("1");
				String req = v.getUser_req();
				// 切割数据，取到渠道ID /invite?channelId=10003
				String[] reqs = StringUtil.split(StringUtil.substringAfter(req, "?"), "&");
				for (String str : reqs) {
					String name = StringUtil.substringBefore(str, "=");
					if (StringUtil.equalsIgnoreCase(name, "channelId")) {
						v.setChannel_id(StringUtil.substringAfter(str, "="));
					}
					if (StringUtil.equalsIgnoreCase(name, "inviteId")) {
						String token = TokenUtil.parse(URLDecoder.decode(StringUtil.substringAfter(str, "=")));
						v.setUserId(StringUtil.substringBefore(token, ","));
					}
				}
			});
			//数据存储 批量插入数据到日志表中
			//根据channel_id分组
			Map<String, List<InviteDataAcquisitionEvent>> data=event.stream().collect(Collectors.groupingBy(InviteDataAcquisitionEvent::getChannel_id));
			data.entrySet().stream().forEach(v->{
				int amount = v.getValue().size();
				addLookCount(v.getKey(),amount);
			});
			// 数据存储 批量插入数据到日志表中
			inviteDataAcquisitionMapper.saveInviteDataAcquisition(event);
		}
	}
	/**
	 * 保存浏览数量，方便报表直接查看，而不需要去重复查询
	 * @param channel_id
	 * @param count
	 */
	private void addLookCount(String channel_id,int count) {
		long lookCount=Long.valueOf(inviteDataAcquisitionMapper.getLookCount(channel_id));
		lookCount+=count;
		inviteDataAcquisitionMapper.addLookCount(channel_id, String.valueOf(lookCount));
	}
}
