package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.TestConfirm;
import com.yz.model.TestProveInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsTestConfirmService;
import com.yz.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 考前确认信息
 * @作者: DuKai
 * @创建时间: 2017/10/24 11:02
 * @版本号: V1.0
 */
@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsTestConfirmApiImpl implements BdsTestConfirmApi {

    @Autowired
    BdsTestConfirmService bdsTestConfirmService;

    @Override
    public Object updateTestConfirmStat(Header header, Body body){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("stdId",header.getStdId());
        paramMap.put("idCard",body.get("idCard").toString());
        paramMap.put("grade","2018");
        TestProveInfo testProveInfo = bdsTestConfirmService.getTestProveInfo(paramMap);
        TestConfirm testConfirm = new TestConfirm();
        testConfirm.setStdId(header.getStdId());
        testConfirm.setLearnId(header.getLearnId());
        if(body.get("isPrintNotice")!=null){
            testConfirm.setIsPrintNotice(body.get("isPrintNotice").toString());
        }
        if(body.get("isPrintProve")!=null){
            testConfirm.setIsPrintProve(body.get("isPrintProve").toString());
        }
        if(body.get("isExamNotice")!=null){
            testConfirm.setIsExamNotice(body.get("isExamNotice").toString());
        }
        if(body.get("isExamSign")!=null){
            testConfirm.setIsExamSign(body.get("isExamSign").toString());
        }
        bdsTestConfirmService.updateTestConfirmStat(testConfirm);

        Map<String, Object> map = new HashMap<>();
        map.put("examNo", testProveInfo.getExamNo());
        map.put("testRoomNo",testProveInfo.getTestRoomNo());
        map.put("seatNo",testProveInfo.getSeatNo());
        map.put("fingerprint",testProveInfo.getFingerprint());
        map.put("testName",testProveInfo.getTestName());
        map.put("testRemark",testProveInfo.getTestRemark());
        return map;
    }

	@Override
	public Object studentExamSignBySelf(Header header, Body body) throws IRpcException
	{
		//当前考试年度
		String eyId = body.getString("eyId");
		String learnIds = body.getString("learnIds");
		if(StringUtil.hasValue(learnIds)){
			String[] learnId = learnIds.split(",");
			//进行签到
			bdsTestConfirmService.studentExamSignBySelf(eyId, learnId);
			
			return bdsTestConfirmService.getSignStudentInfo(learnId);
		}
		//返回信息
		return null;
	}

	@Override
	public Object studentExamSignByTeacher(Header header, Body body) throws IRpcException
	{
		String idCard = body.getString("idCard");
		//当前考试年度
		String eyId = body.getString("eyId");
		String learnId = bdsTestConfirmService.getStudentLearnIdByIdCard(idCard);
		if(StringUtil.hasValue(learnId)){
			String[] learnIds = learnId.split(",");
			//进行签到
			bdsTestConfirmService.studentExamSignBySelf(eyId, learnIds);
			
			return bdsTestConfirmService.getSignStudentInfo(learnIds);
		}
		return null;
		
	}
	
}
