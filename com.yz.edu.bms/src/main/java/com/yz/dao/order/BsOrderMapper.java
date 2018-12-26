package com.yz.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.receipt.BdSfPrint;
import com.yz.model.order.BsActivityOrder;
import com.yz.model.order.BsCourseOrder;
import com.yz.model.order.BsLogistics;
import com.yz.model.order.BsLogisticsInfo;
import com.yz.model.order.BsOrder;
import com.yz.model.order.BsOrderInfo;
import com.yz.model.order.BsOrderQuery;
import com.yz.model.order.BsSalesOrderInfo;
import com.yz.model.order.BsSerialInfo;
import com.yz.model.sf.SFExpressRequest;

public interface BsOrderMapper
{
	public List<BsOrder> getBsOrderList(BsOrderQuery orderQuery);
	
	public List<BsActivityOrder> getBsActivityOrderList(BsOrderQuery orderQuery);
	
	public List<BsCourseOrder> getBsCourseOrderList(BsOrderQuery orderQuery);
	
	public BsOrder getBsOrderDetail(String orderNo);
	
	public void updateOrderLogisticsInfo(BsLogistics log);
	
	public void updateOrderStatus(@Param("orderNo") String orderNo,@Param("status") String status);
	
	public BsCourseOrder getBsCourseOrder(String orderNo);
	
	public String selectLogisticsIdByOrderNo(String orderNo);
	
	public SFExpressRequest selectSfInfoByLogisticsId(String logisticsId);
	
	int updateBsLogisticsRemark(@Param("logisticsId") String logisticsId, @Param("errMsg") String errMsg);
	
	public void updateBsLogistics(BsLogistics logistics);
	
	public BsActivityOrder getBsActivityOrder(String orderNo);
	
	/**
	 * 顺丰打印信息
	 * @param logisticsId
	 * @return
	 */
	BdSfPrint selectSfPrint(@Param("logisticsId") String logisticsId);
	
	//邮寄
	int updateGoodsOrderMailed(@Param("orderNos") String[] orderNos);
	//结算
	int checkOrderInfo(@Param("checkUser") String checkUser,@Param("userId") String userId,@Param("orderNo") String orderNo);
	//批量结算
	int batchCheck(@Param("checkUser") String checkUser,@Param("userId") String userId,@Param("orderNos") String[] orderNos);
	
	String selectDelayByStdId(String stdId);
	
	/**
	 * 得到京东对账订单列表
	 * @param orderQuery
	 * @return
	 */
	public List<BsOrder> getBsJdOrderList(BsOrderQuery orderQuery);
	//得到已拒收订单
	public String selectRefusedJdOrderAmount(BsOrderQuery orderQuery);
	//得到订单总额
	public Map<String, Double> selectJdOrderTotal(BsOrderQuery orderQuery);
	
	//需要同步的京东订单
	public List<BsOrder> getNeedSynchronousJdOrderList();
	//同步京东订单
	public void synchronousJdOrder(@Param("orderlist")List<BsOrder> orderlist);
	
	//详情刷新订单
	public void refreshJdOrder(BsOrder order);
	
	
	/**
	 * 采购下单
	 * @param orderInfo
	 */
	void addNewBsOrder(BsOrderInfo orderInfo);
	
	void addNewBsSerial(BsSerialInfo serialInfo);
	
	void addNewBsSalesOrder(BsSalesOrderInfo orderInfo);
	
	void insertBsLogistics(BsLogisticsInfo logistics);
}
