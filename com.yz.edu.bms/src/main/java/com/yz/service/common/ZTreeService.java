package com.yz.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.common.ZTreeMapper;
import com.yz.model.common.UnvsSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.finance.fee.ZTreeResponse;
import com.yz.util.StringUtil;

@Service
@Transactional
public class ZTreeService {

	@Autowired
	private ZTreeMapper treeMapper;

	public Object searchPfsn(SelectQueryInfo pfsn, String[] pfsnIds) {

		List<ZTreeResponse> pfsns = treeMapper.searchPfsn(pfsn);
		for (ZTreeResponse z : pfsns) {
			if (StringUtil.hasValue(pfsnIds, z.getId())) {
				z.setChecked(true);
			}
		}

		return pfsns;
	}

	public UnvsSelectInfo searchUnvs(String unvsId) {
		return treeMapper.searchUnvs(unvsId);
	}

	public Object searchTa(String[] pfsnIds, String[] taIds) {
		List<ZTreeResponse> tas = new ArrayList<ZTreeResponse>();
		if(pfsnIds.length == 1){
			tas = treeMapper.searchTaSingle(pfsnIds);
		}else if(pfsnIds.length > 1){
			tas = treeMapper.searchTa(pfsnIds);
		}else{
			return null;
		}
		for (ZTreeResponse z : tas) {
			if (StringUtil.hasValue(taIds, z.getId())) {
				z.setChecked(true);
			}
		}
		return tas;
	}

}
