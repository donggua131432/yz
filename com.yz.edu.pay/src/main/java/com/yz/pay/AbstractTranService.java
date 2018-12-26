package com.yz.pay;

import com.yz.pay.cert.CertUtil;

public abstract class AbstractTranService implements TransService{
	
	protected CertUtil certUtil;

	public AbstractTranService() {
		this.certUtil = getCertUtil();
	}

	public abstract CertUtil getCertUtil();

}
