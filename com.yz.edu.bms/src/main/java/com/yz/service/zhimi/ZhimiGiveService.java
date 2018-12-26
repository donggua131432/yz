package com.yz.service.zhimi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.api.AtsAccountApi;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.zhimi.ZhimiGiveMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.finance.AtsAccount;
import com.yz.model.zhimi.ZhimiGive;
import com.yz.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @描述: 手动赠送智米
 * @作者: DuKai
 * @创建时间: 2018/1/23 11:45
 * @版本号: V1.0
 */
@Service
@Transactional
public class ZhimiGiveService {
	@Reference(version = "1.0")
	private AtsAccountApi atsApi;

	@Autowired
	private ZhimiGiveMapper zhimiGiveMapper;

	@Autowired
	private AtsAccountMapper accountMapper;

	public IPageInfo<ZhimiGive> queryGiveZhimiByPage(int start, int length, ZhimiGive zhimiGive) {
		BaseUser user = SessionUtil.getUser();
		PageHelper.offsetPage(start, length);
		List<ZhimiGive> list = zhimiGiveMapper.selectGiveZhimiByPage(zhimiGive, user);
		if (list == null)
			return null;
		for (ZhimiGive zhimiGiveInfo : list) {
			zhimiGiveInfo.setNickName(CodeUtil.base64Decode2String(zhimiGiveInfo.getNickName()));
			// 设置账户信息
			AtsAccount account = accountMapper.getAccountByAccType("2", zhimiGiveInfo.getUserId());
			if (account != null && account.getAccAmount() != null) {
				zhimiGiveInfo.setZhimiAmount(account.getAccAmount());
			}
		}

		return new IPageInfo<ZhimiGive>((Page<ZhimiGive>) list);
	}

	public void insertZhimiGive(ZhimiGive zhimiGive) {
		zhimiGive.setId(IDGenerator.generatorId());
		zhimiGiveMapper.insertSelective(zhimiGive);
	}

	public void deleteGiveRecords(String[] ids) {
		zhimiGiveMapper.deleteGiveRecords(ids);
	}

	/**
	 * 获取用户信息
	 */
	public List<Map<String, String>> findKeyValueUser(String eName) {
		BaseUser user = SessionUtil.getUser();
		List<Map<String, String>> listMap = zhimiGiveMapper.findKeyValueUser(eName, user);
		if (listMap == null)
			return null;
		for (Map<String, String> map : listMap) {
			map.put("nickname", CodeUtil.base64Decode2String(map.get("nickname")));
		}
		return listMap;
	}

	/**
	 * 审核信息详情
	 */
	public ZhimiGive getZhimiGiveInfo(String id) {
		ZhimiGive zhimiGive = zhimiGiveMapper.selectZhimiGiveInfo(id);
		zhimiGive.setNickName(CodeUtil.base64Decode2String(zhimiGive.getNickName()));
		return zhimiGive;
	}

	public void checkZhimiGive(ZhimiGive zhimiGive) {
		zhimiGiveMapper.updateZhimiGive(zhimiGive);
		// 审核通过操作智米账户
		if ("2".equals(zhimiGive.getReasonStatus())) {
			AtsAccount account = accountMapper.getAccountByAccType("2", zhimiGive.getUserId());
			Body body = new Body();
			body.put("accId", account.getAccId());
			body.put("accType", "2");
			body.put("userId", account.getUserId());
			body.put("amount", zhimiGive.getZhimiCount());
			body.put("action", zhimiGive.getZhimiType());
			body.put("excDesc", zhimiGive.getReasonDesc());
			atsApi.trans(body);
		}

	}

}
