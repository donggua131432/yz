package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 12:22
 * @版本号: V1.0
 */
public interface BdsFullScholarshipApi {

    /**
     * 获取报名人数
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getEnrolmentCount",methodRemark="获取全额奖学金活动报名人数",needLogin=false)
    public Object getEnrolmentCount(Header header, Body body) throws IRpcException;


    /**
     * 获取最新报名学员信息
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getNewEnrolmentList",methodRemark="获取全额奖学金活动最新报名学员信息",needLogin=false)
    public Object getNewEnrolmentList(Header header, Body body) throws IRpcException;


    /**
     * 获取系统时间
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="getSystemDateTime",methodRemark="获取系统时间",needLogin=false)
    public Object getSystemDateTime(Header header, Body body) throws IRpcException;
}
