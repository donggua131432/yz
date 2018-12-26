package com.yz.service.educational;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.yz.constants.CheckConstants;
import com.yz.constants.EducationConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.educational.OaTaskInfoMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.educational.BdStudentSendImport;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.statistics.PfsnMatchBookStatInfo;
import com.yz.redis.RedisService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentSendService {

	private static Logger log = LoggerFactory.getLogger(BdStudentSendService.class);
	@Autowired
	private BdStudentSendMapper studentSendMapper;

	@Autowired
	private StuOrderBookAysnImportService orderBookAysnImportService;

	@Autowired
	private OaTaskInfoMapper oaTaskInfoMapper;

	@Autowired
	private BdStudentModifyService modifyService;

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@Autowired
	private UsInfoMapper usInfoMapper;

	/**
	 * 缴费完成后发书
	 * 
	 * @param learnId
	 *            学员ID
	 * @param semester
	 *            学期
	 * @param isFD
	 *            false为非辅导教材 true 为辅导教材
	 */
	public void sendBook(String learnId, String year, boolean isFD) {
		Assert.hasText(learnId, "参数learnId不能为空");
		if (!isFD) {
			if (StudentConstants.YEAR_ONE.equals(year)) {
				// 如果是第一年则生成第一学期第二学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_ONE, StudentConstants.SEMESTER_TWO, isFD);
			} else if (StudentConstants.YEAR_TWO.equals(year)) {
				// 如果是第一年则生成第三学期第四学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_THREE, StudentConstants.SEMESTER_FOUR, isFD);
			} else if (StudentConstants.YEAR_THREE.equals(year)) {
				// 如果是第一年则生成第五学期第六学期的教材发放
				initBookSend(learnId, StudentConstants.SEMESTER_FIVE, StudentConstants.SEMESTER_SIX, isFD);
			}
		} else {
			initBookSend(learnId, null, null, isFD);
		}

	}

	@Autowired
	private StudentAllMapper stdAllMapper;

	public void initBookSend(String learnId, String firstSemester, String secondSemester, boolean isFD) {

		BdStudentSend studentSend = new BdStudentSend();
		studentSend.setLearnId(learnId);
		studentSend.setTextbookType(isFD ? EducationConstants.TEXT_BOOK_TYPE_FD : EducationConstants.TEXT_BOOK_TYPE_XK);
		studentSend.setReceiveStatus(StudentConstants.RECEIVE_STATUS_RECEIVED);
		studentSend.setOrderBookStatus(OrderConstants.ORDER_BOOK_NO);
		if (!isFD) {

			// 走班主任审核
			studentSend.setAddressStatus(CheckConstants.CHECK_TEACHER_1);

			// String userId = stdAllMapper.selectUserIdByLearnId(learnId);
			/*
			 * Map<String, String> addressInfo =
			 * usMapper.selectUsBookAddress(userId);
			 * studentSend.setAddress(addressInfo.get("address"));
			 * studentSend.setMobile(addressInfo.get("mobile"));
			 * studentSend.setUserName(addressInfo.get("saName"));
			 * studentSend.setProvinceCode(addressInfo.get("provinceCode"));
			 * studentSend.setCityCode(addressInfo.get("cityCode"));
			 * studentSend.setDistrictCode(addressInfo.get("districtCode"));
			 */

			// 查询是否存在发书记录

			List<String> firstSendIds = studentSendMapper.selectSendId(firstSemester, learnId);
			// 删除旧的发书记录，插入新发书记录
			for (String sendId : firstSendIds) {
				studentSendMapper.deleteBookSend(sendId);
				studentSendMapper.deleteTextBookSend(sendId);
			}

			List<String> secondSendIds = studentSendMapper.selectSendId(secondSemester, learnId);
			// 删除旧的发书记录，插入新发书记录
			for (String sendId : secondSendIds) {
				studentSendMapper.deleteBookSend(sendId);
				studentSendMapper.deleteTextBookSend(sendId);
			}

			// 插入第一个学期发书记录
			inserStudenSend(studentSend, learnId, firstSemester);
			// 插入第二个学期发书记录
			inserStudenSend(studentSend, learnId, secondSemester);

		} else {
			String[] testSub = studentSendMapper.selectTestSubByLearnId(learnId);

			int subCount = studentSendMapper.selectTestBookCount(testSub);
			if (subCount > 0) {

				List<String> sendIds = studentSendMapper.selectFdSendIds(learnId);
				// 已存在发书记录，退出
				if (sendIds.size() > 0) {
					return;
				}
				/*for (String sendId : sendIds) {
					studentSendMapper.deleteBookSend(sendId);
					studentSendMapper.deleteTextBookSend(sendId);
				}*/

				// 不走班主任审核
				studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_4);

				BdStudentBaseInfo stdInfo = stdAllMapper.getStudentBaseInfoByLearnId(learnId);
				studentSend.setAddress(stdInfo.getAddress());
				studentSend.setMobile(stdInfo.getMobile());
				studentSend.setUserName(stdInfo.getStdName());
				studentSend.setProvinceCode(stdInfo.getNowProvinceCode());
				studentSend.setCityCode(stdInfo.getNowCityCode());
				studentSend.setDistrictCode(stdInfo.getNowDistrictCode());
				// 已定未发lx
				studentSend.setOrderBookStatus(OrderConstants.ORDER_BOOK_NO_SEND);
				studentSend.setSendId(IDGenerator.generatorId());
				studentSendMapper.insertSelective(studentSend);
				studentSendMapper.insertBdTextBookSendFD(studentSend.getSendId(), testSub);
			}

		}
	}

	private void inserStudenSend(BdStudentSend studentSend, String learnId, String semester) {
		// 插入第二学期发书记录
		studentSend.setSemester(semester);
		studentSend.setSendId(IDGenerator.generatorId());
		studentSendMapper.insertSelective(studentSend);
		studentSendMapper.insertBdTextBookSend(studentSend.getSendId(), learnId, semester);
	}

	public List<Map<String, Object>> findAllStudentSendSevi(BdStudentSendMap studentSendMap) {
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		List<String> roleCodeList = user.getRoleCodeList();
		if (roleCodeList.contains("gkzfdy")) {// 国开辅导员
			studentSendMap.setExt1(" and bli.recruit_type = '2' and blr.tutor ='" + user.getEmpId() + "'");
		} else if (roleCodeList.contains("cjzfdy")) { // 成教辅导员
			studentSendMap.setExt1(" and bli.recruit_type = '1' and blr.tutor ='" + user.getEmpId() + "'");
		}

		return studentSendMapper.findAllStudentSendSevi(studentSendMap);
	}

	public void rejectedStudentSend(String[] idArray, String reason, String type) {
		// 要给被驳回的学员发送微信通知 同时重置学员的任务信息
		if (idArray.length > 0) {
			for (String str : idArray) {
				BdStudentSend sendInfo = studentSendMapper.selectByPrimaryKey(str);
				if (null != sendInfo) {
					String userId = oaTaskInfoMapper.getUserIdByLearnId(sendInfo.getLearnId());
					if (StringUtil.hasValue(userId)) {
						String openId = usInfoMapper.selectUserOpenId(userId);
						if (StringUtil.hasValue(openId)) {
							log.debug("班主任驳回教材地址" + openId + "原因:" + reason);
							sendAddressRejectMsg(openId, reason, null, sendInfo.getTextbookType());
						}
					}
					studentSendMapper.resetAddressTask(sendInfo.getLearnId());
				}
			}
		}

		studentSendMapper.rejectedStudentSend(idArray, reason, type, SessionUtil.getUser());
	}

	public void passStudentSend(String[] idArray) {
		// TODO Auto-generated method stub
		studentSendMapper.passStudentSend(idArray);
	}

	public List<Map<String, Object>> findAllOkSend(BdStudentSendMap studentSendMap) {
		// TODO Auto-generated method stub
		// 到时候登录动态获取填入
		BaseUser user = SessionUtil.getUser();
		List<String> jtList = user.getJtList();
		if (jtList.contains("GKZFDY")) {// 国开辅导员
			studentSendMap.setExt1(" and bli.recruit_type = '2' and blr.tutor ='" + user.getEmpId() + "'");
		} else if (jtList.contains("CJZFDY")) { // 成教辅导员
			studentSendMap.setExt1(" and bli.recruit_type = '1' and blr.tutor ='" + user.getEmpId() + "'");
		}
		return studentSendMapper.findAllOkSend(studentSendMap);
	}

	public void okSend(String[] idArray) {
		// TODO Auto-generated method stub
		studentSendMapper.okSend(idArray);
	}

	public List<Map<String, Object>> findAllStudentSendSeviEd(BdStudentSendMap studentSendMap) {
		// TODO Auto-generated method stub
		// 到时候登录动态获取填入
		// BaseUser user = SessionUtil.getUser();
		// String userId = user.getUserId();
		// studentSendMap.setTutor(userId);
		return studentSendMapper.findAllStudentSendSeviEd(studentSendMap);
	}

	public List<Map<String, Object>> findAllStudentOrderBook(BdStudentSendMap studentSendMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> result = studentSendMapper.findAllStudentOrderBook(studentSendMap);
		return result;
	}

	public void okOrder(String[] idArray, String logisticsName) {
		// TODO Auto-generated method stub
		studentSendMapper.okOrder(idArray, logisticsName);
	}

	public void noOrder(String[] idArray) {
		// TODO Auto-generated method stub
		studentSendMapper.noOrder(idArray);
	}

	public void okReceive(String[] idArray) {
		// TODO Auto-generated method stub
		studentSendMapper.okReceive(idArray);
	}

	public void passEducation(String[] idArray) {
		// TODO Auto-generated method stub
		// 如果是辅导教材地址审核,增加地址审核的变更记录20180316版本修改
		if (idArray != null && idArray.length > 0) {
			for (String str : idArray) {
				BdStudentSend stuSend = studentSendMapper.selectByPrimaryKey(str);
				if (null != stuSend && StringUtil.isEmpty(stuSend.getSemester())) {
					// 添加变更记录
					modifyService.addStudentModifyRecord(stuSend.getLearnId(), "辅导书地址审核通过");
				}
			}
		}
		studentSendMapper.passEducation(idArray);
	}

	public void rejectedEducation(String[] idArray, String reason, String type) {
		// 教务驳回,如果是辅导书给招生老师发送信息
		// 如果是学科教材直接给学员推送
		if (idArray.length > 0) {
			for (String str : idArray) {
				BdStudentSend sendInfo = studentSendMapper.selectByPrimaryKey(str);
				if (null != sendInfo) {
					if (sendInfo.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_XK)) { // 学科教材推送给学员
						String userId = oaTaskInfoMapper.getUserIdByLearnId(sendInfo.getLearnId());
						if (StringUtil.hasValue(userId)) {
							String openId = usInfoMapper.selectUserOpenId(userId);
							if (StringUtil.hasValue(openId)) {
								log.debug("教务驳回学科教材地址" + openId + "原因:" + reason);
								sendAddressRejectMsg(openId, reason, null, sendInfo.getTextbookType());
							}
						}
						studentSendMapper.resetAddressTask(sendInfo.getLearnId());
					} else { // 辅导教材推送给招生老师
						String userId = studentSendMapper.getTeacherUserIdByLearnId(sendInfo.getLearnId());
						if (StringUtil.hasValue(userId)) {
							String openId = usInfoMapper.selectUserOpenId(userId);
							if (StringUtil.hasValue(openId)) {
								log.debug("教务驳回辅导教材地址" + openId + "原因:" + reason);
								sendAddressRejectMsg(openId, reason,
										studentSendMapper.getStuNameByLearnId(sendInfo.getLearnId()),
										sendInfo.getTextbookType());
							}
						}
						// 同时给学员发信息
						String stuUserId = oaTaskInfoMapper.getUserIdByLearnId(sendInfo.getLearnId());
						if (StringUtil.hasValue(stuUserId)) {
							String openId = usInfoMapper.selectUserOpenId(userId);
							if (StringUtil.hasValue(openId)) {
								log.debug("教务驳回学科教材地址" + openId + "原因:" + reason);
								sendAddressRejectMsg(openId, reason, null, sendInfo.getTextbookType());
							}
						}
					}
				}
			}
		}

		studentSendMapper.rejectedEducation(idArray, reason, type, SessionUtil.getUser());
	}

	@SuppressWarnings("unchecked")
	public void importSendInfo(MultipartFile sendInfo, String isFd) {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdStudentSendImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentSendImport>();
		testExcelCofing.setSheetName("index").setType(BdStudentSendImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard")).addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("联系人", "userName"))
				.addTitle(new ExcelUtil.IExcelTitle("联系电话", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("省", "provinceCode"))
				.addTitle(new ExcelUtil.IExcelTitle("市", "cityCode"))
				.addTitle(new ExcelUtil.IExcelTitle("区县", "districtCode"))
				.addTitle(new ExcelUtil.IExcelTitle("详细地址", "address"))
				.addTitle(new ExcelUtil.IExcelTitle("订书批次", "batchId"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"));

		// 行数记录
		int index = 2;

		// 对文件进行分析转对象
		List<BdStudentSendImport> list;
		try {
			list = ExcelUtil.importWorkbook(sendInfo.getInputStream(), testExcelCofing, sendInfo.getOriginalFilename());

			if (null != list && list.size() > 0) {

				// 遍历插入
				for (BdStudentSendImport std : list) {

					String grade = dictExchangeUtil.getParamValue("grade", std.getGrade());

					String learnId = null;
					try {
						learnId = studentSendMapper.selectLearnId(std.getIdCard(), grade, std.getPfsnName(),
								std.getUnvsName());
					} catch (Exception e) {
						throw new CustomException("请检查学员" + std.getStdName() + ",查询两条报读记录");
					}

					if (!StringUtil.hasValue(learnId)) {
						throw new CustomException("请检查学员" + std.getStdName() + ",未查询到报读信息");
					}

					String provinceCode = std.getProvinceCode();
					if (StringUtil.hasValue(provinceCode)) {
						provinceCode = dictExchangeUtil.getAreaValue(provinceCode.trim());
					} else {
						provinceCode = "";
					}
					std.setProvinceCode(provinceCode);

					String cityCode = std.getCityCode();
					if (StringUtil.hasValue(cityCode)) {
						cityCode = dictExchangeUtil.getAreaValue(cityCode.trim());
					} else {
						cityCode = "";
					}
					std.setCityCode(cityCode);

					String districtCode = std.getDistrictCode();
					if (StringUtil.hasValue(districtCode)) {
						districtCode = dictExchangeUtil.getAreaValue(districtCode.trim());
					} else {
						districtCode = "";
					}
					std.setDistrictCode(districtCode);

					boolean isFdFlag = false;

					if ("1".equals(isFd)) {
						isFdFlag = true;
					}

					if (!isFdFlag) {
						if (!StringUtil.hasValue(std.getSemester())) {
							throw new CustomException("请检查学员" + std.getStdName() + ",学期不能为空");
						}
					}

					initBookSend(learnId, isFdFlag, std);

					index++;
				}

			}

		} catch (IOException e) {
			throw new CustomException("请检查第" + index + "行数据");
		}

	}

	public void initBookSend(String learnId, boolean isFD, BdStudentSendImport std) {

		BdStudentSend studentSend = new BdStudentSend();
		studentSend.setLearnId(learnId);
		studentSend.setTextbookType(isFD ? EducationConstants.TEXT_BOOK_TYPE_FD : EducationConstants.TEXT_BOOK_TYPE_XK);
		studentSend.setReceiveStatus(StudentConstants.RECEIVE_STATUS_RECEIVED);
		studentSend.setOrderBookStatus(OrderConstants.ORDER_BOOK_NO);
		studentSend.setProvinceCode(std.getProvinceCode());
		studentSend.setCityCode(std.getCityCode());
		studentSend.setAddress(std.getAddress());
		studentSend.setUserName(std.getUserName());
		studentSend.setMobile(std.getMobile());
		studentSend.setBatchId(std.getBatchId());
		studentSend.setDistrictCode(std.getDistrictCode());

		if (!isFD) {

			// 走班主任审核
			studentSend.setAddressStatus(CheckConstants.CHECK_TEACHER_1);

			// 插入对应学期发书记录
			studentSend.setSemester(std.getSemester());
			// 此处验证下是否已经存在发放记录,如果存在进行修改
			BdStudentSend stuSend = studentSendMapper.getRecordByLearnId(learnId, studentSend.getSemester());
			if (null != stuSend) {
				// 修改
				stuSend.setProvinceCode(std.getProvinceCode());
				stuSend.setCityCode(std.getCityCode());
				stuSend.setDistrictCode(std.getDistrictCode());
				stuSend.setMobile(std.getMobile());
				stuSend.setUserName(std.getUserName());
				stuSend.setAddress(std.getAddress());

				studentSendMapper.updateByPrimaryKeySelective(stuSend);
			} else {
				studentSend.setSendId(IDGenerator.generatorId());
				studentSendMapper.insertSelective(studentSend);

				studentSendMapper.insertBdTextBookSend(studentSend.getSendId(), learnId, std.getSemester());
			}
		} else {
			// 不走班主任审核
			studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_4);
			// 此处验证下是否已经存在发放记录,如果存在进行修改
			BdStudentSend stuSend = studentSendMapper.getRecordByLearnId(learnId, studentSend.getSemester());
			if (null != stuSend) {
				// 修改
				stuSend.setProvinceCode(std.getProvinceCode());
				stuSend.setCityCode(std.getCityCode());
				stuSend.setDistrictCode(std.getDistrictCode());
				stuSend.setMobile(std.getMobile());
				stuSend.setUserName(std.getUserName());
				stuSend.setAddress(std.getAddress());

				studentSendMapper.updateByPrimaryKeySelective(stuSend);
			} else {
				studentSendMapper.insertSelective(studentSend);
				String[] testSub = studentSendMapper.selectTestSubByLearnId(learnId);

				int subCount = studentSendMapper.selectTestBookCount(testSub);
				if (subCount > 0) {
					studentSendMapper.insertBdTextBookSendFD(studentSend.getSendId(), testSub);
				}
			}
		}
	}

	public void queryOkOrder(BdStudentSendMap studentSendMap) {
		studentSendMapper.queryOkOrder(studentSendMap);
	}

	@SuppressWarnings("unchecked")
	public void exportMatchBookInfo(BdStudentSendMap studentSendMap, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<PfsnMatchBookStatInfo> testExcelCofing = new ExcelUtil.IExcelConfig<PfsnMatchBookStatInfo>();
		testExcelCofing.setSheetName("index").setType(PfsnMatchBookStatInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semesterName"))
				.addTitle(new ExcelUtil.IExcelTitle("教材专业编码", "textbookPfsncode"))
				.addTitle(new ExcelUtil.IExcelTitle("应发数量", "totalNum"))
				.addTitle(new ExcelUtil.IExcelTitle("已发数量", "sendNum"))
				.addTitle(new ExcelUtil.IExcelTitle("寄书数量", "bookNum"))
				.addTitle(new ExcelUtil.IExcelTitle("教材", "bookText"));

		List<PfsnMatchBookStatInfo> list = studentSendMapper.exportMatchBookInfo(studentSendMap);

		for (PfsnMatchBookStatInfo book : list) {

			StringBuffer sb = new StringBuffer();

			List<Map<String, String>> books = book.getBookInfo();
			if (null != books && books.size() > 0) {
				book.setBookNum(books.size() + "本/套");
				for (Map<String, String> map : books) {
					sb.append(map.get("bookName")).append("\n");
				}
			}
			book.setBookText(sb.toString());
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=matchBookDetail.xls");
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

	public void queryRefreshTextbookData(BdStudentSendMap studentSendMap) {
		studentSendMap.setTextbookType("XK");
		String userId = SessionUtil.getUser().getUserId();
		List<Map<String, Object>> result = studentSendMapper.findAllStudentOrderBook(studentSendMap);
		if (null != result && result.size() > 0) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					refreshTextbookDataAsyn(result, userId);
					log.error("------------------刷新学员教材数据-----------");
				}
			});
		}
	}

	public void refreshTextbookDataAsyn(List<Map<String, Object>> result, String userId) {
		for (Map<String, Object> map : result) {
			String sendId = map.get("sendId").toString();
			String learnId = map.get("learnId").toString();
			String semester = map.get("semester").toString();
			String textbookType = map.get("textbookType").toString();

			// 先验证当前有没有教材，有的话屏蔽
			int count = studentSendMapper.selectSendBookNum(sendId);
			// 如果存在,删除刷新
			if (count > 0) {
				log.debug("--------用户" + userId + "执行刷新教材操作---sendId:" + sendId + "--原教材数量:" + count);
				studentSendMapper.deleteTextBookSend(sendId);
			}
			if (textbookType.equals(EducationConstants.TEXT_BOOK_TYPE_FD)) {
				// 辅导教材
				String[] testSub = studentSendMapper.selectTestSubByLearnId(learnId);

				int subCount = studentSendMapper.selectTestBookCount(testSub);
				if (subCount > 0) {
					studentSendMapper.insertBdTextBookSendFD(sendId, testSub);
				}
			} else { // 学科教材
				studentSendMapper.insertBdTextBookSend(sendId, learnId, semester);
			}

		}
	}

	public void selectRefreshTextbookData(String[] idArray) {
		List<BdStudentSend> list = studentSendMapper.selectRefreshTextbookData(idArray);
		if (null != list && list.size() > 0) {
			for (BdStudentSend sendInfo : list) {
				// 先验证当前有没有教材，有的话屏蔽
				int count = studentSendMapper.selectSendBookNum(sendInfo.getSendId());
				if (count > 0) {
					log.debug("--------用户" + SessionUtil.getUser().getUserId() + "执行刷新教材操作---sendId:"
							+ sendInfo.getSendId() + "--原教材数量:" + count);
					studentSendMapper.deleteTextBookSend(sendInfo.getSendId());
				}
				if (sendInfo.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_FD)) {
					// 辅导教材
					String[] testSub = studentSendMapper.selectTestSubByLearnId(sendInfo.getLearnId());

					int subCount = studentSendMapper.selectTestBookCount(testSub);
					if (subCount > 0) {
						studentSendMapper.insertBdTextBookSendFD(sendInfo.getSendId(), testSub);
					}
				} else { // 学科教材
					studentSendMapper.insertBdTextBookSend(sendInfo.getSendId(), sendInfo.getLearnId(),
							sendInfo.getSemester());
				}
			}
		}
	}

	public void queryUpdateBatch(BdStudentSendMap studentSendMap) {
		String userId = SessionUtil.getUser().getUserId();
		List<Map<String, Object>> result = studentSendMapper.findAllStudentOrderBook(studentSendMap);
		if (null != result && result.size() > 0) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					updateBatchIdAsyn(result, studentSendMap.getUpdateBatchId());
					log.error(userId + "------------------刷新发教材批次-----------" + studentSendMap.getUpdateBatchId());
				}
			});
		}
	}

	public void updateBatchIdAsyn(List<Map<String, Object>> result, String batchId) {
		for (Map<String, Object> map : result) {
			String sendId = map.get("sendId").toString();
			BdStudentSend record = new BdStudentSend();
			record.setSendId(sendId);
			if (StringUtil.hasValue(batchId)) {
				record.setBatchId(batchId);
				record.setOrderBookStatus("2");
			}
			studentSendMapper.updateByPrimaryKeySelective(record);
		}
	}

	public void selectUpdateBatchId(String[] idArray, String batchId) {
		List<BdStudentSend> list = studentSendMapper.selectRefreshTextbookData(idArray);

		if (null != list && list.size() > 0) {
			for (BdStudentSend sendInfo : list) {
				BdStudentSend record = new BdStudentSend();
				record.setSendId(sendInfo.getSendId());
				record.setBatchId(batchId);
				record.setOrderBookStatus("2");
				studentSendMapper.updateByPrimaryKeySelective(record);
			}
		}
		log.error(SessionUtil.getUser().getUserId() + "------------------刷新发教材批次-----------" + batchId);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void importStuOrderBookInfo(MultipartFile stuOrderBookImport) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdStudentSendImport> testExcelCofing = new ExcelUtil.IExcelConfig<BdStudentSendImport>();
		testExcelCofing.setSheetName("index").setType(BdStudentSendImport.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName")).addTitle(new ExcelUtil.IExcelTitle("年度", "year"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"))
				.addTitle(new ExcelUtil.IExcelTitle("订书批次", "batchId"))
				.addTitle(new ExcelUtil.IExcelTitle("快递公司", "logisticsName"));

		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<BdStudentSendImport> list = ExcelUtil.importWorkbook(stuOrderBookImport.getInputStream(),
					testExcelCofing, stuOrderBookImport.getOriginalFilename());
			// 遍历插入
			for (BdStudentSendImport sendImport : list) {
				if (!StringUtil.hasValue(sendImport.getGrade())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！年级不能为空");
				}
				if (!StringUtil.hasValue(sendImport.getSemester())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！学期不能为空");
				}
				if (!StringUtil.hasValue(sendImport.getIdCard())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！身份证号不能为空");
				}
				if (!StringUtil.hasValue(sendImport.getBatchId())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！订书批次不能为空");
				}
				if (!StringUtil.hasValue(sendImport.getLogisticsName())) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！快递公司不能为空");
				}

				if (sendImport.getLogisticsName().equals("京东")) {
					sendImport.setLogisticsName("jd");
				} else if (sendImport.getLogisticsName().equals("顺丰")) {
					sendImport.setLogisticsName("sf");
				} else {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！快递公司格式错误，请输入[京东,顺丰]其中一个");
				}
				index++;

			}
			List<Map<String, Object>> resultList = studentSendMapper.getNonExistsStudent(list);
			if (resultList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("表格信息找不到对应学员信息：<br/>");
				for (Map<String, Object> map : resultList) {
					sb.append(map.get("grade") + "-" + map.get("std_name") + "-" + map.get("id_card").toString()
							+ "<br/>");
				}
				throw new IllegalArgumentException(sb.toString());
			}
			if (null != list && list.size() > 0) {
				if (list.size() > 10000) {
					throw new BusinessException("E000107"); // 目标年级已存在报读信息
				}
				orderBookAysnImportService.importStuOrderBookDataAsyn(list, SessionUtil.getUser());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

	public void addRemark(String sendId, String remark) {
		studentSendMapper.addRemark(sendId, remark);
	}

	public void updateAddress(String sendId, String provinceCode, String cityCode, String districtCode,
			String detailAddress, String takeUserName, String takeMobile) {
		studentSendMapper.updateAddress(sendId, provinceCode, cityCode, districtCode, detailAddress, takeUserName,
				takeMobile);
	}

	/**
	 * 教材驳回发送微信信息
	 * 
	 * @param openId
	 * @param reason
	 * @param stdName
	 * @param bookType
	 */
	public void sendAddressRejectMsg(String openId, String reason, String stdName, String bookType) {

		if (StringUtil.hasValue(stdName)) {// 辅导教材的驳回
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK_FAILED);
			msgVo.addData("now", DateUtil.getNowDateAndTime());

			msgVo.addData("firstWord", "您的学员:【" + stdName + "】辅导书教材收货地址有误，驳回原因:" + reason);
			msgVo.addData("remark", "请及时修改以免耽误教材邮寄，请进入学员系统后修改提交！");
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		} else {
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TASK_FAILED);
			msgVo.addData("now", DateUtil.getNowDateAndTime());
			String sendMsg = "您的学科教材邮寄地址有误，驳回原因:";
			if (bookType.equals(EducationConstants.TEXT_BOOK_TYPE_FD)) {
				sendMsg = "您的辅导教材邮寄地址有误，驳回原因:";
			}
			msgVo.addData("firstWord", sendMsg + reason);
			msgVo.addData("remark", "请及时修改以免耽误教材邮寄，请点击进入任务修改地址并提交！");

			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}

	}
}
