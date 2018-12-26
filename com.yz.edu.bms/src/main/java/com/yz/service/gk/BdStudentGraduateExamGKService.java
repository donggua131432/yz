package com.yz.service.gk;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.gk.StudentCityAffirmGKMapper;
import com.yz.dao.gk.StudentGraduateExamGKMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentCityAffrimGkQuery;
import com.yz.model.gk.StudentGraduateExamGKInfo;
import com.yz.model.gk.StudentGraduateExamGkQuery;
import com.yz.model.stdService.StudentStudyingListInfo;
import com.yz.util.ExcelUtil;
import com.yz.util.ExcelUtil.IExcelConfig;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 国开本科统考跟进
 * @author zlp
 */
@Service
@Transactional
public class BdStudentGraduateExamGKService {
    private static Logger log = LoggerFactory.getLogger(BdStudentGraduateExamGKService.class);


    @Autowired
    private StudentGraduateExamGKMapper studentGraduateExamGKMapper;
    
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

    public Object findStudentGraduateExamGKList(StudentGraduateExamGkQuery query) {
    	BaseUser user=getUser();
    	PageHelper.offsetPage(query.getStart(), query.getLength()).setCountMapper("com.yz.dao.gk.StudentGraduateExamGKMapper.getStudentGraduateExamGkCount");
        List<StudentGraduateExamGKInfo> list = studentGraduateExamGKMapper.findStudentGraduateExamGKList(query, user);
        return new IPageInfo<StudentGraduateExamGKInfo>((Page<StudentGraduateExamGKInfo>) list);
    }
    
    public StudentGraduateExamGKInfo getGraduateExamGKInfoById(String followId) {
    	StudentGraduateExamGkQuery query = new StudentGraduateExamGkQuery();
        query.setFollowId(followId);
        List<StudentGraduateExamGKInfo> list = studentGraduateExamGKMapper.findStudentGraduateExamGKList(query, getUser());
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    
    public int updateRemark(String followId, String remark) {
        return studentGraduateExamGKMapper.updateRemark(followId, remark);
    }
     
    private BaseUser getUser(){
        BaseUser user = SessionUtil.getUser();
        //没有职称看所有数据
        if(user.getJtList()==null||user.getJtList().size()==0) {
        	user.setUserLevel("1");
        }
        // 部门主任，学籍组长
        if (user.getJtList().contains("BMZR")  || user.getJtList().contains("GKXJ") || user.getJtList().contains("XJZZ")||user.getJtList().contains("400")) {
            user.setUserLevel("1");
        }else if(user.getJtList().contains("CJZFDY") || user.getJtList().contains("GKZFDY")) {
        	user.setUserLevel("6");
        }
        return user;
    }
    
    public void synchronous(StudentGraduateExamGkQuery query) {
        //List<StudentGraduateExamGKInfo> list = studentGraduateExamGKMapper.findStudentGraduateExamGKList(query, getUser());
    }
}
