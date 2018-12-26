package com.yz.dao.operate;

import java.util.List;

import com.yz.model.operate.BdTaskCard;

public interface BdTaskCardMapper {
    int deleteByPrimaryKey(String id);

    int insert(BdTaskCard record);

    int insertSelective(BdTaskCard record);

    BdTaskCard selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BdTaskCard record);

    int updateByPrimaryKey(BdTaskCard record);

	List<BdTaskCard> selectTaskCardByPage(BdTaskCard taskCard);

	void publishTaskById(String taskId);
}