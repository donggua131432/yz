package com.yz.api;

import javax.swing.plaf.TextUI;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.constants.GlobalConstants;
import com.yz.exception.BusinessException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.AccountSerialService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class AtsAccountSerialApiImpl implements AtsAccountSerialApi {

	@Autowired
	private AccountSerialService serialService;

	@Override
	public Object getAccountSerials(Header header, Body body) throws BusinessException {
		Object object=null;
		int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
		int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 15);
		String action = body.getString("action");
		String accType=body.getString("accType");
		String userId = header.getUserId();
		if(TextUtils.isEmpty(accType))
			object=serialService.getAccountSerials(page, pageSize, userId, action);
		else
			object=serialService.getAccountSerials(page, pageSize, userId, action,accType);
		
		return object;
	}

}
