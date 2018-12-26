package com.yz.service.educational;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.TeacherManageMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.oa.OaEmployeeImportInfo;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 教师管理
 * 
 * @author lx
 * @date 2017年7月10日 下午7:43:59
 */
@Service
public class TeacherManageExcelService {
	private static final Logger log = LoggerFactory.getLogger(TeacherManageExcelService.class);

	@Autowired
	private TeacherManageMapper teacherManageMapper;
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	
	
	@SuppressWarnings("unchecked")
	public void importTeacherInfo(MultipartFile excelTeacher) {
		//对导入工具进行字段填充
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

				//行数记录
				int index = 2;
				try {
					//对文件进行分析转对象
					List<OaEmployeeImportInfo> list = ExcelUtil.importWorkbook(excelTeacher.getInputStream(), testExcelCofing,excelTeacher.getOriginalFilename());
					
					//遍历插入
					for (OaEmployeeImportInfo importInfo : list) {
						if (!StringUtil.hasValue(importInfo.getEmpName())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getIdCard())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getSex())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！性别不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getAge())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年龄不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getBirthday())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！出生年月日不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getAddress())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！联系地址不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getHourFee())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！课时费用不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getOtherFee())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！其它费用不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getMobile())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！手机不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getFinishSchool())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业院校不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getFinishMajor())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业专业不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getFinishTime())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业时间不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getFinishMajor())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业专业不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getDegree())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学位不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getJobTitle())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！职称不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getCampusName())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！所属校区不能为空");
		                }
						if (!StringUtil.hasValue(importInfo.getWorkPlace())) {
		                    throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！工作单位不能为空");
		                }

						if (!StringUtil.hasValue(importInfo.getFinishTime())) {
							throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业时间不能为空");
		                }else {
		                		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						         try {
						        	format.setLenient(false);
									format.parse(importInfo.getFinishTime());
								} catch (ParseException e) {
									throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！毕业时间格式错误，正确格式如:[2018-01-26]");
								}
		                }
						
						if (!StringUtil.hasValue(importInfo.getBirthday())) {

							throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！出生年月日不能为空");
		                }else {
		                		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						         try {
						        	format.setLenient(false);
									format.parse(importInfo.getBirthday());
								} catch (ParseException e) {
									throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！出生年月日格式错误，正确格式如:[2018-01-26]");
								}
		                }

						//教师资格证种类转换
						if(StringUtil.hasValue(importInfo.getTeachCertType())) {
							String valueTemple = dictExchangeUtil.getParamValue("teachCertType", importInfo.getTeachCertType());
							if(null == valueTemple){
								throw new IllegalArgumentException("excel教师资格证种类数据格式错误，请检查第"+index+"行数据！");
							}else{
								importInfo.setTeachCertType(valueTemple);
							}
						}
						importInfo.setSex(dictExchangeUtil.getParamKey("sex", importInfo.getSex()));
						if(StringUtil.hasValue(importInfo.getNation())) {
							importInfo.setSex(dictExchangeUtil.getParamKey("nation", importInfo.getNation()));
						}
						
						index++;
					}// for end
					
					// 检查必录项的所属校区
					List<Map<String, Object>> campusList = teacherManageMapper.getNonExistsCampus(list);
					if (campusList != null && campusList.size() > 0) {
						StringBuilder sb = new StringBuilder();
						sb.append("核对以下所属校区不存在：<br/>");
						for (Map<String, Object> map : campusList) {
							sb.append(map.get("campusName") + "<br/>");
						}
						throw new IllegalArgumentException(sb.toString());
					}
					BaseUser user = SessionUtil.getUser();
					
					teacherManageMapper.initTmpTeacherInfoTable(list);
					List<Map<String, String>> tearcherList = teacherManageMapper.selectTeacherInfoInsert(user);
					if(tearcherList.isEmpty()){
						return;
					}
					for (Map<String, String> map : tearcherList) {
						map.put("emp_id", IDGenerator.generatorId());
					}
					teacherManageMapper.insertTeacherInfoExcel(tearcherList);
					
					//初始化附件
					List<Map<String, String>> annexList = teacherManageMapper.selectTmpTeacherAnnex();
					for (Map<String, String> map : annexList) {
						map.put("annex_id", IDGenerator.generatorId());
					}
					teacherManageMapper.insertTmpinitTeacherAnnex(annexList);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new IllegalArgumentException("excel数据格式错误，请检查第"+index+"行数据！");
				}
				
	}
	
	
}
