package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 考试成绩查询
 * @作者: DuKai
 * @创建时间: 2017/11/23 18:57
 * @版本号: V1.0
 */
public interface BdsQueryScoreApi {


    /**
     * 查询考试成绩
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="queryTestScore",methodRemark="查询成考成绩",needLogin=true)
    public Object queryTestScore(Header header, Body body) throws IRpcException;

    /**
     * 查询期末成绩
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="queryFinalScore",methodRemark="查询期末成绩",needLogin=true)
    public Object queryFinalScore(Header header, Body body) throws IRpcException;
}
