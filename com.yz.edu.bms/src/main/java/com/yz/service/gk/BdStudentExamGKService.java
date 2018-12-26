package com.yz.service.gk;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.gk.BdStudentExamGKMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.gk.BdStudentExamGKExcel;
import com.yz.model.gk.BdStudentExamGKQuery;
import com.yz.service.pubquery.TutorshipBookSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class BdStudentExamGKService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    BdStudentExamGKMapper studentExamGKMapper;

    /**
     * 国开考场管理查询
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> findAllStudentExamGK(BdStudentExamGKQuery query) {
        BaseUser user = SessionUtil.getUser();
        if(user.getJtList().contains("FDY")){
            user.setUserLevel("6");
        }
        if(user.getJtList().contains("BMZR")){
            user.setUserLevel("1");
        }
        if(user.getJtList().contains("GKXJ")){
            user.setUserLevel("1");
        }
        List<Map<String, Object>> result = studentExamGKMapper.findAllStudentExamGK(query,user);
        return result;
    }

    /**
     * 更新状态
     *
     * @param eigIds
     * @param status
     */
    public void updateJoinStatus(String[] eigIds, String status) {
        studentExamGKMapper.updateJoinStatus(eigIds, status);
    }

    /**
     * 查找考试年度
     * @return
     */
    public List<Map<String, Object>> findExamYear(){
        return studentExamGKMapper.findExamYear();
    }

    /**
     * 查找考试科目
     * @return
     */
    public List<Map<String, Object>> findExamCourse(){
        return studentExamGKMapper.findExamCourse();
    }

    /**
     * 查找考试方式
     * @return
     */
    public List<Map<String, Object>> findExamType(){
        return studentExamGKMapper.findExamType();
    }

    /**
     * 获取导入Excel中系统不存在的学员信息
     * @param studentExamGKExcelList
     * @return
     */
    public List<Map<String, Object>> getNonExistsStudent(List<BdStudentExamGKExcel> studentExamGKExcelList){
        return studentExamGKMapper.getNonExistsStudent(studentExamGKExcelList);
    }

    /**
     * 插入数据
     * @param studentExamGKExcelList
     */
    public void insert(List<BdStudentExamGKExcel> studentExamGKExcelList){
        BaseUser user = SessionUtil.getUser();
        studentExamGKMapper.initTmpExamGk(studentExamGKExcelList);
        List<Map<String, String>> list = studentExamGKMapper.selectTmpEaxamGkInsert(user);
        for (Map<String, String> map : list) {
			map.put("eig_id", IDGenerator.generatorId());
		}
        studentExamGKMapper.insert(list);
    }

    /**
     * 删除国开考场
     *
     * @param eigIds
     */
    public void deleteStudentExamGK(String[] eigIds) {
        studentExamGKMapper.deleteStudentExamGK(eigIds);
    }

    /**
     * 查找考试时间
     * @return
     */
    public List<Map<String, Object>> findExamTime(String date){
        return studentExamGKMapper.findExamTime(date);
    }
}
