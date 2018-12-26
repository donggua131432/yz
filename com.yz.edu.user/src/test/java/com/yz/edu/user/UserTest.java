package com.yz.edu.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yz.app.MyUserApplication;
import com.yz.model.payment.BdPayInfo;
import com.yz.service.BdsTuitionService;
import com.yz.util.JsonUtil;

/**
 * 规则匹配单元测试
 * 
 * @ClassName: RechargeTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月29日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyUserApplication.class)
public class UserTest {
	
	@Autowired
	private BdsTuitionService service;
	
	@Test
	public void test() {
		String jString="{\"accDeduction\":\"0\",\"coupons\":[],\"empId\":\"\",\"itemCodes\":[\"Y0\"],\"learnId\":\"25621424903291549\",\"openId\":\"ozbq0wTOaLMYPNU5v2JDIDLYWjYg\",\"outSerialNo\":\"\",\"paidAmount\":\"\",\"paymentType\":\"4\",\"remark\":\"\",\"tradeType\":\"JSAPI\",\"userId\":\"152757900748978218\",\"years\":[\"\"],\"zmDeduction\":\"0\"}";
		BdPayInfo payinfo=(BdPayInfo) JsonUtil.str2Object(jString);
		service.payTuition(payinfo, "152757900748978218");
	}
}
