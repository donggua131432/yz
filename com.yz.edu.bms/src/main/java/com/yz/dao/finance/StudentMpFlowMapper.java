package com.yz.dao.finance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.communi.Body;

public interface StudentMpFlowMapper {
	/**
	 * 查询奖励规则条件
	 * @param learnId
	 * @return
	 */
	Body getCondition(String learnId);
	/**
	 * 获取历史报读信息
	 * @param learnId
	 * @return
	 */
	List<Map<String, String>> getHistoryLearn(@Param("stdId") String stdId, @Param("learnId") String learnId);
	/**
	 * 查询学员是否绑定用户
	 * @param learnId
	 * @return
	 */
	List<Map<String, String>> getRelation(String learnId);
	/**
	 * 查询学员招生老师信息
	 * @param serialNo
	 * @return
	 */
	Body getRecruitMap(String serialNo);
	/**
	 * 查询学员招生老师信息
	 * @param orderNo
	 * @return
	 */
	Body getRecruitMapByOrderNo(String orderNo);
	/**
	 * 查询学员招生老师信息
	 * @param orderNo
	 * @return
	 */
	Body getRecruitMapBySerialMark(String serialMark);
	
	/**
	 * 根据学员ID查询学员关系
	 * @param stdId
	 * @return
	 */
	List<Map<String, String>> getRelationsByStd(String stdId);
}
