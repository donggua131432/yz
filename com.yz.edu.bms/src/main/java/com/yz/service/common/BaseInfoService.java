package com.yz.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.common.BaseInfoMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.CampusSelectInfo;
import com.yz.model.common.DpSelectInfo;
import com.yz.model.common.EmpListInfo;
import com.yz.model.common.EmpQueryInfo;
import com.yz.model.common.EmpSelectInfo;
import com.yz.model.common.GroupSelectInfo;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.ItemSelectInfo;
import com.yz.model.common.PfsnSelectInfo;
import com.yz.model.common.TaSelectInfo;
import com.yz.model.common.TbListInfo;
import com.yz.model.common.TbQueryInfo;
import com.yz.model.common.ThpListInfo;
import com.yz.model.common.ThpQueryInfo;
import com.yz.model.common.UnvsSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;

@Service
@Transactional
public class BaseInfoService {

	@Autowired
	private BaseInfoMapper baseInfoMapper;

	public IPageInfo<UnvsSelectInfo> getUnvsSelectList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getUnvsSelectList(sqInfo));
	}

	public IPageInfo<PfsnSelectInfo> getPfsnSelectList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getPfsnSelectList(sqInfo));
	}

	public IPageInfo<TaSelectInfo> getTaSelectList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getTaSelectList(sqInfo));
	}

	public IPageInfo<CampusSelectInfo> getCampusList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getCampusList(sqInfo));
	}

	public IPageInfo<DpSelectInfo> getDepartmentList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getDepartmentList(sqInfo));
	}

	public IPageInfo<EmpSelectInfo> getEmployeeList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getEmployeeList(sqInfo));
	}

	public List<ItemSelectInfo> getFeeItemList(SelectQueryInfo sqInfo) {

		return baseInfoMapper.getFeeItemList(sqInfo);
	}

	public IPageInfo<GroupSelectInfo> getGroupList(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) baseInfoMapper.getGroupList(sqInfo));
	}

	public IPageInfo<EmpListInfo> getEmpList(EmpQueryInfo queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		BaseUser user = SessionUtil.getUser();

		List<String> jtList = user.getJtList();

		if (jtList != null && (jtList.contains("XQZL")|| jtList.contains("CJXJ")|| jtList.contains("GKXJ"))) {
			user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
		}
		
		
		return new IPageInfo((Page) baseInfoMapper.getEmpList(queryInfo, user));
	}

	public IPageInfo<ThpListInfo> getThpUnSelectedListByPage(ThpQueryInfo queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		return new IPageInfo((Page) baseInfoMapper.getThpUnSelectedList(queryInfo));
	}
	
	public List<ThpListInfo> getThpSelectedList(ThpQueryInfo queryInfo) {
		return baseInfoMapper.getThpSelectedList(queryInfo);
	}

	public IPageInfo<TbListInfo> getTbUnSelectedListByPage(TbQueryInfo queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		return new IPageInfo((Page) baseInfoMapper.getTbUnSelectedList(queryInfo));
	}

	public List<TbListInfo> getTbSelectedList(TbQueryInfo queryInfo) {
		return baseInfoMapper.getTbSelectedList(queryInfo);
	}


	/**
	 * 所有校监列表
	 * @param eName
	 * @return
	 */
	public List<Map<String, String>> schoolSuperKeyValue(String eName){
		return baseInfoMapper.schoolSuperKeyValue(eName);
	}

	/**
	 * 所有校监列表
	 * @param empId
	 * @return
	 */
	public List<String> getDpIdList(String empId){
		return baseInfoMapper.getDpIdList(empId);
	}

}
