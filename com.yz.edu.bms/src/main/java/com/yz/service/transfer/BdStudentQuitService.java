package com.yz.service.transfer;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.yz.conf.YzSysConfig;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.FileSrcUtil.Type;
import com.yz.core.util.FileUploadUtil;
import com.yz.dao.transfer.BdStudentQuitMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.transfer.BdStudentQuitInfo;
import com.yz.model.transfer.BdStudentQuitQuery;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 休学申请
 * 
 * @author lx
 * @date 2018年5月24日 下午2:27:36
 */
@Service
@Transactional
public class BdStudentQuitService {

	private static final Logger log = LoggerFactory.getLogger(BdStudentQuitService.class);

	@Autowired
	private BdStudentQuitMapper studentQuitMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	public Object findAllBdStudentQuit(int start, int length, BdStudentQuitQuery quitQuery) {
		BaseUser user = SessionUtil.getUser();
		List<String> roleList = user.getRoleCodeList();
		if (roleList != null) {
			if (roleList.contains("zr") || roleList.contains("400kfzy")) { // 学籍组长/部门主任/400
				user.setUserLevel("11");
			} else if (roleList.contains("cjzzz")) {
				user.setUserLevel("12");
			} else if (roleList.contains("gkzzz")) {
				user.setUserLevel("13");
			} else if (roleList.contains("cjzfdy")) {
				user.setUserLevel("14");
			} else if (roleList.contains("gkzfdy")) {
				user.setUserLevel("15");
			}
		}
		PageHelper.offsetPage(start, length);
		List<BdStudentQuitInfo> list = studentQuitMapper.findAllBdStudentQuit(quitQuery, user);
		IPageInfo<BdStudentQuitInfo> page = new IPageInfo<BdStudentQuitInfo>((Page<BdStudentQuitInfo>) list);
		return page;
	}

	public Object findAllBdStudentQuitForCheck(int start, int length, BdStudentQuitQuery quitQuery) {
		BaseUser user = SessionUtil.getUser();

		List<String> roleList = user.getRoleCodeList();
		if (roleList != null) {
			if (roleList.contains("zr")) { // 学籍组长/部门主任/400
				user.setUserLevel("11");
			} else if (roleList.contains("cjzzz")) {
				user.setUserLevel("12");
			} else if (roleList.contains("gkzzz")) {
				user.setUserLevel("13");
			}
		}

		PageHelper.offsetPage(start, length);
		List<BdStudentQuitInfo> list = studentQuitMapper.findAllBdStudentQuitForCheck(quitQuery, user);
		IPageInfo<BdStudentQuitInfo> page = new IPageInfo<BdStudentQuitInfo>((Page<BdStudentQuitInfo>) list);
		return page;
	}

	/**
	 * 申请
	 */
	public void insertQuitSchoolApply(BdStudentQuitInfo quitInfo, MultipartFile attachment) {
		int count = studentQuitMapper.selectQuitCount(quitInfo.getLearnId());
		if (count > 0) {
			throw new BusinessException("E000117"); // 请勿重复提交申请
		}
		quitInfo.setId(IDGenerator.generatorId());
		quitInfo.setCheckStatus("2"); // 待审核
		quitInfo.setApplyTime(DateUtil.getNowDateAndTime());
		if (null != attachment) {

			String bucket = yzSysConfig.getBucket();
			String fileName = attachment.getOriginalFilename();
			try {
				StringBuffer s = new StringBuffer(Type.OUT.get()).append("/");

				if (StringUtil.hasValue(quitInfo.getId())) {
					s.append(quitInfo.getId()).append("/");
				}
				s.append(fileName);
				String url = s.toString();

				FileUploadUtil.upload(bucket, url, attachment.getBytes());

				quitInfo.setFileName(fileName);
				quitInfo.setFileUrl(url);
			} catch (Exception e) {
				throw new BusinessException("E000118"); // 文件上传失败；
			}

		}
		quitInfo.setApplyUserId(quitInfo.getCreateUserId());
		quitInfo.setApplyUserName(quitInfo.getCreateUser());
		studentQuitMapper.insertQuitSchoolApply(quitInfo);
	}

	/**
	 * 审核
	 * 
	 * @param quitQuery
	 */
	public void checkStudentQuitSchoolApply(String id, String rejectReason, String checkStatus, String learnId) {
		BaseUser user = SessionUtil.getUser();
		BdStudentQuitInfo quitInfo = new BdStudentQuitInfo();
		quitInfo.setId(id);
		quitInfo.setRejectReason(rejectReason);
		quitInfo.setOperUserName(user.getUsername());
		quitInfo.setOperUserId(user.getUserId());
		quitInfo.setCheckStatus(checkStatus);

		studentQuitMapper.checkStudentQuitSchoolApply(quitInfo);
		if (checkStatus.equals("3")) {
			// 如果审核通过,则改变学员学业状态为休学状态
			studentQuitMapper.updateStdStageByLearnId(learnId);
		}
	}

	/**
	 * 取消申请
	 * 
	 * @param id
	 */
	public void delStudentQuitSchoolApply(String id) {
		String checkStatus = studentQuitMapper.getStudentQuitSchoolStatus(id);
		if (StringUtil.isNotBlank(checkStatus) && checkStatus.equals("3")) { // 审核通过的不能取消
			throw new BusinessException("E000202");
		}
		studentQuitMapper.delStudentQuitSchoolApply(id);
	}

	/**
	 * 查找学员信息
	 * 
	 * @param page
	 * @param rows
	 * @param sName
	 * @return
	 */
	public IPageInfo<Map<String, String>> findStudentInfo(int page, int rows, String sName) {
		BaseUser user = SessionUtil.getUser();
		// 此处再针对班主任做数据权限2017-11-29
		List<String> roleList = user.getRoleCodeList();
		if (roleList != null) {
			if (roleList.contains("zr") || roleList.contains("400kfzy")) { // 学籍组长/部门主任/400
				user.setUserLevel("11");
			} else if (roleList.contains("cjzzz")) {
				user.setUserLevel("12");
			} else if (roleList.contains("gkzzz")) {
				user.setUserLevel("13");
			} else if (roleList.contains("cjzfdy")) {
				user.setUserLevel("14");
			} else if (roleList.contains("gkzfdy")) {
				user.setUserLevel("15");
			}
		}
		PageHelper.startPage(page, rows);
		return new IPageInfo<Map<String, String>>(
				(Page<Map<String, String>>) studentQuitMapper.findStudentInfo(sName, user));
	}

	/**
	 * 查看
	 * 
	 * @param id
	 */
	public BdStudentQuitInfo getQuitSchoolApplyInfo(String id) {
		return studentQuitMapper.getQuitSchoolApplyInfo(id);
	}

	/**
	 * 批量操作
	 * 
	 * @param ids
	 * @param rejectReason
	 * @param checkStatus
	 */
	public void batchCheck(String[] ids, String rejectReason, String checkStatus) {
		BaseUser user = SessionUtil.getUser();
		List<String> learnIds = new ArrayList<>();
		List<BdStudentQuitInfo> quitInfos = new ArrayList<>();
		if (null != ids && ids.length > 0) {
			for (String id : ids) {
				String[] split = id.split(";");
				BdStudentQuitInfo quitInfo = new BdStudentQuitInfo();
				quitInfo.setId(split[0]);
				quitInfo.setRejectReason(rejectReason);
				quitInfo.setOperUserName(user.getUsername());
				quitInfo.setOperUserId(user.getUserId());
				quitInfo.setCheckStatus(checkStatus);
				quitInfos.add(quitInfo);
				if (checkStatus.equals("3")) { // 通过
					learnIds.add(split[1]);
				}
			}
		}
		if (quitInfos.size() > 0) {
			studentQuitMapper.batchUpdateCheckStatus(quitInfos);
		}
		if (learnIds.size() > 0) {
			// 批量更新学员状态
			studentQuitMapper.batchUpdateStdStageByLearnId(learnIds);
		}
	}

	/**
	 * 导出
	 * 
	 * @param quitQuery
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void exportQuitSchoolApply(BdStudentQuitQuery quitQuery, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdStudentQuitInfo> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentQuitInfo>();
		testExcelCofing.setSheetName("index").setType(BdStudentQuitInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("手机号", "mobile")).addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("学号", "stdNo"))
				.addTitle(new ExcelUtil.IExcelTitle("招生类型", "recruitType"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
				.addTitle(new ExcelUtil.IExcelTitle("现学员状态", "stdStage"))
				.addTitle(new ExcelUtil.IExcelTitle("原学员状态", "oldStdStage"))
				.addTitle(new ExcelUtil.IExcelTitle("休学原因", "reason"))
				.addTitle(new ExcelUtil.IExcelTitle("申请时间", "applyTime"))
				.addTitle(new ExcelUtil.IExcelTitle("申请人", "applyUserName"))
				.addTitle(new ExcelUtil.IExcelTitle("操作时间", "operTime"))
				.addTitle(new ExcelUtil.IExcelTitle("操作人", "operUserName"))
				.addTitle(new ExcelUtil.IExcelTitle("审核状态", "checkStatus"));

		BaseUser user = SessionUtil.getUser();

		List<String> roleList = user.getRoleCodeList();
		if (roleList != null) {
			if (roleList.contains("zr")) { // 学籍组长/部门主任/400
				user.setUserLevel("11");
			} else if (roleList.contains("cjzzz")) {
				user.setUserLevel("12");
			} else if (roleList.contains("gkzzz")) {
				user.setUserLevel("13");
			}
		}
		List<BdStudentQuitInfo> quitSchoolList = studentQuitMapper.findAllBdStudentQuitForExport(quitQuery, user);

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(quitSchoolList, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=quitschool.xls");
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
