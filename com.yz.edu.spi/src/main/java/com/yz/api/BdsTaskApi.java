package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
/**
 * 任务接口
 * @author lx
 * @date 2018年6月9日 上午10:54:29
 */
public interface BdsTaskApi {
	/**
	 * 我的任务
	 * 
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="myTasks",methodRemark="我的任务",needLogin=true)
	public Object myTasks(Header header, Body body) throws IRpcException;

	/**
	 * 完成任务
	 * 
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="updateTaskStatus",methodRemark="学员点击完成任务",needLogin=true)
	public Object updateTaskStatus(Header header, Body body);

	/**
	 * 确认地址
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="affirmAddress",methodRemark="收货地址类型任务",needLogin=true)
	public Object affirmAddress(Header header, Body body);
	

	/**
	 * 确认考场
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="affirmExamInfo",methodRemark="考生确认考场信息",needLogin=true)
	public Object affirmExamInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取考生信息
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getStudentInfo",methodRemark="获取考生信息",needLogin=true)
	public Object getStudentInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取省市区
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getProvince",methodRemark="获取省市区",needLogin=true)
	public Object getProvince(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取考场信息
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getExamPlace",methodRemark="获取考场信息",needLogin=true)
	public Object getExamPlace(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取考场安排信息
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getPlaceYear",methodRemark="获取考场安排",needLogin=true)
	public Object getPlaceYear(Header header, Body body) throws IRpcException;
	
	/**
	 * 查看确认结果
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="bds",methodName="getExamAffirmResult",methodRemark="获取录取结果",needLogin=true)
	public Object getExamAffirmResult(Header header, Body body) throws IRpcException;
	
	/**
	 * 国开考场学员确认
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getStudentExamGk",methodRemark="获取国开考试考场确认学生信息",needLogin=true)
	public Object getStudentExamGk(Header header, Body body) throws IRpcException;
	
	/**
	 * 更改国开考场查看状态
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="updateStudentExamGkIsRead",methodRemark="修改任务查看状态",needLogin=true)
	public Object updateStudentExamGkIsRead(Header header, Body body) throws IRpcException;
	
	/**
	 * 根据学业获取毕业资料提交的模板
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getStuGraduateTemplate",methodRemark="毕业资料提交获取模板",needLogin=true)
	public Object getStudentGraduateTemplate(Header header, Body body) throws IRpcException;
	
	/**
	 * 申请毕业资料登记表
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="applyRegisterForm",methodRemark="毕业资料申请单",needLogin=true)
	public Object applyRegisterForm(Header header, Body body) throws IRpcException;
	
	/**
	 * 学员点击学信网查看信息 更新查看时间
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stuLookXueXinNet",methodRemark="查看学信网信息",needLogin=true)
	public Object stuLookXueXinNet(Header header, Body body) throws IRpcException;
	
	/**
	 * 学员提交信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stuSubmitXueXinInfo",methodRemark="学信网提交确认信息",needLogin=true)
	public Object stuSubmitXueXinInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 英语学位
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="stuDegreeEnglishInfo",methodRemark="英语学位报名",needLogin=true)
	public Object stuDegreeEnglishInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取毕业论文及模板
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getStuGraduatePaperTemplate",methodRemark="获取毕业证书及报告的模板",needLogin=true)
	public Object getStuGraduatePaperTemplate(Header header, Body body) throws IRpcException;
	
	/**
	 * 更改查看时间(开课通知)
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="updateNoticeTaskViewTime",methodRemark="开课通知更新查看时间",needLogin=true)
	public Object updateNoticeTaskViewTime(Header header, Body body) throws IRpcException;
	
	/**
	 * 开课通知 学员提交确认信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="submitAffirmInfo",methodRemark="开通通知提交确认结果",needLogin=true)
	public Object submitAffirmInfo(Header header, Body body) throws IRpcException;

	/**
	 * 青书学堂上课跟进 获取学员青书学堂信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getQingshuInfo",methodRemark="获取学员青书学堂信息",needLogin=true)
	public Object getQingshuInfo(Header header, Body body) throws IRpcException;

	/**
	 * 青书学堂上课跟进 提交确认状态
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="sumbitQingshuConfirmStatus",methodRemark="青书学堂提交确认状态",needLogin=true)
	public Object sumbitQingshuConfirmStatus(Header header, Body body) throws IRpcException;
	
	/**
	 * 国开考试城市信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getExamCityGk",methodRemark="国开考试城市信息",needLogin=true)
	public Object getExamCityGk(Header header, Body body) throws IRpcException;
	
	/**
	 * 国开考试确认城市信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="affirmExamCityGK",methodRemark="考生确认考场城市信息",needLogin=true)
	public Object affirmExamCityGK(Header header, Body body) throws IRpcException;
	
	/**
	 * 国考考试城市确认查看
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="lookGKCityAffirmResult",methodRemark="考生查看确认考试城市信息",needLogin=true)
	public Object lookGKCityAffirmResult(Header header, Body body) throws IRpcException;

	/**
	 * 获取毕业论文任务信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getStuGraduatePaperTaskInfo",methodRemark="获取毕业论文任务信息",needLogin=true)
	public Object getStuGraduatePaperTaskInfo(Header header, Body body) throws IRpcException;
	
	/**
	 * 获取国开统考设置信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="getGkUnifiedExamSet",methodRemark="获取国开统考设置信息",needLogin=true)
	public Object getGkUnifiedExamSet(Header header, Body body) throws IRpcException;
	
	/**
	 * 提交国开统考操作信息
	 * @param header
	 * @param body
	 * @return
	 * @throws IRpcException
	 */
	@YzService(sysBelong="bds",methodName="submitGkUnifiedExamInfo",methodRemark="提交国开统考操作信息",needLogin=true)
	public Object submitGkUnifiedExamInfo(Header header, Body body) throws IRpcException;
}
