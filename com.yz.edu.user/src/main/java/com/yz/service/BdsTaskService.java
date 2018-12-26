package com.yz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.edu.paging.common.PageHelper;
import com.yz.edu.paging.bean.PageInfo;
import com.yz.constants.GlobalConstants;
import com.yz.dao.BdsTaskMapper;
import com.yz.dao.SysProvinceCityDistrictMapper;
import com.yz.exception.BusinessException;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.util.StringUtil;

/**
 * 元智学堂-我的任务
 *
 * @author lx
 * @date 2017年8月17日 上午11:14:54
 */
@Service
@Transactional
public class BdsTaskService {

    private static final Logger log = LoggerFactory.getLogger(BdsTaskService.class);

    @Autowired
    private BdsTaskMapper bdsTaskMapper;

    @Autowired
    private BdsStudentSendService bdStudentSendService;

    @Autowired
	private UsAddressService addressService;

    @Autowired
    private SysProvinceCityDistrictMapper cityMapper;

    public Object myTasks(Header header, Body body) {
        int page = body.getInt(GlobalConstants.PAGE_NUM, 0);
        int pageSize = body.getInt(GlobalConstants.PAGE_SIZE, 15);
        PageHelper.startPage(page, pageSize);
        String taskStatus = body.getString("taskStatus");
        String learnId = body.getString("learnId");

        List<Map<String, String>> list = bdsTaskMapper.getMyTaskInfo(learnId, taskStatus);
        if (null != list && list.size() > 0) {
            for (Map<String, String> map : list) {
                if (map.get("taskType").equals("4")) { //考场确认任务
                    map.put("curTime", System.currentTimeMillis() + "");
                    String erId = bdsTaskMapper.getStudentReasonById(map.get("taskId"), learnId);
                    if (StringUtil.hasValue(erId)) {
                        map.put("ifTeacherOper", "Y");
                    } else {
                        map.put("ifTeacherOper", "N");
                    }
                } else if (map.get("taskType").equals("6")) { //毕业资料提交
                    String userName = bdsTaskMapper.getStudentGraduateAddressById(map.get("taskId"), learnId);
                    if (StringUtil.hasValue(userName)) {
                        map.put("ifApply", "Y");
                    } else {
                        map.put("ifApply", "N");
                    }
                } else if (map.get("taskType").equals("8")) { //英语学位
                    //获取学位英语的信息
                    Map<String, String> engilshInfo = bdsTaskMapper.getStuDegreeEnglishInfo(map.get("taskId"), learnId);
                    if (null != engilshInfo) {
                        map.put("isEnroll", engilshInfo.get("isEnroll"));
                        map.put("enrollNo", engilshInfo.get("enrollNo"));
                    }
                }else if(map.get("taskType").equals("13")){ //国开考试城市确认
                	 map.put("curTime", System.currentTimeMillis() + "");
                     String reason = bdsTaskMapper.getUnconfirmedReason(learnId,map.get("taskId"));
                     if (StringUtil.hasValue(reason)) {
                         map.put("ifTeacherOper", "Y");
                     } else {
                         map.put("ifTeacherOper", "N");
                     }
                }
            }
        }
        PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    public Object updateTaskStatus(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        return null;
    }

    public Object affirmAddress(Header header, Body body) {

        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        String saId = body.getString("saId");
        Map<String, String> addressMap = addressService.getAddressDetailById(saId);
        if (null != addressMap && addressMap.size() > 0) {
            //TODO 此处后期可以优化,按照学期发学服任务,然后只修改当前学期的地址信息
            //收货地址信息
            log.debug("确认收货地址信息:" + learnId + "收件人:" + addressMap.get("saName") + "联系电话:" + addressMap.get("mobile")
                    + "地址:" + addressMap.get("address") + "省:" + addressMap.get("provinceCode") + "市:" + addressMap.get("cityCode") + "区:" + addressMap.get("districtCode"));
            bdStudentSendService.updateStdBookReceive(learnId, addressMap.get("saName"),
                    addressMap.get("mobile"), addressMap.get("address"),
                    addressMap.get("provinceCode"), addressMap.get("cityCode"),
                    addressMap.get("districtCode"));

        } else {
            throw new BusinessException("E200024");
        }

        return null;
    }

    public Object affirmExamInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String pyId = body.getString("pyId");
        String eyId = body.getString("eyId");
        String erId = bdsTaskMapper.getStudentReasonById(taskId, learnId);
        if (StringUtil.hasValue(erId)) {
            throw new BusinessException("E60032");
        }
        int ifPast = bdsTaskMapper.taskIfPast(taskId);
        if (ifPast < 1) {
            throw new BusinessException("E60033");
        }
        //获取当前年度的当前考场已确认的人数以及当前年度的当前考场总数
        int confirmedCount = bdsTaskMapper.getExamAffirmCount(eyId, pyId);
        int totalCount = bdsTaskMapper.getExamSeats(pyId);
        if (totalCount - confirmedCount > 0) {
            //如果还有座位,就进行确认的操作
            bdsTaskMapper.affirmExamInfo(taskId, eyId, learnId, pyId);
            bdsTaskMapper.updateTaskStatus(taskId, learnId);
        } else {
            throw new BusinessException("E60034");
        }
        return null;
    }

    public Object getStudentInfo(Header header, Body body) {
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");
        String semester = null;
        Map<String, String> studentInfo = bdsTaskMapper.getStudentInfo(learnId);
        if (null != studentInfo) {
            String grade = studentInfo.get("grade");
            List<Map<String, String>> yearSubject = bdsTaskMapper.getExamYearSubject(eyId);
            if (null != yearSubject && yearSubject.size() > 0) {
                for (Map<String, String> map : yearSubject) {
                    if (StringUtil.hasValue(grade) && grade.equals(map.get("grade"))) {
                        semester = map.get("semester");
                        break;
                    }
                }
            }
            String testSubject = bdsTaskMapper.getStudentTestSubject(studentInfo.get("pfsnId"), grade, semester);
            studentInfo.put("testSubject", testSubject);
        }
        return studentInfo;
    }

    public Object getProvince(Header header, Body body) {
        Map<String, Object> map = new HashMap<>();
        String eyId = body.getString("eyId");
        map.put("province", bdsTaskMapper.getProvince(eyId));
        map.put("city", bdsTaskMapper.getCity(eyId));
        map.put("district", bdsTaskMapper.getDistrict(eyId));

        return map;
    }

    public Object getExamPlace(Header header, Body body) {
        String provinceCode = body.getString("provinceCode");
        String cityCode = body.getString("cityCode");
        String districtCode = body.getString("districtCode");
        String eyId = body.getString("eyId");
        List<Map<String, String>> resultMap = bdsTaskMapper.getExamPlace(provinceCode, cityCode, districtCode, eyId);
        if (null != resultMap && resultMap.size() > 0) {
            for (Map<String, String> map : resultMap) {
                SysProvince province = cityMapper.selectProvinceByPrimaryKey(map.get("provinceCode"));
                SysCity city = cityMapper.selectCityByPrimaryKey(map.get("cityCode"));
                SysDistrict district = cityMapper.selectDistrictByPrimaryKey(map.get("districtCode"));
                StringBuilder sb = new StringBuilder();
                if (null != province) {
                    sb.append(province.getProvinceName());
                }
                if (null != city) {
                    sb.append(city.getCityName());
                }
                if (null != district) {
                    sb.append(district.getDistrictName());
                }
                map.put("detailAddress", sb.toString() + map.get("address"));
            }
        }
        return resultMap;
    }

    public Object getPlaceYear(Header header, Body body) {
        String placeId = body.getString("placeId");
        String eyId = body.getString("eyId");
        return bdsTaskMapper.getPlaceYear(placeId, eyId);
    }

    public Object getExamAffirmResult(Header header, Body body) {
        String taskId = body.getString("taskId");
        String eyId = body.getString("eyId");
        String learnId = body.getString("learnId");
        Map<String, String> resultMap = bdsTaskMapper.getExamAffirmResult(taskId, eyId, learnId);
        if (null != resultMap) {
            SysProvince province = cityMapper.selectProvinceByPrimaryKey(resultMap.get("provinceCode"));
            SysCity city = cityMapper.selectCityByPrimaryKey(resultMap.get("cityCode"));
            SysDistrict district = cityMapper.selectDistrictByPrimaryKey(resultMap.get("districtCode"));
            StringBuilder sb = new StringBuilder();
            if (null != province) {
                sb.append(province.getProvinceName());
            }
            if (null != city) {
                sb.append(city.getCityName());
            }
            if (null != district) {
                sb.append(district.getDistrictName());
            }
            resultMap.put("detailAddress", sb.toString() + resultMap.get("address"));
            String semester = null;
            String grade = resultMap.get("grade");
            List<Map<String, String>> yearSubject = bdsTaskMapper.getExamYearSubject(eyId);
            if (null != yearSubject && yearSubject.size() > 0) {
                for (Map<String, String> map : yearSubject) {
                    if (StringUtil.hasValue(grade) && grade.equals(map.get("grade"))) {
                        semester = map.get("semester");
                        break;
                    }
                }
            }
            String testSubject = bdsTaskMapper.getStudentTestSubject(resultMap.get("pfsnId"), grade, semester);
            resultMap.put("testSubject", testSubject);
        }
        return resultMap;
    }

    public Object getStudentExamGk(Header header, Body body) {
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");
        Map<String, Object> studentInfo = bdsTaskMapper.getStudentForGkExam(eyId, learnId);
        return studentInfo;
    }

    public Object updateStudentExamGkIsRead(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String eyId = body.getString("eyId");

        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        bdsTaskMapper.updateStudentExamGkIsRead(eyId, learnId);

        return null;
    }

    public Object getStudentGraduateTemplate(Header header, Body body) {
        String learnId = body.getString("learnId");
        String templateUrl = bdsTaskMapper.getStudentGraduateTemplate(learnId);
        return templateUrl;
    }

    public Object applyRegisterForm(Header header, Body body) {

        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");
        String provinceCode = body.getString("provinceCode");
        String cityCode = body.getString("cityCode");
        String districtCode = body.getString("districtCode");
        String userName = body.getString("userName");
        String mobile = body.getString("mobile");
        String address = body.getString("address");

        Map<String, String> map = new HashMap<>();
        map.put("learnId", learnId);
        map.put("taskId", taskId);
        map.put("provinceCode", provinceCode);
        map.put("cityCode", cityCode);
        map.put("districtCode", districtCode);
        map.put("userName", userName);
        map.put("mobile", mobile);
        map.put("address", address);

        bdsTaskMapper.updateStudentGraduateAddress(map);
        return null;
    }

    public Object stuLookXueXinNet(Header header, Body body) {
        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");

        bdsTaskMapper.stuLookXueXinNet(taskId, learnId);
        return null;
    }

    public Object stuSubmitXueXinInfo(Header header, Body body) {
        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");

        //判断是否查看学信息网信息
        int ifLook = bdsTaskMapper.stuIfLookXueXinInfo(taskId, learnId);
        if (ifLook < 1) {
            throw new BusinessException("E60038");
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        String isError = body.getString("isError");
        String feedback = body.getString("feedback");
        bdsTaskMapper.stuSubmitXueXinInfo(taskId, learnId, isError, feedback);
        return null;
    }

    public Object stuDegreeEnglishInfo(Header header, Body body) {

        String learnId = body.getString("learnId");
        String taskId = body.getString("taskId");
        String isEnroll = body.getString("isEnroll");
        if (StringUtil.hasValue(isEnroll)) {
            if (isEnroll.equals("2")) { //不报名
                bdsTaskMapper.stuDegreeEnglishEnroll(taskId, learnId, isEnroll, null);
            } else if (isEnroll.equals("1")) { //报名
                //验证学位考试分数是否合格
                String degreeScore = bdsTaskMapper.getDegreeScoreByLearnId(learnId);
                if (StringUtil.hasValue(degreeScore) && Integer.parseInt(degreeScore) >= 60) {
                    throw new BusinessException("E60040");
                }
                String enrollNo = body.getString("enrollNo"); //必填
                //报名
                bdsTaskMapper.stuDegreeEnglishEnroll(taskId, learnId, isEnroll, enrollNo);
            }
            //修改任务状态为已完成
            bdsTaskMapper.updateTaskStatus(taskId, learnId);
        }

        return null;
    }

    public Object getStuGraduatePaperTemplate(Header header, Body body) {
        String unvsId = body.getString("unvsId");
        String pfsnLevel = body.getString("pfsnLevel");

        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        String grade = body.getString("grade");

        List<Map<String, String>> templateUrl = bdsTaskMapper.getTemplateUrlByUnvsIdAndPfsnLevel(unvsId, pfsnLevel, grade);

        //更新论文状态为已查看
        bdsTaskMapper.updatePaperTaskViewTime(taskId,learnId);

        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        return templateUrl;
    }

    public Object updateNoticeTaskViewTime(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        bdsTaskMapper.updateNoticeTaskViewTime(taskId, learnId);

        return null;
    }

    public Object submitAffirmInfo(Header header, Body body) {
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        String isReceiveBook = body.getString("isReceiveBook");
        String isKnowTimetables = body.getString("isKnowTimetables");
        String isKnowCourseType = body.getString("isKnowCourseType");


        bdsTaskMapper.updateSubmitAffirmInfo(taskId, learnId, isReceiveBook, isKnowCourseType, isKnowTimetables);


        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);

        return null;

    }

    public Object getQingshuInfo(String learnId) {
        return bdsTaskMapper.getQingshuInfo(learnId);
    }

    public Object sumbitQingshuConfirmStatus(String learnId, String taskId, String confirmStatus) {
        bdsTaskMapper.updateQingshuConfirmStatus(learnId, taskId, confirmStatus);
        return null;
    }
    /**
     * 国开考场城市信息
     * @return
     */
    public Object getExamCityGk(){
    	return bdsTaskMapper.getExamCityGk();
    }
    /**
     * 国开城市确认信息
     * @param header
     * @param body
     * @return
     */
    public Object affirmExamCityGK(Header header, Body body){
    	String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String ecId = body.getString("ecId");
        String reason = bdsTaskMapper.getUnconfirmedReason(taskId, learnId);
        if (StringUtil.hasValue(reason)) {
            throw new BusinessException("E60032");
        }
        int ifPast = bdsTaskMapper.taskIfPast(taskId);
        if (ifPast < 1) {
            throw new BusinessException("E60033");
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        
        bdsTaskMapper.affirmExamCityGKInfo(taskId,learnId,ecId);
        
        return null;
    }
    /**
     * 查看国开考试城市选择确认结果
     * @param header
     * @param body
     * @return
     */
    public Object lookGKCityAffirmResult(Header header, Body body){
    	String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        
        String reason = bdsTaskMapper.getUnconfirmedReason(taskId, learnId);
        if (StringUtil.hasValue(reason)) {
           return reason;
        }
        Map<String, String> result = bdsTaskMapper.getAffirmExamCityGKInfo(taskId, learnId);
        if(result !=null && result.size() >0){
        	if(result.get("isAffirm").equals("1")){
        		return result.get("ecName");
        	}else{
        		 throw new BusinessException("E60042");
        	}
        }
        return null;
    }

    /**
     *获取毕业论文任务信息
     * @param header
     * @param body
     * @return
     */
    public Object getStuGraduatePaperTaskInfo(Header header, Body body){
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");

        Map<String,Object> paperTaskInfo = bdsTaskMapper.getStuGraduatePaperTaskInfo(taskId,learnId);
        return paperTaskInfo;
    }
    /**
     * 获取国开统考设置信息
     * @param header
     * @param body
     * @return
     */
    public Object getGkUnifiedExamSet(Header header, Body body){
    	String eyId = body.getString("eyId");
    	Map<String, String> result = bdsTaskMapper.getGkUnifiedExamSet(eyId);
    	return result;
    }
    /**
     * 提交国开统考操作信息
     * @param header
     * @param body
     * @return
     */
    public Object submitGkUnifiedExamInfo(Header header, Body body){
        String taskId = body.getString("taskId");
        String learnId = body.getString("learnId");
        String operType = body.getString("operType"); //操作类型 1：去缴费报名  2:我已知晓，下次报名
        if(StringUtil.isEmpty(operType)){
        	operType = "2";
        }
        //修改任务状态为已完成
        bdsTaskMapper.updateTaskStatus(taskId, learnId);
        bdsTaskMapper.updateGkUnifiedExamInfo(taskId, learnId,operType);
        return null;
    }
    
}