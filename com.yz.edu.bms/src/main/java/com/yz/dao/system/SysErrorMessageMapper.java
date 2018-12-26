package com.yz.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.system.SysErrorMessage;


public interface SysErrorMessageMapper {
    int deleteByPrimaryKey(String errorCode);

    int insert(SysErrorMessage record);

    int insertSelective(SysErrorMessage record);

    SysErrorMessage selectByPrimaryKey(String errorCode);

    int updateByPrimaryKeySelective(SysErrorMessage record);

    int updateByPrimaryKey(SysErrorMessage record);

	List<SysErrorMessage> selectAll();

	void deleteAllSysErrorMessage(@Param("ids") String[] ids);

	List<SysErrorMessage> selectAll(SysErrorMessage sysErrorMessage);

	void deleteErrorMsg(String errorCode);
}