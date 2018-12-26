package com.yz.service.oa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.oa.OaCampusMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaCampusInfo;
import com.yz.model.oa.OaPrincipalModifyInfo;

@Service
@Transactional
public class OaCampusService {

	@Autowired
	private OaCampusMapper campusMapper;
	
	public String getFinanceCode(String recruitEmpId) {
		return campusMapper.getFinanceCode(recruitEmpId);
	}
	/**
	 * 分页查询校区
	 * @param start
	 * @param length
	 * @param role
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryCampusByPage(int start, int length,OaCampusInfo campusInfo) {
		PageHelper.offsetPage(start, length);
		List<OaCampusInfo> campus = campusMapper.selectAllOaCampus(campusInfo);
		return new IPageInfo((Page) campus);
	}
	
	/**
	 * 验证校区是否存在
	 * @param campusName
	 * @return
	 */
	public int isCampusExist(String campusName){
		return campusMapper.selectCountByCampusName(campusName);
	}
	
	/**
	 * 修改校区信息
	 * @param campusInfo
	 */
	public void updateCampus(OaCampusInfo campusInfo){
		//查询出校区的老负责人
		OaCampusInfo oldCampus = campusMapper.getOaCampusInfo(campusInfo.getCampusId());
		if(null != oldCampus){
			OaPrincipalModifyInfo modifyInfo = new OaPrincipalModifyInfo();
			OaPrincipalModifyInfo lastModifyInfo = campusMapper.getPrincipalModifyInfo(campusInfo.getCampusId(),"1");
			if (null !=lastModifyInfo) {
				String endTime = campusMapper.getPrincipalModifyEndTime(lastModifyInfo.getModifyId());
				modifyInfo.setBeginTime(endTime);
			}else{
				modifyInfo.setBeginTime(oldCampus.getCreateTime());
			}
			modifyInfo.setPrincipalType("1");
			modifyInfo.setBusinessId(campusInfo.getCampusId());
			modifyInfo.setOldEmpId(oldCampus.getEmpId());
			modifyInfo.setNewEmpId(campusInfo.getEmpId());
			modifyInfo.setCreateUser(campusInfo.getUpdateUser());
			modifyInfo.setCreateUserId(campusInfo.getUpdateUserId());
			modifyInfo.setModifyId(IDGenerator.generatorId());
			campusMapper.insertOaPrincipalModify(modifyInfo);
		}
		campusMapper.updateCampus(campusInfo);
	}
	/**
	 * 新增校区信息
	 * @param campusInfo
	 * @return
	 */
	public int insertCampus(OaCampusInfo campusInfo){
		campusInfo.setCampusId(IDGenerator.generatorId());
		return campusMapper.insertCampus(campusInfo);
	}
	
	/**
	 * 获取某个校区的详细信息
	 * @param campusId
	 * @return
	 */
	public OaCampusInfo getOaCampusInfo(String campusId){
		return campusMapper.getOaCampusInfo(campusId);
	}
	
	/**
	 * 停用或者启用校区
	 */
	public void campusBlock(OaCampusInfo campusInfo){
		campusMapper.campusBlock(campusInfo);
	}
	/**
	 * 搜素校区 下来列表
	 * @param eName
	 * @return
	 */
	public List<OaCampusInfo> findAllKeyValue(String eName){
		return campusMapper.findAllKeyValue(eName);
	}
	/**
	 * 所有校区
	 * @return
	 */
	public List<OaCampusInfo> findAllList(){
		return campusMapper.findAllList();
	}
	
	/**
	 * 校区财务代码个数
	 * @param financeNo
	 * @return
	 */
	public int isFinanceNoExist(String financeNo){
		return campusMapper.selectCountByFinanceNo(financeNo);
	}
}
