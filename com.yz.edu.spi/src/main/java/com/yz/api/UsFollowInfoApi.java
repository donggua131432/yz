package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 跟进人相关接口
 * @作者: DuKai
 * @创建时间: 2018/3/5 17:42
 * @版本号: V1.0
 */
public interface UsFollowInfoApi {

    /**
     * 获取当前跟进人下的米瓣信息
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="us",methodName="getMbByNameOrMobile",methodRemark="获取跟进人的米瓣信息",needLogin=true)
    Object getMbByNameOrMobile(Header header, Body body) throws IRpcException;

}
