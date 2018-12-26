package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsFullScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 14:31
 * @版本号: V1.0
 */
@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsFullScholarshipApiImpl implements BdsFullScholarshipApi {

    @Autowired
    BdsFullScholarshipService bdsFullScholarshipService;

    @Override
    public Object getEnrolmentCount(Header header, Body body) throws IRpcException {
        Map<String, Object> map = bdsFullScholarshipService.getEnrolmentCount(body.getString("scholarship"));
        return map;
    }

    @Override
    public Object getNewEnrolmentList(Header header, Body body) throws IRpcException {
        return bdsFullScholarshipService.getNewEnrolmentList(body.getString("scholarship"));
    }

    @Override
    public Object getSystemDateTime(Header header, Body body) throws IRpcException {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String systemDateTime = sdf.format(new Date());
        map.put("systemDateTime",systemDateTime);
        return map;
    }
}
