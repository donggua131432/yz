package com.yz.dao.oa;

import java.util.List;

import com.yz.model.oa.OaCommission;
import com.yz.model.oa.OaCommissionQuery;

public interface OaCommissionMapper {

	List<OaCommission> selectComminssionByPage(OaCommissionQuery query);

	
}
