package com.yz.dao.recruit;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.condition.recruit.StudentWhitelistQuery;
import com.yz.model.recruit.StudentWhitelist;
import com.yz.model.recruit.StudentWhitelistPage;

public interface StudentWhitelistMapper {
	/**
	 * 查询学员是否在相应类型的白名单内
	 * @param stdId
	 * @param scholarship
	 * @return
	 */
	int countBy(@Param("stdId") String stdId, @Param("scholarship") String scholarship);
	/**
	 * 删除学员原有设置
	 * @param stdIds
	 */
	void deleteWhitelist(@Param("stdIds") String[] stdIds);
	/**
	 * 设置白名单
	 * @param wlList
	 */
	void setting(@Param("wlList") List<StudentWhitelist> wlList);
	/**
	 * 查询学员信息
	 * @param queryInfo
	 * @return
	 */
	List<StudentWhitelistPage> getStudents(StudentWhitelistQuery queryInfo);
	/**
	 * 获取用户白名单信息
	 * @param stdId
	 * @return
	 */
	List<String> getWhitelist(String stdId);

}
