package com.yz.service.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.goods.GsSalesNotifyMapper;
import com.yz.model.BmsTimer;
import com.yz.model.common.IPageInfo;
import com.yz.model.goods.GsSalesNotify;

@Service
@Transactional
public class GsSalesNotifyService {

	@Autowired
	private GsSalesNotifyMapper gsSalesNotifyMapper;
	
	public IPageInfo<GsSalesNotify> getGsSalesNotify(int start, int length){
		PageHelper.offsetPage(start, length);
		List<GsSalesNotify> notifys = gsSalesNotifyMapper.getGsSalesNotify();
		return new IPageInfo<GsSalesNotify>((Page<GsSalesNotify>)notifys);
	}
	
	
	public void singleDeleteSalesNotify(String notifyId){
		gsSalesNotifyMapper.singleDeleteSalesNotify(notifyId);
	}
	
	public void batchDeleteSalesNotify(String[] ids){
		gsSalesNotifyMapper.batchDeleteSalesNotify(ids);
	}
	
	public List<GsSalesNotify> getAllNotifyBySalesId(String salesId,String planCount){
		return gsSalesNotifyMapper.getAllNotifyBySalesId(salesId,planCount);
	}
	
	public void updateNotifyStatus(String notifyId){
		gsSalesNotifyMapper.updateNotifyStatus(notifyId);
	}

}
