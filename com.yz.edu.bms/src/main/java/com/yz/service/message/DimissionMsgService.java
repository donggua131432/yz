package com.yz.service.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper; 

import com.yz.constants.EmpConstants;
import com.yz.constants.GlobalConstants;
import com.yz.constants.MessageConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.WechatSendServiceMsgUtil;
import com.yz.dao.message.GwDimissionMsgMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.SendSmsVo;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.message.GwDimissionMsg;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;

@Service
@Transactional
public class DimissionMsgService {

	private static final ExecutorService executor = Executors.newCachedThreadPool();

	@Autowired
	private GwDimissionMsgMapper msgMapper;

	@Autowired
	private OaEmployeeMapper empMapper;

	@Autowired
	private UsFollowMapper usMapper;

	/**
	 * 消息记录分页
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public Object getDimissionMsgByPage(int start, int length, GwDimissionMsg query) {
		PageHelper.offsetPage(start, length);
		List<GwDimissionMsg> list = msgMapper.selectDimissionMsgList(query);
		return new IPageInfo<GwDimissionMsg>((Page<GwDimissionMsg>) list);
	}

	/**
	 * 获取离职员工列表
	 * 
	 * @param page
	 * @param rows
	 * @param sName
	 * @return
	 */
	public Object getDimissionEmpByPage(int page, int rows, String sName) {
		PageHelper.startPage(page, rows);
		// 离职职员
		List<Map<String, String>> list = empMapper.selectEmpInfo(sName, EmpConstants.EMP_STATUS_DIMISSION);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	/**
	 * 获取员工名下的学员
	 * 
	 * @param empId
	 * @return
	 */
	public Object getEmpStudents(String empId) {
		List<Map<String, String>> list = empMapper.selectEmpStudents(empId);
		return list;
	}

	/**
	 * 发送离职信息
	 * 
	 * @param empId
	 * @param msgType
	 */
	public void sendDimissionMsg(String empId, String msgType) {

		OaEmployeeBaseInfo emp = empMapper.getEmpBaseInfo(empId);
		if (!EmpConstants.EMP_STATUS_DIMISSION.equals(emp.getEmpStatus())) {
			throw new BusinessException("E000120"); // 非离职员工
		}

		// 学员列表
		List<Map<String, String>> list = empMapper.selectEmpStudents(empId);

		if (list.isEmpty()) {
			return;
		}

		// 防重
		int count = msgMapper.selectDimissionCount(empId, msgType);
		if (count > 0) {
			throw new BusinessException("E000121"); // 该员工离职信息已发送，请勿重复发送
		}

		BaseUser user = SessionUtil.getUser();

		StringBuffer sb = new StringBuffer();
		sb.append("【远智教育】原我校").append(emp.getEmpName()).append("已正式离职，该员工自离职之日起发生的所有业务均属于个人行为，同我校无关，咨询电话4008336013");

		// 发送信息
		sendMsg(list, emp, user, sb.toString(), msgType);

	}

	/**
	 * 异步执行发送信息
	 * 
	 * @param stds
	 * @param emp
	 * @param user
	 * @param content
	 */
	private void sendMsg(List<Map<String, String>> stds, OaEmployeeBaseInfo emp, BaseUser user, String content,String msgType) {
		if (MessageConstants.DIMISSION_MSG_TYPE_WECHAT.equals(msgType)) {
			sendWechatMsg(stds, emp, user, content);
		} else if (MessageConstants.DIMISSION_MSG_TYPE_MESSAGE.equals(msgType)) {
			sendSmsMsg(stds, emp, user);
		}
	}

	private void sendWechatMsg(List<Map<String, String>> stds, OaEmployeeBaseInfo emp, BaseUser user, String content) {
		for (Map<String, String> std : stds) {

			String openId = usMapper.selectOpenIdByStdId(std.get("stdId"));
			
			//推送客服微信公众号信息
			WechatSendServiceMsgUtil.wechatSendServiceMsg(openId, content);
/*
			boolean isSuccess = wechatApi.sendServiceMsg(openId, content);
*/
			GwDimissionMsg msg = new GwDimissionMsg();
			msg.setEmpId(emp.getEmpId());
			msg.setCreateUser(user.getRealName());
			msg.setCreateUserId(user.getUserId());
			msg.setEmpName(emp.getEmpName());
			msg.setIdCard(std.get("idCard"));
			msg.setMobile(std.get("mobile"));
			msg.setMsgType(MessageConstants.DIMISSION_MSG_TYPE_WECHAT);
			msg.setSendTime(DateUtil.getNowDateAndTime());

			msg.setStatus(GlobalConstants.STATUS_SUCCESS);
			
			msg.setStdId(std.get("stdId"));
			msg.setStdName(std.get("stdName"));
			msg.setMsgId(IDGenerator.generatorId());
			msgMapper.insertSelective(msg);

		}
	}

	private void sendSmsMsg(List<Map<String, String>> stds, OaEmployeeBaseInfo emp, BaseUser user) {
		SendSmsVo vo = new SendSmsVo();
		Map<String, String> msgInfo = new HashMap<String, String>();
		msgInfo.put("empName", emp.getEmpName());
		
		List<GwDimissionMsg> msgList = new ArrayList<>();
		for (Map<String, String> std : stds) {
			String mobile = std.get("mobile");
			vo.setMobiles(mobile);
	
			//boolean isSuccess = smsApi.sendMessage(msgInfo, GlobalConstants.TEMPLATE_MSG_DIMISSION, mobile);
            
			GwDimissionMsg msg = new GwDimissionMsg();
			msg.setEmpId(emp.getEmpId());
			msg.setCreateUser(user.getRealName());
			msg.setCreateUserId(user.getUserId());
			msg.setEmpName(emp.getEmpName());
			msg.setIdCard(std.get("idCard"));
			msg.setMobile(std.get("mobile"));
			msg.setMsgType(MessageConstants.DIMISSION_MSG_TYPE_MESSAGE);
			msg.setSendTime(DateUtil.getNowDateAndTime());

			msg.setStatus(GlobalConstants.STATUS_SUCCESS);
			
			msg.setStdId(std.get("stdId"));
			msg.setStdName(std.get("stdName"));
			msg.setMsgId(IDGenerator.generatorId());
			msgList.add(msg);
			
			
            
		}
		msgMapper.batchInsertSelective(msgList);
		
		vo.setContent(msgInfo);
		vo.setTemplateId(GlobalConstants.TEMPLATE_MSG_DIMISSION);
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_SMS_SEND_TASK, JsonUtil.object2String(vo));
	}
}
