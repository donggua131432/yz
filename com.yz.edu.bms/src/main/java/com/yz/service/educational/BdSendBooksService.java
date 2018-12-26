package com.yz.service.educational;

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
 
import com.yz.constants.EducationConstants;
import com.yz.constants.ReptConstants;
import com.yz.constants.StudentConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.BarcodeUtil;
import com.yz.core.util.SfCode128CUtil;
import com.yz.dao.educational.BdSendBooksMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.etms.JDeliveryServiceJsf.ResultInfoDTO;
import com.jd.open.api.sdk.response.etms.EtmsRangeCheckResponse;
import com.jd.open.api.sdk.response.etms.EtmsWaybillSendResponse;
import com.jd.open.api.sdk.response.etms.EtmsWaybillcodeGetResponse;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdSendBookExport;
import com.yz.model.educational.BdSendBooksInfo;
import com.yz.model.educational.BdSendBooksQuery;
import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.jd.JDExpressRequest;
import com.yz.model.sf.SFExpressRequest;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.redis.RedisService;
import com.yz.service.jdexpress.JDExpressService;
import com.yz.service.sfexpress.SfExpressService;
import com.yz.util.DateUtil;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 外包发书
 * 
 * @author lx
 * @date 2017年9月19日 下午2:38:50
 */
@Service
@Transactional
public class BdSendBooksService {
	private static Logger log = LoggerFactory.getLogger(BdSendBooksService.class);
	
	@Autowired
	private BarcodeUtil barcodeUtil;
	
	@Autowired
	private BdSendBooksMapper sendBooksMapper;

	@Autowired
	private SysProvinceMapper provinceMapper;

	@Autowired
	private SysCityMapper cityMapper;

	@Autowired
	private SysDistrictMapper districtMapper;

	@Autowired
	private SfExpressService sfService;
	
	@Autowired
	private SfCode128CUtil sfCode128CUtil;
	
	@Autowired
	private JDExpressService jdService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectSendBooksByPage(int start, int length, BdSendBooksQuery bookQuery) {
		BaseUser user=SessionUtil.getUser();
		if(user.getRoleCodeList().contains("wbfs")) {
			user.setUserLevel("2");
		}if(user.getRoleCodeList().contains("jdwbfs")) {
			user.setUserLevel("3");
		}
		PageHelper.offsetPage(start, length);
		List<BdSendBooksInfo> list = sendBooksMapper.getBdSendBooksInfo(bookQuery,user);
		return new IPageInfo((Page) list);
	}

	@SuppressWarnings("unchecked")
	public void exportSendInfo(BdSendBooksQuery bookQuery, HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdSendBookExport> testExcelCofing = new ExcelUtil.IExcelConfig<BdSendBookExport>();
		testExcelCofing.setSheetName("index").setType(BdSendBookExport.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName")).addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("教材", "bookDesc"))
				.addTitle(new ExcelUtil.IExcelTitle("订书批次", "batchId"))
				.addTitle(new ExcelUtil.IExcelTitle("发送单位", "jCompany"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件人", "jContact"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件人手机", "jPhone"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件省", "jProvince"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件城市", "jCity"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件区县", "jArea"))
				.addTitle(new ExcelUtil.IExcelTitle("寄件地址", "jAddress"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人", "dContact"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人手机", "dPhone"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人省", "dProvince"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人城市", "dCity"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人地区", "dArea"))
				.addTitle(new ExcelUtil.IExcelTitle("收件人地址", "dAddress"))
				.addTitle(new ExcelUtil.IExcelTitle("物流商", "logisticsName"))
				.addTitle(new ExcelUtil.IExcelTitle("快递单号", "logisticsNo"))
				.addTitle(new ExcelUtil.IExcelTitle("下单时间", "sendTime"));
		
		BaseUser user=SessionUtil.getUser();
		if(user.getRoleCodeList().contains("wbfs")) {
			user.setUserLevel("2");
		}if(user.getRoleCodeList().contains("jdwbfs")) {
			user.setUserLevel("3");
		}
		List<BdSendBookExport> list = sendBooksMapper.selectSendExportInfo(bookQuery,user);

		for (BdSendBookExport send : list) {

			StringBuffer sb = new StringBuffer();

			List<Map<String, String>> books = send.getBooks();
			if (null != books && books.size() > 0) {
				for (Map<String, String> map : books) {
					sb.append(map.get("textbookName")).append("、");
				}
			}

			String booksStr = sb.toString();
			if (StringUtil.hasValue(booksStr)) {
				booksStr = booksStr.substring(0,booksStr.length()-1);
			}
			if(StringUtil.hasValue(send.getLogisticsName())){
				if(send.getLogisticsName().equals("jd")){
					send.setLogisticsName("京东物流");
				}else if(send.getLogisticsName().equals("sf")){
					send.setLogisticsName("顺丰物流");
				}else{
					send.setLogisticsName("未分配物流商");
				}
			}
			send.setBookDesc(booksStr);
			send.setJContact("邝老师");
			send.setJAddress("广东省惠州市惠城区江北三新南路22号润宇豪庭3楼");
			send.setJArea("惠城区");
			send.setJCity("惠州市");
			send.setJCompany("远智教育");
			send.setJPhone("13302655995");
			send.setJProvince("广东省");
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=SendInfo.xls");
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
	
	public void sfOrders(String[] sendIds) throws IOException {
		BaseUser user = SessionUtil.getUser();
		for (String sendId : sendIds) {
			SFExpressRequest sforder = sendBooksMapper.selectSfInfoBySendId(sendId);	
			sfOrder(sforder,user.getUsername());
		}
	}
	
	public void jdOrders(String[] sendIds) throws JdException, IOException {
		BaseUser user = SessionUtil.getUser();
		for (String sendId : sendIds) {
			SFExpressRequest sforder = sendBooksMapper.selectSfInfoBySendId(sendId);	
			jdOrder(sforder,user.getUsername());
		}
	}
	
	public void jdOrder(SFExpressRequest sforder,String sendPeople) throws JdException, IOException {
		if (null != sforder) {
			SysProvince province = provinceMapper.selectByPrimaryKey(sforder.getdProvince());
			SysCity city = cityMapper.selectByPrimaryKey(sforder.getdCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(sforder.getdCountry());
			String addressTemp="";
			if (null != province) {
				addressTemp+=province.getProvinceName();
				
			}
			if (null != city) {
				addressTemp+=city.getCityName();
			}
			if (null != district) {
				addressTemp+=district.getDistrictName();						
			}
			//第一步：检查是否可以京配
			EtmsRangeCheckResponse checkresponse=jdService.check(sforder.getOrderid(), addressTemp+sforder.getdAddress());
			//可以京东配送
			if(checkresponse!=null&&checkresponse.getResultInfo().getRcode().toString().equals("100")) {
				
				//避免取得不同的运单号去下单
				String keyStr="jdDeliveryId-"+sforder.getOrderid();
				String deliveryId=RedisService.getRedisService().get(keyStr);
				if(StringUtil.isEmpty(deliveryId)) {
					//第二步：得到运单号
					EtmsWaybillcodeGetResponse ydresponse=jdService.getDeliveryIdList("1");
					if(ydresponse.getResultInfo()!=null&&ydresponse.getResultInfo().getCode().equals("100")) {
						deliveryId=ydresponse.getResultInfo().getDeliveryIdList().get(0);
						if(StringUtil.hasValue(deliveryId)) {
							RedisService.getRedisService().set(keyStr, deliveryId);
						}
					}else {
						log.error("-------------京东获取运单号错误：" + ydresponse.getResultInfo().getMessage());
						String errMsg = "京东下单失败：(" + ydresponse.getResultInfo().getMessage() + ")";
						sendBooksMapper.updateStudentSendRemark(sforder.getOrderid(), errMsg);
						return;
					}
					
				}else {
					//防止同一个订单号重复下单
					log.error("-------------订单重复下单,orderId:"+sforder.getOrderid()+",deliveryId:"+deliveryId);
					return;
				}
				
				JDExpressRequest jdorder =new JDExpressRequest();
				jdorder.setDeliveryId(deliveryId);
				jdorder.setGoodsType("1");
				jdorder.setOrderid(sforder.getOrderid());
				jdorder.setPackageCount(1);
				jdorder.setReceiveAddress(addressTemp+sforder.getdAddress());
				jdorder.setReceiveMobile(sforder.getdMobile());
				jdorder.setReceiveName(sforder.getdContact());
				//jdorder.setSenderAddress("深南大道9998号万利达大厦22层（远智教育）");
				jdorder.setSenderAddress("广东省惠州市惠城区江北三新南路22号润宇豪庭（远智教育）");					
				jdorder.setSenderName("邝老师");
				jdorder.setSenderMobile("13302655995");
				jdorder.setVloumn(0.0);
				jdorder.setWeight(0.0);

				EtmsWaybillSendResponse sendresponse=jdService.sendOrder(jdorder);
				if(sendresponse!=null&&sendresponse.getResultInfo()!=null&&sendresponse.getResultInfo().getCode().equals("100")) {
					//京东下单成 功
					RedisService.getRedisService().del(keyStr);
					log.debug("京东下单返回的信息"+sendresponse.getResultInfo().getCode()+"订单号:"+deliveryId);
					BdSendBooksInfo sendBook = new BdSendBooksInfo();
					sendBook.setLogisticsNo(deliveryId);
					sendBook.setOrderBookStatus("3");
					sendBook.setSendId(sforder.getOrderid());
					sendBook.setRemark(ReptConstants.JD_RESPONSE_SUCCESS_MSG);
					sendBook.setSendPeople(sendPeople);
					//按招生类型来区分下单是否合并
					if(StudentConstants.RECRUIT_TYPE_GK.equals(sforder.getRecruitType())){
						sendBooksMapper.updateBdSendBooks(sendBook);
					}else if(StudentConstants.RECRUIT_TYPE_CJ.equals(sforder.getRecruitType())){
						//两个学期合起来一起发
						if(sforder.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_FD)){ //辅导教材还是照旧
							sendBooksMapper.updateBdSendBooks(sendBook);
						}else if(sforder.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_XK)){
							//获取学期
							String[] semesterArr = strConvertArray(sforder.getSemester());
							//合并处理
							sendBook.setLearnId(sforder.getLearnId());
							sendBook.setSemesters(semesterArr);
							sendBooksMapper.mergeDisposeBdSendBooks(sendBook);
						}
					}
					
					if (StringUtil.hasValue(deliveryId)) { // 生成条形码并上传至OSS文件服务器 文件地址：sf/$mailno
						barcodeUtil.uploadBarcodeImg(deliveryId);
					}
					

				}else {
					log.error("-------------orderId:"+sforder.getOrderid()+"deliveryId:"+deliveryId+"京东获取下单错误：" + sendresponse.getResultInfo().getMessage());
					String errMsg = "京东下单失败：(" + sendresponse.getResultInfo().getMessage() + ")";
					sendBooksMapper.updateStudentSendRemark(sforder.getOrderid(), errMsg);
					RedisService.getRedisService().del(keyStr);
					return;
				}
				
			}else {
				log.error("-------------京东检查是不可以京配：" + checkresponse.getResultInfo().getRmessage());
				String errMsg = "京东下单失败：检查不可以京配,"+ checkresponse.getResultInfo().getRmessage();
				sendBooksMapper.updateStudentSendRemark(sforder.getOrderid(), errMsg);
				return;
			}
			
			
			
			
			
		}
	}
	
	public void sfOrder(SFExpressRequest order,String sendPeople) throws IOException {
		if (null != order) {
			SysProvince province = provinceMapper.selectByPrimaryKey(order.getdProvince());
			SysCity city = cityMapper.selectByPrimaryKey(order.getdCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(order.getdCountry());
			if (null != province) {
				order.setdProvince(province.getProvinceName());
			}
			if (null != city) {
				order.setdCity(city.getCityName());
			}
			if (null != district) {
				order.setdCountry(district.getDistrictName());
			}
			order.setjCompany("惠州远智教育");
			order.setjContact("邝老师");
			order.setjTel("13302655995");
			order.setjProvince("广东省");
			order.setjCity("惠州");
			order.setjCountry("惠城区");
			order.setjAddress("江北三新南路22号润宇豪庭（远智教育）");
			// 顺丰下单接口
			Map<String, String> resp = sfService.sfOrder(order, ReptConstants.SF_ORDER_SENDER_SENDBOOK);
			String headResp = resp.get(ReptConstants.SF_RESPONSE_HEAD);
			if (!StringUtil.hasValue(headResp)) {
				sendBooksMapper.updateStudentSendRemark(order.getOrderid(), ReptConstants.SF_RESPONSE_ERRMSG_NULL);
				return;
			} else if (ReptConstants.SF_RESPONSE_ERR.equalsIgnoreCase(headResp)) {
				log.error("-------------顺丰下单错误：" + resp.toString());
				String errMsg = "下单失败：(" + resp.get("errorCode") + ")" + resp.get("errorMsg");
				sendBooksMapper.updateStudentSendRemark(order.getOrderid(), errMsg);
				return;
			}
			String destCode = resp.get(ReptConstants.SF_EXPRESS_DESTCODE);
			// 寄件地区编码
			String mailno = resp.get(ReptConstants.SF_EXPRESS_MAILNO); // 顺丰订单号
			String custid = resp.get("custid");
			log.info("---------顺丰下单信息:"+order.getOrderid()+"单号："+mailno+"目的地:"+destCode+"----------");
			BdSendBooksInfo sendBook = new BdSendBooksInfo();
			sendBook.setDestCode(destCode);
			sendBook.setLogisticsNo(mailno);
			sendBook.setOrderBookStatus("3");
			sendBook.setSendId(order.getOrderid());
			sendBook.setCustid(custid);
			sendBook.setRemark(ReptConstants.SF_RESPONSE_SUCCESS_MSG);
			sendBook.setSendPeople(sendPeople);
			//按招生类型来区分下单是否合并
			if(StudentConstants.RECRUIT_TYPE_GK.equals(order.getRecruitType())){
				sendBooksMapper.updateBdSendBooks(sendBook);
			}else if(StudentConstants.RECRUIT_TYPE_CJ.equals(order.getRecruitType())){
				//两个学期合起来一起发
				if(order.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_FD)){ //辅导教材还是照旧
					sendBooksMapper.updateBdSendBooks(sendBook);
				}else if(order.getTextbookType().equals(EducationConstants.TEXT_BOOK_TYPE_XK)){
					//获取学期
					String[] semesterArr = strConvertArray(order.getSemester());
					//合并处理
					sendBook.setLearnId(order.getLearnId());
					sendBook.setSemesters(semesterArr);
					sendBooksMapper.mergeDisposeBdSendBooks(sendBook);
				}
				
			}
			
			if (StringUtil.hasValue(mailno)) { // 生成条形码并上传至OSS文件服务器 文件地址：sf/$mailno
				sfCode128CUtil.uploadBarcodeImg(mailno);
			}
		}

	}
	
	private String[] strConvertArray(String str){
		String[] ss = null;
		if(str.equals("1") || str.equals("2")){
			ss = new String[]{"1","2"};
		}else if(str.equals("3") || str.equals("4")){
			ss = new String[]{"3","4"};
		}else if(str.equals("5") || str.equals("6")){
			ss = new String[]{"5","6"};
		}
		return ss;
	}

	public Object sfPrintInfos(String[] sendIds) {
		List<BdSfPrint> sfs = sendBooksMapper.selectSfPrints(sendIds);
		for (BdSfPrint sf : sfs) {
			String mailno = StringUtil.addSpace(sf.getMailno(), 3);
			sf.setMailnoSpace(mailno);

			SysProvince province = provinceMapper.selectByPrimaryKey(sf.getProvince());
			SysCity city = cityMapper.selectByPrimaryKey(sf.getCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(sf.getDistrict());
			StringBuilder sb = new StringBuilder();
			if (null != province) {
				sb.append(province.getProvinceName());
			}
			if (null != city) {
				sb.append(city.getCityName());
			}
			if (null != district) {
				sb.append(district.getDistrictName());
			}
			sb.append(sf.getdAddress());
			sf.setdAddress(sb.toString());
			if(null !=sf.getTextbookType() && sf.getTextbookType().equals("FD")){
				sf.setTextBookName(sendBooksMapper.getTextBookNameBySendId(sf.getSendId()));
			}
		}
		return sfs;
	}
	
	
	public Object jdPrintInfos(String[] sendIds) {
		List<BdSfPrint> sfs = sendBooksMapper.selectSfPrints(sendIds);
		for (BdSfPrint sf : sfs) {
			SysProvince province = provinceMapper.selectByPrimaryKey(sf.getProvince());
			SysCity city = cityMapper.selectByPrimaryKey(sf.getCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(sf.getDistrict());
			StringBuilder sb = new StringBuilder();
			if (null != province) {
				sf.setProvince(province.getProvinceName());
				sb.append(province.getProvinceName());
			}
			if (null != city) {
				sf.setCity(city.getCityName());
				sb.append(city.getCityName());
			}
			if (null != district) {
				sf.setDistrict(district.getDistrictName());
				sb.append(district.getDistrictName());
			}
			sb.append(sf.getdAddress());
			sf.setdAddress(sb.toString());
			sf.setPrintdate(DateUtil.getCurrentDate());
			try {
				EtmsRangeCheckResponse checkresponse=jdService.check(sf.getSendId(), sb.toString());
				//可以京东配送
				if(checkresponse!=null&&checkresponse.getResultInfo().getRcode().toString().equals("100")) {
					ResultInfoDTO  dto=checkresponse.getResultInfo();
					sf.setSourcetSortCenterName(dto.getSourcetSortCenterName());
					sf.setOriginalCrossCode(dto.getOriginalCrossCode());
					sf.setOriginalTabletrolleyCode(dto.getOriginalTabletrolleyCode());
					sf.setTargetSortCenterName(dto.getTargetSortCenterName());
					sf.setDestinationCrossCode(dto.getDestinationCrossCode());
					sf.setDestinationTabletrolleyCode(dto.getDestinationTabletrolleyCode());
					sf.setSiteName(dto.getSiteName());
					sf.setRoad(dto.getRoad());
					sf.setAgingName(dto.getAgingName());
					sf.setAging(dto.getAging());
				}
			} catch (JdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null !=sf.getTextbookType() && sf.getTextbookType().equals("FD")){
				sf.setTextBookName(sendBooksMapper.getTextBookNameBySendId(sf.getSendId()));
			}
		}
		return sfs;
	}

	public Object jdPrintInfo(String sendId) {
		List<BdSfPrint> sfs = new ArrayList<BdSfPrint>();

		BdSfPrint sf = sendBooksMapper.selectSfPrint(sendId);
		if (null != sf) {
			SysProvince province = provinceMapper.selectByPrimaryKey(sf.getProvince());
			SysCity city = cityMapper.selectByPrimaryKey(sf.getCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(sf.getDistrict());
			StringBuilder sb = new StringBuilder();
			if (null != province) {
				sf.setProvince(province.getProvinceName());
				sb.append(province.getProvinceName());
			}
			if (null != city) {
				sf.setCity(city.getCityName());
				sb.append(city.getCityName());
			}
			if (null != district) {
				sf.setDistrict(district.getDistrictName());
				sb.append(district.getDistrictName());
			}
			sb.append(sf.getdAddress());
			sf.setdAddress(sb.toString());
			sf.setPrintdate(DateUtil.getCurrentDate());
			try {
				EtmsRangeCheckResponse checkresponse=jdService.check(sf.getSendId(), sb.toString());
				//可以京东配送
				if(checkresponse!=null&&checkresponse.getResultInfo().getRcode().toString().equals("100")) {
					ResultInfoDTO  dto=checkresponse.getResultInfo();
					sf.setSourcetSortCenterName(dto.getSourcetSortCenterName());
					sf.setOriginalCrossCode(dto.getOriginalCrossCode());
					sf.setOriginalTabletrolleyCode(dto.getOriginalTabletrolleyCode());
					sf.setTargetSortCenterName(dto.getTargetSortCenterName());
					sf.setDestinationCrossCode(dto.getDestinationCrossCode());
					sf.setDestinationTabletrolleyCode(dto.getDestinationTabletrolleyCode());
					sf.setSiteName(dto.getSiteName());
					sf.setRoad(dto.getRoad());
					sf.setAgingName(dto.getAgingName());
					sf.setAging(dto.getAging());
				}
			} catch (JdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null !=sf.getTextbookType() && sf.getTextbookType().equals("FD")){
				sf.setTextBookName(sendBooksMapper.getTextBookNameBySendId(sf.getSendId()));
			}
			sfs.add(sf);
		}
		return sfs;
	}
	public Object sfPrintInfo(String sendId) {
		List<BdSfPrint> sfs = new ArrayList<BdSfPrint>();

		BdSfPrint sf = sendBooksMapper.selectSfPrint(sendId);
		if (null != sf) {
			String mailno = StringUtil.addSpace(sf.getMailno(), 3);
			sf.setMailnoSpace(mailno);

			SysProvince province = provinceMapper.selectByPrimaryKey(sf.getProvince());
			SysCity city = cityMapper.selectByPrimaryKey(sf.getCity());
			SysDistrict district = districtMapper.selectByPrimaryKey(sf.getDistrict());
			StringBuilder sb = new StringBuilder();
			if (null != province) {
				sf.setProvince(province.getProvinceName());
				sb.append(province.getProvinceName());
			}
			if (null != city) {
				sf.setCity(city.getCityName());
				sb.append(city.getCityName());
			}
			if (null != district) {
				sf.setDistrict(district.getDistrictName());
				sb.append(district.getDistrictName());
			}
			sb.append(sf.getdAddress());
			sf.setdAddress(sb.toString());
			if(null !=sf.getTextbookType() && sf.getTextbookType().equals("FD")){
				sf.setTextBookName(sendBooksMapper.getTextBookNameBySendId(sf.getSendId()));
			}
			sfs.add(sf);
		}
		return sfs;
	}
	
	/**
	 * 查询批量下单
	 * @param bookQuery
	 * @throws IOException 
	 */
	public void querySfOrders(BdSendBooksQuery bookQuery) throws IOException{
		BaseUser user=SessionUtil.getUser();
		user.setUserLevel("2");
//		if(user.getRoleCodeList().contains("wbfs")) {
//			user.setUserLevel("2");
//		}if(user.getRoleCodeList().contains("jdwbfs")) {
//			user.setUserLevel("3");
//		}
		List<BdSendBooksInfo> list = sendBooksMapper.getBdSendBooksInfo(bookQuery,user);
		if(null != list && list.size() >0){
			for(BdSendBooksInfo sendBook : list){
				if(StringUtil.isEmpty(sendBook.getLogisticsNo())) {
					SFExpressRequest order=new SFExpressRequest();
					order.setOrderid(sendBook.getSendId());
					order.setdContact(sendBook.getUserName());
					order.setdTel(sendBook.getMobile());
					order.setdMobile(sendBook.getMobile());
					order.setdAddress(sendBook.getAddress());
					order.setdProvince(sendBook.getProvinceCode());
					order.setdCity(sendBook.getCityCode());
					order.setdCountry(sendBook.getDistrictCode());
					order.setLearnId(sendBook.getLearnId());
					order.setRecruitType(sendBook.getRecruitType());
					order.setSemester(sendBook.getSemester());
					order.setTextbookType(sendBook.getTextbookType());
					sfOrder(order,user.getUsername());
				}
			}
		}
	}
	
	
	/**
	 * 查询批量下单
	 * @param bookQuery
	 * @throws IOException 
	 * @throws JdException 
	 */
	public void queryJdOrders(BdSendBooksQuery bookQuery) throws IOException, JdException{
		BaseUser user=SessionUtil.getUser();
		user.setUserLevel("3");
		List<BdSendBooksInfo> list = sendBooksMapper.getBdSendBooksInfo(bookQuery,user);
		if(null != list && list.size() >0){
			for(BdSendBooksInfo sendBook : list){
				if(StringUtil.isEmpty(sendBook.getLogisticsNo())) {
					SFExpressRequest order=new SFExpressRequest();
					order.setOrderid(sendBook.getSendId());
					order.setdContact(sendBook.getUserName());
					order.setdTel(sendBook.getMobile());
					order.setdMobile(sendBook.getMobile());
					order.setdAddress(sendBook.getAddress());
					order.setdProvince(sendBook.getProvinceCode());
					order.setdCity(sendBook.getCityCode());
					order.setdCountry(sendBook.getDistrictCode());
					order.setLearnId(sendBook.getLearnId());
					order.setRecruitType(sendBook.getRecruitType());
					order.setSemester(sendBook.getSemester());
					order.setTextbookType(sendBook.getTextbookType());
					jdOrder(order,user.getUsername());
				}
				
			}
		}
	}

}
