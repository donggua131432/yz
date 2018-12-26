package com.yz.service.recruit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.service.ISecurityService;
import com.yz.dao.admin.BmsUserMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentOther;

/**
 * 钉钉应用 招生老师 录入
 * @author lx
 * @date 2017年12月4日 下午5:23:55
 */
@Transactional
@Service
public class StudentEnterService
{
	@Autowired
	private StudentRecruitMapper recruitMapper;
	
	@Autowired
	private BmsUserMapper userMapper;
	
	@Autowired
	private ISecurityService securityService;
	
	public Object ifExistInfo(String idType, String idCard, String oldIdCard,String recruitType,String userId) {
		//判断是否存在有学员信息存在
		int count = recruitMapper.getCountBy(idType, idCard, oldIdCard);

		if (count == 0) {
			return null;
		}
		//从新取user信息
		BaseUser userInfo = userMapper.selectBaseUserByUserId(userId);
		userInfo = securityService.assembly(userInfo);
		if(!userInfo.getUserLevel().equals("1")){
			//如果有学员信息存在,判断其招生老师是否在职
			int recruitCount = recruitMapper.getStuRecruitStatus(idType, idCard);
			if(recruitCount > 0){
				//验证已经存在的是否是招生关系
				int ifRecruit = recruitMapper.getStuCountByEmpId(idType, idCard, userInfo.getEmpId());
				Map<String, Object> result = new HashMap<String, Object>();
				if (ifRecruit == 0) {
					result.put("errCode", "E00098");
					return result;
				}else{
					if (recruitType.equals("1")) {
						result.put("errCode", "E00099");
						return result;
					}
				}
			}
			
		}
		
		BdStudentBaseInfo baseInfo = recruitMapper.getStudentBaseInfo(idType, idCard);
		BdStudentOther other = recruitMapper.getStudentOtherInfo(baseInfo.getStdId());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("baseInfo", baseInfo);
		result.put("other", other);
		return result;
	}
}
