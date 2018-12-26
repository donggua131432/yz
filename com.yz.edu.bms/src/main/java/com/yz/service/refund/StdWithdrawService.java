package com.yz.service.refund;

import java.io.IOException;
import java.util.HashMap;
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

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.api.AtsAccountApi; 

import com.yz.constants.FinanceConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.refund.BdStudentWithdrawMapper;
import com.yz.dao.refund.UsInfoMapper;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.refund.BdStudentWithdraw;
import com.yz.model.refund.BdWithdrawQuery;
import com.yz.model.refund.BdWithdrawResponse;
import com.yz.model.system.SysDict;
import com.yz.redis.RedisService;
import com.yz.service.system.SysDictService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class StdWithdrawService {

	private static final Logger log = LoggerFactory.getLogger(StdWithdrawService.class);
	@Autowired
	private BdStudentWithdrawMapper withdrawMapper;

	@Autowired
	private UsInfoMapper userMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atsApi;

	@Autowired
	private SysDictService dictService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public IPageInfo<BdWithdrawResponse> selectWithdrawByPage(int start, int length, BdWithdrawQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdWithdrawResponse> list = withdrawMapper.selectWithdrawByPage(query);
		return new IPageInfo<BdWithdrawResponse>((Page<BdWithdrawResponse>) list);
	}

	public BdWithdrawResponse selectWithdraw(String withdrawId) {
		return withdrawMapper.selectWithdrawById(withdrawId);
	}

	/**
	 * 审核通过
	 * 
	 * @param withdrawId
	 */
	public void checkPass(String withdrawId, String remark) {

		BdStudentWithdraw draw = new BdStudentWithdraw();
		draw.setWithdrawId(withdrawId);

		BaseUser user = SessionUtil.getUser();
		draw.setUpdateUserId(user.getUserId());
		draw.setUpdateUser(user.getRealName());
		draw.setEmpId(user.getEmpId());

		BdStudentWithdraw drawInfo = withdrawMapper.selectByPrimaryKey(withdrawId);

		if (TransferConstants.CHECK_RECORD_STATUS_ALLOW.equals(drawInfo.getCheckStatus())) {
			return;
			// throw new BusinessException("E000071"); //此申请已成功提现
		}

		/*
		 * // 用户id String userId =
		 * withdrawMapper.selectUserIdByStdId(drawInfo.getStdId()); // 用户OPENID
		 * String openId = userMapper.selectUserOpenId(userId);
		 * 
		 * Map<String, String> postData = new HashMap<String, String>();
		 * 
		 * // 发送现金红包 postData.put("orderAmount", drawInfo.getAmount());
		 * postData.put("billNo", withdrawId); postData.put("openId", openId);
		 * 
		 * Map<String, String> result = new HashMap<String, String>(); result =
		 * payApi.sendRedPacket(postData);
		 * 
		 * if (null == result) { draw.setRemark("提现失败，微信无响应"); } if
		 * (GlobalConstants.SUCCESS.equals(result.get("resCode"))) {
		 * draw.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW); }
		 * if (GlobalConstants.FAILED.equals(result.get("resCode"))) {
		 * draw.setRemark(result.get("errorMsg")); }
		 */
		draw.setRemark(remark);
		draw.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_ALLOW);
		withdrawMapper.updateByPrimaryKeySelective(draw);

	}

	@SuppressWarnings("unchecked")
	public void reject(String withdrawId, String remark, String rejectReason) {
		BdStudentWithdraw draw = new BdStudentWithdraw();
		draw.setWithdrawId(withdrawId);

		BaseUser user = SessionUtil.getUser();
		draw.setUpdateUserId(user.getUserId());
		draw.setUpdateUser(user.getRealName());
		draw.setEmpId(user.getEmpId());

		BdStudentWithdraw drawInfo = withdrawMapper.selectByPrimaryKey(withdrawId);
		if (TransferConstants.CHECK_RECORD_STATUS_REFUND.equals(drawInfo.getCheckStatus())) {
			return;
		}
		draw.setRemark(remark);
		draw.setCheckStatus(TransferConstants.CHECK_RECORD_STATUS_REFUND);
		draw.setRejectReason(rejectReason);
		withdrawMapper.updateByPrimaryKeySelective(draw);
		// TODO 推送失败消息提醒学员重新申请提现
		String userId = withdrawMapper.selectUserIdByWithId(withdrawId);
		String openId = userMapper.selectUserOpenId(userId);
		
		//推送微信公众号信息
		SysDict dict = dictService.getDict("rejectReason."+rejectReason);
		String reason = "信息错误!";
		if(null != dict){
			reason = dict.getDictName();
		}
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTouser(openId);
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_WITHDRAW_REJECT);
		msgVo.addData("reason", reason);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));

		// 设置转账对象
		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_NORMAL);
		body.put("stdId", drawInfo.getStdId());
		body.put("amount", drawInfo.getAmount());
		body.put("action", FinanceConstants.ACC_ACTION_IN);
		body.put("excDesc", "学员提现驳回退款");
		body.put("mappingId", drawInfo.getWithdrawId());
		atsApi.trans(body);

	}

	/**
	 * 批量发红包
	 * 
	 * @param withdrawIds
	 */
	public void checkPassBatch(String[] withdrawIds,String remark) {
		for (String withdrawId : withdrawIds) {
			checkPass(withdrawId, remark);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void exportWithdraw(BdWithdrawQuery query, HttpServletResponse response){
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdWithdrawResponse> testExcelCofing = new ExcelUtil.IExcelConfig<BdWithdrawResponse>();
		testExcelCofing.setSheetName("index").setType(BdWithdrawResponse.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("手机号", "mobile"))
				.addTitle(new ExcelUtil.IExcelTitle("年级院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("银行类型", "bankType"))
				.addTitle(new ExcelUtil.IExcelTitle("银行分行", "bankOpen"))
				.addTitle(new ExcelUtil.IExcelTitle("银行卡号", "bankCard"))
				.addTitle(new ExcelUtil.IExcelTitle("退款金额", "amount"))
				.addTitle(new ExcelUtil.IExcelTitle("申请时间", "applyTime"))
				.addTitle(new ExcelUtil.IExcelTitle("处理状态", "checkStatus"));
		
		List<BdWithdrawResponse> withdrawList = withdrawMapper.selectWithdrawByExport(query);
		if(withdrawList!=null&&withdrawList.size()>0) {
			for (BdWithdrawResponse withdraw : withdrawList) {
				List<Map<String, String>> learnInfoList=withdrawMapper.selectUnvsInfoByStdId(withdraw.getStdId());
				if(learnInfoList!=null&&learnInfoList.size()>0) {
					StringBuffer tempsb=new StringBuffer();
					for (Map<String, String> map : learnInfoList) {
						tempsb.append("["+dictExchangeUtil.getParamKey("grade",map.get("grade"))+"]"+"-"+map.get("unvsName")+"["+ dictExchangeUtil.getParamKey("stdStage", map.get("stdStage"))+"]\r\n");
					}
					if(StringUtil.hasValue(tempsb.toString())) {
						withdraw.setUnvsName(StringUtil.substringBeforeLast(tempsb.toString(), "\r\n"));
						
					}
				}
			}
		}
		
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(withdrawList, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=withdrawList.xls");
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

