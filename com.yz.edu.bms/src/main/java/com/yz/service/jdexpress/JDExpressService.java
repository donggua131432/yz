package com.yz.service.jdexpress;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.etms.EtmsOrderPrintRequest;
import com.jd.open.api.sdk.request.etms.EtmsRangeCheckRequest;
import com.jd.open.api.sdk.request.etms.EtmsWaybillSendRequest;
import com.jd.open.api.sdk.request.etms.EtmsWaybillcodeGetRequest;
import com.jd.open.api.sdk.response.etms.EtmsOrderPrintResponse;
import com.jd.open.api.sdk.response.etms.EtmsRangeCheckResponse;
import com.jd.open.api.sdk.response.etms.EtmsWaybillSendResponse;
import com.jd.open.api.sdk.response.etms.EtmsWaybillcodeGetResponse;
import com.yz.model.jd.JDExpressRequest;
import com.yz.util.JsonUtil;


/**
 * 京东接口 Description:
 * 
 * @Version: 1.0
 * @Create Date: 2018年02月26日.
 *
 */
@Service
public class JDExpressService {

	@Value("${jd.customerCode}")
	private String customerCode; //商家编码

	@Value("${jd.appKey}")
	private String appKey;

	@Value("${jd.appSecret}")
	private String appSecret;

	@Value("${jd.accessToken}")
	private String accessToken;

	@Value("${jd.server_url}")
	private String server_url;
	
	@Value("${jd.wareHouseCode}")
	private String wareHouseCode;//发货仓
	
	@Value("${jd.salePlat}")
	private String salePlat;//销售平台
	
	
	private static JdClient client=null;
	
	
	
	/**
	 *   获取京东快递运单号
	 * @throws JdException 
	 * 
	 **/
	public EtmsWaybillcodeGetResponse getDeliveryIdList(String preNum) throws JdException {	
		if(client==null) {
			client=new DefaultJdClient(server_url,accessToken,appKey,appSecret); 
		}
		EtmsWaybillcodeGetRequest request=new EtmsWaybillcodeGetRequest();
		request.setPreNum( preNum );
		request.setCustomerCode(customerCode);
		//运单类型。(普通外单：0，O2O外单：1)默认为0
		request.setOrderType( 0 );
		EtmsWaybillcodeGetResponse response=client.execute(request);
		return response;

	}
	
	/**
	 * 是否可以京配
	 * @param receiveAddress
	 * @return ObjectMapper
	 * @throws JdException
	 */
	public EtmsRangeCheckResponse check(String orderId,String receiveAddress) throws JdException {	
		if(client==null) {
			client=new DefaultJdClient(server_url,accessToken,appKey,appSecret); 
		}
		EtmsRangeCheckRequest request=new EtmsRangeCheckRequest();
		request.setCustomerCode(customerCode );
		//配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）默认是1
		request.setGoodsType( 1 );
		request.setOrderId(orderId);
		request.setWareHouseCode(wareHouseCode);
		request.setReceiveAddress( receiveAddress );		
		EtmsRangeCheckResponse response=client.execute(request);
		return response;

	}
	
	
	/**
	 * 青龙接单接口
	 * @param preNum
	 * @return
	 * @throws JdException
	 */
	public EtmsWaybillSendResponse sendOrder(JDExpressRequest order) throws JdException {	
		if(client==null) {
			client=new DefaultJdClient(server_url,accessToken,appKey,appSecret); 
		}
		EtmsWaybillSendRequest request=new EtmsWaybillSendRequest();
		request.setDeliveryId( order.getDeliveryId());
		request.setSalePlat( salePlat);
		request.setCustomerCode( customerCode );
		request.setOrderId( order.getOrderid() );
		request.setSenderName( order.getSenderName());
		request.setSenderAddress( order.getSenderAddress() );
		request.setSenderTel( order.getSenderTel() );
		request.setSenderMobile(order.getSenderMobile());
		request.setReceiveName( order.getReceiveName() );
		request.setReceiveAddress( order.getReceiveAddress());
		request.setReceiveTel( order.getReceiveTel());
		request.setReceiveMobile( order.getReceiveMobile() );
		request.setPackageCount( order.getPackageCount() );
		request.setWeight( order.getWeight());
		request.setVloumn( order.getVloumn() );
		request.setDescription( "书本快递" );
		
		EtmsWaybillSendResponse response=client.execute(request);
		return response;

	}
	
	
	public EtmsOrderPrintResponse  printOrder(String deliverId) throws JdException{
		if(client==null) {
			client=new DefaultJdClient(server_url,accessToken,appKey,appSecret); 
		}
		EtmsOrderPrintRequest request=new EtmsOrderPrintRequest();
		request.setCustomerCode( customerCode);
		request.setDeliveryId(deliverId );
		EtmsOrderPrintResponse response=client.execute(request);
		return response;
	}

	public static void main(String[] args) throws Exception {
		JDExpressService test=new JDExpressService();
		EtmsWaybillcodeGetResponse response=test.getDeliveryIdList("1");
		System.out.println(JsonUtil.object2String(response));
		

	}
}
