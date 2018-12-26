package com.yz.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.exception.IRpcException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.BdsTaskService;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class BdsTaskApiImpl implements BdsTaskApi {

	@Autowired
	private BdsTaskService bdsTaskService;
 

	@Override
	public Object myTasks(Header header, Body body) throws IRpcException {
		return bdsTaskService.myTasks(header, body);
	}

	@Override
	public Object updateTaskStatus(Header header, Body body) {
		return bdsTaskService.updateTaskStatus(header, body);
	}

	@Override
	public Object affirmAddress(Header header, Body body) {

		return bdsTaskService.affirmAddress(header, body);
	}

	@Override
	public Object affirmExamInfo(Header header, Body body)
	{
		return bdsTaskService.affirmExamInfo(header,body);
	}

	@Override
	public Object getStudentInfo(Header header, Body body)
	{
		return bdsTaskService.getStudentInfo(header,body);
	}

	@Override
	public Object getProvince(Header header, Body body)
	{
		return bdsTaskService.getProvince(header,body);
	}

	@Override
	public Object getExamPlace(Header header, Body body)
	{
		return bdsTaskService.getExamPlace(header,body);
	}

	@Override
	public Object getPlaceYear(Header header, Body body)
	{
		return bdsTaskService.getPlaceYear(header, body);
	}

	@Override
	public Object getExamAffirmResult(Header header, Body body)
	{
		return bdsTaskService.getExamAffirmResult(header,body);
	}

	@Override
	public Object getStudentExamGk(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.getStudentExamGk(header, body);
	}

	@Override
	public Object updateStudentExamGkIsRead(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.updateStudentExamGkIsRead(header, body);
	}

	@Override
	public Object getStudentGraduateTemplate(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.getStudentGraduateTemplate(header,body);
	}

	@Override
	public Object applyRegisterForm(Header header, Body body) throws IRpcException
	{
		
		return bdsTaskService.applyRegisterForm(header,body);
	}

	@Override
	public Object stuLookXueXinNet(Header header, Body body) throws IRpcException
	{
		
		return bdsTaskService.stuLookXueXinNet(header,body);
	}

	@Override
	public Object stuSubmitXueXinInfo(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.stuSubmitXueXinInfo(header,body);
	}

	@Override
	public Object stuDegreeEnglishInfo(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.stuDegreeEnglishInfo(header,body);
	}

	@Override
	public Object getStuGraduatePaperTemplate(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.getStuGraduatePaperTemplate(header,body);
	}

	@Override
	public Object updateNoticeTaskViewTime(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.updateNoticeTaskViewTime(header,body);
	}

	@Override
	public Object submitAffirmInfo(Header header, Body body) throws IRpcException
	{
		return bdsTaskService.submitAffirmInfo(header, body);
	}

	@Override
	public Object getQingshuInfo(Header header, Body body) throws IRpcException {
		return bdsTaskService.getQingshuInfo(body.getString("learnId"));
	}

	@Override
	public Object sumbitQingshuConfirmStatus(Header header, Body body) throws IRpcException {
		return bdsTaskService.sumbitQingshuConfirmStatus(body.getString("learnId"),body.getString("taskId"),body.getString("confirmStatus"));
	}

	@Override
	public Object getExamCityGk(Header header, Body body) throws IRpcException {
		return bdsTaskService.getExamCityGk();
	}

	@Override
	public Object affirmExamCityGK(Header header, Body body) throws IRpcException {
		return bdsTaskService.affirmExamCityGK(header,body);
	}

	@Override
	public Object lookGKCityAffirmResult(Header header, Body body) throws IRpcException {
		return bdsTaskService.lookGKCityAffirmResult(header,body);
	}

	@Override
	public Object getStuGraduatePaperTaskInfo(Header header, Body body) throws IRpcException {
		return bdsTaskService.getStuGraduatePaperTaskInfo(header,body);
	}

	@Override
	public Object getGkUnifiedExamSet(Header header, Body body) throws IRpcException {
		return bdsTaskService.getGkUnifiedExamSet(header,body);
	}

	@Override
	public Object submitGkUnifiedExamInfo(Header header, Body body) throws IRpcException {
		return bdsTaskService.submitGkUnifiedExamInfo(header,body);
	}

}
