package com.yz.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.core.util.score.ScoreHttpClientUtil;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsQueryScoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 获取考试分数信息
 * @作者: DuKai
 * @创建时间: 2017/11/23 19:04
 * @版本号: V1.0
 */
@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsQueryScoreApiImpl implements BdsQueryScoreApi {


    @Autowired
    BdsQueryScoreService bdsQueryScoreService;

    @Override
    public Object queryTestScore(Header header, Body body) throws IRpcException {
        //获取用户ID
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("stdId",header.getStdId());
        paramMap.put("idCard", body.get("idCard").toString());
        paramMap.put("learnId", body.get("learnId").toString());
        Map<String, Object> resultMap = bdsQueryScoreService.getQueryScoreInfo(paramMap);
        String mobile = resultMap.get("mobile").toString();
        String ksh = resultMap.get("exam_no").toString();
        String csrq = resultMap.get("id_card").toString().substring(8,12);

        String issueDate = body.get("issueDate").toString();
        String result = ScoreHttpClientUtil.getScoreInfo(ksh,csrq,issueDate);
        Map<String, Object> map = new HashMap<>();
        map.put("result",result);
        return map;
    }

    @Override
    public Object queryFinalScore(Header header, Body body) throws IRpcException {
        return bdsQueryScoreService.queryFinalScore(body.getString("learnId"), body.getString("semester"));
    }
}
