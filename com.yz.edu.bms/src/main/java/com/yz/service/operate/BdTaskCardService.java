package com.yz.service.operate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.operate.BdTaskCardMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.operate.BdTaskCard;
import com.yz.util.DateUtil;

@Service
@Transactional
public class BdTaskCardService {

	@Autowired
	private BdTaskCardMapper tcMapper;

	public Object selectTaskCardByPage(int start, int length, BdTaskCard taskCard) {
		PageHelper.offsetPage(start, length);
		List<BdTaskCard> tasks = tcMapper.selectTaskCardByPage(taskCard);
		for (BdTaskCard task : tasks) {
			task.setNowDate(DateUtil.getCurrentDate());
		}
		return new IPageInfo<BdTaskCard>((Page<BdTaskCard>) tasks);
	}

	public void addTaskCard(BdTaskCard taskCard) {
		taskCard.setTaskId(IDGenerator.generatorId());
		taskCard.setStartTime(DateUtil.getCurrentDate());
		tcMapper.insertSelective(taskCard);
	}

	public BdTaskCard selectTaskCard(String id) {
		return tcMapper.selectByPrimaryKey(id);
	}

	public void updateTaskCard(BdTaskCard taskCard) {
		tcMapper.updateByPrimaryKeySelective(taskCard);
	}

	public void deleteTask(String id) {
		tcMapper.deleteByPrimaryKey(id);
	}

	public void publishTask(String taskId) {
		tcMapper.publishTaskById(taskId);
	}
	
	
	
}
