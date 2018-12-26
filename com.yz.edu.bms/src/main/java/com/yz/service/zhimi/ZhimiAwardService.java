package com.yz.service.zhimi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.invite.InviteUserMapper;
import com.yz.dao.zhimi.ZhimiAwardMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.zhimi.ZhimiAwardQueryInfo;
import com.yz.model.condition.zhimi.ZhimiAwardRecordsQuery;
import com.yz.model.condition.zhimi.ZhimiProductRecordsQuery;
import com.yz.model.zhimi.ZhimiAwardInfo;
import com.yz.model.zhimi.ZhimiAwardList;
import com.yz.model.zhimi.ZhimiAwardRecords;
import com.yz.model.zhimi.ZhimiCustomizeAttr;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class ZhimiAwardService {

	@Autowired
	private ZhimiAwardMapper awardMapper;

	@Autowired
	private InviteUserMapper inviteUserMapper;

	public IPageInfo<ZhimiAwardList> getList(ZhimiAwardQueryInfo queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());

		List<ZhimiAwardList> awardList = awardMapper.getList(queryInfo);

		if (awardList == null)
			return null;

		return new IPageInfo<ZhimiAwardList>((Page<ZhimiAwardList>) awardList);
	}

	public void add(ZhimiAwardInfo awardInfo) {
		ArrayList<ZhimiCustomizeAttr> itemsList = new ArrayList<>();
		if(awardInfo.getItems().size() >0){
			awardInfo.getItems().stream().forEach(v ->{
				if(StringUtil.isNotBlank(v.getAttrValue())){
					itemsList.add(v);
				}
			});
		}
		awardInfo.setItems(itemsList);
		awardMapper.addAwardInfo(awardInfo);
	}

	public void update(ZhimiAwardInfo awardInfo) {
		ArrayList<ZhimiCustomizeAttr> itemsList = new ArrayList<>();
		if(awardInfo.getItems().size() >0){
			awardInfo.getItems().stream().forEach(v ->{
				if(StringUtil.isNotBlank(v.getAttrValue())){
					itemsList.add(v);
				}
			});
		}
		awardInfo.setItems(itemsList);
		awardMapper.updateAwardInfo(awardInfo);
	}

	public ZhimiAwardInfo getAwardInfo(String ruleCode) {
		return awardMapper.getAwardInfo(ruleCode);
	}

	public boolean isExsit(String ruleCode) {
		int count = awardMapper.countBy(ruleCode);
		return count < 1;
	}

	public IPageInfo<ZhimiAwardRecords> getRecordsList(ZhimiAwardRecordsQuery queryInfo) {
		if (StringUtil.hasValue(queryInfo.getNickname()) || StringUtil.hasValue(queryInfo.getMobile())
				|| StringUtil.hasValue(queryInfo.getYzCode()) || StringUtil.hasValue(queryInfo.getRealName())) {
			ZhimiProductRecordsQuery q = new ZhimiProductRecordsQuery();
			q.setMobile(queryInfo.getMobile());
			q.setNickname(queryInfo.getNickname());
			q.setYzCode(queryInfo.getYzCode());
			q.setRealName(queryInfo.getRealName());
			// 用户数据不做模糊搜索
			List<String> userIds = inviteUserMapper.getUserIds(q);
			queryInfo.setUserIds(userIds);
		}

		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<ZhimiAwardRecords> awardRecords = awardMapper.getRecordsList(queryInfo);

		if (awardRecords == null)
			return null;

		Map<String, Map<String, String>> exsit = new HashMap<String, Map<String, String>>();

		for (ZhimiAwardRecords r : awardRecords) {
			String userId = r.getUserId();
			String triggerUserId = r.getTriggerUserId();
			Map<String, String> userInfo = null;
			Map<String, String>triggerUser = null;
			if (!exsit.containsKey(userId)) {
				userInfo = inviteUserMapper.getUserInfo(userId);
				if(triggerUserId!=null && !"".equals(triggerUserId)){
					triggerUser=inviteUserMapper.getUserInfo(triggerUserId);
				}
			} else {
				userInfo = exsit.get(userId);
			}

			if (userInfo != null) {
				String nickname = userInfo.get("nickname");
				if (StringUtil.hasValue(nickname)) {
					try {
						r.setNickname(CodeUtil.base64Decode2String(nickname));
					} catch (Exception e) {
						r.setNickname(nickname);
					}
				}
				r.setRealName(userInfo.get("name"));
				r.setMobile(userInfo.get("mobile"));
				r.setYzCode(userInfo.get("yzCode"));
				r.setIdCard(userInfo.get("idCard"));
			}

			if (triggerUser != null) {
				String triggerNickname = triggerUser.get("nickname");
				if (StringUtil.hasValue(triggerNickname)) {
					try {
						r.setTriggerNickname(CodeUtil.base64Decode2String(triggerNickname));
					} catch (Exception e) {
						r.setTriggerNickname(triggerNickname);
					}
				}
				r.setTriggerRealName(triggerUser.get("name"));
				r.setTriggerMobile(triggerUser.get("mobile"));
				r.setTriggerYzCode(triggerUser.get("yzCode"));
				r.setTriggerIdCard(triggerUser.get("idCard"));
			}
		}

		return new IPageInfo<ZhimiAwardRecords>((Page<ZhimiAwardRecords>) awardRecords);
	}
	/**
	 * 禁用或者启用智米赠送规则
	 */
	public void zhimiAwardToggle(ZhimiAwardInfo awardInfo){
		awardMapper.zhimiAwardToggle(awardInfo);
	}
	
	/**
	 * 数据字典规则分组
	 * @return
	 */
	public List<Map<String, String>> getRuleGroupAttrList(){
		return awardMapper.getRuleGroupAttrList();
	}
	
	/**
	 * 同组的智米赠送规则
	 * @return
	 */
	public List<ZhimiAwardInfo> getAwardListInfo(String ruleGroup){
		return awardMapper.getAwardListInfo(ruleGroup);
	}
}
