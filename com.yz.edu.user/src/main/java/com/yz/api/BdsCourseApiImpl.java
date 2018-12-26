package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsCourseService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsCourseApiImpl implements BdsCourseApi {

	@Autowired
	private BdsCourseService courseService;

	@Override
	public Object myCourseResource(Header header, Body body) throws BusinessException {
		return courseService.selectCourseResource(body.getString("learnId"));
	}

	@Override
	public Object mySyllabus(Header header, Body body) throws IRpcException {
		return courseService.selectMyCourseByTerm(body.getString("learnId"), body.getString("term"));
	}

	@Override
	public Object presentTerm(Header header, Body body) throws IRpcException {
		return courseService.selectPresentTerm(body.getString("learnId"));
	}

	@Override
	public void sendClassInformMorning(Body body) throws IRpcException {
		courseService.sendClassInformMorning();
	}

	@Override
	public void sendClassInformAfternoon(Body body) throws IRpcException {
		courseService.sendClassInformAfternoon();

	}

	@Override
	public void sendClassInformNight(Body body) throws IRpcException {
		courseService.sendClassInformNight();

	}

	@Override
	public Object getCourseLive(Header header, Body body) throws IRpcException {
		String userId = header.getUserId();
		return courseService.getCourseLive(userId, body.getString("learnId"), body.getString("term"));
	}

	@Override
	public Object existsCourseLive(Header header, Body body) throws IRpcException {
		return courseService.existsCourseLive(body.getString("learnId"), body.getString("term"),body.getString("courseId"));
	}

	@Override
	public Object getRecentCourse(Header header, Body body) throws IRpcException {
		return courseService.getRecentCourse(header,body);
	}
}
