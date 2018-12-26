package com.yz.service;


import com.yz.dao.BdsLearnMapper;
import com.yz.dao.BdsQueryScoreMapper;
import com.yz.exception.BusinessException;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 查询考试成绩
 * @作者: DuKai
 * @创建时间: 2017/11/23 19:01
 * @版本号: V1.0
 */
@Service
public class BdsQueryScoreService {
    @Autowired
    BdsQueryScoreMapper bdsQueryScoreMapper;

    @Autowired
    BdsLearnMapper bdsLearnMapper;

    public Map<String, Object> getQueryScoreInfo(Map map) {
        Map<String, Object> scoreInfoMap = bdsQueryScoreMapper.selectScoreInfo(map);
        if (scoreInfoMap != null && scoreInfoMap.get("exam_no") != null) {
            return scoreInfoMap;
        } else {
            throw new BusinessException("E60028");
        }
    }

    public Map<String, Object> queryFinalScore(String learnId, String semester) {
        Assert.hasText(learnId, "learnId不能为空");
        Assert.hasText(semester, "semester不能为空");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String,String> learnMap = bdsLearnMapper.selectLearnInfoByLearnId(learnId);
        String pfsnName = null;
        if (!learnMap.isEmpty()) {
            pfsnName = String.valueOf(learnMap.get("pfsnName"));
        }
        map.put("pfsnName", pfsnName);
        map.put("scores",bdsQueryScoreMapper.selectFinalScoreInfo(learnId, semester));
        return map;
    }

}
