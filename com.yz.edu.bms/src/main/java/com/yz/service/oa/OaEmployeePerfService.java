package com.yz.service.oa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.invite.InviteFansMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.oa.OaEmployeePerfMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.EmpModifyListInfo;
import com.yz.model.oa.OaEmpLearnInfoPerfInfo;
import com.yz.model.oa.OaEmployeePerfQuery;
import com.yz.util.JsonUtil;

@Service
@Transactional
public class OaEmployeePerfService
{
	private static final Logger log = LoggerFactory.getLogger(OaEmployeePerfService.class);
	
	@Autowired
	private OaEmployeePerfMapper perfMapper;
	
	@Autowired
	private OaEmployeeMapper empMapper;
	
	@Autowired
	private InviteFansMapper fansMapper;
	
	/**
	 * 根据条件获取待分配的学员信息
	 * @param queryInfo
	 * @return
	 */
	public IPageInfo<OaEmpLearnInfoPerfInfo> getLearnInfoPerfInfoList(OaEmployeePerfQuery queryInfo) {
		PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
		
		List<OaEmpLearnInfoPerfInfo> list = perfMapper.getOaEmpLearnInfoPerfInfo(queryInfo);
		return new IPageInfo<OaEmpLearnInfoPerfInfo>((Page<OaEmpLearnInfoPerfInfo>) list);
	}
	/**
	 * 分配学员
	 * @param idArray
	 * @param operType
	 */
	public void allocationLearn(String[] idArray,String operType,String empId,BaseUser user,String personType){
		EmpModifyListInfo modifyInfo = empMapper.getLastModifyInfo(empId);
		//解析idArray
		List<OaEmpLearnInfoPerfInfo> perfInfos = new ArrayList<>();
		for(String str : idArray){
			OaEmpLearnInfoPerfInfo perfInfo =new OaEmpLearnInfoPerfInfo();
			String[] splitStr = str.split(";");
			
			perfInfo.setLearnId(splitStr[0]);
			perfInfo.setRecrodsNo(splitStr[1]);
			
			perfInfos.add(perfInfo);
		}
		if(null != modifyInfo){
			if(operType.equals("1")){       //部分归属到原部门
				perfMapper.partChangeToOld(perfInfos, user.getRealName(), user.getUserId(),empId,modifyInfo.getOldDpId(),modifyInfo.getOldCampusId());
			}else if(operType.equals("2")){ //部分归属到新部门
				log.error("======================绩效变更设计到的学员="+JsonUtil.object2String(perfInfos));
				perfMapper.updateBdLearnRules(perfInfos,modifyInfo.getNewDpId(), modifyInfo.getNewCampusId());
				List<String> userIds = perfMapper.getUserIdByLearnId(perfInfos);
				if(null != userIds && userIds.size() >0){
					fansMapper.updateUsFollow(userIds, modifyInfo.getNewCampusId(), modifyInfo.getNewDpId());	
				}
				perfMapper.partChangeToNew(perfInfos, user.getRealName(), user.getUserId(), empId, modifyInfo.getNewDpId(), modifyInfo.getNewCampusId());
			}else if(operType.equals("3")){ //全部归属到原部门
				perfMapper.allChangeToOld(modifyInfo.getModifyId(), user.getRealName(), user.getUserId(),empId,modifyInfo.getOldDpId(),modifyInfo.getOldCampusId());
			}else if(operType.equals("4")){ //全部归属到新部门
				perfMapper.updateAllBdLearnRules(empId,modifyInfo.getNewDpId(), modifyInfo.getNewCampusId(), modifyInfo.getOldDpId());
				List<String> userIds = perfMapper.getAllUserIdByLearnId(empId, modifyInfo.getOldDpId());
				log.error("======================绩效变更设计到的学员="+JsonUtil.object2String(userIds));
				if(null != userIds && userIds.size() >0){
					fansMapper.updateUsFollow(userIds, modifyInfo.getNewCampusId(), modifyInfo.getNewDpId());	
				}
				perfMapper.allChangeToNew(modifyInfo.getModifyId(), user.getRealName(), user.getUserId(), empId, modifyInfo.getNewDpId(), modifyInfo.getNewCampusId());
			}
			
			if(personType.equals("2")){ //不合并的统一处理
				//清理跟进关系以及招生关系
				List<Map<String, String>> list = fansMapper.selectList(empId);
				log.debug("------------------------------- empId :[ " + empId + "]先记录一下， 免的那群鬼又TM反悔 GGG : " + JsonUtil.object2String(list));
				fansMapper.clearFollow(empId, "2");
				//empMapper.clearLearnRules(empId);
			}
		}
	}
}
