package com.yz.service.recruit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper; 
import com.yz.dao.recruit.StudentWhitelistMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.StudentWhitelistQuery;
import com.yz.model.recruit.StudentWhitelist;
import com.yz.model.recruit.StudentWhitelistEdit;
import com.yz.model.recruit.StudentWhitelistPage;
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;
import com.yz.util.Assert;

@Service
@Transactional
public class StudentWhiteListService {
	
	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private StudentWhitelistMapper whitelistMapper;

	public void setting(StudentWhitelistEdit wlInfo) {
		String[] stdIds = wlInfo.getStdIds();

		String[] scholarships = wlInfo.getScholarships();

		Assert.notEmpty(stdIds, "被设置的学员Id不能为空");

		List<StudentWhitelist> wlList = new ArrayList<StudentWhitelist>();

		whitelistMapper.deleteWhitelist(stdIds);

		if (scholarships != null && scholarships.length > 0) {
			for (String scholarship : scholarships) {
				for (String stdId : stdIds) {
					StudentWhitelist wl = new StudentWhitelist();
					wl.setStdId(stdId);
					
					SysDict dict = sysDictService.getDict("scholarship." + scholarship);
					String sg = dict.getExt1();//优惠分组
					
					wl.setSg(sg);
					wl.setScholarship(scholarship);
					wl.setUpdateUser(wlInfo.getUpdateUser());
					wl.setUpdateUserId(wlInfo.getUpdateUserId());
					wl.setWlId(IDGenerator.generatorId());
					wlList.add(wl);
				}
			}

			whitelistMapper.setting(wlList);
		}
	}
	/**
	 * 查询学员信息
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<StudentWhitelistPage> getStudents(StudentWhitelistQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		List<StudentWhitelistPage> pageList = whitelistMapper.getStudents(queryInfo);
		
		if(pageList == null)
			return null;
		
		return new IPageInfo<StudentWhitelistPage>((Page<StudentWhitelistPage>) pageList);
	}
	public List<String> getWhitelist(String stdId) {
		return whitelistMapper.getWhitelist(stdId);
	}

}
