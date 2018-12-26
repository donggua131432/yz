package com.yz.dao.common;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.common.UnvsSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.finance.fee.ZTreeResponse;

public interface ZTreeMapper {

	List<ZTreeResponse> searchPfsn(SelectQueryInfo pfsn);

	UnvsSelectInfo searchUnvs(String unvsId);

	List<ZTreeResponse> searchTa(@Param("pfsnIds") String[] pfsnIds);

	List<ZTreeResponse> searchTaSingle(@Param("pfsnIds") String[] pfsnIds);

}