package com.yz.service.educational;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.JStudentStudyingMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.JStudentStudyingImportExcel;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdLearnRules;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
public class JStudentStudyingImportService {

	private static final Logger log = LoggerFactory.getLogger(JStudentStudyingImportService.class);

	@Autowired
	private JStudentStudyingMapper studyingMapper;

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	// private static final ExecutorService executor =
	// Executors.newCachedThreadPool();

	@SuppressWarnings("unchecked")
	public void importWbStudentInfo(MultipartFile excelStudentInfo) {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<JStudentStudyingImportExcel> testExcelCofing = new ExcelUtil.IExcelConfig<JStudentStudyingImportExcel>();
		testExcelCofing.setSheetName("index").setType(JStudentStudyingImportExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("学籍编号", "stdNo"))
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("证件号码", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("性别", "sex")).addTitle(new ExcelUtil.IExcelTitle("民族", "nation"))
				.addTitle(new ExcelUtil.IExcelTitle("生日", "birthday"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("报考层次", "enrollPfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("录取院校", "admitUnvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("录取专业", "adminPfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("在读层次", "learnPfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("在读院校", "learnUnvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("在读专业", "learnPfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("考生号", "examNo"))
				.addTitle(new ExcelUtil.IExcelTitle("归属校区", "homeCampusName"))
				.addTitle(new ExcelUtil.IExcelTitle("分校", "recruitCampusName"))
				.addTitle(new ExcelUtil.IExcelTitle("招生老师", "recruitName"));

		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<JStudentStudyingImportExcel> list = ExcelUtil.importWorkbook(excelStudentInfo.getInputStream(),
					testExcelCofing, excelStudentInfo.getOriginalFilename());
			if (list != null && list.size() > 3000) {
				throw new IllegalArgumentException("该excel导入一次性导入最多只能导500条记录！");
			}
			for (JStudentStudyingImportExcel student : list) {

				if (!StringUtil.hasValue(student.getStdName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！姓名不能为空");
				}
				if (!StringUtil.hasValue(student.getIdCard())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！证件号码不能为空");
				}
				// 生日
				if (StringUtil.hasValue(student.getBirthday())) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						format.parse(student.getBirthday());
					} catch (ParseException e) {
						throw new IllegalArgumentException(
								"excel数据格式错误，请检查第" + index + "行数据！生日格式错误，正确格式如:[2018-01-26]");
					}
				}
				String valueTemple = "";
				// 民族
				if (StringUtil.hasValue(student.getNation())) {
					valueTemple = dictExchangeUtil.getParamValue("nation", student.getNation().trim());
					if (null == valueTemple) {
						throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！民族错误");
					} else {
						student.setNation(valueTemple);
					}
				}
				// 年级转换
				valueTemple = dictExchangeUtil.getParamValue("grade", student.getGrade().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级格式错误,正确格式如:[2016级]");
				} else {
					student.setGrade(valueTemple);
				}
				// 报考层次
				valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", student.getEnrollPfsnLevel().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException(
							"excel数据格式错误，请检查第" + index + "行数据！报考层次格式错误，正确格式如:[1>专科升本科类,5>高中起点高职高专]");
				} else {
					student.setEnrollPfsnLevel(valueTemple);
				}
				// 在读层次
				valueTemple = dictExchangeUtil.getParamValue("pfsnLevel", student.getLearnPfsnLevel().trim());
				if (null == valueTemple) {
					throw new IllegalArgumentException(
							"excel数据格式错误，请检查第" + index + "行数据！在读层次格式错误，正确格式如:[1>专科升本科类,5>高中起点高职高专]");
				} else {
					student.setLearnPfsnLevel(valueTemple);
				}

				if (!StringUtil.hasValue(student.getLearnUnvsName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！在读院校不能为空");
				}
				if (!StringUtil.hasValue(student.getLearnPfsnName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！在读专业不能为空");
				}
				if (!StringUtil.hasValue(student.getHomeCampusName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！归属校区不能为空");
				}
				if (!StringUtil.hasValue(student.getRecruitCampusName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！分校不能为空");
				}
				if (!StringUtil.hasValue(student.getRecruitName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！招生老师不能为空");
				}

				index++;
			} // for end

			// 检查必录项的院校与专业
			List<Map<String, Object>> resultList = studyingMapper.getNonExistsAdmitUnvsNamePsfnName(list);
			if (resultList != null && resultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("核对以下录取院校,录取专业不存在：<br/>");
				for (Map<String, Object> map : resultList) {
					sb.append(map.get("admitUnvsName") + "-" + map.get("adminPfsnName") + "<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}

			// 检查必录项的院校与专业
			List<Map<String, Object>> LearnresultList = studyingMapper.getNonExistsLearnUnvsNamePsfnName(list);
			if (LearnresultList != null && LearnresultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("核对以下在读院校,在读专业不存在：<br/>");
				for (Map<String, Object> map : LearnresultList) {
					sb.append(map.get("learnUnvsName") + "-" + map.get("learnPfsnName") + "<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}

			// 检查必录项的归属校区
			List<Map<String, Object>> homeCampusresultList = studyingMapper.getNonExistsHomeCampus(list);
			if (homeCampusresultList != null && homeCampusresultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("核对以下归属校区不存在：<br/>");
				for (Map<String, Object> map : homeCampusresultList) {
					sb.append(map.get("homeCampusName") + "<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}

			// 检查必录项的分校
			List<Map<String, Object>> recruitCampusresultList = studyingMapper.getNonExistsRecruitCampus(list);
			if (recruitCampusresultList != null && recruitCampusresultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("核对以下分校不存在：<br/>");
				for (Map<String, Object> map : recruitCampusresultList) {
					sb.append(map.get("recruitCampusName") + "<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}
			BaseUser user = SessionUtil.getUser();
			
			studyingMapper.initTmpWbStudentInfoTable(list);
			List<Map<String, String>> stdList = studyingMapper.selectStudentInfoInsert(user);
			if(null != stdList && stdList.size()>0){
				for (Map<String, String> map : stdList) {
					map.put("std_id", IDGenerator.generatorId());
				}
				studyingMapper.insertStudentInfoExcel(stdList);
			}
			
			List<Map<String, String>> learnList = studyingMapper.selectTmpLearnInfoInsert(user);
			if(null != learnList && learnList.size() >0){
				for (Map<String, String> map : learnList) {
					map.put("learn_id", IDGenerator.generatorId());
				}
				studyingMapper.insertTmpLearnInfo(learnList);
			}
			
			
			List<Map<String, String>> empList = studyingMapper.selectTmpEmpInsert(user);
			if(null != empList && empList.size() >0){
				for (Map<String, String> map : empList) {
					map.put("emp_id", IDGenerator.generatorId());
				}
				studyingMapper.insertTmpEmp(empList);
			}
			
			
			List<Map<String, String>> annexList = studyingMapper.selectTmpLearnAnnex();
			
			if(null != annexList && annexList.size() >0){
				for (Map<String, String> map : annexList) {
					map.put("annex_id", IDGenerator.generatorId());
				}
				studyingMapper.insertTmpLearnAnnex(annexList);
			}
			
			// 批量插入
			studyingMapper.insertByExcel(list, user, IDGenerator.generatorId(), IDGenerator.generatorId(),
					IDGenerator.generatorId(), IDGenerator.generatorId());
			// importStuInfoAsyn(list,user);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

	// /*******************异步添加**********************************/
	// public void importStuInfoAsyn(List<JStudentStudyingImportExcel>
	// list,BaseUser user) {
	//
	// final int count = list.size();
	//
	// final int eSize = count / tSize;
	//
	// for (int i = 1; i <= tSize; i++) {
	// final int ii = i;
	// executor.execute(new Runnable() {
	//
	// @Override
	// public void run() {
	// insertStuInfo(ii, eSize, count, list,user);
	// isDone++;
	// log.error("----------------------------------- 外校区学员导入： 线程[" + ii + "] is
	// done! | " + isDone);
	// }
	// });
	// }
	//
	// }
	//
	// @Transactional
	// private void insertStuInfo(int num, int eSize, int
	// all,List<JStudentStudyingImportExcel> importList,BaseUser user) {
	//
	// int _size = 400;
	//
	// int _count = num == tSize ? all : eSize * num;
	//
	// int _start = num == 1 ? 0 : ((num - 1) * eSize);
	//
	// log.error("------------------------------- 外校区学员导入： 线程[" + num + "] ：起始："
	// + _start + " | 总数：" + _count + " | 每页："
	// + _size);
	//
	// int batch = 1;
	//
	// while (_start < _count) {
	// int __size = 0;
	// if (_start + _size > _count) {
	// __size = _count - _start;
	// } else {
	// __size = _size;
	// }
	// List<JStudentStudyingImportExcel> list = importList.subList(_start,
	// _start + __size);
	// if (list != null) {
	// for (JStudentStudyingImportExcel stu : list) {
	// try {
	// BdStudentBaseInfo o_baseInfo = recruitMapper.getStudentBaseInfo("1",
	// stu.getIdCard());
	// BdStudentBaseInfo baseInfo=new BdStudentBaseInfo();
	// if(o_baseInfo == null) {
	// baseInfo.setStdSource("X"); //学员系统录入
	// baseInfo.setStdName(stu.getStdName());
	// baseInfo.setIdCard(stu.getIdCard());
	// baseInfo.setIdType("1");
	// baseInfo.setIdCard(stu.getIdCard());
	// baseInfo.setSex(DictExchangeUtil.getParamValue("sex",
	// stu.getSex().trim()));
	// baseInfo.setBirthday(stu.getBirthday());
	// baseInfo.setNation(stu.getNation());
	// baseInfo.setMobile("");
	// baseInfo.setCreateUser(user.getUsername());
	// baseInfo.setCreateUserId(user.getUserId());
	// recruitMapper.insertStudentBaseInfo(baseInfo);
	// baseInfo.setNew(true);
	// log.debug("---------------------------- 学员[" + baseInfo.getStdId() +
	// "]基础信息插入成功");
	// } else {
	// baseInfo.setStdId(o_baseInfo.getStdId());
	// baseInfo.setStdName(stu.getStdName());
	// recruitMapper.updateStudentBaseInfo(baseInfo);
	// baseInfo.setNew(false);
	// log.debug("---------------------------- 学员[" + baseInfo.getStdId() +
	// "]基础信息已存在，信息更新成功");
	// }
	//
	// // 如果为新增学员， 需要新增附属信息、附件信息
	//// if (baseInfo.isNew()) {
	//// BdStudentOther other=new BdStudentOther();
	//// other.setStdId(baseInfo.getStdId());
	////
	//// // 新增附属信息
	//// recruitMapper.insertStudentOtherInfo(other);
	//// // 初始化收货地址信息
	//// BdShoppingAddress saInfo = new BdShoppingAddress();
	//// saInfo.setIsDefault(GlobalConstants.TRUE);
	//// saInfo.setStdId(baseInfo.getStdId());
	//// saInfo.setUpdateUser(baseInfo.getUpdateUser());
	//// saInfo.setUpdateUserId(baseInfo.getUpdateUserId());
	//// recruitMapper.insertShoppingAddress(saInfo);
	//// }
	//
	// // 初始化学业信息
	// BdLearnInfo learnInfo = new BdLearnInfo();
	// learnInfo.setStdId(baseInfo.getStdId());
	// learnInfo.setStdNo(stu.getStdNo());
	// learnInfo.setSchoolRoll(stu.getSchoolRoll());
	// learnInfo.setStdType(StudentConstants.STD_TYPE_ALIEN);
	// learnInfo.setRecruitType(StudentConstants.RECRUIT_TYPE_CJ);
	// learnInfo.setGrade(stu.getGrade());
	// learnInfo.setUpdateUser(baseInfo.getUpdateUser());
	// learnInfo.setUpdateUserId(baseInfo.getUpdateUserId());
	// learnInfo.setCreateUser(baseInfo.getCreateUser());
	// learnInfo.setCreateUserId(baseInfo.getCreateUserId());
	// learnInfo.setStdStage(StudentConstants.STD_STAGE_STUDYING);
	// learnInfo.setStdName(stu.getStdName());
	//
	// Map<String, Object>
	// learn_unvsinfo=studyingMapper.getUnvsIdPfsnIdByNames(stu.getLearnPfsnName(),
	// stu.getLearnUnvsName(),stu.getGrade(),stu.getLearnPfsnLevel());
	// if(learn_unvsinfo!=null&&learn_unvsinfo.get("unvsId")!=null) {
	// learnInfo.setUnvsId(learn_unvsinfo.get("unvsId").toString());
	// }
	// if(learn_unvsinfo!=null&&learn_unvsinfo.get("pfsnId")!=null) {
	// learnInfo.setPfsnId(learn_unvsinfo.get("pfsnId").toString());
	// }
	// Map<String, Object>
	// homecampus=studyingMapper.getHomeCampusIdByName(stu.getHomeCampusName());
	// if(homecampus!=null&&homecampus.get("campusId")!=null) {
	// learnInfo.setHomeCampusId(homecampus.get("campusId").toString());
	// }
	// learnInfo.setIsOnline(GlobalConstants.FALSE);
	// learnInfo.setEnrollChannel(StudentConstants.ENROLL_CHANNEL_LOCAL);
	// // 检测资料是否已完善
	// learnInfo.setIsDataCompleted(GlobalConstants.FALSE);
	// recruitMapper.initLearnInfo(learnInfo);
	//
	//
	// //插入招生老师信息
	// OaEmployeeBaseInfo employee=new OaEmployeeBaseInfo();
	// employee.setEmpName(stu.getRecruitName());
	// employee.setEmpStatus("2");//离职
	// employee.setEmpType("1");//招生老师
	// employee.setEmpSource("2");//外校区
	// employeeMapper.insertEmpBaseInfo(employee);
	//
	// // 插入数据权限
	// BdLearnRules rulesInfo = new BdLearnRules();
	// rulesInfo.setLearnId(learnInfo.getLearnId());
	//
	// Map<String, Object>
	// recruitcampus=studyingMapper.getRecruitCampusIdByName(stu.getRecruitCampusName());
	// if(recruitcampus!=null&&recruitcampus.get("campusId")!=null) {
	// rulesInfo.setRecruitCampusId(recruitcampus.get("campusId").toString());
	// }
	// rulesInfo.setRecruit(employee.getEmpId());
	// recruitMapper.insertLearnRules(rulesInfo);
	//
	//
	// String learnId = learnInfo.getLearnId();
	// //初始化报读信息
	// BdStudentRecruit recruit=new BdStudentRecruit();
	// recruit.setLearnId(learnId);
	// recruit.setStdId(baseInfo.getStdId());
	// recruit.setGrade(stu.getGrade());
	// recruit.setPfsnLevel(stu.getEnrollPfsnLevel());
	//
	// recruitMapper.insertStudentEnrolInfo(recruit);
	//
	//
	// //初始化录取信息
	// BdStudentRecruit recruitadmit=new BdStudentRecruit();
	// recruitadmit.setLearnId(learnId);
	// recruitadmit.setStdId(baseInfo.getStdId());
	// if(StringUtil.hasValue(stu.getAdminPfsnName())&&StringUtil.hasValue(stu.getAdmitUnvsName()))
	// {
	// Map<String, Object>
	// admin_unvsinfo=studyingMapper.getUnvsIdPfsnIdByNames(stu.getAdminPfsnName(),
	// stu.getAdmitUnvsName(),stu.getGrade(),stu.getEnrollPfsnLevel());
	// if(admin_unvsinfo!=null&&admin_unvsinfo.get("unvsId")!=null) {
	// recruitadmit.setUnvsId(admin_unvsinfo.get("unvsId").toString());
	// }
	// if(admin_unvsinfo!=null&&admin_unvsinfo.get("pfsnId")!=null) {
	// recruitadmit.setPfsnId(admin_unvsinfo.get("pfsnId").toString());
	// }
	// }
	// recruitadmit.setGrade(stu.getGrade());
	// recruitadmit.setExamNum(stu.getExamNo());
	// recruitMapper.insertStudentAdmitInfo(recruit);
	//
	// } catch (Exception e) {
	// log.error("------------
	// 外校区学员导入：添加异常="+stu.getIdCard()+"信息:"+e.getMessage());
	// throw new IllegalArgumentException("------------
	// 外校区学员导入：添加异常="+stu.getIdCard()+"信息:"+e.getMessage());
	//
	// }
	// }
	//
	// _start += _size;
	//
	// batch++;
	//
	// log.error("------------------------------- 外校区学员导入： [" + batch + "] 线程 ["
	// + num + "] ：当前记录数 ：" + _start);
	// }
	// }
	// }
}
