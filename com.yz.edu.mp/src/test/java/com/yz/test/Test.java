package com.yz.test;

import java.util.Date;

import com.yz.constants.StudentConstants;
import com.yz.constants.UsConstants;
import com.yz.flow.charge.ChargeFlow;
import com.yz.flow.register.RegisterFlow;
import com.yz.flow.stage.StageFlow;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

public class Test {

	public static void main(String[] args) {
//		testChargeFlow();
//		testRegisterFlow();
		testStageFlow();
	}

	private static void testStageFlow() {
		MpCondition condition = getChargeCondition1();

		StageFlow f = new StageFlow();
		f.setFlowId(StringUtil.randomNumber(12));

		MpResult result = f.match(condition);

		if (result.hasValue()) {
			System.out.println(JsonUtil.object2String(result.getTarget()));
		} else {
			System.err.println("没有返回值");
		}
	}

	private static void testRegisterFlow() {
		MpCondition condition = getChargeCondition1();

		RegisterFlow f = new RegisterFlow();
		f.setFlowId(StringUtil.randomNumber(12));

		MpResult result = f.match(condition);

		if (result.hasValue()) {
			System.out.println(JsonUtil.object2String(result.getTarget()));
		} else {
			System.err.println("没有返回值");
		}
	}

	private static void testChargeFlow() {

		MpCondition condition = getChargeCondition1();

		ChargeFlow cf = new ChargeFlow();
		cf.setFlowId(StringUtil.randomNumber(12));

		MpResult result = cf.match(condition);

		if (result.hasValue()) {
			System.out.println(JsonUtil.object2String(result.getTarget()));
		} else {
			System.err.println("没有返回值");
		}
	}

	private static MpCondition getChargeCondition() {

		Date createTime = DateUtil.convertDateStrToDate("20160501", "yyyyMMdd");

		MpCondition c = new MpCondition();

		c.put("createTime", createTime);
		c.put("itemYear", 1);
		c.put("pUserType", UsConstants.USER_TYPE_MB);
		c.put("userId", "12");
		c.put("pId", "34");
		c.put("payable", "5600");
		return c;
	}

	private static MpCondition getChargeCondition1() {

		Date createTime = DateUtil.convertDateStrToDate("20160817120001", "yyyyMMddHHmmss");

		MpCondition c = new MpCondition();

		c.put("createTime", createTime);
		c.put("itemYear", 1);
		c.put("pUserType", UsConstants.USER_TYPE_MB);
		c.put("userId", "12");
		c.put("pId", "34");
		c.put("payable", "5600");
		c.put("itemCode", "Y1");
		c.put("recruitType", StudentConstants.RECRUIT_TYPE_GK);
		c.put("scholarship", StudentConstants.SCHOLARSHIP_DONGGUAN);
		c.put("stdStage", StudentConstants.STD_STAGE_STUDYING);
		return c;
	}
}
