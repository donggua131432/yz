package com.yz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.dao.BdsUserMapper;


@Service
@Transactional
public class BdsOaEmployeeService {

	@Autowired
	private BdsUserMapper userMapper;

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEmployInfo(String keywords) {
		List<Map<String, Object>> empInfolist = userMapper.getEmpInfoByNameOrMobile(keywords);
		if(empInfolist!=null) {
			HashMap<String, Object> empIdCard=new HashMap<String, Object>();
			List<Map<String, Object>>  empxzlist=new ArrayList<Map<String, Object>>();
			for (Map<String, Object> empInfo : empInfolist) {
				//去掉重复离职员工
				if(empIdCard.containsKey(empInfo.get("idCard").toString())) {
					if(((Map<String, Object>)empIdCard.get(empInfo.get("idCard").toString())).get("empStatus").equals("2")) {
						if(empInfo.get("empStatus").toString().equals("3")) {
							empxzlist.add((Map<String, Object>)empIdCard.get(empInfo.get("idCard").toString()));
						}
					}
					if(empInfo.get("empStatus").toString().equals("2")) {
						empxzlist.add(empInfo);
					}
					
				}else {
					empIdCard.put(empInfo.get("idCard").toString(), empInfo);
				}
				//封装员工职称
				List<Map<String, String>> jdIdMap =userMapper.findEmpTitle(empInfo.get("empId").toString());
				empInfo.put("jdIds", jdIdMap);
				for (Map<String, String> jdid : jdIdMap) {
					//如果是校办身份则不显示查询结果
					if(jdid.get("jtId").toString().equalsIgnoreCase("XB")||jdid.get("jtId").toString().equalsIgnoreCase("XZ")||jdid.get("jtId").toString().equalsIgnoreCase("FXZ")) {
						empxzlist.add(empInfo);
						break;
					}
					
				}
				
			}
			if(empxzlist!=null) {
				for (Map<String, Object> empxz : empxzlist) {
					empInfolist.remove(empxz);
				}
			}
			
		}
		//得到职位信息
		return empInfolist;
	}

}
