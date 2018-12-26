package com.yz.controller.stdService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.FileUploadUtil;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.UploadException;
import com.yz.model.stdService.StudentAttachment;
import com.yz.model.stdService.StudentGraduatePaperInfo;
import com.yz.model.stdService.StudentGraduatePaperQuery;
import com.yz.service.stdService.StudentGraduatePaperService;
import com.yz.util.FileUtil;
import com.yz.util.StringUtil;

/**
 * 学服任务--毕业论文及报告提交
 *
 * @author jyt
 */
@Controller
@RequestMapping("/graduatePaper")
public class StudentGraduatePaperController {
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	
    @Autowired
    private StudentGraduatePaperService studentGraduatePaperService;

    @RequestMapping("/toList")
    @Rule("graduatePaper:query")
    public String toList(Model model, HttpServletRequest request) {
        return "/stdservice/graduatePaper/student_graduate_paper_list";
    }

    @RequestMapping("/paperDataStatus")
    @Rule("graduatePaper:updatePaperStatus")
    public String paperDataStatus(Model model, HttpServletRequest request) {
        return "/stdservice/graduatePaper/student_graduate_paper-status";
    }

    @RequestMapping("/paperAttachment")
    public String paperAttachment(@RequestParam(name = "attachmentId") String attachmentId, Model model) {
        StudentAttachment result = studentGraduatePaperService.getAttachmentById(attachmentId);
        model.addAttribute("attachments", result);
        return "/stdservice/graduatePaper/student_graduate_paper-check";
    }

    @RequestMapping("/findAllPaperList")
    @Rule("graduatePaper:query")
    @ResponseBody
    public Object findAllPaperList(@RequestParam(name = "start", defaultValue = "0") int start,
                                   @RequestParam(name = "length", defaultValue = "10") int length, StudentGraduatePaperQuery query) {
        PageHelper.offsetPage(start, length);
        return studentGraduatePaperService.findAllPaperList(query);
    }

    @RequestMapping("/getAttachments")
    @ResponseBody
    public Object getAttachments(@RequestParam(name = "start", defaultValue = "0") int start,
                                 @RequestParam(name = "length", defaultValue = "10") int length, String learnId) {
        return studentGraduatePaperService.selectStudentAttachment(learnId);
    }

    @RequestMapping("/getCheckAttachments")
    @ResponseBody
    public Object getCheckAttachments(@RequestParam(name = "start", defaultValue = "0") int start,
                                 @RequestParam(name = "length", defaultValue = "10") int length, String learnId) {
        return studentGraduatePaperService.selectCheckStudentAttachment(learnId);
    }

    @RequestMapping("/paperImport")
    @Rule("graduatePaper:import")
    public String paperImport(HttpServletRequest request) {
        return "stdservice/graduatePaper/student_graduate_paper-import";
    }

    @RequestMapping("/attachmentList")
    public String attachmentList(HttpServletRequest request) {
        return "stdservice/graduatePaper/student_graduate_paper_attachment";
    }

    @RequestMapping("/commentView")
    public String commentView(HttpServletRequest request) {
        return "stdservice/graduatePaper/student_graduate_paper_comment_view";
    }

    @RequestMapping("/uploadPaper")
    @Rule("graduatePaper:import")
    @ResponseBody
    public Object uploadPaper(
            @RequestParam(value = "paperImport", required = false) MultipartFile stuPaperImport) {
        studentGraduatePaperService.importStuGraduatePaperInfo(stuPaperImport);
        return "SUCCESS";
    }

    @RequestMapping("/updateAttachment")
    @Rule("graduatePaper:check")
    @ResponseBody
    public Object updateAttachment(StudentAttachment attachment) {
        studentGraduatePaperService.updateAttachment(attachment);
        return "SUCCESS";
    }

    @RequestMapping("/updatePaperStatus")
    @Rule("graduatePaper:updatePaperStatus")
    @ResponseBody
    public Object updatePaperStatus(StudentGraduatePaperInfo paperInfo) {
        studentGraduatePaperService.updatePaperStatus(paperInfo);
        return "SUCCESS";
    }

    @RequestMapping("/exportPaperInfo")
    @Rule("graduatePaper:export")
    public void exportPaperInfo(StudentGraduatePaperQuery query, HttpServletResponse response) {
        studentGraduatePaperService.exportPaperInfo(query, response);
    }

    @RequestMapping("/addRemark")
    @ResponseBody
    @Rule("graduatePaper:addRemark")
    public Object addRemark(@RequestParam(name = "addRemark", required = true) String addRemark,
                            @RequestParam(name = "gpId", required = true) String gpId) {
        studentGraduatePaperService.updateRemark(gpId, addRemark);
        return "success";
    }

    @RequestMapping("/webuploader")
    @Rule("graduatePaper:upload")
    @ResponseBody
    public void fileUploadForWebuploader(String learnId,MultipartHttpServletRequest mRequest, HttpServletResponse response) {
        String resourceType = mRequest.getParameter("resourceType");
        if (StringUtil.isEmpty(resourceType)) {
            resourceType = GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE;
        }

        String bucket = yzSysConfig.getBucket();
        try {
            if (GlobalConstants.UPLOAD_RESOURCE_TYPE_SINGLE.equals(resourceType)) {
                MultipartFile file = mRequest.getFile("file");
                String fileSrc = createTempUrl(file);
                FileUploadUtil.upload(bucket, fileSrc, file.getBytes());

                response.setContentType("text/html;charset=UTF-8");
                response.setHeader("Connection", "close");
                PrintWriter writer = response.getWriter();
                writer.write(fileSrc);
                writer.flush();
                writer.close();
                Iterator<String> itr =  mRequest.getFileNames();
                String fileName = "";
                while(itr.hasNext()){
                    String str = itr.next(); 
                    fileName = mRequest.getFile(str).getOriginalFilename();
                }
                StudentAttachment attachment = new StudentAttachment();
                attachment.setLearnId(learnId);
                attachment.setAttachmentName(fileName);
                attachment.setPaperUploadType("0");
                attachment.setAttachmentUrl(fileSrc);
                studentGraduatePaperService.insertAttachment(attachment);

            } else {
                throw new UploadException("文件上传失败");
            }

        } catch (Exception e) {
            throw new UploadException("文件上传失败");
        }
    }

    /**
     * 创建临时文件访问路径
     *
     * @param file
     * @return
     * @throws IOException
     */
    private String createTempUrl(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        String fileId = StringUtil.UUID();

        String suffix = FileUtil.getSuffix(fileName);

        if (StringUtil.hasValue(suffix)) {
            fileName = fileId + "." + FileUtil.getSuffix(fileName);
        } else {
            fileName = fileId;
        }

        return "graduate_paper/" + fileName;
    }
}
