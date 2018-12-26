package com.yz.dao.gk;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.gk.BdStudentGkUnifiedExamInfo;
import com.yz.model.gk.BdStudentGkUnifiedExamQuery;

/**
 * 国开统考
 * @author lx
 * @date 2018年6月6日 上午11:37:49
 */
public interface BdStudentGKUnifiedExamMapper {

	/**
	 * 获取列表
	 * @param query
	 * @return
	 */
	List<BdStudentGkUnifiedExamInfo> findStdGKUnifiedExamList(BdStudentGkUnifiedExamQuery query);
	
	/**
	 * 新增
	 * @param info
	 */
	void insertStdGKUnifiedExamInfo(BdStudentGkUnifiedExamInfo info);
	
	/**
	 * 获取某个具体的
	 * @param id
	 * @return
	 */
	BdStudentGkUnifiedExamInfo getGkUnifiedExam(String id);
	
	/**
	 * 删除
	 * @param id
	 */
	void delGkUnifiedExam(String id);
	
	/**
	 * 禁用或者启用
	 * @param id
	 * @param ifShow
	 */
	void startOrStopGkUnifiedExam(@Param("id") String id,@Param("ifShow") String ifShow);
	
	/**
	 * 修改
	 * @param info
	 */
	void updateGkUnifiedExam(BdStudentGkUnifiedExamInfo info);
	
	/**
	 * 获取被引入数量
	 * @return
	 */
	int getTaskCountByGKUniFiedId(String id);
	
	/**
	 * 针对修改附件
	 * @param info
	 */
	void updateGkunifiedFileInfo(BdStudentGkUnifiedExamInfo info);
}
