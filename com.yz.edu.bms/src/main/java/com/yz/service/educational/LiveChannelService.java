package com.yz.service.educational;

import com.yz.dao.educational.LiveChannelMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.educational.LiveChannel;
import com.yz.model.educational.LiveChannelExport;
import com.yz.model.educational.LiveChannelQuery;
import com.yz.model.educational.LivePolyvInfo;
import com.yz.model.gk.StudentCityAffirmGKInfo;
import com.yz.model.gk.StudentCityAffrimGkQuery;
import com.yz.service.pubquery.TutorshipBookSendService;
import com.yz.util.CodeUtil;
import com.yz.util.ExcelUtil; 
import com.yz.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class LiveChannelService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Value("${polyv.baseUrl}")
    private String baseUrl;

    @Value("${polyv.appId}")
    private String appId;

    @Value("${polyv.appSecret}")
    private String appSecret;

    @Value("${polyv.userId}")
    private String userId;

    @Autowired
    LiveChannelMapper liveChannelMapper;

    /**
     * 直播频道查询
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> findAllLiveChannel(LiveChannelQuery query) {
        List<Map<String, Object>> result = liveChannelMapper.findAllLiveChannel(query);
        return result;
    }

    /**
     * 根据年度查询课程
     *
     * @param sName
     * @param year
     * @return
     */
    public List<Map<String, Object>> findCourseByYear(String sName, String year) {
        List<Map<String, Object>> result = liveChannelMapper.findCourseByYear(sName, year);
        return result;
    }

    public LiveChannel getById(String id) {
        return liveChannelMapper.getById(id);
    }

    /**
     * 修改密码
     *
     * @param lcId
     * @param password
     */
    public void changePassword(String lcId, String password) {
        LiveChannel liveChannel = liveChannelMapper.getById(lcId);
        if (!password.equals(liveChannel.getChannelPassword())) {
            if (channelPasswordSetting(liveChannel.getChannelId(), password)) {
                liveChannel.setChannelPassword(password);
                liveChannelMapper.update(liveChannel);
            } else {
                throw new BusinessException("E40011");
            }
        }
    }

    /**
     * 新增直播频道
     *
     * @param liveChannel
     */
    public void insert(LiveChannel liveChannel) {
        if (liveChannelMapper.countCourseId(liveChannel.getCourseId()) > 0) {
            throw new BusinessException("E40009");
        }
        //生成随机6位密码
        LiveChannelQuery query = new LiveChannelQuery();
        String password = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        liveChannel.setChannelPassword(password);
        String channelId = createLiveChannel(liveChannel.getChannelName(), password);
        if (channelId != null) {
            liveChannel.setChannelId(channelId);
            liveChannel.setLcId(IDGenerator.generatorId());
            liveChannelMapper.insert(liveChannel);
        } else {
            throw new BusinessException("E40010");
        }
    }

    /**
     * 编辑直播频道
     *
     * @param liveChannel
     */
    public void edit(LiveChannel liveChannel) {
        liveChannelMapper.update(liveChannel);
    }

    /**
     * 删除直播频道
     *
     * @param lcIds
     * @return
     */
    public void deletes(String[] lcIds) {
        for (String lcId : lcIds) {
            LiveChannel liveChannel = liveChannelMapper.getById(lcId);
            if (StringUtil.hasValue(liveChannel.getChannelId())) {
                deleteLiveChannel(liveChannel.getChannelId());
            }
        }
        liveChannelMapper.deletes(lcIds);
    }

    /**
     * 生成频道
     *
     * @param year
     */
    public void gen(String year) {
        List<Map<String, Object>> result = liveChannelMapper.findCourseByYear("", year);
        for (Map<String, Object> map : result) {
            String courseId = String.valueOf(map.get("course_id"));
            String courseName = String.valueOf(map.get("course_name"));
            if (liveChannelMapper.countCourseId(courseId) > 0) {
                continue;
            }
            LiveChannel liveChannel = new LiveChannel();
            String password = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            liveChannel.setCourseId(courseId);
            liveChannel.setChannelName(courseName);
            liveChannel.setChannelPassword(password);
            String channelId = createLiveChannel(courseName, password);
            liveChannel.setChannelId(channelId);
            liveChannel.setLcId(IDGenerator.generatorId());
            liveChannelMapper.insert(liveChannel);
        }
    }

    /**
     * 创建直播频道
     */
    public String createLiveChannel(String name, String password) {
        String channelId;
        String url = baseUrl + "v2/channels/";
        Map<String, String> map = new HashMap<String, String>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        map.put("appId", appId);
        map.put("timestamp", timestamp);
        map.put("userId", userId);
        map.put("name", name);
        map.put("channelPasswd", password);
        map.put("sign", getSign(map));

        String result = HttpUtil.sendPost(url, map,HttpTraceInterceptor.TRACE_INTERCEPTOR);
        if (result.contains("success")) {
            channelId = RegexMatch("\"channelId\":(\\d+)", result);
            return channelId;
        }
        log.error("创建直播频道失败:" + result);
        return null;
    }

    /**
     * 删除直播频道
     *
     * @param channelId 频道ID
     * @return
     */
    public boolean deleteLiveChannel(String channelId) {
        String url = baseUrl + "v1/channels/" + channelId;
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);
        map.put("timestamp", timestamp);
        map.put("userId", userId);
        map.put("sign", getSign(map));
        String result = HttpUtil.sendDelete(url, map, HttpTraceInterceptor.TRACE_INTERCEPTOR);
        if (result.contains("success")) {
            return true;
        }
        log.error("删除直播频道失败:" + result);
        return false;
    }

    /**
     * 设置频道密码
     *
     * @param channelId
     * @param password
     * @return
     */
    public boolean channelPasswordSetting(String channelId, String password) {
        String url = baseUrl + "v2/channels/" + userId + "/passwdSetting";
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<String, String>();
        map.put("channelId", channelId);
        map.put("appId", appId);
        map.put("timestamp", timestamp);
        map.put("passwd", password);
        map.put("sign", getSign(map));

        String result = HttpUtil.sendPost(url, map,HttpTraceInterceptor.TRACE_INTERCEPTOR);
        if (result.contains("success")) {
            return true;
        }
        log.error("修改频道密码失败:" + result);
        return false;
    }

    /**
     * 获取签名
     *
     * @param map
     * @return
     */
    private String getSign(Map<String, String> map) {
        //根据Map的key升序排序
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuilder sb = new StringBuilder();
        sb.append(appSecret);
        for (Map.Entry<String, String> mapping : list) {
            sb.append(mapping.getKey()).append(mapping.getValue());
        }
        sb.append(appSecret);

        return CodeUtil.MD5.encrypt(sb.toString()).toUpperCase();
    }

    /**
     * 正则匹配数据
     *
     * @param pattern
     * @param input
     * @return
     */
    private String RegexMatch(String pattern, String input) {
        String result = "";
        try {
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(input);
            if (m.find()) {
                result = m.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<LivePolyvInfo> getUserViewInfo(String channelId, String date) {
        String url = baseUrl + "v1/statistics/" + channelId + "/viewlog";
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<String, String>();
        map.put("currentDay", date);
        map.put("userId", userId);
        map.put("appId", appId);
        map.put("timestamp", timestamp);
        map.put("sign", getSign(map));
        String result = HttpUtil.sendGet(url, map,HttpTraceInterceptor.TRACE_INTERCEPTOR);
        JSONObject jsonObj = JSONObject.fromObject(result);
        List<LivePolyvInfo> livePolyvInfos = new ArrayList<LivePolyvInfo>();
        if (jsonObj.get("status").equals("success")) {
            JSONArray array = JSONArray.fromObject(jsonObj.get("result"));
            for (int i = 0; i < array.size(); i++) {
                LivePolyvInfo livePolyvInfo = new LivePolyvInfo();
                JSONObject object = array.getJSONObject(i);
                livePolyvInfo.setUserId(object.getString("param1"));
                if (object.getString("param2").equals("高校")) {
                    continue;
                }
                livePolyvInfo.setPlayDuration(object.getString("playDuration"));
                livePolyvInfo.setIpAddress(object.getString("ipAddress"));
                livePolyvInfo.setBrowser(object.getString("browser"));
                livePolyvInfo.setIsMobile(object.getString("isMobile").equals("N") ? "否" : "是");
                livePolyvInfo.setProvince(object.getString("province"));
                livePolyvInfo.setCity(object.getString("city"));
                livePolyvInfos.add(livePolyvInfo);
            }
        }
        return livePolyvInfos;
    }

    public void recordExport(String channelId, String liveDate, HttpServletResponse response) {
        // 对导出工具进行字段填充
        ExcelUtil.IExcelConfig<LiveChannelExport> testExcelCofing = new ExcelUtil.IExcelConfig<LiveChannelExport>();
        testExcelCofing.setSheetName("index").setType(LiveChannelExport.class)
                .addTitle(new ExcelUtil.IExcelTitle("上课课程", "courseName"))
                .addTitle(new ExcelUtil.IExcelTitle("观看直播时长", "playDuration"))
                .addTitle(new ExcelUtil.IExcelTitle("上课学员姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("观众IP", "ipAddress"))
                .addTitle(new ExcelUtil.IExcelTitle("地区（省市）", "provinceCity"))
                .addTitle(new ExcelUtil.IExcelTitle("浏览器类型", "browser"))
                .addTitle(new ExcelUtil.IExcelTitle("是否移动端", "isMobile"));

        String courseName = "";
        String courseId = "";
        String channelName="";
        List<Map<String, Object>> mapList = liveChannelMapper.findChannel(channelId);
        if (mapList.size() > 0) {
            courseName = String.valueOf((mapList.get(0).get("course_name")));
            courseId = String.valueOf((mapList.get(0).get("course_id")));
            channelName= String.valueOf((mapList.get(0).get("channel_name")));
        }
        List<LivePolyvInfo> livePolyvInfos = getUserViewInfo(channelId, liveDate);
        List<LiveChannelExport> list = new ArrayList<LiveChannelExport>();
        if (livePolyvInfos.size() > 0) {
            list = liveChannelMapper.getLiveChannelViewLog(livePolyvInfos, courseId);
        }
        for (LiveChannelExport liveChannelExport : list) {
            if (liveChannelExport == null) {
                continue;
            }
            liveChannelExport.setProvinceCity(liveChannelExport.getProvince() + liveChannelExport.getCity());
            liveChannelExport.setCourseName(courseName);
            liveChannelExport.setPlayDuration(secToTime(Integer.parseInt(liveChannelExport.getPlayDuration())));
            //现场确认结果
            if (liveChannelExport.getPfsnLevel() != null && liveChannelExport.getPfsnLevel().equals("1")) {
                liveChannelExport.setPfsnLevel("专科升本科类 ");
            } else if (liveChannelExport.getPfsnLevel() != null && liveChannelExport.getPfsnLevel().equals("5")) {
                liveChannelExport.setPfsnLevel("高中起点高职高专");
            } else {
                liveChannelExport.setPfsnLevel("");
            }
        }

        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

        ServletOutputStream out = null;
        String filename=channelId+"-"+channelName+"-"+liveDate;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");  
            response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gb2312"), "iso8859-1")+ ".xls");
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public List<Map<String, Object>> findChannel(String sName) {
        List<Map<String, Object>> result = liveChannelMapper.findChannel(sName);
        return result;
    }

    public List<Map<String, Object>> findLiveDate(String channelId) {
        List<Map<String, Object>> result = liveChannelMapper.findLiveDate(channelId);
        return result;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
