package com.yz.dao.enroll;

import java.util.List;

import com.yz.model.enroll.BdTestConfirm;
import com.yz.model.enroll.BdTestConfirmMap;


public interface BdTestConfirmMapper {
    int deleteByPrimaryKey(String tcId);

    int insert(BdTestConfirm record);

    int insertSelective(BdTestConfirm record);

    BdTestConfirm selectByPrimaryKey(String tcId);

    int updateByPrimaryKeySelective(BdTestConfirm record);

    int updateByPrimaryKey(BdTestConfirm record);

	List<BdTestConfirmMap> selectAll(BdTestConfirmMap testConfirmMap);

	void okConfirm(String learnId);

}