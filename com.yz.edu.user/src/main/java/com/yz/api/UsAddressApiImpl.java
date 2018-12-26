package com.yz.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.constants.GlobalConstants;
import com.yz.core.constants.AppConstants;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.UsAddress;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsAddressService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsAddressApiImpl implements UsAddressApi {
	
	@Autowired
	private UsAddressService addressService;

	@Override
	public void editAddress(Header header, Body body) throws IRuntimeException {
		
		String userId = header.getUserId();
		String excType = body.getString("excType");
		String provinceCode = body.getString("provinceCode");
		String cityCode = body.getString("cityCode");
		String districtCode = body.getString("districtCode");
		String address = body.getString("address");
		String saName = body.getString("saName");
		String mobile = body.getString("mobile");
		String isDefault = body.getString("isDefault");
		String saId = body.getString("saId");
		String saType = body.getString("saType");
		String streetCode = body.getString("streetCode");
		String email = body.getString("email");
		String provinceName = body.getString("provinceName");
		String cityName = body.getString("cityName");
		String districtName = body.getString("districtName");
		String streetName = body.getString("streetName");
		
		if(AppConstants.EXC_TYPE_UPDATE.equals(excType)) {
			Assert.hasText(saId, "地址ID不能为空");
		}
		
		if(StringUtil.isEmpty(isDefault)) {//是否默认地址 默认为否
			isDefault = GlobalConstants.FALSE;
		}
		
		UsAddress addressInfo = new UsAddress();
		addressInfo.setUserId(userId);
		addressInfo.setProvinceCode(provinceCode);
		addressInfo.setCityCode(cityCode);
		addressInfo.setDistrictCode(districtCode);
		addressInfo.setAddress(address);
		addressInfo.setSaName(saName);
		addressInfo.setMobile(mobile);
		addressInfo.setIsDefault(isDefault);
		addressInfo.setSaId(saId);
		addressInfo.setSaType(saType);
		addressInfo.setStreetCode(streetCode);
		addressInfo.setEmail(email);
		addressInfo.setProvinceName(provinceName);
		addressInfo.setCityName(cityName);
		addressInfo.setDistrictName(districtName);
		addressInfo.setStreetName(streetName);
		
		addressService.editAddress(excType, addressInfo);
	}

	@Override
	public Object getAddressList(Header header, Body body) throws IRuntimeException {
		String saType = body.getString("saType");
		return addressService.getAddressList(header.getUserId(),saType);
	}

	@Override
	public Object getAddress(Header header, Body body) throws IRuntimeException {
		String saId = body.getString("saId");
		return addressService.getAddress(saId);
	}

	@Override
	public Map<String, String> getAddressDetailById(String saId) throws IRpcException {
		return addressService.getAddressDetailById(saId);
	}

}
