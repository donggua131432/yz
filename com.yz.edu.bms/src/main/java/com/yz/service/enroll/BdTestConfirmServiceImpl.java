package com.yz.service.enroll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdTestConfirmMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.enroll.BdTestConfirm;
import com.yz.model.enroll.BdTestConfirmMap;
import com.yz.util.StringUtil;


@Service
@Transactional
public class BdTestConfirmServiceImpl{

	@Autowired
	private BdTestConfirmMapper testConfirmMapper;
	
	public List<BdTestConfirmMap> selectAll(BdTestConfirmMap testConfirmMap) {
		// TODO Auto-generated method stub
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		if(jtList.contains("GKZFDY")){//国开辅导员
			testConfirmMap.setExt1(" and bli.recruit_type = '2' and blr.tutor ='"+user.getEmpId()+"'");
		}else if(jtList.contains("CJZFDY")){  //成教辅导员
			testConfirmMap.setExt1(" and bli.recruit_type = '1' and blr.tutor ='"+user.getEmpId() +"'");
		}else if(jtList.contains("GKZFDY") && jtList.contains("CJZFDY")){
			if(StringUtil.hasValue(testConfirmMap.getRecruitType())){ //两者都有的辅导员
				testConfirmMap.setExt1(" and bli.recruit_type='"+testConfirmMap.getRecruitType() +"'");
			}
		}
		return testConfirmMapper.selectAll(testConfirmMap);
	}


	public void okConfirm(String learnId) {
		// TODO Auto-generated method stub
		/*BdTestConfirm testConfirm = new BdTestConfirm();
		testConfirm.setTcId(tcId);
		testConfirm.setIsOk("1");
		testConfirmMapper.updateByPrimaryKeySelective(testConfirm);*/
		testConfirmMapper.okConfirm(learnId);
	}

}
