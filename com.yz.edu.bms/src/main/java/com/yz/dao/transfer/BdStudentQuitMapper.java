package com.yz.dao.transfer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BaseUser;
import com.yz.model.transfer.BdStudentQuitInfo;
import com.yz.model.transfer.BdStudentQuitQuery;

/**
 * 休学申请
 * 
 * @author lx
 * @date 2018年5月24日 下午2:29:21
 */
public interface BdStudentQuitMapper {

	/**
	 * 获取申请列表(休学申请)
	 * @param quitQuery
	 * @param user
	 * @return
	 */
	List<BdStudentQuitInfo> findAllBdStudentQuit(@Param("quit") BdStudentQuitQuery quitQuery,
			@Param("user") BaseUser user);
	/**
	 * 休学申请
	 * @param quitInfo
	 */
	void insertQuitSchoolApply(BdStudentQuitInfo quitInfo);

	/**
	 * 审核申请
	 * @param quitInfo
	 */
	void checkStudentQuitSchoolApply(BdStudentQuitInfo quitInfo);

	/**
	 * 获取某个发起的休学状态
	 * @param id
	 * @return
	 */
	String getStudentQuitSchoolStatus(String id);

	/**
	 * 撤销申请
	 * @param id
	 */
	void delStudentQuitSchoolApply(String id);
	
	
	/**
	 * 获取申请列表(休学审核)
	 * @param quitQuery
	 * @param user
	 * @return
	 */
	List<BdStudentQuitInfo> findAllBdStudentQuitForCheck(@Param("quit") BdStudentQuitQuery quitQuery,
			@Param("user") BaseUser user);
	
	/**
	 * 查找要申请休学的学员
	 * @param sName
	 * @param user
	 * @return
	 */
	List<Map<String, String>> findStudentInfo(@Param("sName") String sName, @Param("user") BaseUser user);
	
	/**
	 * 查看是否有休学申请
	 * @param learnId
	 * @return
	 */
	int selectQuitCount(String learnId);
	
	/**
	 * 某个具体的消息
	 * @param id
	 * @return
	 */
	BdStudentQuitInfo getQuitSchoolApplyInfo(String id);
	
	/**
	 * 变更学员的学业状态
	 * @param learnId
	 */
	void updateStdStageByLearnId(String learnId);
	
	/**
	 * 批量更新
	 * @param list
	 */
	void batchUpdateStdStageByLearnId(@Param("list") List<String> list);
	
	/**
	 * 批量审核
	 * @param list
	 */
	void batchUpdateCheckStatus(@Param("list") List<BdStudentQuitInfo> list);
	
	
	/**
	 * 休息数据的导出
	 * @param quitQuery
	 * @return
	 */
	List<BdStudentQuitInfo> findAllBdStudentQuitForExport(@Param("quit") BdStudentQuitQuery quitQuery,@Param("user") BaseUser user);
}
