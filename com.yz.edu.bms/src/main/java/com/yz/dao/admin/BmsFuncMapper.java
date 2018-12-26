package com.yz.dao.admin;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.BmsFunc;
import com.yz.model.admin.BmsFuncResponse;

public interface BmsFuncMapper {
	int deleteByPrimaryKey(String funcId);

	int insert(BmsFunc record);

	int insertSelective(BmsFunc record);

	BmsFunc selectByPrimaryKey(String funcId);

	int updateByPrimaryKeySelective(BmsFunc record);

	int updateByPrimaryKey(BmsFunc record);

	List<BmsFunc> selectAllFunc(BmsFunc func);

	List<BmsFuncResponse> selectMenuAndFuncs();

	int selectFuncNameIsExist(@Param("funcName") String funcName, @Param("oldFuncName") String oldFuncName);

	int selectFuncCodeIsExist(@Param("funcCode") String funcCode, @Param("oldFuncCode") String oldFuncCode);
}