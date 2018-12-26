package com.yz.service.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper; 
import com.yz.dao.system.SysDictPlusMapper;
import com.yz.exception.CustomException;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.PubInfo;
import com.yz.model.system.SysDictPlus;
import com.yz.util.StringUtil;

@Service
@Transactional
public class SysdictPlusService {

	private static final Logger log = LoggerFactory.getLogger(SysdictPlusService.class);

	@Autowired
	private SysDictPlusMapper sdpMapper;

	public IPageInfo<SysDictPlus> getList(SysDictPlus queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<SysDictPlus> list = sdpMapper.getList(queryInfo);
		return new IPageInfo((Page) list);
	}

	public SysDictPlus getDictInfo(String dictId) {

		SysDictPlus gradeInfo = sdpMapper.getOne(dictId);

		if (gradeInfo == null)
			throw new CustomException("年级信息不存在 或已被删除");

		return gradeInfo;
	}

	public void add(SysDictPlus dict) {
		sdpMapper.add(dict);
	}
	
	public void addBySeq(SysDictPlus dict) {
		int dictId = sdpMapper.seq(dict.getpId());
		dict.setDictId(dict.getpId() + "." + dictId);
		dict.setDictValue(dictId+"");
		sdpMapper.add(dict);
	}

	public void update(SysDictPlus dict) {
		sdpMapper.update(dict);
	}

	public boolean isExsit(String dictId) {
		int count = sdpMapper.countBy(dictId);
		return count < 1;
	}
	
	public boolean isExsit(String dictName, String pId, String oldName) {
		if(StringUtil.hasValue(dictName) && dictName.equals(oldName))
			return true;
		int count = sdpMapper.countByName(dictName, pId);
		return count < 1;
	}

	public void batch(String[] dictIds, String isEnable, PubInfo pubInfo) {
		sdpMapper.batch(dictIds, isEnable, pubInfo);
	}

	@Value("${cacheFile.dict}")
	private String dictFileName;

	/**
	 * 修改字典状态，不刷新缓存和json文件
	 * @param dict
	 */
	public void updateUnCache(SysDictPlus dict) {
		sdpMapper.updateUnCache(dict);
	}

}
