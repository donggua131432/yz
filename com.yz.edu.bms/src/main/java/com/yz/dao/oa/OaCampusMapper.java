package com.yz.dao.oa;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.oa.OaCampusInfo;
import com.yz.model.oa.OaPrincipalModifyInfo;
/**
 * 校区
 * @author lx
 * @date 2017年6月30日 下午5:25:58
 */
public interface OaCampusMapper {

	/**
	 * 根据招生老师编号 获取 校区财务代码
	 * */
	public String getFinanceCode(String recruitEmpId);
	
	
	/**
	 * 所有的校区
	 */
	public List<OaCampusInfo> selectAllOaCampus(OaCampusInfo campusInfo);
	
	/**
	 * 根据校区名字查询校区数量
	 */
	public int selectCountByCampusName(String campusName);
	
	/**
	 * 新增校区
	 * @param campusInfo
	 * @return
	 */
	public int insertCampus(OaCampusInfo campusInfo);
	
	/**
	 * 获取某个校区的详细
	 * @param campusId
	 * @return
	 */
	public OaCampusInfo getOaCampusInfo(String campusId);
	
	/**
	 * 修改校区信息
	 * @param campusInfo
	 */
	public void updateCampus(OaCampusInfo campusInfo);
	
	/**
	 * 启用或者停用校区
	 * @param campusInfo
	 */
	public void campusBlock(OaCampusInfo campusInfo);
	
	/**
	 * 搜素校区 下来列表
	 * @param eName
	 * @return
	 */
	public List<OaCampusInfo> findAllKeyValue(@Param("eName") String eName);
	/**
	 * 所有校区
	 * @return
	 */
	public List<OaCampusInfo> findAllList();
	
	
	/**
	 * 根据校区财务代码查询校区数量
	 */
	public int selectCountByFinanceNo(String financeNo);
	
	/**
	 * 获取负责人变更最新id
	 * @return
	 */
	public OaPrincipalModifyInfo getPrincipalModifyInfo(@Param("businessId") String businessId,@Param("principalType") String principalType);
	
	/**
	 * 修改上一条的最后结束时间
	 * @param modifyId
	 */
	public void updateLastModifyEndTime(@Param("modifyId") String modifyId);
	
	/**
	 * 插入变更记录
	 * @param modifyInfo
	 */
	public void insertOaPrincipalModify(OaPrincipalModifyInfo modifyInfo);
	
	/**
	 * 最后一条变更的结束时间
	 * @param modifyId
	 * @return
	 */
	public String getPrincipalModifyEndTime(@Param("modifyId") String modifyId);
}
