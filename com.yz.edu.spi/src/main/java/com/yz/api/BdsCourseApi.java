package com.yz.api;

import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface BdsCourseApi {
	/**
	 * 我的课程资源
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="myCourseResource",methodRemark="我的课程资源",needLogin=true)
	public Object myCourseResource(Header header, Body body) throws IRpcException;
	
	/**
	 * 我的课表
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="mySyllabus",methodRemark="我的课表",needLogin=true)
	public Object mySyllabus(Header header, Body body) throws IRpcException;
	
	/**
	 * 当前学期
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws BusinessException
	 */
	@YzService(sysBelong="bds",methodName="presentTerm",methodRemark="获取当前时间的学期",needLogin=true)
	public Object presentTerm(Header header, Body body) throws IRpcException;
	
	/**
	 * 上午上课通知定时
	 * @throws IRpcException
	 */
	public void sendClassInformMorning(Body body) throws IRpcException;
	
	/**
	 * 下午上课通知定时
	 * @throws IRpcException
	 */
	public void sendClassInformAfternoon(Body body) throws IRpcException;
	
	/**
	 * 晚上上课通知定时
	 * @throws IRpcException
	 */
	public void sendClassInformNight(Body body) throws IRpcException;

	/**
	 * 获取课程直播
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getCourseLive",methodRemark="获取课程直播",needLogin=true)
	public Object getCourseLive(Header header, Body body) throws IRpcException;

	/**
	 * 学员是否能查看课程直播
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="existsCourseLive",methodRemark="学员是否能查看课程直播",needLogin=true)
	public Object existsCourseLive(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取最近课程资源
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getRecentCourse",methodRemark="获取最近课程",needLogin=true)
	public Object getRecentCourse(Header header, Body body) throws IRpcException;
}
