package com.yz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yz.dao.GwCertInfoMapper;
import com.yz.model.GwCertInfo;
import com.yz.model.GwCertInfoKey;
import com.yz.pay.YzPayCertService;
import com.yz.pay.cert.Cert;
import com.yz.pay.constants.PayConstants;
import com.yz.pay.constants.TransType; 


@Service
public class BsCertInfoService implements YzPayCertService{ 


	@Autowired
	private GwCertInfoMapper certInfoMapper;


	/***
	 * 
	 * @param certName
	 * @return
	 *
	 */
	public Cert getCert(String certName) {
		Cert cert = null;
		switch (certName) {
		case PayConstants.WECHAT_CERTS:
			cert= this.getBsCertInfo(PayConstants.YZ_EDUCATION_TEST, TransType.WECHAT.type());
			break;
		case PayConstants.ALLINPAY_JSAPI_CERTS:
			cert = this.getBsCertInfo(PayConstants.YZ_ALLINPAY_JSAPI, TransType.ALLINPAY.type());
			break;
		case PayConstants.ALLINPAY_NATIVE_CERTS:
			cert = this.getBsCertInfo(PayConstants.YZ_NATIVE_JSAPI, TransType.ALLINPAY.type());
			break;  
		}
		return cert;
	}

	/**
	 * 
	 * @param certId
	 * @param transType
	 * @return
	 */
	public Cert getBsCertInfo(String certId, String transType) {
		GwCertInfoKey key = new GwCertInfoKey();
		key.setCertId(certId);
		key.setTransType(transType);
		GwCertInfo certInfo = certInfoMapper.selectByPrimaryKey(key);
		Cert cert = new Cert();
		cert.setCertId(certInfo.getPrivateKey());
		cert.setMerchantId(certInfo.getMerchantId());
		cert.setAppId(certInfo.getAppid());
		return cert;
	}

}
