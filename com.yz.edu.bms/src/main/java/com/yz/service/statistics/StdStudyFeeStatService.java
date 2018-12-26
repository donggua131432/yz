package com.yz.service.statistics;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.statistics.StdStudyFeeStatMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.stdService.StudentStudyingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @描述: 在读学员应缴费用统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 12:29
 * @版本号: V1.0
 */
@Service
@Transactional
public class StdStudyFeeStatService {
    @Autowired
    StdStudyFeeStatMapper stdStudyFeeStatMapper;

    /**
     * 分页查询在读学员应缴费用统计
     * @param start
     * @param length
     * @return
     */
    public IPageInfo queryStdStudyFeeStatPage(int start, int length, StudentStudyingQuery studentStudyingQuery) {
        PageHelper.offsetPage(start, length);
        List<Map<String,Object>> campus = stdStudyFeeStatMapper.selectStdStudyFeeStat(studentStudyingQuery);
        return new IPageInfo((Page) campus);
    }


}
