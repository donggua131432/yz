package com.yz.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.core.constants.AppConstants;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsGiveStatus;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsInfoService;
import com.yz.util.Assert;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsInfoApiImpl implements UsInfoApi {

	@Autowired
	private UsInfoService infoService;

	@Override
	public void completeInfo(Header header, Body body) throws IRuntimeException {

		String userId = header.getUserId();
		String fieldName = body.getString("fieldName");
		String fieldDesc = body.getString("fieldDesc");
		String fieldValue = body.getString("fieldValue");

		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(fieldName, "完善资料字段名称不能为空");
		Assert.hasText(fieldDesc, "完善资料字段描述不能为空");
		Assert.hasText(fieldValue, "完善资料字段的值不能为空");

		UsBaseInfo baseInfo = new UsBaseInfo();

		UsGiveStatus giveStatus = new UsGiveStatus();

		giveStatus.setFieldName(fieldName);
		giveStatus.setFieldDesc(fieldDesc);
		giveStatus.setUserId(userId);
		baseInfo.setUserId(userId);
		switch (fieldName) {
		case AppConstants.COMPLETE_FIELD_NAME:
			baseInfo.setRealName(fieldValue);
			break;
		case AppConstants.COMPLETE_FIELD_SEX:
			baseInfo.setSex(fieldValue);
			break;
		case AppConstants.COMPLETE_FIELD_BIRTHDAY:
			baseInfo.setBirthday(fieldValue);
			break;
		case AppConstants.COMPLETE_FIELD_EDUCATION:
			baseInfo.setEducation(fieldValue);
			break;
		case AppConstants.COMPLETE_FIELD_PROFESSION:
			baseInfo.setProfession(fieldValue);
			break;
		case AppConstants.COMPLETE_FIELD_GRADUATETIME:
			baseInfo.setGraduateTime(fieldValue);
			break;
		}

		infoService.complete(baseInfo, giveStatus);
	}

	@Override
	public Object getOtherInfo(Header header, Body body) throws IRuntimeException {
		String userId = header.getUserId();

		Assert.hasText(userId, "用户ID不能为空");

		return infoService.getOtherInfo(userId);
	}

	@Override
	public Object myFans(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		int pageNum = body.getInt("pageNum", 1);
		int pageSize = body.getInt("pageSize", 10);
		return infoService.getFans(userId, pageNum, pageSize);
	}

	@Override
	public Object getOpenIdByUserId(Body body) throws IRpcException
	{
		String userId = body.getString("userId");
		return infoService.getOpenIdByUserId(userId);
	}

	@Override
	public List<String> getOpenIdsByUserIds(List<String> userIds) throws IRpcException {
		return infoService.getOpenIdsByUserIds(userIds);
	}

	@Override
	public Object getNewRegList(Header header, Body body) throws IRpcException {
		return infoService.getNewRegList();
	}
}
