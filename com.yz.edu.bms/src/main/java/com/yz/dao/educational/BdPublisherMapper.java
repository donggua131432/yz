package com.yz.dao.educational;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdPublisher;

public interface BdPublisherMapper {
    int deleteByPrimaryKey(String pressId);

    int insert(BdPublisher record);

    int insertSelective(BdPublisher record);

    BdPublisher selectByPrimaryKey(String pressId);

    int updateByPrimaryKeySelective(BdPublisher record);

    int updateByPrimaryKey(BdPublisher record);

	boolean isPublisher(String publisher);

	List<BdPublisher> findPublisherByName(@Param("sName")String sName);
}