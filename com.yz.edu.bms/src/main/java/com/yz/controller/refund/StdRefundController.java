package com.yz.controller.refund;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.refund.BdRefundQuery;
import com.yz.model.refund.BdStudentRefund;
import com.yz.service.refund.StdRefundService;
import com.yz.service.transfer.BdStudentOutService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/refund")
public class StdRefundController {
	private static final Logger log = LoggerFactory.getLogger(StdRefundController.class);

	@Autowired
	private StdRefundService refundService;

	@Autowired
	private BdStudentOutService studentOutService;

	@RequestMapping("/toList")
	@Rule("refund:query")
	public String toList(HttpServletRequest request) {
		return "refund/apply/refund-list";
	}

	@RequestMapping("/edit")
	@Rule("refund:insert")
	@Token(action = Flag.Save, groupId = "refund:insert")
	public String edit(HttpServletRequest request) {
		return "refund/apply/refund-edit";
	}

	@RequestMapping("/refundApply")
	@ResponseBody
	@Rule("refund:insert")
	@Token(action = Flag.Remove, groupId = "refund:insert")
	public Object addStudentOut(BdStudentRefund refund) {
		Assert.hasText(refund.getLearnId(), "学业ID不能为空");
		Assert.hasText(refund.getRefundAmount(), "退款金额能为空");
		refundService.addStudentRefund(refund);

		return "SUCCESS";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param studentOutMap
	 *            BdStudentOutMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/list")
	@ResponseBody
	@Rule("refund:query")
	public Object list(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdRefundQuery query) {
		Object o = refundService.selectRefundInfoByPage(start, length, query);
//		log.debug("---------------------审批：" + JsonUtil.object2String(o));
		return o;
	}

	/**
	 * Description: 根据名字,身份证,电话查询符合条件的学生信息
	 * 
	 * @param sName
	 *            名字,身份证,电话
	 * @return 返回IPageInfo
	 * @see com.yz.controller.transfer.BdStudentOutController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("refund:query")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		return refundService.findStudentInfo(rows, page, sName, sName, sName);
	}

	/**
	 * Description: 学业编号查询学生信息
	 * 
	 * @param learnId
	 *            学员id,learnId 学业编号
	 * @return 返回json
	 * 
	 */
	@RequestMapping("/findStudentInfoById")
	@ResponseBody
	@Rule("refund:query")
	public Object findStudentInfoById(String learnId) {
		return studentOutService.findStudentInfoById(learnId);
	}

	/**
	 * Description: 学员ID和年级查询学生信息
	 * 
	 * @param stdId
	 *            学员id,learnId 学业编号
	 * @return 返回json
	 * 
	 */
	@RequestMapping("/findStudentInfoByGraStdId")
	@ResponseBody
	@Rule("refund:query")
	public Object findStudentInfoByGraStdId(String learnId) {
		return refundService.findStudentInfoByGraStdId(learnId);
	}
	
	
	@RequestMapping("/cancelRefund")
	@ResponseBody
	@Rule("refund:delete")
	public Object cancelRefund(@RequestParam(name="refundIds[]")String[] refundIds){
		refundService.cancelRefunds(refundIds);
		return "SUCCESS";
	}
	
	
	
}
