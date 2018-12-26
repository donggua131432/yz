package com.yz.service.statistics;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.statistics.StudentStatMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaCampusInfo;
import com.yz.model.statistics.StudentStatQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @描述: 学员统计
 * @作者: DuKai
 * @创建时间: 2017/10/10 12:29
 * @版本号: V1.0
 */
@Service
@Transactional
public class StudentStatService {
    @Autowired
    StudentStatMapper studentStatMapper;

    /**
     * 分页查询统计学员信息
     * @param start
     * @param length
     * @return
     */
    public IPageInfo queryStudentStatPage(int start, int length, StudentStatQuery studentStatQuery) {
    	BaseUser user = SessionUtil.getUser();
        PageHelper.offsetPage(start, length).setRmGroup(false);
        List<Map<String,Object>> campus = studentStatMapper.selectStudentStat(studentStatQuery,user);
        return new IPageInfo((Page) campus);
    }

    /**
     * 统计学员总数
     * @param studentStatQuery
     * @return
     */
    public Map<String,Object> queryStudentStatTotal(StudentStatQuery studentStatQuery) {
    	BaseUser user = SessionUtil.getUser();
        Map<String,Object> countTotalMap = studentStatMapper.selectStudentStatTotal(studentStatQuery,user);
        return countTotalMap;
    }

}
