package com.yz.service.course;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.course.GsCourseClassMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.course.GsCourseClassInfo;

@Service
@Transactional
public class GsCourseClassService
{
	
	@Autowired
	private GsCourseClassMapper gsCourseClassMapper;
	
	public IPageInfo<GsCourseClassInfo> getGsCourseClassInfo(int page,int pageSize){
		PageHelper.offsetPage(page, pageSize);
		List<GsCourseClassInfo> classList = gsCourseClassMapper.getGsCourseClassInfo();
		
		return new IPageInfo<GsCourseClassInfo>((Page<GsCourseClassInfo>)classList);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo<List<Map<String, String>>> findAllKeyValue(int rows,int page,String sName){
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = gsCourseClassMapper.findAllKeyValue(sName);
		if (null == resultList) {
			return null;
		}
		return new IPageInfo((Page) resultList);
	}
	
	public GsCourseClassInfo getGsCourseClassDetailInfo(String classId){
		return gsCourseClassMapper.getGsCourseClassDetailInfo(classId);
	}
	
	public void updateClass(GsCourseClassInfo classInfo){
		gsCourseClassMapper.updateClass(classInfo);
	}
    public void insertClass(GsCourseClassInfo classInfo){
    	classInfo.setClassId(IDGenerator.generatorId());
    	gsCourseClassMapper.insertClass(classInfo);
	}
}
