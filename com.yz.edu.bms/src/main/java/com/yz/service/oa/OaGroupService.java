package com.yz.service.oa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.oa.OaCampusMapper;
import com.yz.dao.oa.OaGroupMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.DepartmentInfo;
import com.yz.model.oa.OaGroupInfo;
import com.yz.model.oa.OaPrincipalModifyInfo;

@Service
@Transactional
public class OaGroupService {

	@Autowired
	private OaGroupMapper groupMapper;
	
	@Autowired
	private OaCampusMapper campusMapper;
	
	
	/**
	 * 分页查询部门招生组
	 * @param start
	 * @param length
	 * @param role
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryGroupByPage(int start, int length,OaGroupInfo groupInfo) {
		PageHelper.offsetPage(start, length);
		List<OaGroupInfo> groupInfos = groupMapper.selectAllGroupInfo(groupInfo);
		return new IPageInfo((Page) groupInfos);
	}
	
	/**
	 * 验证部门招生组是否存在
	 * @param groupName
	 * @param dpId
	 * @return
	 */
	public int isGroupNameExist(String groupName,String dpId){
		return groupMapper.selectCountByGroupName(groupName,dpId);
	}
	
	/**
	 * 修改部门招生组信息
	 * @param groupInfo
	 */
	public void updateOaGroupInfo(OaGroupInfo groupInfo){
		//查询出招生组的老负责人
		OaGroupInfo oldGroupInfo = groupMapper.getOaGroupInfo(groupInfo.getGroupId());
		if(null != oldGroupInfo){
			OaPrincipalModifyInfo modifyInfo = new OaPrincipalModifyInfo();
			OaPrincipalModifyInfo lastModifyInfo = campusMapper.getPrincipalModifyInfo(groupInfo.getDpId(),"3");
			if (null !=lastModifyInfo) {
				String endTime = campusMapper.getPrincipalModifyEndTime(lastModifyInfo.getModifyId());
				modifyInfo.setBeginTime(endTime);
			}else{
				modifyInfo.setBeginTime(oldGroupInfo.getCreateTime());
			}
			modifyInfo.setPrincipalType("3");
			modifyInfo.setBusinessId(groupInfo.getGroupId());
			modifyInfo.setOldEmpId(oldGroupInfo.getEmpId());
			modifyInfo.setNewEmpId(groupInfo.getEmpId());
			modifyInfo.setCreateUser(groupInfo.getUpdateUser());
			modifyInfo.setCreateUserId(groupInfo.getUpdateUserId());
			modifyInfo.setModifyId(IDGenerator.generatorId());
			campusMapper.insertOaPrincipalModify(modifyInfo);
		}
		groupMapper.updateOaGroupInfo(groupInfo);
	}
	
	/**
	 * 新增部门招生组信息
	 * @param groupInfo
	 * @return
	 */
	public int insertGroupInfo(OaGroupInfo groupInfo){
		groupInfo.setGroupId(IDGenerator.generatorId());
		return groupMapper.insertGroupInfo(groupInfo);
	}
	
	/**
	 * 获取某个部门招生组的详细信息
	 * @param dpId
	 * @return
	 */
	public OaGroupInfo getOaGroupInfo(String groupId){
		return groupMapper.getOaGroupInfo(groupId);
	}
	
	/**
	 * 停用或者启用部门招生组
	 */
	public void groupBlock(OaGroupInfo groupInfo){
		groupMapper.groupBlock(groupInfo);
	}
	/**
	 * 某个部门下的所有招生组信息
	 * @param dpId
	 * @return
	 */
	public List<Map<String, String>> findAllListByDpId(String dpId){
		return groupMapper.findAllListByDpId(dpId);
	}
	
}
