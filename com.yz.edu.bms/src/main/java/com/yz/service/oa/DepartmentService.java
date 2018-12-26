package com.yz.service.oa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.oa.DepartmentMapper;
import com.yz.dao.oa.OaCampusMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.DepartmentInfo;
import com.yz.model.oa.OaDepQueryInfo;
import com.yz.model.oa.OaPrincipalModifyInfo;
import com.yz.util.StringUtil;

@Service
@Transactional
public class DepartmentService {

	@Autowired
	private DepartmentMapper depMapper;
	
	@Autowired
	private OaCampusMapper campusMapper;
	
	/**
	 * 分页查询部门
	 * @param start
	 * @param length
	 * @param role
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryDepByPage(OaDepQueryInfo depInfo) {
		PageHelper.offsetPage(depInfo.getStart(), depInfo.getLength());
		List<DepartmentInfo> depInfos = depMapper.selectAllDepartmentInfo(depInfo);
		if(null != depInfos && depInfos.size() >0){
			for(DepartmentInfo dpInfo :depInfos){
				if(StringUtil.hasValue(dpInfo.getRecruitRules())){
					dpInfo.setRecruitTypes(dpInfo.getRecruitRules().split(";"));
				}
			}
		}
		return new IPageInfo((Page) depInfos);
	}
	
	/**
	 * 验证部门是否存在
	 * @param campusName
	 * @return
	 */
	public int isDepExist(String dpName,String campusId){
		return depMapper.selectCountByDepName(dpName,campusId);
	}
	
	/**
	 * 修改部门信息
	 * @param dpInfo
	 */
	public void updateDepartmentInfo(DepartmentInfo dpInfo,String[] jdIds){
		
		//查询出部门的老负责人
		DepartmentInfo oldDpInfo = depMapper.getDepartmentInfo(dpInfo.getDpId());
		if(null != oldDpInfo){
			OaPrincipalModifyInfo modifyInfo = new OaPrincipalModifyInfo();
			OaPrincipalModifyInfo lastModifyInfo = campusMapper.getPrincipalModifyInfo(dpInfo.getDpId(),"2");
			if (null !=lastModifyInfo) {
				String endTime = campusMapper.getPrincipalModifyEndTime(lastModifyInfo.getModifyId());
				modifyInfo.setBeginTime(endTime);
			}else{
				modifyInfo.setBeginTime(oldDpInfo.getCreateTime());
			}
			modifyInfo.setPrincipalType("2");
			modifyInfo.setBusinessId(dpInfo.getDpId());
			modifyInfo.setOldEmpId(oldDpInfo.getEmpId());
			modifyInfo.setNewEmpId(dpInfo.getEmpId());
			modifyInfo.setCreateUser(dpInfo.getUpdateUser());
			modifyInfo.setCreateUserId(dpInfo.getUpdateUserId());
			modifyInfo.setModifyId(IDGenerator.generatorId());
			campusMapper.insertOaPrincipalModify(modifyInfo);
		}
		depMapper.updateDepartmentInfo(dpInfo);
		if(null !=jdIds && jdIds.length >0){
			depMapper.deleteDepTitle(dpInfo.getDpId());
			depMapper.insertDepJdIds(dpInfo.getDpId(),jdIds);
		}
	}
	
	/**
	 * 新增部门信息
	 * @param dpInfo
	 * @return
	 */
	public void insertDpInfo(DepartmentInfo dpInfo,String[] jdIds){
		dpInfo.setDpId(IDGenerator.generatorId());
	    depMapper.insertDpInfo(dpInfo);
		if(null != jdIds && jdIds.length >0){
			depMapper.insertDepJdIds(dpInfo.getDpId(),jdIds);
		}
	}
	
	/**
	 * 获取某个部门的详细信息
	 * @param dpId
	 * @return
	 */
	public DepartmentInfo getDepartmentInfo(String dpId){
		return depMapper.getDepartmentInfo(dpId);
	}
	
	/**
	 * 停用或者启用部门
	 */
	public void depBlock(DepartmentInfo dpInfo){
		depMapper.depBlock(dpInfo);
	}
	
	/**
	 * 下拉列表
	 * @param eName
	 * @return
	 */
	public List<Map<String, String>> findAllKeyValue(String eName){
		return depMapper.findAllKeyValue(eName);
	}
	/**
	 * 某个校区小的所有部门
	 * @param campusId
	 * @return
	 */
	public List<Map<String, String>> findAllListByCampusId(String campusId){
		return depMapper.findAllListByCampusId(campusId);
	}
	
	/**
	 * 部门下的所有职称
	 * @param depId
	 * @return
	 */
	public List<Map<String, String>> findDepTitle(String depId){
		return depMapper.findDepTitle(depId);
	}
}
