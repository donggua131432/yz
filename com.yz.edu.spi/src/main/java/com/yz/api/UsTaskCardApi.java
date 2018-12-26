package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 任务卡接口
 * @作者: DuKai
 * @创建时间: 2018/6/7 15:31
 * @版本号: V1.0
 */
public interface UsTaskCardApi {


    /**
     * 领取任务
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
    @YzService(sysBelong="us",methodName="getTaskCard",methodRemark="领取任务",needLogin=true)
    Object getTaskCard(Header header, Body body) throws IRpcException;


    /**
     * 任务卡列表
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
    @YzService(sysBelong="us",methodName="taskCardList",methodRemark="获取任务列表",needLogin=true)
    Object taskCardList(Header header, Body body) throws IRpcException;


    /**
     * 获取任务详情
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
    @YzService(sysBelong="us",methodName="taskCardDetailList",methodRemark="获取任务详情",needLogin=true)
    Object taskCardDetailList(Header header, Body body) throws IRpcException;

    /**
     * 记录任务完成进度
     * @param userId
     * @param itemCodes
     * @param learnId
     */
    void addUsTaskCardDetail(String userId,String[] itemCodes, String learnId);



}
