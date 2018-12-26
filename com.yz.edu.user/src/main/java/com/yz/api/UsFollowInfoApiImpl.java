package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsFollowInfoService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @描述: 跟进人相关接口
 * @作者: DuKai
 * @创建时间: 2018/3/5 17:46
 * @版本号: V1.0
 */
@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsFollowInfoApiImpl implements UsFollowInfoApi{

    @Autowired
    private UsFollowInfoService usFollowInfoService;

    @Override
    public Object getMbByNameOrMobile(Header header, Body body) throws IRpcException {
        String userId = header.getUserId();
        String empId = header.getEmpId();
        String nameOrMobile = body.getString("nameOrMobile");
        return usFollowInfoService.getUsFollowMbList(empId,nameOrMobile);
    }
}
