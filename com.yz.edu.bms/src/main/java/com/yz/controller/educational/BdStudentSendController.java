package com.yz.controller.educational;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.service.educational.BdStudentSendService;
import com.yz.service.educational.BdStudentTScoreExcelService;
import com.yz.util.JsonUtil;

@RequestMapping("/studentSend")
@Controller
public class BdStudentSendController {

	private static final Logger log = LoggerFactory.getLogger(BdStudentSendController.class);

	@Autowired
	private BdStudentSendService studentSendService;
	
	@Autowired
	private BdStudentTScoreExcelService excelService;

	@RequestMapping("/list")
	@Rule("studentSend:find")
	public String showList(HttpServletRequest request) {
		return "educational/studentSend/student-send-list";
	}

	@RequestMapping("/listToService")
	@Rule("studentSend:findService")
	public String showService(HttpServletRequest request) {
		return "educational/studentSend/student-send-service-list";
	}

	/**
	 * 学员服务--》教材发放班主任查询
	 * 
	 * @param studentSendMap
	 *            BdStudentSendMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findAllStudentSendSevi")
	@ResponseBody
	@Rule("studentSend:findService")
	public Object findAllStudentSendSevi(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentSendMap studentSendMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, Object>> resultList = studentSendService.findAllStudentSendSevi(studentSendMap);
		//log.debug("------------------------ 教务发书分页数据：" + JsonUtil.object2String(resultList));
		return new IPageInfo((Page) resultList);
	}

	/**
	 * 教务管理--》教材发放-->订书管理
	 * 
	 * @param studentSendMap
	 *            BdStudentSendMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findAllStudentOrderBook")
	@ResponseBody
	@Rule("studentSend:find")
	public Object findAllStudentOrderBook(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentSendMap studentSendMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, Object>> resultList = studentSendService.findAllStudentOrderBook(studentSendMap);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * 教务管理--》教材发放教务查询
	 * 
	 * @param studentSendMap
	 *            BdStudentSendMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findAllStudentSendSeviEd")
	@ResponseBody
	@Rule("studentSend:find")
	public Object findAllStudentSendSeviEd(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentSendMap studentSendMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, Object>> resultList = studentSendService.findAllStudentSendSeviEd(studentSendMap);
		//log.debug("------------------------ 教务发书地址审核数据：" + JsonUtil.object2String(resultList));
		return new IPageInfo((Page) resultList);
	}

	/**
	 * 学员服务--》教材发放--》发放确认数据查询
	 * 
	 * @param studentSendMap
	 *            BdStudentSendMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findAllOkSend")
	@ResponseBody
	@Rule("studentSend:findOkSend")
	public Object findAllOkSend(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdStudentSendMap studentSendMap) {
		PageHelper.offsetPage(start, length);
		List<Map<String, Object>> resultList = studentSendService.findAllOkSend(studentSendMap);
		return new IPageInfo((Page) resultList);
	}

	/**
	 * Description: 审核通过
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/pass")
	@ResponseBody
	@Rule("studentSend:auditServ")
	public Object pass(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.passStudentSend(idArray);
		return "success";
	}

	@RequestMapping("/passEducation")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object passEducation(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.passEducation(idArray);
		return "success";
	}

	/**
	 * Description: 确认已订
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/okOrder")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object okOrder(@RequestParam(name = "idArray[]", required = true) String[] idArray,@RequestParam(name = "logisticsName", required = true) String logisticsName ) {
		studentSendService.okOrder(idArray,logisticsName);
		return "success";
	}

	/**
	 * Description: 未订
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/noOrder")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object noOrder(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.noOrder(idArray);
		return "success";
	}

	/**
	 * Description: 确认已收件
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/okReceive")
	@ResponseBody
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public Object okReceive(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.okReceive(idArray);
		return "success";
	}

	/**
	 * Description: 确认收货
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/okSend")
	@ResponseBody
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public Object okSend(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.okSend(idArray);
		return "success";
	}

	/**
	 * Description: 确认收货
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/okSendServ")
	@ResponseBody
	@Rule("studentSend:auditServ")
	public Object okSendServ(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.okSend(idArray);
		return "success";
	}

	/**
	 * Description: 审核驳回(班主任)
	 * 
	 * @param idArray
	 *            String[]集合
	 * @return 返回boolean型
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentSendController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/rejected")
	@ResponseBody
	@Rule({"studentSend:audit","studentSend:auditServ"})
	public Object rejected(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "reason", required = false) String reason) {
		studentSendService.rejectedStudentSend(idArray,reason,type);
		return "success";
	}

	/**
	 * 教务驳回
	 * @param idArray
	 * @return
	 */
	@RequestMapping("/rejectedEducation")
	@ResponseBody
	@Rule({"studentSend:audit","studentSend:auditServ"})
	public Object rejectedEducation(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "reason", required = false) String reason) {
		studentSendService.rejectedEducation(idArray,reason,type);
		return "success";
	}

	@RequestMapping("/importSendInfo")
	@ResponseBody
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public Object importSendInfo(@RequestParam(value = "sendServInfo", required = false) MultipartFile sendServInfo,
			@RequestParam(value = "isFd", required = false) String isFd) {
		studentSendService.importSendInfo(sendServInfo, isFd);
		return "success";
	}

	@RequestMapping("/toServImport")
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public String toServImport(HttpServletRequest request) {

		return "educational/studentSend/send-serv-import";
	}
	
	
	@RequestMapping("/toQueryLogistics")
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public String toQueryLogistics(HttpServletRequest request) {

		return "educational/studentSend/student-query-logistics";
	}
	
	@RequestMapping("/toSelectLogistics")
	@Rule("studentSend:audit")
	public String toSelectLogistics(HttpServletRequest request) {

		return "educational/studentSend/student-select-logistics";
	}
	
	@RequestMapping("/queryOkOrder")
	@ResponseBody
	@Rule({ "studentSend:auditServ", "studentSend:audit" })
	public Object queryOkOrder(BdStudentSendMap studentSendMap) {
		studentSendService.queryOkOrder(studentSendMap);
		return "success";
	}
	
	@RequestMapping("/export")
	@Rule("studentSend:audit")
	public void export(BdStudentSendMap studentSendMap, HttpServletResponse response) {
		studentSendService.exportMatchBookInfo(studentSendMap, response);
	}
	
	
	@RequestMapping("/queryRefreshTextbookData")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object queryRefreshTextbookData(BdStudentSendMap studentSendMap) {
		studentSendService.queryRefreshTextbookData(studentSendMap);
		return "success";
	}
	
	@RequestMapping("/selectRefreshTextbookData")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object selectRefreshTextbookData(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		studentSendService.selectRefreshTextbookData(idArray);
		return "success";
	}
	
	
	@RequestMapping("/toQueryUpdateBatch")
	@Rule("studentSend:audit")
	public String toQueryUpdateBatch(HttpServletRequest request) {
		return "educational/studentSend/update-send-batchid";
	}
	
	@RequestMapping("/toSelectUpdateBatchId")
	@Rule("studentSend:audit")
	public String toSelectUpdateBatchId(HttpServletRequest request) {
		return "educational/studentSend/select-update-send-batchid";
	}
	
	@RequestMapping("/queryUpdateBatch")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object queryUpdateBatch(BdStudentSendMap studentSendMap) {
		studentSendService.queryUpdateBatch(studentSendMap);
	  return "SUCCESS";
	}
	
	@RequestMapping("/selectUpdateBatchId")
	@ResponseBody
	@Rule("studentSend:audit")
	public Object selectUpdateBatchId(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(value = "updateBatchId",required = true) String updateBatchId) {
		studentSendService.selectUpdateBatchId(idArray,updateBatchId);
		return "success";
	}
	
	@RequestMapping("/toImportOrderBook")
	@Rule("studentSend:audit")
	public String toImportOrderBook(HttpServletRequest request) {
		return "educational/studentSend/student_order_book_import";
	}
	
	@RequestMapping("/toImportTextbookPfsncode")
	@Rule("studentSend:importTextBookPfsnCode")
	public String toImportTextbookPfsncode(HttpServletRequest request) {
		return "educational/studentSend/student_textbook_pfsncode_import";
	}
	
    @RequestMapping("/importOrderBook")
    @Rule("studentSend:audit")
    @ResponseBody
    public Object uploadStuGraduateData(
            @RequestParam(value = "stuOrderBookImport", required = false) MultipartFile stuOrderBookImport) {

    	studentSendService.importStuOrderBookInfo(stuOrderBookImport);
    	 return "SUCCESS";
    }
    
    /**
     * 导入教材专业编码
     * @param textBookPfsCodeImport
     * @return
     */
    @RequestMapping("/importTextBookPfsnCode")
    @Rule("studentSend:importTextBookPfsnCode")
    @ResponseBody
    public Object importTextBookPfsnCode(
            @RequestParam(value = "textBookPfsCodeImport", required = false) MultipartFile textBookPfsCodeImport) {
    	excelService.importTextBookPfsnCode(textBookPfsCodeImport);
    	return "SUCCESS";
    }
    
    
    /**
     * 增加备注
     * @param addRemark
     * @param sendId
     * @return
     */
    @RequestMapping("/addRemark")
	@ResponseBody
	@Rule("studentSend:auditServ")
	public Object addRemark(@RequestParam(name = "addRemark", required = true) String addRemark,
			@RequestParam(name = "sendId", required = true) String sendId) {
		studentSendService.addRemark(sendId,addRemark);
		return "success";
	}
    
    @RequestMapping("/updateAddress")
	@ResponseBody
	@Rule("studentSend:auditServ")
	public Object updateAddress(@RequestParam(name = "sendId", required = true) String sendId,
			@RequestParam(name = "detailAddress", required = true) String detailAddress,
			@RequestParam(name = "provinceCode", required = true) String provinceCode,
			@RequestParam(name = "cityCode", required = true) String cityCode,
			@RequestParam(name = "districtCode", required = true) String districtCode,
			@RequestParam(name = "takeUserName") String takeUserName,
			@RequestParam(name = "takeMobile") String takeMobile) {
		studentSendService.updateAddress(sendId,provinceCode,cityCode,districtCode,detailAddress,takeUserName,takeMobile);
		return "success";
	}
    
    
    
}
