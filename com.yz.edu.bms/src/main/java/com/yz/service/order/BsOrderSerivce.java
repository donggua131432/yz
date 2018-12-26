package com.yz.service.order;

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

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper; 
import com.yz.constants.ReptConstants;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.SfCode128CUtil;
import com.yz.dao.goods.GsGoodsMapper;
import com.yz.dao.order.BsOrderMapper;
import com.yz.dao.system.SysCityMapper;
import com.yz.dao.system.SysDistrictMapper;
import com.yz.dao.system.SysProvinceMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.goods.GsActivitiesGoods;
import com.yz.model.goods.GsCourseGoods;
import com.yz.model.order.BsActivityOrder;
import com.yz.model.order.BsCourseOrder;
import com.yz.model.order.BsLogistics;
import com.yz.model.order.BsOrder;
import com.yz.model.order.BsOrderQuery;
import com.yz.model.sf.SFExpressRequest;
import com.yz.model.system.SysCity;
import com.yz.model.system.SysDistrict;
import com.yz.model.system.SysProvince;
import com.yz.service.sfexpress.SfExpressService;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 智米中心-订单管理
 * 
 * @author lx
 * @date 2017年8月8日 下午2:32:02
 */
@Service
@Transactional
public class BsOrderSerivce {
	private static Logger log = LoggerFactory.getLogger(BsOrderSerivce.class);
	@Autowired
	private BsOrderMapper bsOrderMapper;

	@Autowired
	private SfExpressService sfService;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	@Autowired
	private SysProvinceMapper provinceMapper;

	@Autowired
	private SysCityMapper cityMapper;

	@Autowired
	private SysDistrictMapper districtMapper;

	@Autowired
	private GsGoodsMapper gsGoodsMapper;
	
	@Autowired
	private SfCode128CUtil sfCode128CUtil;
	
	@Autowired
	private BsJdOrderSerivce bsJdOrderService;

	public IPageInfo<BsOrder> getBsOrderList(int start, int length, BsOrderQuery orderQuery) {
		PageHelper.offsetPage(start, length);
		List<BsOrder> resultList = bsOrderMapper.getBsOrderList(orderQuery);
		if(null != resultList && resultList.size() >0){
			for(BsOrder order :resultList){
//				SysProvince province = provinceMapper.selectByPrimaryKey(order.getProvince());
//				SysCity city = cityMapper.selectByPrimaryKey(order.getCity());
//				SysDistrict district = districtMapper.selectByPrimaryKey(order.getDistrict());
				StringBuilder sb = new StringBuilder();
				if (null != order.getProvinceName()) {
					sb.append(order.getProvinceName());
				}
				if (null != order.getCityName()) {
					sb.append(order.getCityName());
				}
				if (null != order.getDistrictName()) {
					sb.append(order.getDistrictName());
				}
				order.setAddress(sb.toString()+order.getAddress());
			}
		}
		
		return new IPageInfo<BsOrder>((Page<BsOrder>) resultList);
	}


	public IPageInfo<BsActivityOrder> getBsActivityOrderList(int start, int length, BsOrderQuery orderQuery) {
		PageHelper.offsetPage(start, length);
		List<BsActivityOrder> resultList = bsOrderMapper.getBsActivityOrderList(orderQuery);
		return new IPageInfo<BsActivityOrder>((Page<BsActivityOrder>) resultList);
	}

	public IPageInfo<BsCourseOrder> getBsCourseOrderList(int start, int length, BsOrderQuery orderQuery) {
		PageHelper.offsetPage(start, length);
		List<BsCourseOrder> resultList = bsOrderMapper.getBsCourseOrderList(orderQuery);
		if (null != resultList && resultList.size() > 0) {
			for (BsCourseOrder order : resultList) {
				GsCourseGoods goodsInfo = gsGoodsMapper.getGsCourseGoods(order.getGoodsId());
				if (null != goodsInfo) {
					order.setAddress(goodsInfo.getAddress());
					order.setCourseType(goodsInfo.getCourseType());
					order.setStartTime(goodsInfo.getStartTime());
					order.setEndTime(goodsInfo.getEndTime());
				}
			}
		}
		return new IPageInfo<BsCourseOrder>((Page<BsCourseOrder>) resultList);
	}

	public IPageInfo<BsOrder> getBsTextBookOrderList(int start, int length, BsOrderQuery orderQuery) {
		PageHelper.offsetPage(start, length);
		List<BsOrder> resultList = bsOrderMapper.getBsOrderList(orderQuery);
		return new IPageInfo<BsOrder>((Page<BsOrder>) resultList);
	}

	public BsOrder getBsOrderDetail(String orderNo) {
		BsOrder order = bsOrderMapper.getBsOrderDetail(orderNo);
//		SysProvince province = provinceMapper.selectByPrimaryKey(order.getProvince());
//		SysCity city = cityMapper.selectByPrimaryKey(order.getCity());
//		SysDistrict district = districtMapper.selectByPrimaryKey(order.getDistrict());
		StringBuilder sb = new StringBuilder();
		if (null != order.getProvinceName()) {
			sb.append(order.getProvinceName());
		}
		if (null != order.getCityName()) {
			sb.append(order.getCityName());
		}
		if (null != order.getDistrictName()) {
			sb.append(order.getDistrictName());
		}
	
		order.setAddress(sb.toString()+order.getAddress());
		

		//自动刷新订单
		if(order.getSalesType().equalsIgnoreCase("1")&&StringUtil.hasValue(order.getJdOrderId())&&order.getOrderStatus()!="3"&&order.getOrderStatus()!="5") {
			BsOrder jdorder=bsJdOrderService.getJdOrderInfoByjdOrderId(order.getJdOrderId());
			if(jdorder!=null) {
				order.setFreight(jdorder.getFreight());
				order.setOrderPrice(jdorder.getOrderPrice());
				order.setJdPrice(jdorder.getJdPrice());
				order.setOrderStatus(jdorder.getOrderStatus());
				bsOrderMapper.refreshJdOrder(order);
			}
		}
		

		return order;
	}

	public void updateOrderLogisticsInfo(BsLogistics log) {
		bsOrderMapper.updateOrderLogisticsInfo(log);
	}

	public void updateOrderStatus(String orderNo,String status) {
		bsOrderMapper.updateOrderStatus(orderNo,status);
	}
	public BsCourseOrder getBsCourseOrder(String orderNo) {
		BsCourseOrder order = bsOrderMapper.getBsCourseOrder(orderNo);
		if (null != order) {
			GsCourseGoods goodsInfo = gsGoodsMapper.getGsCourseGoods(order.getGoodsId());
			if (null != goodsInfo) {
				order.setAddress(goodsInfo.getAddress());
				order.setCourseType(goodsInfo.getCourseType());
				order.setStartTime(goodsInfo.getStartTime());
				order.setEndTime(goodsInfo.getEndTime());
			}
		}
		return order;
	}

	public BsActivityOrder getBsActivityOrder(String orderNo) {
		BsActivityOrder order = bsOrderMapper.getBsActivityOrder(orderNo);
		if (null != order) {
			GsActivitiesGoods goodsInfo = gsGoodsMapper.getGsActivitiesGoods(order.getGoodsId());
			if (null != goodsInfo) {
				order.setAddress(goodsInfo.getAddress());
				order.setStartTime(goodsInfo.getStartTime());
				order.setEndTime(goodsInfo.getEndTime());
				order.setTakeIn(goodsInfo.getTakein());
			}
		}
		return order;
	}

	public void sfOrder(String orderNo) throws IOException {
		String logisticsId = bsOrderMapper.selectLogisticsIdByOrderNo(orderNo);
		if (!StringUtil.hasValue(logisticsId)) {
			return;
		}
		SFExpressRequest order = bsOrderMapper.selectSfInfoByLogisticsId(logisticsId);
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
			order.setjContact("智米中心-运营部");
			order.setjTel("07522686013");
			order.setjProvince("广东省");
			order.setjCity("惠州");
			order.setjCountry("惠城区");
			order.setjAddress("江北三新南路22号润宇豪庭3楼");
		} else {
			return;
		}

		// 顺丰下单接口
		Map<String, String> resp = sfService.sfOrder(order, ReptConstants.SF_ORDER_SENDER_ZHIMI);
		String headResp = resp.get(ReptConstants.SF_RESPONSE_HEAD);
		if (!StringUtil.hasValue(headResp)) {
			bsOrderMapper.updateBsLogisticsRemark(logisticsId, ReptConstants.SF_RESPONSE_ERRMSG_NULL);
			return;
		} else if (ReptConstants.SF_RESPONSE_ERR.equalsIgnoreCase(headResp)) {
			log.error("-------------顺丰下单错误：" + resp.toString());
			String errMsg = "下单失败：(" + resp.get("errorCode") + ")" + resp.get("errorMsg");
			bsOrderMapper.updateBsLogisticsRemark(logisticsId, errMsg);
			return;
		}
		String destCode = resp.get(ReptConstants.SF_EXPRESS_DESTCODE); // 寄件地区编码
		String mailno = resp.get(ReptConstants.SF_EXPRESS_MAILNO); // 顺丰订单号
		log.info("---------顺丰下单信息:"+logisticsId+"单号："+mailno+"目的地:"+destCode+"----------");
		
		BsLogistics logistics = new BsLogistics();
		logistics.setZipCode(destCode);
		logistics.setTransportNo(mailno);
		logistics.setLogisticsStatus("2");
		logistics.setRemark(ReptConstants.SF_RESPONSE_SUCCESS_MSG);
		logistics.setLogisticsId(logisticsId);
		bsOrderMapper.updateBsLogistics(logistics);
		if (StringUtil.hasValue(mailno)) { // 生成条形码并上传至OSS文件服务器 文件地址：sf/$mailno
			sfCode128CUtil.uploadBarcodeImg(mailno);
		}
		// 修改订单状态
		bsOrderMapper.updateOrderStatus(orderNo,"1");
	}

	public Object sfPrintInfo(String logisticsId) {
		List<BdSfPrint> sfs = new ArrayList<BdSfPrint>();
		BdSfPrint sf = bsOrderMapper.selectSfPrint(logisticsId);
		if (null != sf) {
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
			sfs.add(sf);
		}

		return sfs;
	}
	//订单物品邮寄
	public void goodsOrderMailed(String[] orderNos) {
		bsOrderMapper.updateGoodsOrderMailed(orderNos);
	}
	//结算
	public void checkOrderInfo(String checkUser,String userId,String orderNo){
		bsOrderMapper.checkOrderInfo(checkUser, userId, orderNo);
	}
	//批量结算
	public void batchCheck(String checkUser,String userId,String[] orderNos){
		bsOrderMapper.batchCheck(checkUser,userId,orderNos);
	}
	//批量打印快递单
	public Object sfPrintInfos(String[] orderNos) {
		List<BdSfPrint> sfs = new ArrayList<BdSfPrint>();
		for(int i=0;i<orderNos.length;i++){
			String logisticsId = bsOrderMapper.selectLogisticsIdByOrderNo(orderNos[i]);
			BdSfPrint sf = bsOrderMapper.selectSfPrint(logisticsId);
			if (null != sf) {
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
				sfs.add(sf);
			}
		}
		return sfs;
	}
	
	/**
	 * 导出订单
	 * @param orderQuery
	 * @param response
	 */
	/**
	 * @param orderQuery
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void exportOrder(BsOrderQuery orderQuery, HttpServletResponse response)
	{
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BsOrder> testExcelCofing = new ExcelUtil.IExcelConfig<BsOrder>();
		testExcelCofing.setSheetName("index").setType(BsOrder.class)
				.addTitle(new ExcelUtil.IExcelTitle("订单号", "orderNo"))
				.addTitle(new ExcelUtil.IExcelTitle("活动类型", "salesType"))
				.addTitle(new ExcelUtil.IExcelTitle("商品名称", "goodsName"))
				.addTitle(new ExcelUtil.IExcelTitle("单价(智米)", "unitPrice"))
				.addTitle(new ExcelUtil.IExcelTitle("数量", "goodsCount"))
				.addTitle(new ExcelUtil.IExcelTitle("总价(智米)", "transAmount"))
				.addTitle(new ExcelUtil.IExcelTitle("下单人信息", "userName"))
				.addTitle(new ExcelUtil.IExcelTitle("下单时间", "orderTime"))
				.addTitle(new ExcelUtil.IExcelTitle("收货信息", "takeUserName"))
				.addTitle(new ExcelUtil.IExcelTitle("快递单号", "transportNo"))
				.addTitle(new ExcelUtil.IExcelTitle("订单状态", "orderStatus"));

		List<BsOrder> resultList = bsOrderMapper.getBsOrderList(orderQuery);
		if(null != resultList && resultList.size() >0){
			for(BsOrder order : resultList){
				String salesType = dictExchangeUtil.getParamKey("salesType", order.getSalesType());
				order.setSalesType(salesType);
				order.setUserName("昵称："+order.getUserName()+"\r\n"+"手机："+order.getMobile());
				SysProvince province = provinceMapper.selectByPrimaryKey(order.getProvince());
				SysCity city = cityMapper.selectByPrimaryKey(order.getCity());
				SysDistrict district = districtMapper.selectByPrimaryKey(order.getDistrict());
				StringBuilder sb = new StringBuilder();
				if(StringUtil.isEmpty(order.getJdOrderId())) {
					if (null != province) {
						sb.append(province.getProvinceName());
					}
					if (null != city) {
						sb.append(city.getCityName());
					}
					if (null != district) {
						sb.append(district.getDistrictName());
					}
				}else {
					if (null != order.getProvinceName()) {
						sb.append(order.getProvinceName());
					}
					if (null != order.getCityName()) {
						sb.append(order.getCityName());
					}
					if (null != order.getDistrictName()) {
						sb.append(order.getDistrictName());
					}
				}
				
				order.setAddress(sb.toString()+order.getAddress());
				
				order.setTakeUserName("收货人："+order.getTakeUserName()+"\r\n"+"联系手机："+order.getTakeMobile()+"\r\n"+"收货地址："+order.getAddress());
				String orderStatus;
				if(order.getOrderStatus().equals("1")){
					orderStatus = "待发货";
				}else if(order.getOrderStatus().equals("2")){
					orderStatus = "已发货";
				}else if(order.getOrderStatus().equals("3")){
					orderStatus = "已完成";
				}else if(order.getOrderStatus().equals("4")){
					orderStatus = "待下单";
				}else if(order.getOrderStatus().equals("5")){
					orderStatus = "已拒收";
				}else if(order.getOrderStatus().equals("6")){
					orderStatus = "待收货";
				}else{
					orderStatus = "未知状态";
				}
				order.setOrderStatus(orderStatus);
				
//				String printStatus = null;
//				if(order.getOrderStatus().equals("2")){
//					printStatus="未打印";
//				}else if(order.getOrderStatus().equals("3")){
//					printStatus="已打印";
//				}
//				order.setPrintStatus(printStatus);
//				if(order.getCheckStatus().equals("1")){
//					order.setCheckStatus("未结算");
//				}else if(order.getCheckStatus().equals("2")){
//					order.setCheckStatus("结算状态:已结算"+"\r\n"+"结算人："+order.getCheckUser() +"\r\n"+"结算时间："+order.getCheckTime());
//				}
			}
		}
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(resultList, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=OrderInfo.xls");
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
