package com.yz.dao.educational;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.educational.BdTextBook;

public interface BdTextBookMapper {
    int deleteByPrimaryKey(String textbookId);

    int insert(BdTextBook record);

    int insertSelective(BdTextBook record);

    BdTextBook selectByPrimaryKey(String textbookId);

    int updateByPrimaryKeySelective(BdTextBook record);

    int updateByPrimaryKey(BdTextBook record);

    List<BdTextBook> findAllTextBook(BdTextBook textBook);

    List<Map<String,Object>> findProfessional(String textbookId);

	int publisherCount(String code);

	void deletePublisher(@Param("ids") String[] ids);
	
	void deleteCourseTextbook(@Param("ids") String[] ids);

	List<BdTextBook> findTextBookByName(String sName);

	List<BdTextBook> findTextBookByThpId(String thpId);

	List<BdTextBook> getTextBook(String courseId);
}