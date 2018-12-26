package com.yz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.constants.AppConstants;
import com.yz.dao.UsAddressMapper;
import com.yz.exception.BusinessException;
import com.yz.model.UsAddress;

@Service
@Transactional
public class UsAddressService {
	
	@Autowired
	private UsAddressMapper addressMapper;
	
	@Autowired
	private YzSysConfig yzSysConfig;

	public void editAddress(String excType, UsAddress addressInfo) {
		
		switch(excType) {
		case AppConstants.EXC_TYPE_ADD :
			
			int count = addressMapper.countBy(addressInfo.getUserId());
			
			int max = Integer.valueOf(yzSysConfig.getAddressMaxSize());
			
			if(count >= max) {
				throw new BusinessException("E30001");//已达到添加地址的最大数
			}
			
			if(GlobalConstants.TRUE.equals(addressInfo.getIsDefault())) {
				addressMapper.clearDefault(addressInfo.getUserId(),addressInfo.getSaType());
			}
			
			addressMapper.insertAddress(addressInfo);
			break;
		case AppConstants.EXC_TYPE_UPDATE : 
			if(GlobalConstants.TRUE.equals(addressInfo.getIsDefault())) {
				addressMapper.clearDefault(addressInfo.getUserId(),addressInfo.getSaType());
			}
			addressMapper.updateAddress(addressInfo);
			break;
		case AppConstants.EXC_TYPE_DELETE : 
			addressMapper.deleteAddress(addressInfo.getSaId());
			break;
		}
	}

	public List<Map<String, String>> getAddressList(String userId,String saType) {
		List<Map<String, String>> list = addressMapper.getAddressList(userId,saType);
		return list;
	}
	
	public Map<String, String> getAddressDetailById(String saId){
		return addressMapper.getAddress(saId);
	}
	public Object getAddress(String saId) {
		Map<String, String> address = addressMapper.getAddress(saId);
		return address;
	}

}
