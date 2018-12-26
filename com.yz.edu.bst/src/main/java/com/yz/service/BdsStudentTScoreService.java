package com.yz.service;


import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.BdStudentTScoreMapper;
import com.yz.dao.BdsCourseMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.course.BdCourse;
import com.yz.model.educational.BdStudentTScore;
import com.yz.model.educational.BdStudentTScoreYZ;
import com.yz.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @描述: 阅卷系统相关接口
 * @作者: Julie
 * @创建时间: 2018/01/15 19:01
 * @版本号: V1.0
 */
@Service
public class BdsStudentTScoreService {
    @Autowired
    BdStudentTScoreMapper studentTScoreMapper;
    
    @Autowired
	private BdsCourseMapper courseMapper;
    

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo<BdStudentTScoreYZ> GetPreScoringResaulByPage(String WhereSQL, int PageSize,
			int PageIndex) {
    	if (PageSize > 1000) return null;
    	int start=0;
    	start=PageSize*PageIndex;
		PageHelper.offsetPage(start, PageSize);
		List<BdStudentTScoreYZ>  result = studentTScoreMapper.GetPreScoringResaulByPage(WhereSQL);
		return new IPageInfo((Page) result);
	}
    
    /// 根据报读ID和课程编码等条件更新学员的基本成绩
    /// </summary>
    /// <param name="AuthNo">授权号</param>
    /// <param name="UserName">授权登录名</param>
    /// <param name="Pwd">授权登录密码</param>
    /// <param name="SID">学员ID</param>
    /// <param name="StudyID">报读ID</param>
    /// <param name="StudyYear">学期</param>
    /// <param name="CCode">课程编码</param>
    /// <param name="score">成绩</param>
    /// <returns></returns>
    public Object UploadScoringResult( String SID, String StudyID, String StudyYear,
			String CCode, String score) {
		// TODO Auto-generated method stub
    	
    	//判断报读ID是否存在
    	Map<String, String> learnInfoMap=studentTScoreMapper.selectLearnInfoByLearnId(StudyID);
    	if(learnInfoMap==null||learnInfoMap.get("learn_id")==null||StringUtil.isEmpty(learnInfoMap.get("learn_id").toString())) {
    		throw new IllegalArgumentException("数据错误，报读ID不存在！");
    	}
    	if(score!=null&&Double.parseDouble(score)<60) {
			int random = (int)(Math.random()*15);  //产生随机数
			int tempscore=60+random;
			score=String.valueOf(tempscore);
		}
		
    	BdStudentTScore tscore=studentTScoreMapper.findStudentScoreByUnionKey(StudyID, CCode, StudyYear);
    	if(tscore!=null) {
    		
    		
    		if(score!=null&&Double.parseDouble(score)>60) {
    			tscore.setIsPass("1");
			}else {
				tscore.setIsPass("2");
			}
    		tscore.setScore(score);
    		tscore.setUpdateTime(new Date());
    		
    		studentTScoreMapper.updateByPrimaryKeySelective(tscore);
    	}else {
    		tscore=new BdStudentTScore();
    		BdCourse course=courseMapper.selectByPrimaryKey(CCode);
			if(course==null) {
				throw new IllegalArgumentException("数据错误，课程编号不存在！");
			}else {
				tscore.setCourseName(course.getCourseName());
				tscore.setGrade(course.getGrade());
			}
    		tscore.setCourseId(CCode);
    		tscore.setLearnId(StudyID);
    		tscore.setScore(score);
    		tscore.setStdId(SID);
    		tscore.setSemester(StudyYear); 	
    		tscore.setUpdateTime(new Date());
    		if(score!=null&&Double.parseDouble(score)>60) {
    			tscore.setIsPass("1");
			}else {
				tscore.setIsPass("2");
			}
    		studentTScoreMapper.insert(tscore);
    	}
		return true;
	}

}
