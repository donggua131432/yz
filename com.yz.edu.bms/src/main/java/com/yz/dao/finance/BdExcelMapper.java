package com.yz.dao.finance;

import java.util.List;

import com.yz.model.finance.statistics.BdExcel;

public interface BdExcelMapper {
    int deleteByPrimaryKey(String excelId);

    int insert(BdExcel record);

    int insertSelective(BdExcel record);

    BdExcel selectByPrimaryKey(String excelId);

    int updateByPrimaryKeySelective(BdExcel record);

    int updateByPrimaryKey(BdExcel record);

	List<BdExcel> selectExcel();
}