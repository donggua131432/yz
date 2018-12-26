package com.yz.dao;

import com.yz.model.TestConfirm;
import com.yz.model.TestProveInfo;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @描述: ${DESCRIPTION}
 * @作者: DuKai
 * @创建时间: 2017/10/24 11:09
 * @版本号: V1.0
 */
public interface BdsTestConfirmMapper {

    int updateTestConfirmStat(TestConfirm testConfirm);

    TestProveInfo selectTestProveInfo(Map map);
    
    
    public void studentExamSignBySelf(@Param("eyId") String eyId, @Param("ids") String[] ids);
    
    public String getStudentLearnIdByIdCard(@Param("idCard") String idCard);
    
    public Map<String, String> getSignStudentInfo(@Param("ids") String[] ids);
    
    public int ifExamInfo(@Param("eyId") String eyId, @Param("ids") String[] ids,@Param("pyId") String pyId);
}
