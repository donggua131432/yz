package com.yz.service.educational;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.EducationConstants;
import com.yz.constants.GlobalConstants;
import com.yz.controller.educational.BdCourseController;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.baseinfo.BdUniversityMapper;
import com.yz.dao.educational.BdCourseMapper;
import com.yz.dao.educational.BdCourseResourceMapper;
import com.yz.dao.educational.BdCourseTextbookMapper;
import com.yz.dao.educational.BdPlanCourseMapper;
import com.yz.dao.educational.BdTextBookMapper;
import com.yz.dao.system.SysParameterMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.baseinfo.BdCourse;
import com.yz.model.baseinfo.BdCourseEditInfo;
import com.yz.model.baseinfo.BdCourseMap;
import com.yz.model.baseinfo.CourseResourceEdit;
import com.yz.model.common.IPageInfo;
import com.yz.model.common.PfsnSelectInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.course.CourseExport;
import com.yz.model.educational.BdCourseResource;
import com.yz.model.educational.BdCourseTextbookKey;
import com.yz.model.educational.BdPlanCourseKey;
import com.yz.model.educational.BdTextBook;
import com.yz.model.educational.BdTimeTableQuery;
import com.yz.model.educational.CourseExcel;
import com.yz.model.educational.CourseExcel.InformationExcel;
import com.yz.model.gk.StudentGraduateExamGKInfo;
import com.yz.model.educational.TeachTaskBookQuery;
import com.yz.service.baseinfo.BdTeachPlanServiceImpl;
import com.yz.service.educational.doc.TeachTaskBookDoc;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdCourseService {

    private static final Logger log = LoggerFactory.getLogger(BdCourseController.class);

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private YzSysConfig yzSysConfig;

    @Autowired
    private BdCourseMapper courseMapper;
    @Autowired
    private BdCourseResourceMapper courseResourceMapper;
    @Autowired
    private BdPlanCourseMapper planCourseMapper;
    @Autowired
    private BdTeachPlanServiceImpl teachPlanService;
    @Autowired
    private BdCourseTextbookMapper courseTextbookMapper;
    @Autowired
    private BdTextBookMapper textBookMapper;
    @Autowired
    private BdTextBookService textBookService;
    @Autowired
	private BdUniversityMapper bdUniversityMapper;
    @Autowired
    private SysParameterMapper parameterMapper;
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public IPageInfo<PfsnSelectInfo> getPfsnSelectList(SelectQueryInfo sqInfo) {
        PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
        return new IPageInfo((Page) courseMapper.getPfsnSelectList(sqInfo));
    }

    public Map<String, Object> getTimetable(BdTimeTableQuery query) throws ParseException {

        Assert.hasText(query.getGrade(), "年级不能为空");
        Assert.hasText(query.getCourseType(), "学科类型不能为空");
        Assert.hasText(query.getSemester(), "学期不能为空");
        Assert.hasText(query.getUnvsId(), "院校不能为空");
        //Assert.hasText(query.getPfsnLevel(), "专业层次不能为空");
        
        Map<String, Object> courses = new HashMap<String, Object>();
        //如果选择了专业
        if(StringUtil.hasValue(query.getPfsnId())) {
	        //得到上课人数
	        int stdCount = courseMapper.selectStdCountByPfsnId(query.getPfsnId());
	        courses.put("stdCount", stdCount);
	        Map<String, String> pfsnInfo = courseMapper.selectPfsnInfo(query.getPfsnId());
	        courses.put("pfsnName", pfsnInfo.get("pfsnName"));
	        courses.put("pfsnCode", pfsnInfo.get("pfsnCode"));
	        courses.put("pfsnLevel", pfsnInfo.get("pfsnLevel"));
	        courses.put("unvsName", pfsnInfo.get("unvsName"));
	        courses.put("grade", pfsnInfo.get("grade"));
	        
        }else {
        	courses.put("stdCount", "");
        	courses.put("pfsnName","");
 	        courses.put("pfsnCode","");
 	        courses.put("pfsnLevel",query.getPfsnLevel());
        	courses.put("unvsName", bdUniversityMapper.selectByPrimaryKey(query.getUnvsId()).getUnvsName());
        	courses.put("grade",query.getGrade());
        }
        courses.put("year", DateUtil.getCurrentDate("yyyy"));

        
        String[] cpIds = null;
        if (query.getCourseType() != null && query.getCourseType().equals("FD")) {
            cpIds = courseMapper.selectFDTermCpId(query);
        } else {
            cpIds = courseMapper.selectCpIds(query);
        }

        if (null == cpIds || cpIds.length <= 0) {
            return null;
        }

        Map<String, String> courseDate = courseMapper.selectCourseDate(cpIds);
        courses.put("startDate", courseDate.get("startDate"));
        courses.put("endDate", courseDate.get("endDate"));
        courses.put("courses", courseMapper.selectCourseNames(cpIds));

        
        List<String> courseDates = courseMapper.selectCourseDates(cpIds);
        List<Map<String, Object>> cours = new ArrayList<Map<String, Object>>();
        for (String date : courseDates) {

            if (StringUtil.hasValue(date)) {

                Map<String, Object> cInfo = new HashMap<String, Object>();

                List<String> morning = courseMapper.selectAmCourse(date, cpIds, "08:00", "12:00");
                List<String> afternoon = courseMapper.selectAmCourse(date, cpIds, "12:00", "19:00");
                List<String> night = courseMapper.selectAmCourse(date, cpIds, "19:00", "23:59");
                cInfo.put("date", date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                cInfo.put("week", DateUtil.getDayCN(sdf.parse(date)));

                cInfo.put("morning", morning);
                cInfo.put("afternoon", afternoon);
                cInfo.put("night", night);
                cours.add(cInfo);
            }

        }

        courses.put("courseInfos", cours);
       
        return courses;
    }

    public List<BdCourse> selectAll(BdCourse course) {
        // TODO Auto-generated method stub
        return courseMapper.selectAll(course);
    }

    public List<BdCourseMap> selectAllToExport(BdCourse course) {
        // TODO Auto-generated method stub
        return courseMapper.selectAllToExport(course);
    }

    public int stdtCount(String[] thpIds) {
        // TODO Auto-generated method stub
        return courseMapper.stdtCount(thpIds);
    }
    public int stdFdCount(String courseId) {
        // TODO Auto-generated method stub
        return courseMapper.stdFdCount(courseId);
    }
    
    public int stdXkCountByThpId(String thpId) {
        // TODO Auto-generated method stub
        return courseMapper.stdXkCountByThpId(thpId);
    }
    public String[] getThpIds(String courseId, BdCourse course) {
        // TODO Auto-generated method stub
        return courseMapper.getThpIds(courseId, course);
    }

    public Object exchangePage(int start, int length, BdCourse course) {

        //PageHelper.offsetPage(start, length);
        //List<Map<String, Object>> courseList = courseMapper.selectAllCourse(course);
        PageHelper.offsetPage(start, length).setCountMapper("com.yz.dao.educational.BdCourseMapper.getSelectAllCourseCount");
        List<Map<String, Object>> courseList = courseMapper.selectAllCourse(course);
        
        if (null != courseList)
            for (Map<String, Object> bdCourse : courseList) {
                // 教学计划
                String[] thpIds = getThpIds((String) bdCourse.get("courseId"), course);
                List<Map<String, Object>> teachPlanl = new ArrayList<Map<String, Object>>();
                if (null != thpIds)
                    for (String thpId : thpIds) {
                        teachPlanl.add(teachPlanService.selectOne(thpId));
                    }
                bdCourse.put("teachPlan", teachPlanl);
                // 教材
                if ("FD".equals((String) bdCourse.get("courseType"))) {
                    bdCourse.put("textBook", textBookMapper.getTextBook((String) bdCourse.get("courseId")));
                } else {
                    List<BdTextBook> textBookList = new ArrayList<BdTextBook>();
                    for (String thpId : thpIds) {
                        List<BdTextBook> textBook = textBookService.findTextBookByThpId(thpId);
                        textBookList.addAll(textBook);
                    }
                    bdCourse.put("textBook", textBookList);
                }
                // 人数
                if (thpIds.length > 0)
                    bdCourse.put("stdtCount", stdtCount(thpIds));
                else
                    bdCourse.put("stdtCount", stdFdCount((String) bdCourse.get("courseId")));

                //课程资源
                List<BdCourseResource> courseResourceList = this.getCourseResource((String) bdCourse.get("courseId"));
                bdCourse.put("courseresource", courseResourceList);
            }
        return new IPageInfo<>((Page) courseList);
    }

    public List<Map<String, Object>> exchange(List<BdCourse> courseList, BdCourse course) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (null != courseList)
            for (BdCourse bdCourse : courseList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("course", bdCourse);
                // 教学计划
                String[] thpIds = getThpIds(bdCourse.getCourseId(), course);
                List<Map<String, Object>> teachPlanl = new ArrayList<Map<String, Object>>();
                if (null != thpIds)
                    for (String thpId : thpIds) {
                        teachPlanl.add(teachPlanService.selectOne(thpId));
                    }
                map.put("teachPlan", teachPlanl);
//				// 教材
//				if ("FD".equals(bdCourse.getCourseType())) {
//					map.put("textBook", textBookMapper.getTextBook(bdCourse.getCourseId()));
//				} else {
//					List<BdTextBook> textBookList = new ArrayList<BdTextBook>();
//					for (String thpId : thpIds) {
//						List<BdTextBook> textBook = textBookService.findTextBookByThpId(thpId);
//						textBookList.addAll(textBook);
//					}
//					map.put("textBook", textBookList);
//				}
                // 人数
                if (thpIds.length > 0)
                    map.put("stdtCount", stdtCount(thpIds));
                else
                    map.put("stdtCount", "0");
                resultList.add(map);
            }
        return resultList;
    }

    public void insertCourse(BdCourse course, BdCourseEditInfo editInfo) {
        String courseType = course.getCourseType();
        courseMapper.insertSelective(course);

        String[] textbookIds = editInfo.getTextbookIds();
        String[] thpIds = editInfo.getThpIds();
        List<CourseResourceEdit> courseResources = editInfo.getCourseResource();

        if (EducationConstants.TEXT_BOOK_TYPE_FD.equals(courseType)) {
            if (null != textbookIds && textbookIds.length > 0) {
                courseTextbookMapper.insertBatch(textbookIds, course.getCourseId());
            }
        } else if (EducationConstants.TEXT_BOOK_TYPE_XK.equals(courseType)) {
            if (null != thpIds && thpIds.length > 0) {
                //同一门课程不能绑定相同年级、院校、层次、专业的2条教学计划
                List<Map<String, Object>> ifRepeatTeachPlanList = courseMapper.findRepeatTeachPlan(course.getCourseId(), thpIds);
                if (ifRepeatTeachPlanList != null && ifRepeatTeachPlanList.size() > 0) {
                    String message = "同一门课程不能绑定相同年级、院校、层次、专业、学期的2条教学计划";
                    throw new IllegalArgumentException(message);
                }
                planCourseMapper.insertBatch(thpIds, course.getCourseId());
            }
        }

        if (null != courseResources) {
            for (CourseResourceEdit courseResource : courseResources) {
                if (courseResource == null)
                    continue;
                String isNew = courseResource.getIsNew();
                if (GlobalConstants.TRUE.equals(isNew)) {
                    String resourceName = courseResource.getName();
                    BdCourseResource courseR = new BdCourseResource();
                    courseR.setCourseId(course.getCourseId());
                    courseR.setResourceName(resourceName);
                    String url = FileSrcUtil.createFileSrc(Type.COURSE, course.getCourseId(), resourceName);
                    courseR.setResourceUrl(url);
                    courseR.setResourceId(IDGenerator.generatorId());
                    courseResourceMapper.insertSelective(courseR);
                    try {
                        fileUploadUtil.copyToDisplay(resourceName, url);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }

    }

    public void updateCourse(BdCourse course, BdCourseEditInfo editInfo) {
        courseMapper.updateByPrimaryKeySelective(course);

        String courseType = course.getCourseType();

        String[] textbookIds = editInfo.getTextbookIds();
        String[] thpIds = editInfo.getThpIds();
        List<CourseResourceEdit> courseResources = editInfo.getCourseResource();
        String[] delResourceIds = editInfo.getDelResourceIds();
        String[] delResourceUrls = editInfo.getDelResourceUrls();

        if (EducationConstants.TEXT_BOOK_TYPE_FD.equals(courseType)) {
            courseTextbookMapper.deleteByPrimaryKey(course.getCourseId());
            if (null != textbookIds && textbookIds.length > 0) {
                courseTextbookMapper.insertBatch(textbookIds, course.getCourseId());
            }
        } else if (EducationConstants.TEXT_BOOK_TYPE_XK.equals(courseType)) {
            planCourseMapper.deleteByCourseId(course.getCourseId());
            if (null != thpIds && thpIds.length > 0) {
                //同一门课程不能绑定相同年级、院校、层次、专业的2条教学计划
                List<Map<String, Object>> ifRepeatTeachPlanList = courseMapper.findRepeatTeachPlan(course.getCourseId(), thpIds);
                if (ifRepeatTeachPlanList != null && ifRepeatTeachPlanList.size() > 0) {
                    String message = "同一门课程不能绑定相同年级、院校、层次、专业、学期的2条教学计划";
                    throw new IllegalArgumentException(message);
                }
                planCourseMapper.insertBatch(thpIds, course.getCourseId());
            }
        }

        if (null != delResourceIds && delResourceIds.length > 0) {
            courseResourceMapper.deleteBatch(delResourceIds);
        }

        if (delResourceUrls != null && delResourceUrls.length > 0) {
            String bucket =yzSysConfig.getBucket();
            try {
                FileUploadUtil.deleteMore(bucket, Arrays.asList(delResourceUrls));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        if (null != courseResources) {
            for (CourseResourceEdit courseResource : courseResources) {
                if (courseResource == null)
                    continue;
                String isNew = courseResource.getIsNew();
                if (GlobalConstants.TRUE.equals(isNew)) {
                    String resourceName = courseResource.getName();
                    BdCourseResource courseR = new BdCourseResource();
                    courseR.setCourseId(course.getCourseId());
                    courseR.setResourceName(resourceName);
                    String url = FileSrcUtil.createFileSrc(Type.COURSE, course.getCourseId(), resourceName);
                    courseR.setResourceUrl(url);
                    courseR.setResourceId(IDGenerator.generatorId());
                    courseResourceMapper.insertSelective(courseR);
                    try {
                        fileUploadUtil.copyToDisplay(resourceName, url);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    public BdCourse findCourseById(String courseId) {
        // TODO Auto-generated method stub
        return courseMapper.selectByPrimaryKey(courseId);
    }

    public String tempUpload(MultipartFile myfile) {
        String bucket = yzSysConfig.getTempBucket();
        String fileName = myfile.getOriginalFilename();
        try {
            FileUploadUtil.upload(bucket, fileName, myfile.getBytes());
        } catch (Exception e) {
            log.error("--------------- 上传文件失败");
        }

        return fileName;
    }

    public List<BdCourseResource> getCourseResource(String courseId) {
        // TODO Auto-generated method stub
        return courseResourceMapper.getCourseResource(courseId);
    }

    public void down(String resourceId, HttpServletResponse res) {
        // TODO Auto-generated method stub
        BdCourseResource courseRes = courseResourceMapper.selectByPrimaryKey(resourceId);
        downloadFile(courseRes.getResourceName(), courseRes.getResourceUrl(), res);
    }

    public void downloadFile(String fileName, String realPath, HttpServletResponse response) {
    	String filePath = parameterMapper.selectByPrimaryKey(GlobalConstants.FILE_BROWSER_URL).getParamValue() + realPath;
    	//String filePath =yzSysConfig.getFileBrowserUrl()+ realPath;
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
        // File file = new File(realPath);
        // if (file.exists()) {
        // response.setContentType("application/force-download");// 设置强制下载不打开
        // response.addHeader("Content-Disposition", "attachment;fileName=" +
        // fileName);// 设置文件名
        // byte[] buffer = new byte[1024];
        // FileInputStream fis = null;
        // BufferedInputStream bis = null;
        // try {
        // fis = new FileInputStream(file);
        // bis = new BufferedInputStream(fis);
        // OutputStream os = response.getOutputStream();
        // int i = bis.read(buffer);
        // while (i != -1) {
        // os.write(buffer, 0, i);
        // i = bis.read(buffer);
        // }
        // } catch (Exception e) {
        // // TODO: handle exception
        // e.printStackTrace();
        // } finally {
        // if (bis != null) {
        // try {
        // bis.close();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // if (fis != null) {
        // try {
        // fis.close();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        // }
        // }

    }

    public List<BdCourse> findCourseName(String sName, String courseType) {
        return courseMapper.findCourseName(sName, courseType);
    }

    public List<BdTextBook> getTextBook(String courseId) {
        return textBookMapper.getTextBook(courseId);
    }

    /**
     * 导入课程
     *
     * @param exsitList
     */
    public void insertCourseExcel(List<CourseExcel> exsitList) {
        for (CourseExcel courseExcel : exsitList) {

            BdCourse course = courseMapper.selectBy(courseExcel);

            if (course == null) {
                course = new BdCourse();
                course.setCourseType(courseExcel.getCourseType());
                course.setCourseName(courseExcel.getCourseName());
                course.setYear(courseExcel.getYear());
                courseMapper.insertSelective(course);
            }

            List<InformationExcel> infoList = courseExcel.getInfoList();

            if (!infoList.isEmpty()) {
                for (InformationExcel info : infoList) {
                    if (EducationConstants.TEXT_BOOK_TYPE_XK.equals(course.getCourseType())) {
                        BdPlanCourseKey pc = new BdPlanCourseKey();
                        pc.setCourseId(course.getCourseId());
                        pc.setThpId(info.getMappingId());
                        try {
                            planCourseMapper.insert(pc);
                        } catch (Exception e) {
                            log.error("建立教学计划与课程关系时不影响批量导入", e);
                        }
                    } else if (EducationConstants.TEXT_BOOK_TYPE_FD.equals(course.getCourseType())) {
                        BdCourseTextbookKey ct = new BdCourseTextbookKey();
                        ct.setCourseId(course.getCourseId());
                        ct.setTextbookId(info.getMappingId());
                        try {
                            courseTextbookMapper.insertSelective(ct);
                        } catch (Exception e) {
                            log.error("建立辅导课程与教材关系时不影响批量导入", e);
                        }
                    }
                }
            }

        }
    }

    /**
     * 上课平台安排
     *
     * @param course
     * @return
     */
    public List<Map<String, Object>> findExportCourse(CourseExport course) {
        if (course.getCourseType().equals("XK")) {
            return courseMapper.findExportCourseXK(course);
        } else {
            return courseMapper.findExportCourseFD(course);
        }
    }


    public void deleteCourse(String[] idArray) {
        // TODO Auto-generated method stub
        courseMapper.deleteCourse(idArray);
    }

    public Object getCourseByPfsnId(int page, int rows, String pfsnId, String semester) {
        PageHelper.startPage(page, rows);
        return new IPageInfo((Page) courseResourceMapper.getCourseByPfsnId(pfsnId, semester));
    }

    public void teachTaskBookExport(TeachTaskBookQuery bookQuery, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-word");
            TeachTaskBookDoc bookDoc = new TeachTaskBookDoc();
            String fileName = bookQuery.getYear() + "_" + bookQuery.getGrade() + "_" + bookQuery.getUnvsName() + "_" + bookDoc.pfsnLevelConvertSimple(bookQuery.getPfsnLevel()) + "_" + bookDoc.pfsnNameConvert(bookQuery.getPfsnName()) + "_" + bookQuery.getCourseName() + ".doc";
            String name = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-disposition", "attachment;filename=\"" + name + "\"");
            File file = ResourceUtils.getFile("classpath:" + bookDoc.getDocPath());
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String text = new String(buffer, "UTF-8");
            Map<String, Object> courseScore = courseResourceMapper.getTeacherInfo(bookQuery.getCourseId());
            text = bookDoc.docRender(courseScore, bookQuery, text);
            out = response.getOutputStream();
            out.write(text.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void updeteRecourceStatus(String resourceId,String status) {
        // TODO Auto-generated method stub
        courseMapper.updeteRecourceStatus(resourceId,status);
    }


}
