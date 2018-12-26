package com.yz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.BdsCourseApi;
import com.yz.constants.StudentConstants;
import com.yz.core.util.SessionUtil;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.SysParameterMapper;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.util.DateUtil;
import com.yz.vo.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.GlobalConstants;
import com.yz.dao.BdsCourseMapper;
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

    @Autowired
    private BdsCourseMapper resourceMapper;

    @Autowired
    private BdsLearnMapper learnMapper;

    @Reference(version = "1.0")
    private BdsCourseApi courseApi;

    @Autowired
    SysParameterMapper parameterMapper;

    /**
     * 我的课程资源查询
     *
     * @param learnId 学员ID
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

    public void down(String resourceId, HttpServletResponse res) {
        // TODO Auto-generated method stub
        BdCourseResource courseRes = resourceMapper.selectResourceByPrimaryKey(resourceId);
        downloadFile(courseRes.getResourceName(), courseRes.getResourceUrl(), res);
    }


    public void downloadFile(String fileName, String realPath, HttpServletResponse response) {
        String filePath = parameterMapper.selectByPrimaryKey(GlobalConstants.FILE_BROWSER_URL).getParamValue() + realPath;
        String downFilePath = "d:\\course";
        File dirFile = new File(downFilePath);
        if (!dirFile.exists()) {// 文件路径不存在时，自动创建目录
            dirFile.mkdir();
        }
        // 从服务器上获取图片并保存
        URL url;
        try {
            url = new URL(filePath);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();

            FileOutputStream os = new FileOutputStream(downFilePath + "\\" + fileName);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            os.close();
            in.close();

            // 对本地的文件再进行处
            File file = new File(downFilePath + "\\" + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            byte[] b = new byte[1024];
            int len = 0;
            try {
                inputStream = new FileInputStream(file);
                outputStream = response.getOutputStream();

                response.setContentType("application/force-download");
                String filename = file.getName();
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
                response.setContentLength((int) file.length());

                while ((len = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        inputStream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                        outputStream = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            file.delete(); // 删除
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public Object presentTerm(String learnId) {
        Header header = new Header();
        Body body = new Body();
        body.put("learnId", learnId);
        Object term = courseApi.presentTerm(header, body);
        return term;
    }

    public Object mySyllabus(String learnId, String term) {
        Header header = new Header();
        Body body = new Body();
        body.put("learnId", learnId);
        body.put("term", term);
        Object mySyllabus = courseApi.mySyllabus(header, body);
        return mySyllabus;
    }

    public Object getCourseLive(String learnId, String term) {
        Header header = new Header();
        Body body = new Body();
        body.put("learnId", learnId);
        body.put("term", term);
        Object courseLive = courseApi.getCourseLive(header, body);
        return courseLive;
    }

    public List<Map<String, String>> getUnvsAllCourseLive(String stdId) {
        if(resourceMapper.selectStdAllLive(stdId)==0){
            return null;
        }
        String unvsId = "0";
        Map<String, Object> rt = learnMapper.selectStudentInfoByStdId(stdId);
        List<Map<String, String>> learnInfos = (List<Map<String, String>>) rt.get("learnInfos");
        if(learnInfos.size()>0){
            unvsId = learnInfos.get(0).get("unvsId");
        }
        return resourceMapper.getUnvsAllCourseLive(unvsId, DateUtil.getCurrentDate("yyyy-MM-dd"));
    }

    public boolean allowAllLive(){
        LoginUser user = SessionUtil.getUser();
        if(resourceMapper.selectStdAllLive(user.getStdId())==0){
            return false;
        }
        return true;
    }
}
