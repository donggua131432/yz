package com.yz.dao;

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
	 * 查询学员绑定关系
	 * @param userMap
	 * @return
	 */
	int countRelation(Map<String, String> userMap);
	/**
	 * 更新学员绑定关系
	 * @param userMap
	 */
	int updateRelation(Map<String, String> userMap);
	/**
	 * 插入学员绑定关系
	 * @param userMap
	 */
	int insertRelation(Map<String, String> userMap);
	/**
	 * 删除学员绑定关系
	 * @param stdId
	 * @return
	 */
	int deleteRelation(String stdId);
	/**
	 * 根据学员ID查询学员关系
	 * @param stdId
	 * @return
	 */
	List<Map<String, String>> getRelationsByStd(String stdId);
	
}
