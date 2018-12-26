package com.yz.service.educational;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.educational.BdStudentEScoreMapper;
import com.yz.dao.educational.BdStudentTScoreMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.system.SysParameterMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.*;
import com.yz.model.system.SysParameter;
import com.yz.service.educational.excel.BaseTemplateExcel;
import com.yz.service.educational.doc.TemplateDoc;
import com.yz.service.educational.excel.TemplateExcelFactory;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
public class BdStudentScoreService {

    private static final Logger log = LoggerFactory.getLogger(BdStudentScoreService.class);

    @Autowired
    private BdStudentEScoreMapper studentScoreMapper;

    @Autowired
    private BdStdPayFeeMapper payMapper;

    @Autowired
    private BdStudentTScoreMapper studentTScoreMapper;

    @Autowired
    private DictExchangeUtil dictExchangeUtil;

    @Autowired
    private SysParameterMapper sysParameterMapper;

    public List<BdStudentScoreMap> selectAll(BdStudentScoreMap studentScoreMap) {
        // TODO Auto-generated method stub
        return studentScoreMapper.selectAll(studentScoreMap);
    }

    public List<BdStudentEScore> findStudentScore(String learnId) {
        // TODO Auto-generated method stub
        return studentScoreMapper.findStudentScore(learnId);
    }

    public void updateStudentScore(BdStudentEScore bdStudentEScore) {
        // TODO Auto-generated method stub
        studentScoreMapper.updateByPrimaryKeySelective(bdStudentEScore);
    }

    public List<BdStudentTScore> findStudentTScoreBySemester(String learnId, String semester) {
        // TODO Auto-generated method stub
        List<BdStudentTScore> result = studentTScoreMapper.findStudentTScoreBySemester(learnId, semester);
        if (result != null && result.size() > 0) {
            for (BdStudentTScore score : result) {
                HashMap<String, String> teacherinfo = studentTScoreMapper.getTeacherByCourseId(score.getCourseId());
                if (teacherinfo != null && teacherinfo.size() > 0) {
                    score.setTeacher(score.getTeacher() == null ? teacherinfo.get("teacher") : score.getTeacher());
                    score.setTeacherId(score.getTeacherId() == null ? teacherinfo.get("teacher_id") : score.getTeacherId());
                }
            }
        }
        return result;
    }

    public Map<String, Object> getBdStudentTScoreLearnInfo(String learnId) {
        Map<String, Object> learnmap = studentTScoreMapper.getBdStudentTScoreLearnInfo(learnId);
        return learnmap;
    }

    /**
     * 获取对应专业入学考试科目
     *
     * @param learnId
     * @return
     */
    public Object findStudentExamCourse(String learnId) {
        List<Map<String, String>> courseName = studentScoreMapper.selectStudentExamCourse(learnId);
        return courseName;
    }

    public void insertStudentScore(BdStudentEScoreReceive scores) {
        BaseUser user = SessionUtil.getUser();
        scores.setUpdateUser(user.getRealName());
        scores.setUpdateUserId(user.getUserId());
        String stdId = studentScoreMapper.selectStdIdByLearnId(scores.getLearnId());
        scores.setStdId(stdId);
        for (BdStudentEScore s : scores.getScores()) {
			s.setEscoreId(IDGenerator.generatorId());
		}
        studentScoreMapper.insertStudentScore(scores);
        payMapper.updateStdStage(scores.getLearnId(), StudentConstants.STD_STAGE_UNENROLLED);
    }

    /**
     * 获取学员的优惠类型入围状态（全省奖学金-普：默认为奖学金未入围）
     *
     * @param learnId
     * @return
     */
    public String getScholarshipSelectedStatus(String learnId) {
        return studentScoreMapper.getScholarshipSelectedStatus(learnId);
    }


    public void insertStudentTScore(BdStudentTScoreReceive scores) {
        BaseUser user = SessionUtil.getUser();
        scores.setUpdateUser(user.getRealName());
        scores.setUpdateUserId(user.getUserId());
        if (scores.getScores() != null && scores.getScores().size() > 0) {
            ArrayList<BdStudentTScore> scoreslist = scores.getScores();
            ArrayList<BdStudentTScore> scoresListAdd = new ArrayList<BdStudentTScore>();
            for (BdStudentTScore score : scoreslist) {
                if (StringUtil.hasValue(score.getScore()) && Double.parseDouble(score.getScore()) >= 60) {
                    score.setIsPass("1");
                } else {
                    score.setIsPass("2");
                }
                BdStudentTScore existTScore = studentTScoreMapper.findStudentScoreByUnionKey(scores.getLearnId(), score.getCourseId(), scores.getSemester());
                if (existTScore != null) {
                    BdStudentTScore record = new BdStudentTScore();
                    record.setTscoreId(existTScore.getTscoreId());
                    record.setScore(score.getScore().trim());
                    record.setTotalmark(score.getTotalmark());
                    record.setIsPass(score.getIsPass());
                    record.setTeacher(score.getTeacher());
                    record.setTeacherId(score.getTeacherId());

                    studentTScoreMapper.updateByPrimaryKeySelective(record);
                } else {
                	score.setTscoreId(IDGenerator.generatorId());
                    scoresListAdd.add(score);
                }
            }
            if (scoresListAdd.size() > 0) {
                scores.setScores(scoresListAdd);
                studentTScoreMapper.insertStudentTScore(scores);
            }
        }

    }

    /**
     * 查询所有在读学员期未考试成绩
     *
     * @param start
     * @param length
     * @param studentScoreMap
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public IPageInfo findAllStudentTScorePage(int start, int length, BdStudentTScoreMap studentScoreMap) {
        BaseUser user = SessionUtil.getUser();
        PageHelper.offsetPage(start, length);
        List<Map<String, Object>> result = studentTScoreMapper.findAllStudentTScorePage(studentScoreMap, user);
        return new IPageInfo((Page) result);
    }

    /**
     * 期末成绩导出excel
     *
     * @param excelTScore
     * @param type
     */
    public void exportStudentTScore(BdStudentTScoreMap studentScoreMap, String type, HttpServletResponse response) {
        // 对导出工具进行字段填充
        ExcelUtil.IExcelConfig<BdStudentTScoreExcel> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentTScoreExcel>();
        testExcelCofing.setSheetName("index").setType(BdStudentTScoreExcel.class)
                .addTitle(new ExcelUtil.IExcelTitle("大学名称", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"))
                .addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
                .addTitle(new ExcelUtil.IExcelTitle("在读专业", "pfsnName"))
                .addTitle(new ExcelUtil.IExcelTitle("在读院校", "unvsName"))
                .addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
                .addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
                .addTitle(new ExcelUtil.IExcelTitle("身份证号码", "idCard"))
                .addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
                .addTitle(new ExcelUtil.IExcelTitle("课程名称", "courseName"))
                .addTitle(new ExcelUtil.IExcelTitle("期末分数", "score"))
                .addTitle(new ExcelUtil.IExcelTitle("上报分数", "totalmark"))
                .addTitle(new ExcelUtil.IExcelTitle("授课老师", "teacher"));
        List<BdStudentTScoreExcel> resultList = new ArrayList<BdStudentTScoreExcel>();

        if (type != null && type.equals("checkExport")) {
            String[] idArray = studentScoreMap.getLearnIdArray();
            resultList = studentTScoreMapper.findAllStudentTScoreListByLearnIds(idArray);
        } else {
            resultList = studentTScoreMapper.findAllStudentTScoreList(studentScoreMap);
        }

        for (BdStudentTScoreExcel tscoreExcl : resultList) {
            // 转换专业层次
            String pfsnLevel = dictExchangeUtil.getParamKey("pfsnLevel", tscoreExcl.getPfsnLevel());
            tscoreExcl.setPfsnLevel(pfsnLevel);
            // 转换年度
            String grade = dictExchangeUtil.getParamKey("grade", tscoreExcl.getGrade());
            tscoreExcl.setGrade(grade);
            // 学期转换
            String semester = dictExchangeUtil.getParamKey("semester", tscoreExcl.getSemester());
            tscoreExcl.setSemester(semester);
        }
        SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=StudentTScores.xls");
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void templateExcelExport(TemplateExcel excel, HttpServletResponse response) {
        try {
            String fileSrc = studentTScoreMapper.getTemplateExcelUrl(excel);
            PrintWriter writer = response.getWriter();
            if (!StringUtil.isEmpty(fileSrc)) {
                writer.write(URLEncoder.encode(fileSrc));
                writer.flush();
                writer.close();
                return;
            }
            BaseTemplateExcel templateExcel = TemplateExcelFactory.createTemplateExcel(excel.getUnvsId());
            if (templateExcel == null) {
                writer.write("none");
                writer.flush();
                writer.close();
                return;
            }
            String fileName = excel.getGrade() + "_" + excel.getUnvsName() + "_" + templateExcel.pfsnLevelConvert(excel.getPfsnLevel()) + "_" + templateExcel.pfsnNameConvert(excel.getPfsnName());
            String name = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            File file = ResourceUtils.getFile("classpath:" + templateExcel.getExcelPath());
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String text = new String(buffer, "UTF-8");
            List<Map<String, Object>> listStudent = studentTScoreMapper.getStudentInfo(excel.getGrade(), excel.getUnvsId(), excel.getPfsnId());
            List<Map<String, Object>> listCourse = studentTScoreMapper.getCourseName(excel.getGrade(), excel.getSemester(), excel.getPfsnId());
            text = templateExcel.excelRender(listStudent, listCourse, excel, text);
            String bucket = "";
            SysParameter parameter = sysParameterMapper.selectByPrimaryKey(GlobalConstants.FILE_BUCKET);
            if (parameter != null) bucket = parameter.getParamValue();
            fileSrc = "template_excel/" + StringUtil.UUID() + "/" + fileName + ".xls";
            if (FileUploadUtil.upload(bucket, fileSrc, text.getBytes("UTF-8"))) {
                BdStudentTemplateExcel studentTemplateExcel = new BdStudentTemplateExcel();
                studentTemplateExcel.setYear(excel.getYear());
                studentTemplateExcel.setSemester(excel.getSemester());
                studentTemplateExcel.setGrade(excel.getGrade());
                studentTemplateExcel.setUnvsId(excel.getUnvsId());
                studentTemplateExcel.setPfsnLevel(excel.getPfsnLevel());
                studentTemplateExcel.setPfsnId(excel.getPfsnId());
                studentTemplateExcel.setExcelUrl(fileSrc);
                if (listStudent.size() > 0) {
                	studentTemplateExcel.setTeId(IDGenerator.generatorId());
                    int result = studentTScoreMapper.insertTemplateExcel(studentTemplateExcel);
                    if (result > 0) {
                        for (BdStudentScoreExcel scoreExcel : templateExcel.getStudentScoreExcels()) {
                            scoreExcel.setTeId(studentTemplateExcel.getTeId());
                            scoreExcel.setSeId(IDGenerator.generatorId());
                        }
                        if (templateExcel.getStudentScoreExcels().size() > 0) {
                            studentTScoreMapper.insertScoreExcel(templateExcel.getStudentScoreExcels());
                        }
                    }
                }
            }
            writer.write(URLEncoder.encode(fileSrc));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getCourseName(int page, int rows, String grade, String semester, String pfsnId) {
        PageHelper.startPage(page, rows).setCountMapper("com.yz.dao.educational.BdStudentTScoreMapper.getCourseNameCount");
        return new IPageInfo((Page) studentTScoreMapper.getCourseName(grade, semester, pfsnId));
    }

    public void templateDocExport(TemplateDocQuery docQuery, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-word");
            TemplateDoc templateDoc = new TemplateDoc();
            String fileName = docQuery.getGrade() + "_" + docQuery.getUnvsName() + "_" + templateDoc.pfsnLevelConvertSimple(docQuery.getPfsnLevel()) + "_" + templateDoc.pfsnNameConvert(docQuery.getPfsnName()) + "_" + docQuery.getThpName() + ".doc";
            String name = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-disposition", "attachment;filename=\"" + name + "\"");
            File file = ResourceUtils.getFile("classpath:" + templateDoc.getDocPath());
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String text = new String(buffer, "UTF-8");
            Map<String, Object> courseScore  = studentTScoreMapper.getCourseScore(docQuery.getUnvsId(),docQuery.getThpId());
            text = templateDoc.docRender(courseScore, docQuery, text);
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

    public boolean existsScore(TemplateExcel excel) {
        String fileSrc = studentTScoreMapper.getTemplateExcelUrl(excel);
        if(StringUtil.hasValue(fileSrc)){
            return true;
        }
        return false;
    }
}
