package com.yz.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.yz.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.EducationConstants;
import com.yz.core.constants.StudentConstants;
import com.yz.dao.BdsCourseMapper;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.course.BdCourseResource;

import net.sf.json.JSONArray;

/**
 * 远智学堂-我的课程 Description:
 *
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年7月24日.
 */
@Service
@Transactional
public class BdsCourseService {

	private static final Logger log = LoggerFactory.getLogger(BdsCourseService.class);

	private static final String liveUrl = "http://api.polyv.net/live/watchtoken/gettoken";

	@Autowired
	private BdsCourseMapper resourceMapper;

	@Autowired
	private UsInfoService infoService;

    @Value("${polyv.baseUrl}")
    private String baseUrl;

    @Value("${polyv.appId}")
    private String appId;

    @Value("${polyv.appSecret}")
    private String appSecret ;

    @Value("${polyv.userId}")
    private String userId;

	/**
	 * 我的课程资源查询
	 *
	 * @param learnId
	 *            学员ID
	 * @return
	 */
	public Object selectCourseResource(String learnId) {
		String stdStage = resourceMapper.selectStdStage(learnId);
		List<BdCourseResource> list = new ArrayList<BdCourseResource>();
		if (StudentConstants.STD_STAGE_CONFIRM.equals(stdStage)
				|| StudentConstants.STD_STAGE_HELPING.equals(stdStage)) {
			list = resourceMapper.selectCourseResourceUnEnroll(learnId);
		} else if (StudentConstants.STD_STAGE_STUDYING.equals(stdStage)) {
			list = resourceMapper.selectCourseResourceReading(learnId);
		}

		return JSONArray.fromObject(list);
	}

	public String selectPresentTerm(String learnId) {
		// 学员年级
		String grade = resourceMapper.selectGradeByLearnId(learnId);

		String stdStage = resourceMapper.selectStdStage(learnId);

		if (StudentConstants.STD_STAGE_CONFIRM.equals(stdStage)
				|| StudentConstants.STD_STAGE_HELPING.equals(stdStage)) {
			return StudentConstants.SEMESTER_TUTOR;
		}

		if (StringUtil.hasValue(grade)) {
			// 截取前四位年级
			grade = grade.substring(0, 4);
		} else {
			return StudentConstants.SEMESTER_ONE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		// 拼接第一学期开始月份
		grade += "-02";

		Calendar year = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		try {
			year.setTime(sdf.parse(grade));
		} catch (ParseException e) {
			log.error(e.toString());
			return StudentConstants.SEMESTER_ONE;
		}

		// 月份相差
		int result = now.get(Calendar.MONTH) - year.get(Calendar.MONTH);
		// 年份相差月数
		int month = (now.get(Calendar.YEAR) - year.get(Calendar.YEAR)) * 12;
		if (month <= 0) {
			return StudentConstants.SEMESTER_ONE;
		}
		// 总共相差月数
		int interval = Math.abs(month + result);

		// 整除几次再+1就是当前学期
		int iterm = interval / 6 + 1;

		if (iterm > 6) {
			iterm = 6;
		}

		return String.valueOf(iterm);
	}

	public Map<String, Object> selectMyCourseByTerm(String learnId, String term) {

		Assert.hasText(learnId, "learnId不能为空");
		Assert.hasText(term, "term不能为空");

		String[] cpIds = null;

		if (StudentConstants.SEMESTER_TUTOR.equals(term)) {
			cpIds = resourceMapper.selectFDTermCpId(learnId);
		} else {
			cpIds = resourceMapper.selectTermCpId(learnId, term);
		}

		if (null == cpIds || cpIds.length <= 0) {
			return null;
		}
		Map<String, String> courseDate = resourceMapper.selectCourseDate(cpIds);

		Map<String, Object> courses = new HashMap<String, Object>();

		courses.put("startDate", courseDate.get("startDate"));
		courses.put("endDate", courseDate.get("endDate"));

		Map<String, String> pfsnInfo = resourceMapper.selectPfsnInfoByLearnId(learnId);

		// 学号

		courses.put("pfsnName", pfsnInfo.get("pfsnName"));
		courses.put("pfsnCode", pfsnInfo.get("pfsnCode"));
		courses.put("pfsnLevel", pfsnInfo.get("pfsnLevel"));
		courses.put("unvsName", pfsnInfo.get("unvsName"));
		courses.put("grade", pfsnInfo.get("grade"));
		Map<String, String> qingshuInfo = resourceMapper.selectQingshuInfiByLearnId(learnId);
		// String schoolroll = qingshuInfo.get("schoolRoll");
		// String unvsId= qingshuInfo.get("unvsId");
		// String qingshuId="";
		// String qingshuPwd="";
		// //仲恺农业工程学院
		// if(unvsId.equals("2")) {
		// qingshuId="zkny_"+schoolroll;
		// qingshuPwd=schoolroll;
		// }else if(unvsId.equals("35")) {
		// qingshuId="hnnd_"+schoolroll;
		// qingshuPwd=schoolroll;
		// }else {
		// qingshuId=schoolroll;
		// qingshuPwd=schoolroll;
		// }
		// courses.put("qingshuId", qingshuId);
		// courses.put("qingshuPwd", qingshuPwd);

		courses.put("qingshuId", qingshuInfo.get("qingshuId"));
		courses.put("qingshuPwd", qingshuInfo.get("qingshuPwd"));

		courses.put("courses", resourceMapper.selectCourseNames(cpIds));

		String[] courseDates = resourceMapper.selectCourseDates(cpIds);
		List<Map<String, Object>> cours = new ArrayList<Map<String, Object>>();
		for (String date : courseDates) {
			if (StringUtil.hasValue(date)) {

				List<Map<String, String>> details = resourceMapper.selectDetails(cpIds, date);

				Map<String, Object> cInfo = new HashMap<String, Object>();
				cInfo.put("date", date);
				cInfo.put("details", details);
				cours.add(cInfo);
			}
		}
		courses.put("courseInfos", cours);

		return courses;
	}

	/**
	 * 发送上午课程通知
	 */
	public void sendClassInform(String startTimePre, String endTimePre) {

		log.debug("-------------------------------- 进入发送上课推送，开始时间:" + startTimePre + "结束时间:" + endTimePre);

		// 查找学科课程
		List<Map<String, String>> courseInfoXK = resourceMapper.selectCourse(EducationConstants.COURSE_TYPE_XK,
				startTimePre, endTimePre);
		sendCourseInfoMsg(courseInfoXK, StudentConstants.STD_STAGE_STUDYING, EducationConstants.COURSE_TYPE_XK);
		// 辅导课程
		List<Map<String, String>> courseInfoFD = resourceMapper.selectCourse(EducationConstants.COURSE_TYPE_FD,
				startTimePre, endTimePre);
		sendCourseInfoMsg(courseInfoFD, StudentConstants.STD_STAGE_HELPING, EducationConstants.COURSE_TYPE_FD);
	}

	/**
	 * 发送上课通知
	 *
	 * @param courseInfo
	 */
	private void sendCourseInfoMsg(List<Map<String, String>> courseInfo, String stdStage, String courseType) {
		if (null != courseInfo && courseInfo.size() > 0) {

			log.debug("---------------------------- 课程信息：" + JsonUtil.object2String(courseInfo));

			for (Map<String, String> map : courseInfo) {
				String courseId = map.get("courseId");
				String courseName = map.get("courseName");
				StringBuffer classTime = new StringBuffer();

				String date = map.get("cpDate");
				String startTime = map.get("startTime");
				String endTime = map.get("endTime");

				classTime.append(date).append(" ").append(startTime).append(" 至 ").append(endTime);

				// String address = map.get("address");
				String address = "1、登录网络上课平台;2、请看课程表";

				String cpType = map.get("cpType");
				if ("6".equals(cpType)) {	// 直播类型
					address = "①手机端：点击页面下方：学员服务--远智学堂--我的课程表，进入课程直播。 ②网页版：远智教育官网https://xt.yzou.cn/-远智学堂-登录-我的课表，进入课程直播。（账号：身份证号码 密码：身份证后六位） 。";
				}

				/**
				 * 根据课程id与学员阶段查询userId
				 */
				List<String> userIds = new ArrayList<String>();

				if (EducationConstants.COURSE_TYPE_FD.equals(courseType)) {
					userIds = resourceMapper.selectUserIdByFDCourseId(courseId, stdStage);
				} else if(EducationConstants.COURSE_TYPE_XK.equals(courseType)) {

					userIds = resourceMapper.selectUserIdByCourseId(courseId, stdStage);
				}
				if (userIds.contains("1754661637538597253")) {

					userIds.clear();
					userIds.add("1754661637538597253");
					// 查询所有需发送的openId
//					List<String> openIds = usApi.getOpenIdsByUserIds(userIds);
					List<String> openIds = infoService.getOpenIdsByUserIds(userIds);
					log.debug("---------------------------- 发送openId集合：" + JsonUtil.object2String(openIds));
					log.debug("---------------------------- 总发送人数：：" + openIds.size());

					if (null != openIds && openIds.size() > 0) {
						// 微信发送推送 已迁移到 task项目中跑
						//wechatApi.sendClassMessage(openIds, courseName, classTime.toString(), address, null);
					}
				}

			}

		}
	}

	/**
	 * 发送上午课程通知
	 */
	public void sendClassInformMorning() {
		String startTimePre = "1970-01-01 08:00";
		String endTimePre = "1970-01-01 12:00";
		sendClassInform(startTimePre, endTimePre);
	}

	/**
	 * 发送下午课程通知
	 */
	public void sendClassInformAfternoon() {

		String startTimePre = "1970-01-01 12:00";
		String endTimePre = "1970-01-01 18:00";
		sendClassInform(startTimePre, endTimePre);
	}

	/**
	 * 发送晚上课程通知
	 */
	public void sendClassInformNight() {

		String startTimePre = "1970-01-01 18:00";
		String endTimePre = "1970-01-01 22:00";
		sendClassInform(startTimePre, endTimePre);
	}

	/**
	 * 获取课程直播
	 *
	 * @param learnId
	 * @param term
	 * @return
	 */
	public Map<String, Object> getCourseLive(String userId, String learnId, String term) {

		Assert.hasText(learnId, "learnId不能为空");
		Assert.hasText(term, "term不能为空");

		Map<String, Object> coursesLive = new HashMap<String, Object>();

		coursesLive.put("now", System.currentTimeMillis());
		coursesLive.put("userId", userId);
		coursesLive.put("token", getLiveToken());
		java.util.Date dNow = new java.util.Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		String date = ft.format(dNow);
		coursesLive.put("courseLive", resourceMapper.selectCourseLive(learnId, term, date));
		return coursesLive;
	}

	/**
	 * 学员是否能查看课程直播
	 * 
	 * @param learnId
	 * @param term
	 * @param courseId
	 * @return
	 */
	public boolean existsCourseLive(String learnId, String term, String courseId) {

		Assert.hasText(learnId, "learnId不能为空");
		Assert.hasText(term, "term不能为空");
		Assert.hasText(term, "courseId不能为空");

		Map<String, Object> coursesLive = new HashMap<String, Object>();

		List<Map<String, String>> list = resourceMapper.selectCourseLive(learnId, term, "");
		for (Map<String, String> map : list) {
			if (map.get("courseId").toString().equals(courseId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取直播token
	 *
	 * @return
	 */
	private String getLiveToken() {
		Map<String, String> map = new HashMap<String, String>();
		String ts = String.valueOf(System.currentTimeMillis() / 1000);
		map.put("ts", ts);
		String sign = CodeUtil.MD5.encrypt(ts + "polyvsign");
		map.put("sign", sign.toLowerCase());
		String result = HttpUtil.sendGet(liveUrl, map,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		return result;
	}
	
	public Object getRecentCourse(Header header, Body body){
		
		String learnId = body.getString("learnId");
		Assert.hasText(learnId, "learnId不能为空");

		String cpId = resourceMapper.getCpIdByLearnId(learnId);

		if (!StringUtil.hasValue(cpId)) {
			return null;
		}
		Map<String, String> recentCourse = resourceMapper.getRecentCourse(cpId);
		if(null !=recentCourse && recentCourse.size() >0){
			if(recentCourse.get("cpType").equals("4")){//青书学堂
				Map<String, String> qingshuInfo = resourceMapper.selectQingshuInfiByLearnId(learnId);
				recentCourse.put("qingshuId", qingshuInfo.get("qingshuId"));
				recentCourse.put("qingshuPwd", qingshuInfo.get("qingshuPwd"));
			}
		}
		return recentCourse;
	}
	
	public Object getChannelsInfo(Header header, Body body){
		//TODO 获取直播频道信息 暂时不做
		return null;
	}
	
	public Object getLiveStatusByStream(Header header, Body body){
		//TODO 获取频道直播状态
		return null;
	}
}
