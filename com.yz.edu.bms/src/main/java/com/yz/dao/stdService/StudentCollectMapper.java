package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentCollectMapper {

    List<StudentCollectInfo> findAllCollectList(@Param("queryInfo") StudentCollectQuery query, @Param("user") BaseUser user);

	/**
	 * 添加备注
	 * @param ctId
	 * @param remark
	 * @return
	 */
	int updateRemark(@Param("ctId") String ctId, @Param("remark") String remark);

	int updateCollect(StudentCollectInfo studentCollectInfo);
	
	/**
	 * 检查是否存在
	 * @param learnId
	 * @return
	 */
	public int checkIfExistByLearnId(String learnId);
	
	/**
	 * 批量初始化学籍资料跟进的数据
	 * @param list
	 */
	public void addStuCollectInfo(@Param("list") List<StudentCollectInfo> list);
	
	
	/**
	 * 单个生成学籍资料跟进的数据
	 */
	public void singleAddStuCollectInfo(StudentCollectInfo collInfo);
	
	/**
	 * 批量删除学籍资料跟进的数据
	 * @param ids
	 * @param taskId
	 */
	public void delStuCollectInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
	
	/**
	 * 单个删除
	 * @param learnId
	 * @param taskId
	 */
	public void aloneDelStuCollectInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);

}
