package com.yz.service.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;

import com.yz.constants.MessageConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.message.GwMsgTemplateMapper;
import com.yz.dao.message.GwReceiverMapper;
import com.yz.dao.message.GwSendRecordsMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.MsgPubVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwMessageQuery;
import com.yz.model.message.GwMsgStudentQuery;
import com.yz.model.message.GwMsgTemplate;
import com.yz.model.message.GwReceiver;
import com.yz.model.message.GwRecordExport;
import com.yz.model.message.GwRecordQuery;
import com.yz.model.message.GwStdInfo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class MsgPublishService {

	private final static Logger log = LoggerFactory.getLogger(MsgPublishService.class);

	@Autowired
	private GwMsgTemplateMapper msgMapper;

	@Autowired
	private GwReceiverMapper recMapper;

	@Autowired
	private BdStdEnrollMapper stdMapper;

	@Autowired
	private GwSendRecordsMapper recordMapper;

	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@Autowired
	private UsInfoMapper usInfoMapper;

	public Object getMsgByPage(int start, int length, GwMessageQuery msg) {
		PageHelper.offsetPage(start, length);
		List<GwMsgTemplate> msgs = msgMapper.selectMsgByPage(msg);
		return new IPageInfo<GwMsgTemplate>((Page<GwMsgTemplate>) msgs);
	}

	public Object getMsgRecordByPage(int start, int length, GwRecordQuery record) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = recordMapper.selectRecordByPage(record);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public Object getMsgStdByPage(int start, int length, String mtpId) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> learnInfos = msgMapper.selectMsgReceiver(mtpId);
		if (null != learnInfos && learnInfos.size() > 0) {
			for (Map<String, String> map : learnInfos) {

				Map<String, String> res = stdMapper.selectStdLearnInfo(map.get("learnId"));
				if (null != res) {

					map.put("stdId", res.get("stdId"));
					map.put("learnId", res.get("learnId"));
					map.put("stdStage", res.get("stdStage"));
					map.put("stdName", res.get("stdName"));
					map.put("unvsName", res.get("unvsName"));
					map.put("pfsnCode", res.get("pfsnCode"));
					map.put("pfsnName", res.get("pfsnName"));
					map.put("pfsnLevel", res.get("pfsnLevel"));
					map.put("recruitType", res.get("recruitType"));
					map.put("grade", res.get("grade"));
				}
			}

		}
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) learnInfos);
	}

	@SuppressWarnings("unused")
	public void insertMsgPublish(GwMsgTemplate msg) {
		Date date = DateUtil.convertDateStrToDate(msg.getSendTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String year = calendar.get(Calendar.YEAR) + "";
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";

		String cron = null;// CronExpressionUtil.pointFormat(year, month, day,
							// String.valueOf(date.getHours()),String.valueOf(date.getMinutes()),
							// String.valueOf(date.getMinutes()));
		msg.setCronStr(cron);

		if (MessageConstants.MESSAGE_CHANNEL_WECHAT.equals(msg.getMsgChannel())) {
			if (MessageConstants.MESSAGE_TYPE_STUDENT.equals(msg.getMtpType())) {
				String templateId = msgMapper.selectWechatTemplate(MessageConstants.MESSAGE_TYPE_STUDENT);
				msg.setOtpId(templateId);
			}
		} else if (MessageConstants.MESSAGE_CHANNEL_MSG.equals(msg.getMsgChannel())) {
			// TODO 短信模板
		} else if (MessageConstants.MESSAGE_CHANNEL_DINGDING.equals(msg.getMsgChannel())) {
			// TODO 钉钉模板
		} else if (MessageConstants.MESSAGE_CHANNEL_EMAIL.equals(msg.getMsgChannel())) {
			// TODO 邮箱模板
		}
		msg.setMtpId(IDGenerator.generatorId());
		msgMapper.insertSelective(msg);

	}

	@SuppressWarnings("unused")
	public void updateMsgPublish(GwMsgTemplate msg) {
		Date date = DateUtil.convertDateStrToDate(msg.getSendTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String year = calendar.get(Calendar.YEAR) + "";
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";

		String cron = null;// CronExpressionUtil.pointFormat(year, month, day,
							// String.valueOf(date.getHours()),String.valueOf(date.getMinutes()),
							// String.valueOf(date.getMinutes()));
		msg.setCronStr(cron);
		msg.setMtpStatus(MessageConstants.MESSAGE_STATUS_UNSUBMIT);
		msgMapper.updateByPrimaryKeySelective(msg);
	}

	public void deleteMsg(String mtpId) {
		msgMapper.deleteByPrimaryKey(mtpId);
		msgMapper.deleteMsgReceiver(mtpId);
	}

	public void submitMsg(String mtpId) {
		msgMapper.updateMtpStatus(mtpId, MessageConstants.MESSAGE_STATUS_UNCHECK, null);
	}

	public void submitMsgs(String[] mtpIds) {
		msgMapper.updateMtpStatusBatch(mtpIds, MessageConstants.MESSAGE_STATUS_UNCHECK);
	}

	public void rejectMsg(String mtpId, String remark) {
		msgMapper.updateMtpStatus(mtpId, MessageConstants.MESSAGE_STATUS_REJECT, remark);
	}

	public void checkMsg(String mtpId) {
		GwMsgTemplate msgUpdate = new GwMsgTemplate();
		BaseUser user = SessionUtil.getUser();

		msgUpdate.setMtpId(mtpId);
		msgUpdate.setCheckUser(user.getRealName());
		msgUpdate.setCheckUserId(user.getUserId());
		msgUpdate.setCheckTime(DateUtil.getNowDateAndTime());
		msgMapper.updateMtpStatus(mtpId, MessageConstants.MESSAGE_STATUS_UNSEND, null);
		msgMapper.updateByPrimaryKeySelective(msgUpdate);

		// 发微信公众号消息推送
		GwMsgTemplate msg = msgMapper.selectByPrimaryKey(mtpId);
		if (null != msg) {
			MsgPubVo pubVo = new MsgPubVo();
			pubVo.setMsgId(mtpId);
			pubVo.setMsgTitle(msg.getMsgTitle());
			pubVo.setMsgCode(msg.getMsgCode());
			pubVo.setMsgContent(msg.getMsgContent());
			pubVo.setMsgName(msg.getMsgName());
			pubVo.setUrl(msg.getSkipUrl());
			pubVo.setExt1(msg.getMsgChannel());
			pubVo.setMsgType(WechatMsgConstants.PUB_MSG_TYPE_NOTICE);
			RedisService.getRedisService().zadd(YzTaskConstants.YZ_MSG_PUB_TASK_SET,
					DateUtil.stringToLong(msg.getSendTime(), DateUtil.YYYYMMDDHHMMSS_SPLIT),
					JsonUtil.object2String(pubVo));
		} else {
			log.info("not template.............");
		}
	}

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	public void addReceiver(String[] learnIds, String mtpId) {
		for (String learnId : learnIds) {
			int count = recMapper.selectCountByLearnId(learnId, mtpId);
			if (count > 0) {
				continue;
			}
			GwReceiver std = stdMapper.selectStdInfoByLearnId(learnId);
			String openId = usInfoMapper.selectUserOpenId(std.getUserId());
			std.setOpenId(openId);
			List<String> receiverId = recMapper.selectByLearnId(std.getLearnId());

			if (null != receiverId && receiverId.size() == 1) { // 接收人已存在，更新信息
				std.setReceiverId(receiverId.get(0));
				recMapper.updateByPrimaryKey(std);
			} else {
				for (String id : receiverId) {
					recMapper.deleteByPrimaryKey(id);
				}
				std.setReceiverId(IDGenerator.generatorId());
				recMapper.insertSelective(std);
			}

			String receiveMsgId = recMapper.selectReceiveMsgId(std.getReceiverId(), mtpId);

			if (!StringUtil.hasValue(receiveMsgId)) {
				recMapper.insertMsgReceiver(std.getReceiverId(), mtpId);
			}
		}
	}

	public void delMtpReceiver(String[] learnIds, String mtpId) {
		recMapper.deleteMtpReceiverByLearnId(learnIds, mtpId);
	}

	@SuppressWarnings("unchecked")
	public void importStudentAsyn(MultipartFile excelRegist, String mtpId) {
		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<GwReceiver> testExcelCofing = new ExcelUtil.IExcelConfig<GwReceiver>();
		testExcelCofing.setSheetName("index").setType(GwReceiver.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"));

		// 行数记录
		int index = 2;
		try {
			// 对文件进行分析转对象
			List<GwReceiver> list = ExcelUtil.importWorkbook(excelRegist.getInputStream(), testExcelCofing,
					excelRegist.getOriginalFilename());
			if (null != list && list.size() > 0) {

				// 遍历插入
				for (GwReceiver std : list) {
					try {
						log.debug("---------------------------- 学员信息：" + JsonUtil.object2String(std));
						if (null != std) {
							std.setGrade(dictExchangeUtil.getParamValue("grade", std.getGrade()));
						} else {
							log.debug("---------------------------- 学员信息为空:" + JsonUtil.object2String(std));
							continue;
						}
						std = stdMapper.selectStdInfo(std);
					} catch (Exception e) {
						log.error("--------------------请检查学员：" + std.getStdName() + ",有多条学业,第" + index + "行");
					}
					if (null == std) {
						log.error("请检查第" + index + "行,未查询报读信息");
					} else {
						String openId = usInfoMapper.selectUserOpenId(std.getUserId());
						std.setOpenId(openId);
						List<String> receiverId = recMapper.selectByLearnId(std.getLearnId());

						if (null != receiverId && receiverId.size() == 1) { // 接收人已存在，更新信息
							std.setReceiverId(receiverId.get(0));
							recMapper.updateByPrimaryKey(std);
						} else {
							for (String id : receiverId) {
								recMapper.deleteByPrimaryKey(id);
							}
							std.setReceiverId(IDGenerator.generatorId());
							recMapper.insertSelective(std);
						}

						String receiveMsgId = recMapper.selectReceiveMsgId(std.getReceiverId(), mtpId);

						if (!StringUtil.hasValue(receiveMsgId)) {
							recMapper.insertMsgReceiver(std.getReceiverId(), mtpId);
						}
					}

					index++;
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}
	}

	public void importStudent(MultipartFile excelRegist, String mtpId) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				importStudentAsyn(excelRegist, mtpId);
				log.error("----------------------------------- 消息mtpId:" + mtpId + "，学员导入成功");
			}
		});
	}

	public Object selectMtpById(String mtpId) {
		return msgMapper.selectByPrimaryKey(mtpId);
	}

	public Object queryMsgStudentInfo(int start, int length, GwMsgStudentQuery studentQuery) {

		if (null == studentQuery.getStdStages() || studentQuery.getStdStages().length <= 0) {
			studentQuery.setStdStages(null);
		}
		BaseUser user = SessionUtil.getUser();

		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if (jtList.contains("XJZZ") || jtList.contains("BMZR") || jtList.contains("CJXJ")
					|| jtList.contains("GKXJ")) { // 学籍组长
				user.setUserLevel("11");
			} else if (jtList.contains("FDY") || jtList.contains("CJFDY") || jtList.contains("GKFDY")) { // 辅导员
				user.setUserLevel("10");
			}
		}
		/*
		 * if(StringUtil.hasValue(studentQuery.getIsChecked())){ String[]
		 * learnIds = msgMapper.selectLearnIdsByMtpId(studentQuery.getMtpId());
		 * studentQuery.setLearnIds(learnIds); }
		 */
		PageHelper.offsetPage(start, length);
		List<GwStdInfo> stdInfos = stdMapper.queryMsgStudentInfo(studentQuery, user);
		for (GwStdInfo std : stdInfos) {
			ArrayList<String> mtpIds = msgMapper.selectMtpIdByLearnId(std.getLearnId());
			if (mtpIds.contains(studentQuery.getMtpId())) {
				std.setIsChecked(MessageConstants.MESSAGE_STD_CHECKED);
			} else {
				std.setIsChecked(MessageConstants.MESSAGE_STD_UNCHECK);
			}
		}
		return new IPageInfo<GwStdInfo>((Page<GwStdInfo>) stdInfos);
	}

	public void addAllReceiver(GwMsgStudentQuery studentQuery, String mtpId) {
		if (null == studentQuery.getStdStages() || studentQuery.getStdStages().length <= 0) {
			studentQuery.setStdStages(null);
		}
		BaseUser user = SessionUtil.getUser();

		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if (jtList.contains("XJZZ") || jtList.contains("BMZR") || jtList.contains("CJXJ")
					|| jtList.contains("GKXJ")) { // 学籍组长
				user.setUserLevel("11");
			} else if (jtList.contains("FDY") || jtList.contains("CJFDY") || jtList.contains("GKFDY")) { // 辅导员
				user.setUserLevel("10");
			}
		}
		// 查询学业id数组
		String[] learnIds = stdMapper.queryMsgLearnIds(studentQuery, user);
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// 插入学员
				addReceiver(learnIds, mtpId);
				log.error("----------------------------------- 消息mtpId:" + mtpId + "，学员导入成功");
			}
		});
	}

	public void delAllReceiver(GwMsgStudentQuery studentQuery, String mtpId) {
		if (null == studentQuery.getStdStages() || studentQuery.getStdStages().length <= 0) {
			studentQuery.setStdStages(null);
		}
		BaseUser user = SessionUtil.getUser();

		List<String> jtList = user.getJtList();
		if (jtList != null) {
			if (jtList.contains("XJZZ") || jtList.contains("BMZR") || jtList.contains("CJXJ")
					|| jtList.contains("GKXJ")) { // 学籍组长
				user.setUserLevel("11");
			} else if (jtList.contains("FDY") || jtList.contains("CJFDY") || jtList.contains("GKFDY")) { // 辅导员
				user.setUserLevel("10");
			}
		}
		// 查询学业id数组
		String[] learnIds = stdMapper.queryMsgLearnIds(studentQuery, user);
		executor.execute(new Runnable() {

			@Override
			public void run() {
				delMtpReceiver(learnIds, mtpId);
				// 插入学员
				log.error("----------------------------------- 消息mtpId:" + mtpId + "，学员删除成功");
			}
		});
	}
	/**
	 * 导出
	 * @param record
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void getMsgRecordForExport(GwRecordQuery record, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<GwRecordExport> testExcelCofing = new ExcelUtil.IExcelConfig<GwRecordExport>();
		testExcelCofing.setSheetName("index").setType(GwRecordExport.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("手机号", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("发送方式", "msgChannel"))
				.addTitle(new ExcelUtil.IExcelTitle("发送状态", "sendStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("发送时间", "sendTime"))
				.addTitle(new ExcelUtil.IExcelTitle("班主任", "tutorName"));

		List<GwRecordExport> list = recordMapper.selectRecordForExport(record);

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=msgSendRecord.xls");
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
