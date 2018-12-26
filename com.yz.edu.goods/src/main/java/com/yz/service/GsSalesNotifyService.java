package com.yz.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.dao.GsSalesNotifyMapper;
import com.yz.model.BmsTimer;
import com.yz.model.GsSalesNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GsSalesNotifyService {

	@Autowired
	private GsSalesNotifyMapper gsSalesNotifyMapper;
	
	public List<GsSalesNotify> getAllNotifyBySalesId(String salesId,String planCount){
		return gsSalesNotifyMapper.getAllNotifyBySalesId(salesId,planCount);
	}
	
	public void updateNotifyStatus(String notifyId){
		gsSalesNotifyMapper.updateNotifyStatus(notifyId);
	}
	
	public void deleteTimer(String salesId){
		/*BmsTimer timer = scheduleApi.selectByJobName(salesId + "beginRemind");
		if(null != timer){
			scheduleApi.removeTimer(timer.getId());
			scheduleApi.deleteTimer(timer.getId());
		}*/
	}
}
