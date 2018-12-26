package com.yz.service;

import com.yz.dao.BdsFullScholarshipMapper;
import com.yz.redis.RedisService;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 全额奖学金活动
 * @作者: DuKai
 * @创建时间: 2018/2/27 14:37
 * @版本号: V1.0
 */
@Service
public class BdsFullScholarshipService {

    @Autowired
    BdsFullScholarshipMapper bdsFullScholarshipMapper;


    public Map<String, Object> getEnrolmentCount(String scholarship){
        Map<String, Object> resultMap = new HashMap<>();
        //先从缓存中取
        String learnCount =RedisService.getRedisService().get("enrolmentCount");
        //如果缓存中没有就从数据库中取
        if(learnCount == null){
            resultMap = bdsFullScholarshipMapper.selectEnrolmentCount(scholarship);
            //设置缓存时长
            RedisService.getRedisService().setex("enrolmentCount", resultMap.get("learnCount").toString(), 600);
        }else{
            resultMap.put("learnCount",learnCount);
        }
        return resultMap;
    }

    public Object getNewEnrolmentList(String scholarship){
        Object obj = JSONArray.fromObject(bdsFullScholarshipMapper.selectNewEnrolmentList(scholarship));
        return obj;
    }


}
