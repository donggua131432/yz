package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.UsTaskCardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @描述: ${DESCRIPTION}
 * @作者: DuKai
 * @创建时间: 2018/6/7 17:15
 * @版本号: V1.0
 */
@Service(version = "1.0", timeout = 30000, retries = 0)
public class UsTaskCardApiImpl implements UsTaskCardApi {

    @Autowired
    private UsTaskCardService usTaskCardService;

    @Override
    public Object getTaskCard(Header header, Body body) throws IRpcException {
        body.put("userId", header.getUserId());
        usTaskCardService.addUsTaskCard(body);
        return null;
    }

    @Override
    public Object taskCardList(Header header, Body body) throws IRpcException {
        String userId = header.getUserId();
        List<Map<String,Object>> releaseList =  usTaskCardService.getReleaseTaskCard(userId);
        return releaseList;
    }

    @Override
    public void addUsTaskCardDetail(String userId, String[] itemCodes, String learnId) {
        usTaskCardService.addUsTaskCardDetail(userId,itemCodes,learnId);
    }

    @Override
    public Object taskCardDetailList(Header header, Body body) throws IRpcException {
        body.put("userId",header.getUserId());
        return usTaskCardService.getUsTaskCardDetail(body);
    }
}
