package com.yz.service;

import com.yz.dao.BdsTestConfirmMapper;
import com.yz.exception.BusinessException;
import com.yz.model.TestConfirm;
import com.yz.model.TestProveInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @描述: 考前确认信息
 * @作者: DuKai
 * @创建时间: 2017/10/24 11:07
 * @版本号: V1.0
 */
@Service
public class BdsTestConfirmService {

    @Autowired
    BdsTestConfirmMapper bdsTestConfirmMapper;

    public TestProveInfo getTestProveInfo(Map map){
        TestProveInfo testProveInfo = bdsTestConfirmMapper.selectTestProveInfo(map);
        if(testProveInfo!=null){
            return testProveInfo;
        }else{
            throw new BusinessException("E60023");
        }
    }

    public int updateTestConfirmStat(TestConfirm testConfirm){
       int flag = bdsTestConfirmMapper.updateTestConfirmStat(testConfirm);
       if(flag<=0){
           throw new BusinessException("E60023");
       }
       return flag;
    }
    
    public void studentExamSignBySelf(String eyId,String[] idArrays){
    	/*判断当前年度,是否有考试信息*/
    	int ifExam = bdsTestConfirmMapper.ifExamInfo(eyId,idArrays,"0");
    	if(ifExam <=0){
    		throw new BusinessException("E60035");
    	}
        int ifAffirm =bdsTestConfirmMapper.ifExamInfo(eyId, idArrays, "1");
        if(ifAffirm <=0){
    		throw new BusinessException("E60036");
    	}		
    	bdsTestConfirmMapper.studentExamSignBySelf(eyId,idArrays);
    }
    
    public String getStudentLearnIdByIdCard(String idCard){
    	return bdsTestConfirmMapper.getStudentLearnIdByIdCard(idCard);
    }
    
    public Map<String, String> getSignStudentInfo(String[] ids){
    	return bdsTestConfirmMapper.getSignStudentInfo(ids);
    }
}
