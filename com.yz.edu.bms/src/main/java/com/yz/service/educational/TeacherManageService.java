package com.yz.service.educational;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.constants.StudentConstants;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.FileSrcUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.educational.TeacherManageMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.educational.TeacherInfoQuery;
import com.yz.model.educational.BdTeacherRecommendQuery;
import com.yz.model.educational.TeacherShowInfo;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeImportInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;
import com.yz.model.system.SysDict;
import com.yz.service.educational.excel.TeacherTemplateExcel;
import com.yz.service.system.SysDictService;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 教师管理
 * 
 * @author lx
 * @date 2017年7月10日 下午7:43:59
 */
@Service
@Transactional
public class TeacherManageService {
	private static final Logger log = LoggerFactory.getLogger(TeacherManageService.class);
	
	@Autowired
	private YzSysConfig yzSysConfig;
	
	@Autowired
	private SysDictService sysDictService;
	
	@Autowired
	private TeacherManageMapper teacherManageMapper;

	@Autowired
	private OaEmployeeMapper employeeMapper;
	

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryTeacherShowInfoByPage(int start, int length, TeacherInfoQuery queryInfo) {
		PageHelper.offsetPage(start, length);
		List<TeacherShowInfo> recruiterInfos = teacherManageMapper.selectAllTeacherInfo(queryInfo);
		return new IPageInfo((Page) recruiterInfos);
	}

	public Object insertTeacherBaseInfo(OaEmployeeBaseInfo baseInfo, OaEmployeeOtherInfo otherInfo) {
		baseInfo.setEmpId(IDGenerator.generatorId());
		employeeMapper.insertEmpBaseInfo(baseInfo); //
		initTeacherAnnex(baseInfo);

		otherInfo.setEmpId(baseInfo.getEmpId());
		initTeacherOther(otherInfo);
		OaEmployeeJobInfo jobInfo = new OaEmployeeJobInfo();
		jobInfo.setEmpId(baseInfo.getEmpId());
		jobInfo.setCampusId(otherInfo.getCampusId());
		initEmpJob(jobInfo);

		return baseInfo.getEmpId();
	}

	public void updateTeacherBaseInfo(OaEmployeeBaseInfo baseInfo, OaEmployeeOtherInfo otherInfo) {
		employeeMapper.updateEmpBaseInfo(baseInfo);
		updateTeacherOther(otherInfo);
		if (StringUtil.hasValue(otherInfo.getCampusId())) {
			employeeMapper.updateTeacherJob(otherInfo);
		}
	}

	public void initTeacherAnnex(OaEmployeeBaseInfo baseInfo) {
		// 初始化附件信息
		List<SysDict> annexTypes = sysDictService.getDicts("teacherAnnexType");
		if (annexTypes != null && !annexTypes.isEmpty()) {
			List<OaEmployeeAnnex> annexInfos = new ArrayList<OaEmployeeAnnex>();
			for (SysDict dict : annexTypes) {
				OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
				annexInfo.setEmpId(baseInfo.getEmpId());
				annexInfo.setIsRequire(dict.getExt1());
				annexInfo.setEmpAnnexType(dict.getDictValue());
				annexInfo.setAnnexName(dict.getDictName());
				annexInfo.setUpdateUser(baseInfo.getCreateUser());
				annexInfo.setUpdateUserId(baseInfo.getCreateUserId());
				annexInfo.setAnnexId(IDGenerator.generatorId());
				annexInfos.add(annexInfo);
			}
			employeeMapper.insertEmpAnnexInfos(annexInfos);
			log.debug("---------------------------- 教师[" + baseInfo.getEmpId() + "]附件信息初始化成功");
		}
	}

	public void initTeacherOther(OaEmployeeOtherInfo other) {
		boolean isDeleteFile = false;
		boolean isUpdate = false;

		String realFilePath = null;
		// 处理头像
		Object headPic = other.getHeadPic();
		String headPortrait = other.getHeadPortrait();

		byte[] fileByteArray = null;

		if (headPic != null && headPic instanceof MultipartFile) {
			MultipartFile file = (MultipartFile) other.getHeadPic();

			if (StringUtil.isEmpty(headPortrait)) {
				realFilePath = FileSrcUtil.createFileSrc(Type.EMPLOYEE, other.getEmpId(), file.getOriginalFilename());
			} else {
				realFilePath = headPortrait;
			}
			try {
				fileByteArray = file.getBytes();
				isUpdate = true;
			} catch (IOException e) {
				log.error("文件上传失败", e);
			}

			other.setHeadPortrait(realFilePath);
		} else {
			realFilePath = headPortrait;
			other.setHeadPortrait("");
			isDeleteFile = true;
		}

		OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
		annexInfo.setEmpAnnexType(StudentConstants.TEACHER_ANNEX_TYPE_PHOTO);
		annexInfo.setEmpId(other.getEmpId());
		annexInfo.setAnnexUrl(other.getHeadPortrait());

		employeeMapper.updateAnnexInfo(annexInfo);
		// 新增附属信息
		if (StringUtil.isEmpty(other.getFinishTime())) {
			other.setFinishTime(null);
		}
		employeeMapper.insertEmpOtherInfo(other);

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

		log.debug("---------------------------- 教师[" + other.getEmpId() + "]附属信息初始化成功");
	}

	public void initEmpJob(OaEmployeeJobInfo jobInfo) {

		// 初始化部门信息
		if (StringUtil.isEmpty(jobInfo.getEntryDate())) {
			jobInfo.setEntryDate(null);
		}
		if (StringUtil.isEmpty(jobInfo.getLeaveDate())) {
			jobInfo.setLeaveDate(null);
		}
		if (StringUtil.isEmpty(jobInfo.getPactStartTime())) {
			jobInfo.setPactStartTime(null);
		}
		if (StringUtil.isEmpty(jobInfo.getPactEndTime())) {
			jobInfo.setPactEndTime(null);
		}
		employeeMapper.initEmpJob(jobInfo);

		log.debug("---------------------------- 教师[" + jobInfo.getEmpId() + "]部门信息初始化成功");
	}

	public void updateTeacherOther(OaEmployeeOtherInfo other) {
		boolean isDeleteFile = false;
		boolean isUpdate = false;
		String realFilePath = null;
		byte[] fileByteArray = null;
		if ("1".equals(other.getIsPhotoChange())) {
			Object headPic = other.getHeadPic();
			String headPortrait = other.getHeadPortrait();

			if (headPic != null && headPic instanceof MultipartFile) {
				MultipartFile file = (MultipartFile) other.getHeadPic();

				if (StringUtil.isEmpty(headPortrait)) {
					realFilePath = FileSrcUtil.createFileSrc(Type.EMPLOYEE, other.getEmpId(),
							file.getOriginalFilename());
				} else {
					realFilePath = headPortrait;
				}
				try {
					fileByteArray = file.getBytes();
					isUpdate = true;
				} catch (IOException e) {
					log.error("文件上传失败", e);
				}

				other.setHeadPortrait(realFilePath);
			} else {
				realFilePath = headPortrait;
				other.setHeadPortrait("");
				isDeleteFile = true;
			}
		}
		OaEmployeeAnnex annexInfo = new OaEmployeeAnnex();
		annexInfo.setEmpAnnexType(StudentConstants.RECRUITER_ANNEX_TYPE_PHOTO);
		annexInfo.setEmpId(other.getEmpId());
		annexInfo.setAnnexUrl(other.getHeadPortrait());

		employeeMapper.updateAnnexInfo(annexInfo);
		if (StringUtil.isEmpty(other.getFinishTime())) {
			other.setFinishTime(null);
		}
		employeeMapper.updateOtherInfo(other);// TODO 更新附属信息

		String bucket = yzSysConfig.getBucket();
		if (isDeleteFile) {
			FileUploadUtil.delete(bucket, realFilePath);
		} else if (isUpdate && fileByteArray != null) {
			FileUploadUtil.upload(bucket, realFilePath, fileByteArray);
		}

		log.debug("---------------------------- 教师[" + other.getEmpId() + "]附属信息更新成功");
	}

	public void recommandExport(BdTeacherRecommendQuery recommendQuery, HttpServletResponse response) {
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			TeacherTemplateExcel templateExcel = new TeacherTemplateExcel();
			String fileName = recommendQuery.getYear() + "_" + recommendQuery.getGrade() + "_" + recommendQuery.getUnvsName() + "_" + templateExcel.pfsnLevelConvertSimple(recommendQuery.getPfsnLevel()) + "_" + templateExcel.pfsnNameConvert(recommendQuery.getPfsnName())  + ".xls";
			String name = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
			response.setHeader("Content-disposition", "attachment;filename=\"" + name + "\"");
			File file = ResourceUtils.getFile("classpath:" + templateExcel.getExcelPath());
			FileInputStream in = new FileInputStream(file);
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			String text = new String(buffer, "UTF-8");
			List<Map<String, Object>> teacherRecommend = teacherManageMapper.getTeacherRecommend(recommendQuery);
			text = templateExcel.excelRender(teacherRecommend, recommendQuery, text);
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
	
	public void deleteTeacher(String[] idArray) {
		// TODO Auto-generated method stub
		employeeMapper.deleteTeacher(idArray);
	}
	
	@SuppressWarnings("unchecked")
	public void exportTeacherInfo(TeacherInfoQuery queryInfo, HttpServletResponse response) {
		
		ExcelUtil.IExcelConfig<OaEmployeeImportInfo> testExcelCofing = new ExcelUtil.IExcelConfig<OaEmployeeImportInfo>();
		testExcelCofing.setSheetName("index").setType(OaEmployeeImportInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "empName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
				.addTitle(new ExcelUtil.IExcelTitle("年龄", "age"))
				.addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
				.addTitle(new ExcelUtil.IExcelTitle("出生年月日", "birthday"))
				.addTitle(new ExcelUtil.IExcelTitle("联系地址", "address"))
				.addTitle(new ExcelUtil.IExcelTitle("课时费用", "hourFee"))
				.addTitle(new ExcelUtil.IExcelTitle("其它费用", "otherFee"))
				.addTitle(new ExcelUtil.IExcelTitle("手机", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("毕业院校", "finishSchool"))
				.addTitle(new ExcelUtil.IExcelTitle("毕业专业", "finishMajor"))
				.addTitle(new ExcelUtil.IExcelTitle("毕业时间", "finishTime"))
				.addTitle(new ExcelUtil.IExcelTitle("学位", "degree"))
				.addTitle(new ExcelUtil.IExcelTitle("职称", "jobTitle"))
				.addTitle(new ExcelUtil.IExcelTitle("职称评定时间", "professionalTime"))		
				.addTitle(new ExcelUtil.IExcelTitle("所属校区", "campusName"))
				.addTitle(new ExcelUtil.IExcelTitle("任教学课", "teach"))
				.addTitle(new ExcelUtil.IExcelTitle("银行账号", "bankCard"))
				.addTitle(new ExcelUtil.IExcelTitle("邮箱", "email"))
				.addTitle(new ExcelUtil.IExcelTitle("教师资格证种类", "teachCertType"))
				.addTitle(new ExcelUtil.IExcelTitle("教师资格证书号", "teachCertNo"))
				.addTitle(new ExcelUtil.IExcelTitle("工作单位", "workPlace"));	
		
		
		List<OaEmployeeImportInfo> list =  teacherManageMapper.selectExprotTeacherInfo(queryInfo);

		for (OaEmployeeImportInfo exportInfo : list) {
			//exportInfo.setBirthday(birthday);
			//性别				
			exportInfo.setSex(dictExchangeUtil.getParamKey("sex", exportInfo.getSex()));
			//民族
			exportInfo.setNation(dictExchangeUtil.getParamKey("nation", exportInfo.getNation()));
			if(StringUtil.hasValue(exportInfo.getTeachCertType())) {
				exportInfo.setTeachCertType(dictExchangeUtil.getParamKey("teachCertType", exportInfo.getTeachCertType()));
			}
			
			
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=TeacherInfo.xls");
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
	
}
